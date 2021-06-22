package com.vllenin.basemvvm.base

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vllenin.basemvvm.R
import com.vllenin.basemvvm.base.extensions.bouncingAnimation
import com.vllenin.basemvvm.base.extensions.getStatusBarHeight
import com.vllenin.basemvvm.base.extensions.setOnSingleClickListener
import com.vllenin.basemvvm.di.component.ApplicationComponent
import com.vllenin.basemvvm.model.entities.Resource
import kotlinx.android.synthetic.main.screen_base_action_bar.view.*
import kotlinx.coroutines.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
abstract class BaseFragmentX<VM : ViewModel> : Fragment(), IBaseScreen {

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.applicationContext as? ApplicationX)?.appComponent?.let { inject(it) }
        if (isStableScreen()) {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            } else {
                activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
        }
    }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val baseFragment = inflater.inflate(getLayoutBase(), container, false)
        baseFragment.setOnClickListener {

        }

        if (getLayoutBase() == R.layout.screen_base_action_bar) {
            inflater.inflate(getLayoutResId(), baseFragment.contentFragment, true)
            if (isShowActionBar()) {
                val lp = baseFragment.actionbarContainer.layoutParams as RelativeLayout.LayoutParams
                lp.topMargin = requireContext().getStatusBarHeight()
                baseFragment.actionbarContainer.layoutParams = lp

                baseFragment.actionbarContainer.visibility = View.VISIBLE
                baseFragment.shadowOfActionsBar.visibility = View.VISIBLE
                baseFragment.buttonBackScreen.setOnSingleClickListener {
                    it.bouncingAnimation()
                    activity?.onBackPressed()
                }
            }
        }

        return baseFragment
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initActions()
        initData(arguments)
        registerNetworkChangeListener(baseNetworkChangeListener)
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        if (enter) {
            val anim = AnimationUtils.loadAnimation(context, nextAnim)
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    fragmentTransactionAnimationEnded()
                }
            })
            return anim
        }
        return super.onCreateAnimation(transit, enter, nextAnim)
    }

    @CallSuper
    override fun onDestroyView() {
        unRegisterNetworkChangeListener(baseNetworkChangeListener)
        super.onDestroyView()
    }

    override fun onDestroy() {
        displayLoadingView(false)
        super.onDestroy()
    }

    abstract val viewModel: VM

    abstract fun getLayoutResId(): Int

    abstract fun isShowActionBar(): Boolean

    abstract fun inject(component: ApplicationComponent)

    abstract fun initViews()

    abstract fun initActions()

    abstract fun initData(argument: Bundle?)

    override fun getLayoutBase(): Int = R.layout.screen_base_action_bar

    override fun displayLoadingView(show: Boolean) {
        (activity as? BaseActivityX<*>)?.displayLoadingView(show)
    }

    override fun displayBackgroundTrans(show: Boolean, type: String) {
        (activity as? BaseActivityX<*>)?.displayBackgroundTrans(show, type)
    }

    override fun displayToast(show: Boolean, content: String) {
        (activity as? BaseActivityX<*>)?.displayToast(show, content)
    }

    override fun isStableScreen(): Boolean = true

    override fun registerNetworkChangeListener(iNetworkChangeListener: INetworkChangeListener) {
        (activity as? BaseActivityX<*>)?.registerNetworkChangeListener(iNetworkChangeListener)
    }

    override fun unRegisterNetworkChangeListener(iNetworkChangeListener: INetworkChangeListener) {
        (activity as? BaseActivityX<*>)?.unRegisterNetworkChangeListener(iNetworkChangeListener)
    }

    protected open fun fragmentTransactionAnimationEnded() {

    }

    protected open fun networkStateChange(isConnection: Boolean) {

    }

    protected fun Resource<*>.handleResource(callbackLoading: () -> Unit = {},
                                 callbackExpired: () -> Unit = {},
                                 callbackError: () -> Unit = {},
                                 callbackSuccess: () -> Unit) {
        when (status) {
            Resource.Status.LOADING -> {
                displayLoadingView(true)
                callbackLoading.invoke()
            }
            Resource.Status.EXPIRED -> {
                displayLoadingView(false)
                callbackExpired.invoke()
                logOut(message ?: "")
            }
            Resource.Status.SUCCESS -> {
                displayLoadingView(false)
                callbackSuccess.invoke()
            }
            else -> {
                displayLoadingView(false)
                callbackError.invoke()
            }
        }
    }

    protected fun logOut(message: String = "") {

    }

    protected fun setTittleScreen(tittle: String) {
        view?.findViewById<TextView>(R.id.titleScreen)?.text = tittle
    }

    protected fun displayActionBar(show: Boolean) {
        if (show) {
            view?.findViewById<View>(R.id.actionbarContainer)?.visibility = View.VISIBLE
            view?.findViewById<View>(R.id.shadowOfActionsBar)?.visibility = View.VISIBLE
        } else {
            view?.findViewById<View>(R.id.actionbarContainer)?.visibility = View.GONE
            view?.findViewById<View>(R.id.shadowOfActionsBar)?.visibility = View.GONE
        }
    }

    protected fun hideKeyBroad(view: View) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private val baseNetworkChangeListener = object : INetworkChangeListener {
        override fun networkChanged(isConnection: Boolean) {
            networkStateChange(isConnection)
        }
    }

}