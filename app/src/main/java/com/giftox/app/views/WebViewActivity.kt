package com.giftox.app.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.giftox.app.R
import com.giftox.app.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityWebViewBinding

    companion object {

        const val titlePassKey = "titlePassKey"
        const val urlPassKey = "urlPassKey"

        fun newInstance(context: Context, title: String, url: String): Intent {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(titlePassKey, title)
            intent.putExtra(urlPassKey, url)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_web_view)
        initView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        mBinding.webView.settings.javaScriptEnabled = true
        val url = intent.getStringExtra(urlPassKey) ?: ""
        mBinding.actionBarTitleTv.text = intent.getStringExtra(titlePassKey)
        mBinding.actionBarTitleTv.visibility = View.VISIBLE
        mBinding.webView.loadUrl(url)
        mBinding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        if (mBinding.webView.canGoBack())
            mBinding.webView.goBack()
        else {
            super.onBackPressed()
            overridePendingTransition(R.anim.hold, R.anim.slide_out_bottom)
        }
    }

}