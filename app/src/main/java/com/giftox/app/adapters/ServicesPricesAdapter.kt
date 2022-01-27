package com.giftox.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.giftox.app.data.Product
import com.giftox.app.databinding.ItemServicePriceBinding

class ServicesPricesAdapter : ListAdapter<Product, ServicesPricesAdapter.ViewHolder>(
    ProductDiffCallback()
) {

    var userServices: ArrayList<Product> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemServicePriceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val mBinding: ItemServicePriceBinding) : RecyclerView.ViewHolder(mBinding.root) {

        fun bind(product: Product) {
            mBinding.product = product
            userServices.forEach {
                if (product.productDetails?.title == it.productDetails?.title && it.price != 0) {
                    mBinding.priceEt.setText(it.price.toString())
                    product.price = it.price
                }
            }
            mBinding.priceEt.doAfterTextChanged {
                if (mBinding.priceEt.text.isNullOrEmpty())
                    product.price = 0
                else
                    product.price = mBinding.priceEt.text.toString().toInt()
            }
        }
    }
}