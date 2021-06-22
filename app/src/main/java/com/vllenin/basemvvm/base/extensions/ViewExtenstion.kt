package com.vllenin.basemvvm.base.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import com.vllenin.basemvvm.base.common.OnSingleClickListener
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by Vllenin on 6/21/21.
 */
@ExperimentalCoroutinesApi
fun View.setOnSingleClickListener(timeInterval: Int = OnSingleClickListener.MIN_CLICK_INTERVAL_NORMAL,
                                  callback: (view: View) -> Unit) {
    setOnClickListener(object : OnSingleClickListener(timeInterval) {
        override fun onSingleClick(view: View?) {
            callback.invoke(view!!)
        }
    })
}

fun View.bouncingAnimation(decrease: Boolean = false, callbackWhenEnded: () -> Unit = {}) {
    var valueFirst = 0.85f
    var valueSecond = 1.15f
    var valueThird = 0.9f
    val valueForth = 1f
    if (decrease) {
        valueFirst = 0.94f
        valueSecond = 1.05f
        valueThird = 0.97f
    }
    animate().scaleX(valueFirst).scaleY(valueFirst).setDuration(50)
        .withEndAction {
            animate().scaleX(valueSecond).scaleY(valueSecond).setDuration(50)
                .withEndAction {
                    animate().scaleX(valueThird).scaleY(valueThird).setDuration(50)
                        .withEndAction {
                            animate().scaleX(valueForth).scaleY(valueForth).setDuration(50)
                                .withEndAction {
                                    animation?.cancel()
                                    post {
                                        callbackWhenEnded.invoke()
                                    }
                                }
                                .start()
                        }
                        .start()
                }
                .start()
        }
        .start()
}

fun View.convertToBitmap(): Bitmap {
    //Define a bitmap with the same size as the view
    val returnedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    //Bind a canvas to it
    val canvas = Canvas(returnedBitmap)
    //Get the view's background
    val bgDrawable = background
    if (bgDrawable != null) //has background drawable, then draw it on the canvas
        bgDrawable.draw(canvas) else  //does not have background drawable, then draw white background on the canvas
        canvas.drawColor(Color.WHITE)
    // draw the view on the canvas
    draw(canvas)
    //return the bitmap
    return returnedBitmap
}