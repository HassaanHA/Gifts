package com.giftox.app.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.giftox.app.R
import com.giftox.app.databinding.FragmentProfileBinding
import com.giftox.app.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var mBinding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentProfileBinding.inflate(inflater, container, false)
        viewModel.fetchUser()
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
        viewModel.user.observe(viewLifecycleOwner, {
            viewModel.firstName.postValue(it?.firstName)
            viewModel.email.postValue(it?.email)
            viewModel.mobile.postValue(it?.phone)
            viewModel.country.postValue(it?.countryOfResidence)
            viewModel.birthday.postValue(it?.dateOfBirth)
            viewModel.gender.postValue(it?.gender)
        })
        viewModel.selectedTab.observe(viewLifecycleOwner, {
            viewModel.reset()
            val fragment = when (it) {
                0 -> PasswordsFragment()
                1 -> PersonalInfoFragment()
                else -> PortfolioFragment()
            }
            childFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragment).commitAllowingStateLoss()
        })
    }

    private fun initView() {
        mBinding.drawerBtn.setOnClickListener {
            (activity as MainActivity?)?.openDrawer()
        }
    }

    override fun onDetach() {
        viewModel.reset()
        viewModel.setSelectedTab(0)
        super.onDetach()
    }
}