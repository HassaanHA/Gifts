package com.giftox.app.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.giftox.app.adapters.CollectionsAdapter
import com.giftox.app.adapters.HomeBannersAdapter
import com.giftox.app.data.Celebrity
import com.giftox.app.databinding.FragmentHomeBinding
import com.giftox.app.viewmodels.HomeViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val bannersAdapter = HomeBannersAdapter()
    private val collectionsAdapter = CollectionsAdapter { celebrity -> onCelebrityClicked(celebrity) }
    private lateinit var mBinding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        mBinding.viewModel = viewModel
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.lifecycleOwner = viewLifecycleOwner
        initView()
    }

    private fun initView() {
        mBinding.bannersVp.adapter = bannersAdapter
        mBinding.collectionsRv.adapter = collectionsAdapter
        mBinding.twitterMarqueeTv.isSelected = true
        mBinding.drawerBtn.setOnClickListener {
            (activity as MainActivity?)?.openDrawer()
        }
        TabLayoutMediator(mBinding.bannersIndicator, mBinding.bannersVp) { _, _ -> }.attach()
    }

    private fun onCelebrityClicked(celebrity: Celebrity) {
        val action = HomeFragmentDirections.actionHomeFragmentToCelebrityDetailsFragment(celebrity)
        findNavController().navigate(action)
    }

}