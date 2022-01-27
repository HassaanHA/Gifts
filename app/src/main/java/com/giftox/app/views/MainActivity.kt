package com.giftox.app.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.giftox.app.R
import com.giftox.app.databinding.ActivityMainBinding
import com.giftox.app.utils.Constants
import com.giftox.app.utils.isFirstLaunch
import com.giftox.app.viewmodels.MainViewModel
import com.giftox.app.views.on_boarding.OnBoardingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.lifecycleOwner = this
        mBinding.viewModel = viewModel
        keepSplashIfNeeded()
        setupObservers()
        initView()
    }

    private fun setupObservers() {

    }

    private fun initView() {
        mBinding.searchTab.setOnClickListener {
            val navOption = NavOptions.Builder().setPopUpTo(R.id.home_fragment, false).build()
            findNavController(R.id.nav_host).navigate(R.id.search_fragment, null, navOption)
        }
        mBinding.giftTab.setOnClickListener {
            val navOption = NavOptions.Builder().setPopUpTo(0, false).build()
            findNavController(R.id.nav_host).navigate(R.id.home_fragment, null, navOption)
        }
        mBinding.profileTab.setOnClickListener {
            if (viewModel.user.value != null) {
                val navOption = NavOptions.Builder().setPopUpTo(R.id.home_fragment, false).build()
                findNavController(R.id.nav_host).navigate(R.id.profile_fragment, null, navOption)
            } else {
                findNavController(R.id.nav_host).navigate(R.id.authentication_activity)
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.hold)
            }
        }
        mBinding.drawer.ordersBtn.setOnClickListener {
            mBinding.drawerLayout.close()
            val navOption = NavOptions.Builder().setPopUpTo(R.id.home_fragment, false).build()
            findNavController(R.id.nav_host).navigate(R.id.my_orders_fragment, null, navOption)
        }
        mBinding.drawer.tAndCBtn.setOnClickListener {
            startActivity(
                WebViewActivity.newInstance(
                    this,
                    "Terms & Conditions",
                    Constants.TERMS_CONDITIONS_URL
                )
            )
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.hold)
        }
        mBinding.drawer.tAndCBtn.setOnClickListener {
            openWebView("Terms & Conditions", Constants.TERMS_CONDITIONS_URL)
        }
        mBinding.drawer.privacyPolicyBtn.setOnClickListener {
            openWebView("Privacy Policy", Constants.PRIVACY_POLICY_URL)
        }
        mBinding.drawer.instaBtn.setOnClickListener {
            openSocial(Constants.INSTA_URL)
        }
        mBinding.drawer.linkedInBtn.setOnClickListener {
            openSocial(Constants.LINKED_IN_URL)
        }
        mBinding.drawer.facebookBtn.setOnClickListener {
            openSocial(Constants.FB_URL)
        }
        mBinding.drawer.twitterBtn.setOnClickListener {
            openSocial(Constants.TWITTER_URL)
        }
        mBinding.drawer.logoutBtn.setOnClickListener {
            mBinding.drawerLayout.close()
            LogoutFragment { mBinding.giftTab.callOnClick() }.show(supportFragmentManager, null)
        }
    }

    fun openDrawer() {
        mBinding.drawerLayout.open()
    }

    private fun openWebView(title: String, url: String) {
        startActivity(WebViewActivity.newInstance(this, title, url))
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.hold)
    }

    private fun openSocial(link: String) {
        startActivity(Intent.createChooser(Intent(Intent.ACTION_VIEW, Uri.parse(link)), "Choose"))
    }

    private fun keepSplashIfNeeded() {
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (isFirstLaunch()) {
                        startActivity(Intent(this@MainActivity, OnBoardingActivity::class.java))
                        finishAffinity()
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        false
                    } else {
                        true
                    }
                }
            }
        )
    }
}