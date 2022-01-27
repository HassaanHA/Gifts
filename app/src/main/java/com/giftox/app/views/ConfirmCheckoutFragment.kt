package com.giftox.app.views

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.giftox.app.R
import com.giftox.app.adapters.ProductsAdapter
import com.giftox.app.databinding.FragmentConfirmCheckoutBinding
import com.giftox.app.utils.showToast
import com.giftox.app.viewmodels.CelebrityViewModel
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.Order
import com.paypal.checkout.order.PurchaseUnit

class ConfirmCheckoutFragment : Fragment() {

    private val productsAdapter = ProductsAdapter(shouldShowSelectionOption = false)
    private lateinit var mBinding: FragmentConfirmCheckoutBinding
    private val viewModel: CelebrityViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentConfirmCheckoutBinding.inflate(inflater, container, false)
        mBinding.viewModel = viewModel
        mBinding.selectedProductsRv.adapter = productsAdapter
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.lifecycleOwner = viewLifecycleOwner
        setupObservers()
        initView()
    }

    private fun setupObservers() {
        viewModel.placeOrderObservable.observe(viewLifecycleOwner, {
            showToast(it?.message)
            if (it?.status == 200) {
                findNavController().navigate(
                    ConfirmCheckoutFragmentDirections.actionConfirmCheckoutFragmentToHomeFragment()
                )
            }
        })
    }

    private fun initView() {
        val tAndCSpan = SpannableString(getString(R.string.i_agree_to_the_terms_and_conditions))
        val clickableUnderlinedSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                showToast("TODO")
                mBinding.termsAndConditionsCb.isChecked = !mBinding.termsAndConditionsCb.isChecked
            }
        }
        tAndCSpan.setSpan(clickableUnderlinedSpan, 15, tAndCSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        mBinding.termsAndConditionsCb.text = tAndCSpan
        mBinding.termsAndConditionsCb.movementMethod = LinkMovementMethod.getInstance()
        mBinding.payPalButton.setup(
            createOrder = CreateOrder { createOrderActions ->
                val order = Order(
                    intent = OrderIntent.CAPTURE,
                    appContext = AppContext(
                        userAction = UserAction.PAY_NOW
                    ),
                    purchaseUnitList = listOf(
                        PurchaseUnit(
                            amount = Amount(
                                currencyCode = CurrencyCode.USD,
                                value = viewModel.total.toString()
                            )
                        )
                    )
                )
                createOrderActions.create(order)
            },
            onApprove = OnApprove { approval ->
                approval.orderActions.capture {
                    viewModel.placeOrder(approval.data)
                }
            }
        )
        mBinding.checkoutBtn.setOnClickListener {
            if (mBinding.termsAndConditionsCb.isChecked) {
                if (viewModel.user.value != null) {
                    mBinding.payPalButton.callOnClick()
                } else {
                    context?.let {
                        authResultLauncher.launch(Intent(it, AuthenticationActivity::class.java))
                    }
                }
            } else {
                showToast(R.string.error_terms_and_conditions_not_checked)
            }
        }
        mBinding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private val authResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            mBinding.payPalButton.callOnClick()
        }
    }

}