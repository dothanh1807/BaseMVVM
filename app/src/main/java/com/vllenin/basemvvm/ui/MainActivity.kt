package com.vllenin.basemvvm.ui

import android.view.View
import androidx.activity.viewModels
import com.vllenin.basemvvm.R
import com.vllenin.basemvvm.base.BaseActivityX
import com.vllenin.basemvvm.base.IBaseScreen
import com.vllenin.basemvvm.base.extensions.replaceScreen
import com.vllenin.basemvvm.di.component.ApplicationComponent
import com.vllenin.basemvvm.ui.sample.SampleScreen
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by Vllenin on 6/18/21.
 */
@ExperimentalCoroutinesApi
class MainActivity : BaseActivityX<MainActivityVM>() {

    override val viewModel: MainActivityVM by viewModels { viewModelFactory }

    override val layoutResourceId: Int = R.layout.activity_main

    override fun inject(component: ApplicationComponent) {
        component.inject(this)
    }

    override fun initViews() {
        supportFragmentManager.replaceScreen(SampleScreen(), SampleScreen::class.toString())
    }

    override fun initActions() {

    }

    override fun initData() {

    }

    override fun displayLoadingView(show: Boolean) {
        loadingProgress.visibility = if (show) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    override fun displayBackgroundTrans(show: Boolean, type: String) {
        if (show) {
            if (type == IBaseScreen.WHITE_TRANS) {
                backgroundWhiteTrans.visibility = View.VISIBLE
            } else {
                backgroundBlackTrans.visibility = View.VISIBLE
            }
        } else {
            if (type == IBaseScreen.WHITE_TRANS) {
                backgroundWhiteTrans.visibility = View.INVISIBLE
            } else {
                backgroundBlackTrans.visibility = View.INVISIBLE
            }
        }
    }

}