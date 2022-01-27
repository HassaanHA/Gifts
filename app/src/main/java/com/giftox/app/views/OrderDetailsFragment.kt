package com.giftox.app.views

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.giftox.app.R
import com.giftox.app.data.MyOrder
import com.giftox.app.databinding.FragmentOrderDetailsBinding
import com.giftox.app.utils.showOrderStatusOptionsDialog
import com.giftox.app.utils.showToast
import com.giftox.app.viewmodels.MyOrdersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailsFragment(private val myOrder: MyOrder) : DialogFragment() {

    private lateinit var mBinding: FragmentOrderDetailsBinding
    private val viewModel: MyOrdersViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentOrderDetailsBinding.inflate(inflater, container, false)
        mBinding.viewModel = viewModel
        viewModel.fetchOrderDetails(myOrder.id)
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
        viewModel.updateOrderObservable.observe(viewLifecycleOwner, {
            showToast(it.message)
            dismiss()
        })
    }

    private fun initView() {
        mBinding.editBtn.setOnClickListener {
            context?.let {
                it.showOrderStatusOptionsDialog { status ->
                    viewModel.updateOrder(
                        orderId = myOrder.id,
                        orderType = myOrder.type,
                        status = status
                    )
                }
            }
        }
        mBinding.uploadVideoBtn.setOnClickListener {
            val intent = Intent()
            intent.type = "video/*"
            intent.action = Intent.ACTION_GET_CONTENT
            val chooser = Intent.createChooser(intent, "Choose Video")
            resultLauncherForVideo.launch(chooser)
        }
        mBinding.closeBtn.setOnClickListener {
            dismiss()
        }
    }

    private val resultLauncherForVideo =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data != null && it.data!!.data != null) {
                context?.let { context ->
                    val videoInputStream = context.contentResolver.openInputStream(it.data!!.data!!)
                    viewModel.updateOrder(
                        orderId = myOrder.id,
                        orderType = myOrder.type,
                        video = videoInputStream
                    )
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.selectedOrder.postValue(null)
    }
}