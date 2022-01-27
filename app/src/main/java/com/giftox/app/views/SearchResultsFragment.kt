package com.giftox.app.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.giftox.app.adapters.CelebritiesAdapter
import com.giftox.app.data.Celebrity
import com.giftox.app.databinding.FragmentSearchResultsBinding
import com.giftox.app.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultsFragment : Fragment() {

    private val celebritiesAdapter = CelebritiesAdapter { celebrity -> onCelebrityClicked(celebrity) }
    private lateinit var mBinding: FragmentSearchResultsBinding
    private val viewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentSearchResultsBinding.inflate(inflater, container, false)
        mBinding.celebritiesRv.adapter = celebritiesAdapter
        mBinding.viewModel = viewModel
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun onCelebrityClicked(celebrity: Celebrity) {
        val action = SearchResultsFragmentDirections.actionSearchResultsFragmentToCelebrityDetailsFragment(celebrity)
        findNavController().navigate(action)
    }
}