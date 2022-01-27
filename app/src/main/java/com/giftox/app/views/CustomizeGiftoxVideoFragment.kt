package com.giftox.app.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.giftox.app.adapters.OccasionsAdapter
import com.giftox.app.data.Occasion
import com.giftox.app.databinding.FragmentCustomizeGiftoxVideoBinding
import com.giftox.app.utils.showToast
import com.giftox.app.viewmodels.CelebrityViewModel

class CustomizeGiftoxVideoFragment : Fragment() {

    private val occasionsAdapter = OccasionsAdapter { occasion -> onOccasionSelectionChanged(occasion) }
    private lateinit var mBinding: FragmentCustomizeGiftoxVideoBinding
    private val viewModel: CelebrityViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentCustomizeGiftoxVideoBinding.inflate(inflater, container, false)
        mBinding.viewModel = viewModel
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.occasionsRv.adapter = occasionsAdapter
        setupObservers()
        initView()
    }

    private fun setupObservers() {
        viewModel.validationErrors.observe(viewLifecycleOwner, {
            showToast(it)
        })
        viewModel.giftoxVideoObservable.observe(viewLifecycleOwner, {
            if (viewModel.selectedSocialOptions.isNotEmpty()) {
                findNavController().navigate(
                    CustomizeGiftoxVideoFragmentDirections.actionCustomizeGiftoxVideoFragmentToCustomizeGiftoxSocialMediaFragment()
                )
            } else {
                findNavController().navigate(
                    CustomizeGiftoxVideoFragmentDirections.actionCustomizeGiftoxVideoFragmentToConfirmCheckoutFragment()
                )
            }
        })
    }

    private fun initView() {
        mBinding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun onOccasionSelectionChanged(occasion: Occasion) {
        viewModel.selectedOccasion = occasion
    }
}