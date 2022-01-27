package com.giftox.app.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.giftox.app.databinding.FragmentPasswordsBinding
import com.giftox.app.utils.showToast
import com.giftox.app.utils.togglePasswordVisibility
import com.giftox.app.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordsFragment : Fragment() {

    private lateinit var mBinding: FragmentPasswordsBinding
    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentPasswordsBinding.inflate(inflater, container, false)
        mBinding.viewModel = viewModel
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.lifecycleOwner = viewLifecycleOwner
        setupObservers()
    }


    private fun setupObservers() {
        viewModel.isNewPasswordVisible.observe(viewLifecycleOwner, {
            mBinding.newPasswordEt.togglePasswordVisibility(it, mBinding.newPasswordVisibilityToggle)
        })
        viewModel.isRepeatNewPasswordVisible.observe(viewLifecycleOwner, {
            mBinding.repeatNewPasswordEt.togglePasswordVisibility(it, mBinding.repeatNewPasswordVisibilityToggle)
        })
        viewModel.changePasswordObservable.observe(this, {
            showToast(it.message)
        })
    }
}