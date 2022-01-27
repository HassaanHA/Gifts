package com.giftox.app.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.giftox.app.adapters.MyOrdersAdapter
import com.giftox.app.data.MyOrder
import com.giftox.app.databinding.FragmentOrderBinding
import com.giftox.app.utils.Constants
import com.giftox.app.viewmodels.MyOrdersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment(private val orderType: String) : Fragment() {

    private val myOrdersAdapter = MyOrdersAdapter { myOrder -> onOrderClicked(myOrder) }
    private lateinit var mBinding: FragmentOrderBinding
    private val viewModel: MyOrdersViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentOrderBinding.inflate(inflater, container, false)
        mBinding.viewModel = viewModel
        mBinding.ordersRv.adapter = myOrdersAdapter
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.lifecycleOwner = viewLifecycleOwner
        setupObservers()
    }

    private fun setupObservers() {
        if (orderType == Constants.orderStatusCompleted) {
            viewModel.completedOrders.observe(viewLifecycleOwner, {
                myOrdersAdapter.submitList(it)
            })
        } else {
            viewModel.pendingOrders.observe(viewLifecycleOwner, {
                myOrdersAdapter.submitList(it)
            })
        }
    }

    private fun onOrderClicked(myOrder: MyOrder) {
        OrderDetailsFragment(myOrder).show(childFragmentManager, null)
    }
}