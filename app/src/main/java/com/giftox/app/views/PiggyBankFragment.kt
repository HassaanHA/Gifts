package com.giftox.app.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.giftox.app.adapters.PiggyBankItemsAdapter
import com.giftox.app.databinding.FragmentPiggyBankBinding
import com.giftox.app.viewmodels.MyOrdersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PiggyBankFragment: Fragment() {

    private val piggyBankItemsAdapter = PiggyBankItemsAdapter()
    private lateinit var mBinding: FragmentPiggyBankBinding
    private val viewModel: MyOrdersViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentPiggyBankBinding.inflate(inflater, container, false)
        mBinding.viewModel = viewModel
        mBinding.piggyBankItemsRv.adapter = piggyBankItemsAdapter
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.lifecycleOwner = viewLifecycleOwner
        viewModel.fetchPiggyBank()
    }
}