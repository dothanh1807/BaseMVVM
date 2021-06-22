package com.vllenin.basemvvm.base

import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by Vllenin on 2019-09-02.
 */
@ExperimentalCoroutinesApi
interface IBaseScreen {

    companion object {
        const val NONE = 0
        const val WHITE_TRANS = "white_trans"
        const val BLACK_TRANS = "black_trans"
    }

    fun getLayoutBase(): Int

    fun displayLoadingView(show: Boolean)

    fun displayBackgroundTrans(show: Boolean, type: String)

    fun displayToast(show: Boolean, content: String = "")

    fun isStableScreen(): Boolean

    fun registerNetworkChangeListener(iNetworkChangeListener: INetworkChangeListener)

    fun unRegisterNetworkChangeListener(iNetworkChangeListener: INetworkChangeListener)

}