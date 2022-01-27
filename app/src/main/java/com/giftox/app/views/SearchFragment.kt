package com.giftox.app.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.giftox.app.adapters.CategoriesAdapter
import com.giftox.app.databinding.FragmentSearchBinding
import com.giftox.app.utils.getIdsArrayList
import com.giftox.app.utils.showCountriesDialog
import com.giftox.app.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val categoriesAdapter = CategoriesAdapter()
    private lateinit var mBinding: FragmentSearchBinding
    private val viewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentSearchBinding.inflate(inflater, container, false)
        mBinding.viewModel = viewModel
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.lifecycleOwner = viewLifecycleOwner
        initView()
    }

    private fun initView() {
        mBinding.categoriesRv.adapter = categoriesAdapter
        viewModel.celebrities.observe(viewLifecycleOwner, {
            if (mBinding.applyBtn.isEnabled.not()) {
                mBinding.applyBtn.isEnabled = true
                findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToSearchResultsFragment())
            }
        })
        mBinding.priceRs.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                viewModel.priceRange.value = value.toInt()
            }
        }
        mBinding.countryTv.setOnClickListener {
            context?.showCountriesDialog({
                viewModel.country.value = it
                mBinding.clearCountrySelectionBtn.visibility = View.VISIBLE
                mBinding.clearCountrySelectionBtn.isEnabled = true
            }, viewModel.country.value)
        }
        mBinding.clearCountrySelectionBtn.setOnClickListener {
            viewModel.country.value = ""
            mBinding.clearCountrySelectionBtn.visibility = View.INVISIBLE
            mBinding.clearCountrySelectionBtn.isEnabled = false
        }
        mBinding.applyBtn.setOnClickListener {
            mBinding.applyBtn.isEnabled = false
            viewModel.selectedCategories.clear()
            viewModel.selectedCategories.addAll(categoriesAdapter.selectedCategories.getIdsArrayList())
            viewModel.search()
        }
        mBinding.priceRs.setLabelFormatter { value: Float ->
            return@setLabelFormatter "$${value.roundToInt()}"
        }
        mBinding.drawerBtn.setOnClickListener {
            (activity as MainActivity?)?.openDrawer()
        }
        val layoutManager = GridLayoutManager(requireContext(), 6)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position < 3) 2 else 3
            }

        }
        mBinding.categoriesRv.layoutManager = layoutManager
    }
}