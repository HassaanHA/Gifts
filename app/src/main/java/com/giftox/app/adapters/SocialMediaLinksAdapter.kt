package com.giftox.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.giftox.app.data.Product
import com.giftox.app.databinding.ItemSocialMediaLinksBinding

class SocialMediaLinksAdapter : ListAdapter<Product, SocialMediaLinksAdapter.ViewHolder>(
    ProductDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSocialMediaLinksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val mBinding: ItemSocialMediaLinksBinding) : RecyclerView.ViewHolder(mBinding.root) {

        fun bind(product: Product) {
            mBinding.product = product
            mBinding.linkEt.doAfterTextChanged {
                product.link = mBinding.linkEt.text?.toString()
            }
        }

    }
}