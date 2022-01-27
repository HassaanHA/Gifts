package com.giftox.app.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.giftox.app.adapters.RatingsAdapter
import com.giftox.app.databinding.FragmentCelebrityReviewsBinding
import com.giftox.app.utils.showToast
import com.giftox.app.viewmodels.CelebrityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CelebrityReviewsFragment : Fragment() {

    private val ratingsAdapter = RatingsAdapter()
    private lateinit var mBinding: FragmentCelebrityReviewsBinding
    private val viewModel: CelebrityViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentCelebrityReviewsBinding.inflate(inflater, container, false)
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
        viewModel.apiErrors.observe(viewLifecycleOwner, {
            showToast(it)
        })
    }

    private fun initView() {
        mBinding.reviewsRv.adapter = ratingsAdapter
        mBinding.writeReviewBtn.setOnClickListener {
            AddRatingFragment().show(childFragmentManager, null)
        }
        mBinding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}