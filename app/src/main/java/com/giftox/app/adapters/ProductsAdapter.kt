package com.giftox.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.giftox.app.data.Product
import com.giftox.app.databinding.ItemProductBinding

class ProductsAdapter(
    private val onProductSelectionChanged: ((Product) -> Unit?)? = null,
    private val shouldShowSelectionOption: Boolean = true
) : ListAdapter<Product, ProductsAdapter.ViewHolder>(
    ProductDiffCallback()
) {

    private val selectedProducts: ArrayList<Product> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val mBinding: ItemProductBinding) : RecyclerView.ViewHolder(mBinding.root) {

        fun bind(product: Product) {
            mBinding.product = product
            mBinding.isSelected = selectedProducts.contains(product)
            mBinding.shouldShowSelectionOption = shouldShowSelectionOption
            mBinding.checkboxIv.setOnClickListener {
                onProductSelectionChanged?.invoke(product)
                if (selectedProducts.contains(product)) {
                    selectedProducts.remove(product)
                } else {
                    selectedProducts.add(product)
                }
                notifyItemChanged(adapterPosition)
            }
        }

    }
}

class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}