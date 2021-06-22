package com.vllenin.basemvvm.base.extensions

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.vllenin.basemvvm.base.Constant.FOLDER_NAME
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Vllenin on 6/21/21.
 */
fun Bitmap.saveToImageFile(context: Context): Uri {
    val fileName = "RikiCertificate_${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(
        System.currentTimeMillis())}"
    val imageUri: Uri?

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val resolver = context.contentResolver
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM + File.separator + FOLDER_NAME)
        imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        val fos = resolver.openOutputStream(imageUri!!)
        compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos?.flush()
        fos?.close()
    } else {
        imageUri = Uri.parse(MediaStore.Images.Media.insertImage(context.contentResolver, this, fileName, fileName))
    }

    return imageUri!!
}

fun Context.downloadAndSaveFilePDF(urlFile: String, callbackResult: (isSuccess: Boolean, uri: Uri?) -> Unit) {
    val fileName = "RikiDocument_${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(System.currentTimeMillis())}"
    var fileUri: Uri? = null

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + File.separator + FOLDER_NAME)

        fileUri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        try {
            val url = URL(urlFile)
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connect()
            val inputStream: InputStream = urlConnection.inputStream
            val bytes = inputStream.readBytes()
            val buf = BufferedInputStream(inputStream)
            buf.read(bytes, 0, bytes.size)
            buf.close()
            val fos = contentResolver.openOutputStream(fileUri!!)
            fos?.write(bytes)
            fos?.flush()
            fos?.close()
        } catch (e: Exception) {
            callbackResult.invoke(false, null)
            e.printStackTrace()
        }
    } else {
        try {
            val dcimStorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
            val folderRiki = File("$dcimStorageDirectory/$FOLDER_NAME")
            if (!folderRiki.exists()) {
                folderRiki.mkdir()
            }
            val filePDF = File("$dcimStorageDirectory/$FOLDER_NAME", "$fileName.pdf")
            val url = URL(urlFile)
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connect()
            val inputStream: InputStream = urlConnection.inputStream
            val fileOutputStream = FileOutputStream(filePDF)
            val buffer = ByteArray(1024 * 1024)
            var bufferLength: Int
            while (inputStream.read(buffer).also { bufferLength = it } > 0) {
                fileOutputStream.write(buffer, 0, bufferLength)
            }
            fileOutputStream.close()

            fileUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    this,
                    "vn.nihongo.riki.provider",
                    filePDF
                )
            } else {
                Uri.fromFile(filePDF)
            }
        } catch (e: FileNotFoundException) {
            callbackResult.invoke(false, null)
            e.printStackTrace()
        } catch (e: MalformedURLException) {
            callbackResult.invoke(false, null)
            e.printStackTrace()
        } catch (e: IOException) {
            callbackResult.invoke(false, null)
            e.printStackTrace()
        }
    }

    callbackResult.invoke(true, fileUri)
}