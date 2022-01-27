package com.giftox.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.giftox.app.data.Category
import com.giftox.app.data.Occasion
import com.giftox.app.databinding.ItemOccasionBinding

class OccasionsAdapter(
    private val onOccasionSelectionChanged: ((Occasion) -> Unit)? = null
) : ListAdapter<Occasion, OccasionsAdapter.ViewHolder>(
    OccasionDiffCallback()
) {

    private var selectedOccasion: Occasion? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemOccasionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val mBinding: ItemOccasionBinding) : RecyclerView.ViewHolder(mBinding.root) {

        fun bind(occasion: Occasion) {
            mBinding.occasion = occasion
            mBinding.isSelected = occasion.id == selectedOccasion?.id
            mBinding.rootView.setOnClickListener {
                selectedOccasion = occasion
                onOccasionSelectionChanged?.invoke(occasion)
                notifyItemRangeChanged(0, itemCount)
            }
        }

    }
}

class OccasionDiffCallback : DiffUtil.ItemCallback<Occasion>() {
    override fun areItemsTheSame(oldItem: Occasion, newItem: Occasion): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Occasion, newItem: Occasion): Boolean {
        return oldItem == newItem
    }
}