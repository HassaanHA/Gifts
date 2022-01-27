package com.giftox.app.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.giftox.app.databinding.FragmentLoginBinding
import com.giftox.app.utils.showToast
import com.giftox.app.utils.togglePasswordVisibility
import com.giftox.app.viewmodels.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val email = "email"
    private val publicProfile = "public_profile"
    private val callbackManager = CallbackManager.Factory.create()
    private lateinit var mBinding: FragmentLoginBinding
    private val viewModel: AuthenticationViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentLoginBinding.inflate(inflater, container, false)
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
        viewModel.registrationType.observe(this, {
            viewModel.reset()
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            )
        })
        viewModel.loginErrorMessage.observe(viewLifecycleOwner, {
            showToast(it)
        })
    }

    private fun initView() {
        mBinding.fbLoginBtn.setPermissions(email, publicProfile)
        mBinding.fbLoginBtn.registerCallback(callbackManager, viewModel.facebookLoginCallback)
        mBinding.continueWithFbBtn.setOnClickListener {
            mBinding.fbLoginBtn.callOnClick()
        }
        mBinding.closeBtn.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}