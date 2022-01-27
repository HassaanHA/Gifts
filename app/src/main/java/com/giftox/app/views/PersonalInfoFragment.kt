package com.giftox.app.views

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.giftox.app.databinding.FragmentPersonalInfoBinding
import com.giftox.app.utils.showCountriesDialog
import com.giftox.app.utils.showDatePickerDialog
import com.giftox.app.utils.showToast
import com.giftox.app.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalInfoFragment : Fragment() {

    private lateinit var mBinding: FragmentPersonalInfoBinding
    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentPersonalInfoBinding.inflate(inflater, container, false)
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
        viewModel.updatePersonalInfoObservable.observe(this, {
            showToast(it.message)
        })
        viewModel.validationErrors.observe(this, {
            showToast(it)
        })
    }

    private fun initView() {
        mBinding.birthdayTv.setOnClickListener {
            context?.showDatePickerDialog(onBirthdaySetListener)
        }
        mBinding.countryTv.setOnClickListener {
            context?.showCountriesDialog({
                viewModel.country.value = it
            }, viewModel.country.value)
        }
        mBinding.genderRg.setOnCheckedChangeListener { group, checkedId ->
            viewModel.gender.value = group.findViewById<RadioButton>(checkedId).text.toString().lowercase()
        }
    }

    private val onBirthdaySetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        viewModel.birthday.value = "$year-${String.format("%2d", month)}-${String.format("%2d", dayOfMonth)}"
    }
}