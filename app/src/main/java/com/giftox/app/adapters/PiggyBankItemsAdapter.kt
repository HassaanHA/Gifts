package com.giftox.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.giftox.app.data.PiggyBankItem
import com.giftox.app.databinding.ItemPiggyBankBinding

class PiggyBankItemsAdapter : ListAdapter<PiggyBankItem, PiggyBankItemsAdapter.ViewHolder>(
    PiggyBankItemDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PiggyBankItemsAdapter.ViewHolder {
        return ViewHolder(
            ItemPiggyBankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PiggyBankItemsAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val mBinding: ItemPiggyBankBinding) : RecyclerView.ViewHolder(mBinding.root) {

        fun bind(item: PiggyBankItem) {
            mBinding.item = item
        }

    }
}

class PiggyBankItemDiffCallback : DiffUtil.ItemCallback<PiggyBankItem>() {

    override fun areItemsTheSame(oldItem: PiggyBankItem, newItem: PiggyBankItem): Boolean {
        return oldItem.price == newItem.price
    }

    override fun areContentsTheSame(oldItem: PiggyBankItem, newItem: PiggyBankItem): Boolean {
        return oldItem == newItem
    }
}