package com.vllenin.basemvvm.ui.sample

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.vllenin.basemvvm.R
import com.vllenin.basemvvm.base.BaseFragmentX
import com.vllenin.basemvvm.base.extensions.setOnSingleClickListener
import com.vllenin.basemvvm.di.component.ApplicationComponent
import com.vllenin.basemvvm.model.entities.Resource
import com.vllenin.basemvvm.model.entities.sample.RealSampleData
import kotlinx.android.synthetic.main.screen_sample.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by Vllenin on 6/21/21.
 */
@ExperimentalCoroutinesApi
class SampleScreen : BaseFragmentX<SampleVM>() {

    /**
     * this: Có nghĩa là viewModel này sẽ được put vào viewModelStore của Fragment.
     * viewModelFactory: Để có thể inject các thành phần trong constructor(..) của viewModel.
     */
    override val viewModel: SampleVM by viewModels({this}, {viewModelFactory})

    override fun getLayoutResId(): Int = R.layout.screen_sample

    override fun isShowActionBar(): Boolean = true

    override fun inject(component: ApplicationComponent) {
        component.inject(this)
    }

    override fun initViews() {
        itemCollection.layoutManager = LinearLayoutManager(requireContext())
        itemCollection.adapter = SampleAdapter()
            .setItemClickCallback {
                viewModel.requestDataOf(it) { data ->
                    Toast.makeText(context, data, Toast.LENGTH_LONG).show()
                }
            }

        view?.findViewById<TextView>(R.id.textAction)?.apply {
            text = "Update"
            setOnSingleClickListener {
                if (text.toString().contentEquals("Quit")) {
                    activity?.finish()
                } else {
                    viewModel.requestUpdateData {
                        text = "Quit"
                    }
                }
            }
        }
    }

    override fun initActions() {

    }

    override fun initData(argument: Bundle?) {
        viewModel.fetchSampleContent()
        viewModel.sampleData.observe(this, Observer<Resource<RealSampleData>> { resource ->
            setTittleScreen(resource.data?.realTitle ?: "")
            (itemCollection.adapter as? SampleAdapter)?.setData(resource.data?.realItems)
        })
    }

}