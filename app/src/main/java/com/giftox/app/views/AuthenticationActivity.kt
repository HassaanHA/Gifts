package com.giftox.app.views

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.giftox.app.R
import com.giftox.app.viewmodels.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationActivity : AppCompatActivity() {

    private val viewModel: AuthenticationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        viewModel.loginObservable.observe(this, {
            setResult(RESULT_OK)
            finish()
            overridePendingTransition(R.anim.hold, R.anim.slide_out_bottom)
        })
        viewModel.registerObservable.observe(this, {
            setResult(RESULT_OK)
            finish()
            overridePendingTransition(R.anim.hold, R.anim.slide_out_bottom)
        })
    }

    override fun onBackPressed() {
        viewModel.reset()
        super.onBackPressed()
        overridePendingTransition(R.anim.hold, R.anim.slide_out_bottom)
    }
}