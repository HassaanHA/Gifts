package com.giftox.app.views

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.giftox.app.R
import com.giftox.app.databinding.FragmentAddRatingBinding
import com.giftox.app.utils.showToast
import com.giftox.app.viewmodels.CelebrityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddRatingFragment : DialogFragment() {

    private lateinit var mBinding: FragmentAddRatingBinding
    private val viewModel: CelebrityViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentAddRatingBinding.inflate(inflater, container, false)
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
        viewModel.inProgress.observe(viewLifecycleOwner, {
            isCancelable = !it
        })
        viewModel.submitRatingObservable.observe(viewLifecycleOwner, {
            showToast(it?.message)
            dismiss()
        })
    }

    private fun initView() {
        mBinding.submitBtn.setOnClickListener {
            if (mBinding.reviewEt.text.isNullOrEmpty()) {
                mBinding.reviewEt.error = getString(R.string.error_required)
                mBinding.reviewEt.requestFocus()
            } else if (mBinding.ratingBar.rating == 0f) {
                showToast(getString(R.string.error_add_a_rating))
            } else {
                viewModel.submitRating(mBinding.ratingBar.rating.toInt(), mBinding.reviewEt.text.toString())
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        params?.width = resources.getDimension(R.dimen._280sdp).toInt()
        params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

}