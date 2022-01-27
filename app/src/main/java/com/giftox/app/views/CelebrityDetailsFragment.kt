package com.giftox.app.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.giftox.app.R
import com.giftox.app.adapters.ProductsAdapter
import com.giftox.app.databinding.FragmentCelebrityDetailsBinding
import com.giftox.app.utils.showToast
import com.giftox.app.viewmodels.CelebrityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CelebrityDetailsFragment : Fragment() {

    private val productsAdapter = ProductsAdapter({ product -> viewModel.onProductSelectionChanged(product) })
    private val args: CelebrityDetailsFragmentArgs by navArgs()
    private lateinit var mBinding: FragmentCelebrityDetailsBinding
    private val viewModel: CelebrityViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentCelebrityDetailsBinding.inflate(inflater, container, false)
        viewModel.getCelebrity(args.celebrity)
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

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        mBinding.productsRv.adapter = productsAdapter
        mBinding.customizeGiftoxBtn.setOnClickListener {
            viewModel.selectedSocialOptions.clear()
            if (viewModel.selectedProducts.isNotEmpty()) {//check if at least one option selected
                viewModel.selectedProducts.forEach {
                    if (it.productDetails?.title?.contains("video", true) == false)
                        viewModel.selectedSocialOptions.add(it)
                }
                if (viewModel.isVideoOptionSelected) {//check if video option is selected
                    //move to video options fragment
                    findNavController()
                        .navigate(
                            CelebrityDetailsFragmentDirections
                                .actionCelebrityDetailsFragmentToCustomizeGiftoxVideoFragment()
                        )
                } else {
                    //move to social media options fragment
                    findNavController()
                        .navigate(
                            CelebrityDetailsFragmentDirections
                                .actionCelebrityDetailsFragmentToCustomizeGiftoxSocialMediaFragment()
                        )
                }
            } else {
                //show error message
                showToast(R.string.select_at_least_one_product)
            }
        }
        mBinding.ratingBar.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                findNavController().navigate(
                    CelebrityDetailsFragmentDirections
                        .actionCelebrityDetailsFragmentToCelebrityReviewsFragment()
                )
            }
            return@setOnTouchListener true
        }
        mBinding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDetach() {
        viewModel.reset()
        super.onDetach()
    }

}