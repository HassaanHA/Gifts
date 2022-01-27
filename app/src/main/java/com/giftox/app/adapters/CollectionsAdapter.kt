package com.giftox.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.giftox.app.data.Celebrity
import com.giftox.app.data.Collection
import com.giftox.app.databinding.ItemCollectionBinding

class CollectionsAdapter(private val onCelebrityClicked: (Celebrity) -> Unit) :
    ListAdapter<Collection, CollectionsAdapter.ViewHolder>(
        CollectionDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val mBinding: ItemCollectionBinding) : RecyclerView.ViewHolder(mBinding.root) {

        fun bind(collection: Collection) {
            mBinding.celebritiesRv.adapter = CelebritiesAdapter(onCelebrityClicked)
            mBinding.collection = collection
            mBinding.executePendingBindings()
        }

    }
}

class CollectionDiffCallback : DiffUtil.ItemCallback<Collection>() {
    override fun areItemsTheSame(oldItem: Collection, newItem: Collection): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Collection, newItem: Collection): Boolean {
        return oldItem == newItem
    }
}