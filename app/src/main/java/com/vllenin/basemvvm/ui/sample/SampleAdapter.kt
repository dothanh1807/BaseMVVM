package com.vllenin.basemvvm.ui.sample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vllenin.basemvvm.R
import com.vllenin.basemvvm.base.extensions.bouncingAnimation
import com.vllenin.basemvvm.base.extensions.setOnSingleClickListener
import com.vllenin.basemvvm.model.entities.sample.RealItem
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by Vllenin on 6/21/21.
 */
@ExperimentalCoroutinesApi
class SampleAdapter : RecyclerView.Adapter<SampleAdapter.ItemViewHolder>() {

    private var listItem: List<RealItem>? = null
    private var itemClickCallback: (item: RealItem?) -> Unit = {  }

    override fun getItemCount(): Int = listItem?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {}

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int, payloads: MutableList<Any>) {
        holder.bindData(listItem?.get(position))
    }

    fun setData(listItem: List<RealItem>?) = apply {
        this.listItem = listItem
        notifyDataSetChanged()
    }

    fun setItemClickCallback(itemClickCallback: (item: RealItem?) -> Unit) = apply {
        this.itemClickCallback = itemClickCallback
    }



    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            itemView.setOnSingleClickListener {
                it.bouncingAnimation()
                itemClickCallback.invoke(listItem?.get(adapterPosition))
            }
        }

        fun bindData(item: RealItem?) {
            (itemView as? TextView)?.text = item?.realTitleItem
        }

    }
}