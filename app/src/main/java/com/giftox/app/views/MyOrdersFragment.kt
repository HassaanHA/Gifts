package com.giftox.app.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.giftox.app.R
import com.giftox.app.databinding.FragmentMyOrdersBinding
import com.giftox.app.utils.Constants
import com.giftox.app.viewmodels.MyOrdersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyOrdersFragment : Fragment() {

    private lateinit var mBinding: FragmentMyOrdersBinding
    private val viewModel: MyOrdersViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentMyOrdersBinding.inflate(inflater, container, false)
        mBinding.viewModel = viewModel
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.lifecycleOwner = viewLifecycleOwner
        setupObservers()
        initView()
    }

    private fun setupObservers() {
        viewModel.selectedTab.observe(viewLifecycleOwner, {
            val fragment = when (it) {
                0 -> OrdersFragment(Constants.orderStatusPending)
                1 -> OrdersFragment(Constants.orderStatusCompleted)
                else -> PiggyBankFragment()
            }
            childFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragment).commitAllowingStateLoss()
        })
    }

    private fun initView() {
        mBinding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.reset()
    }
}