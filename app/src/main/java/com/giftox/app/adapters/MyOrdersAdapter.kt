package com.giftox.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.giftox.app.data.MyOrder
import com.giftox.app.databinding.ItemOrderBinding

class MyOrdersAdapter(
    private val onOrderClicked: (MyOrder) -> Unit
) : ListAdapter<MyOrder, MyOrdersAdapter.ViewHolder>(
    MyOrderDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrdersAdapter.ViewHolder {
        return ViewHolder(
            ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyOrdersAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val mBinding: ItemOrderBinding) : RecyclerView.ViewHolder(mBinding.root) {

        fun bind(myOrder: MyOrder) {
            mBinding.myOrder = myOrder
            mBinding.viewOrderBtn.setOnClickListener {
                onOrderClicked(myOrder)
            }
        }
    }
}

class MyOrderDiffCallback() : DiffUtil.ItemCallback<MyOrder>() {
    override fun areItemsTheSame(oldItem: MyOrder, newItem: MyOrder): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MyOrder, newItem: MyOrder): Boolean {
        return oldItem == newItem
    }
}