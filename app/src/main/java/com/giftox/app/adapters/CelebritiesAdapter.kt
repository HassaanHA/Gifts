package com.giftox.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.giftox.app.data.Celebrity
import com.giftox.app.databinding.ItemCelebrityBinding

class CelebritiesAdapter(private val onCelebrityClicked: (Celebrity) -> Unit) :
    ListAdapter<Celebrity, CelebritiesAdapter.ViewHolder>(
        CelebrityDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCelebrityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val mBinding: ItemCelebrityBinding) : RecyclerView.ViewHolder(mBinding.root) {

        fun bind(celebrity: Celebrity) {
            mBinding.image = celebrity.avatar
            mBinding.rootView.setOnClickListener {
                onCelebrityClicked(celebrity)
            }
        }

    }
}

class CelebrityDiffCallback : DiffUtil.ItemCallback<Celebrity>() {
    override fun areItemsTheSame(oldItem: Celebrity, newItem: Celebrity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Celebrity, newItem: Celebrity): Boolean {
        return oldItem == newItem
    }
}