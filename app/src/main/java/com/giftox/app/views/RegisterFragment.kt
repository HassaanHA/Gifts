package com.giftox.app.views

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.giftox.app.R
import com.giftox.app.adapters.CategoriesAdapter
import com.giftox.app.data.Category
import com.giftox.app.databinding.FragmentRegisterBinding
import com.giftox.app.utils.showDatePickerDialog
import com.giftox.app.utils.showToast
import com.giftox.app.utils.togglePasswordVisibility
import com.giftox.app.viewmodels.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val categoriesAdapter = CategoriesAdapter { category -> onCategorySelectionChanged(category) }
    private lateinit var mBinding: FragmentRegisterBinding
    private val viewModel: AuthenticationViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentRegisterBinding.inflate(inflater, container, false)
        mBinding.categoriesRv.adapter = categoriesAdapter
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
        viewModel.isPasswordVisible.observe(viewLifecycleOwner, {
            mBinding.passwordEt.togglePasswordVisibility(it, mBinding.passwordVisibilityToggle)
        })
        viewModel.isRepeatPasswordVisible.observe(viewLifecycleOwner, {
            mBinding.repeatPasswordEt.togglePasswordVisibility(it, mBinding.repeatPasswordVisibilityToggle)
        })
        viewModel.validationErrors.observe(viewLifecycleOwner, {
            showToast(it)
        })
        viewModel.registerErrorMessage.observe(viewLifecycleOwner, {
            showToast(it)
        })
    }

    private fun onCategorySelectionChanged(category: Category) {
        viewModel.onCategorySelectionChanged(category)
    }

    private fun initView() {
        mBinding.birthdayTv.setOnClickListener {
            context?.showDatePickerDialog(onBirthdaySetListener)
        }
        mBinding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
        val layoutManager = GridLayoutManager(requireContext(), 6)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position < 3) 2 else 3
            }

        }
        mBinding.categoriesRv.layoutManager = layoutManager
    }

    private val onBirthdaySetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        viewModel.dateOfBirth.value = "$year-${String.format("%02d", month+1)}-${String.format("%02d", dayOfMonth)}"
    }
}