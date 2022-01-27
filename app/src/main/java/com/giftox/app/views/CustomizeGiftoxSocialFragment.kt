package com.giftox.app.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.giftox.app.adapters.SocialMediaLinksAdapter
import com.giftox.app.data.Product
import com.giftox.app.databinding.FragmentCustomizeGiftoxSocialMediaBinding
import com.giftox.app.utils.showToast
import com.giftox.app.utils.toSocialLinksList
import com.giftox.app.viewmodels.CelebrityViewModel

class CustomizeGiftoxSocialFragment : Fragment() {

    private val socialMediaLinksAdapter = SocialMediaLinksAdapter()
    private lateinit var mBinding: FragmentCustomizeGiftoxSocialMediaBinding
    private val viewModel: CelebrityViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentCustomizeGiftoxSocialMediaBinding.inflate(inflater, container, false)
        mBinding.socialLinksRv.adapter = socialMediaLinksAdapter
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
        viewModel.validationErrors.observe(viewLifecycleOwner, {
            showToast(it)
        })
        viewModel.socialLinksObservable.observe(viewLifecycleOwner, {
            findNavController().navigate(
                CustomizeGiftoxSocialFragmentDirections.actionCustomizeGiftoxSocialMediaFragmentToConfirmCheckoutFragment()
            )
        })
    }

    private fun initView() {
        mBinding.nextBtn.setOnClickListener {
            viewModel.socialLinks.clear()
            viewModel.socialLinks.addAll(
                ArrayList<Product>(socialMediaLinksAdapter.currentList).toSocialLinksList()
            )
            viewModel.continueFromSocialLinks()
        }
        mBinding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}