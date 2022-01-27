package com.giftox.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.giftox.app.data.Category
import com.giftox.app.databinding.ItemCategorySearchBinding

class CategoriesAdapter(
    private val onCategorySelectionChanged: ((Category) -> Unit)? = null
) : ListAdapter<Category, CategoriesAdapter.ViewHolder>(
    CategoryDiffCallback()
) {

    val selectedCategories: ArrayList<Category> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCategorySearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val mBinding: ItemCategorySearchBinding) : RecyclerView.ViewHolder(mBinding.root) {

        fun bind(category: Category) {
            mBinding.category = category
            mBinding.isSelected = selectedCategories.contains(category)
            mBinding.root.setOnClickListener {
                onCategorySelectionChanged?.invoke(category)
                if (selectedCategories.contains(category))
                    selectedCategories.remove(category)
                else
                    selectedCategories.add(category)
                notifyItemChanged(adapterPosition)
            }
        }

    }
}

class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }

}