package com.vllenin.basemvvm.base

import android.annotation.SuppressLint
import android.content.*
import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Size
import android.view.*
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vllenin.basemvvm.R
import com.vllenin.basemvvm.base.extensions.isNetworkConnection
import com.vllenin.basemvvm.di.component.ApplicationComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
abstract class BaseActivityX<VM : ViewModel> : AppCompatActivity(), IBaseScreen {

    private lateinit var toast: Toast
    private lateinit var networkChangeReceiver: NetworkChangeReceiver
    private var listNetworkChangeListener = ArrayList<INetworkChangeListener>()

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory

    abstract val viewModel: VM

    abstract val layoutResourceId: Int

    @CallSuper
    override fun setTheme(resId: Int) {
        super.setTheme(resId)
        window?.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        inject((applicationContext as ApplicationX).appComponent)
        super.onCreate(savedInstanceState)
        setContentView(layoutResourceId)

        initScreenSize()
        initViews()
        initData()
        initToast()

        networkChangeReceiver = NetworkChangeReceiver()
        registerReceiver(networkChangeReceiver, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
//        FirebaseCrashlytics.getInstance().setUserId(emailUser ?: "")
    }

    @CallSuper
    override fun onDestroy() {
        unregisterReceiver(networkChangeReceiver)
        super.onDestroy()
    }

    abstract fun inject(component: ApplicationComponent)

    abstract fun initViews()

    abstract fun initActions()

    abstract fun initData()

    override fun getLayoutBase(): Int = IBaseScreen.NONE

    override fun displayToast(show: Boolean, content: String) {
        if (show) {
            if (content.isNotEmpty()) {
                toast.setText(content)
            }
            toast.show()
        } else {
            toast.cancel()
        }
    }

    override fun isStableScreen(): Boolean = true

    override fun registerNetworkChangeListener(iNetworkChangeListener: INetworkChangeListener) {
        listNetworkChangeListener.add(iNetworkChangeListener)
    }

    override fun unRegisterNetworkChangeListener(iNetworkChangeListener: INetworkChangeListener) {
        listNetworkChangeListener.remove(iNetworkChangeListener)
    }

    protected fun getToken(): String? {
        val sharePrefs = getSharedPreferences(Constant.SHARE_PREFS_NAME, Context.MODE_PRIVATE)
        return sharePrefs.getString(Constant.KEY_TOKEN, null)
    }

    @SuppressLint("ShowToast")
    private fun initToast() {
        toast = Toast.makeText(this, "-", Toast.LENGTH_SHORT)
    }

    private fun initScreenSize() {
        ApplicationX.SCREEN_SIZE = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            Size(windowMetrics.bounds.width(), windowMetrics.bounds.height() - insets.bottom)
        } else {
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            Size(displayMetrics.widthPixels, displayMetrics.heightPixels)
        }
    }

    inner class NetworkChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            ApplicationX.isNetworkAvailable = isNetworkConnection()
            listNetworkChangeListener.forEach {
                it.networkChanged(ApplicationX.isNetworkAvailable)
            }
            if (!ApplicationX.isNetworkAvailable) {
                AlertDialog.Builder(context!!)
                    .setTitle("Network Status")
                    .setMessage("Network disconnected.")
                    .setNegativeButton(R.string.app_name) { _, _ ->

                    }
                    .setPositiveButton(R.string.app_name) { _, _ ->
                        startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                    }
                    .show()
            }
        }
    }

}