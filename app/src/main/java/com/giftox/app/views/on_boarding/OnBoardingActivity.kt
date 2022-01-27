package com.giftox.app.views.on_boarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.giftox.app.R
import com.giftox.app.databinding.ActivityOnBoardingBinding
import com.giftox.app.views.MainActivity

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_on_boarding)
        mBinding.onBoardingVp.adapter = OnBoardingPagerAdapter(this)
        mBinding.onBoardingVp.registerOnPageChangeCallback(pageChangeCallback)
        mBinding.skipButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }
    }

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            mBinding.viewPagerIndicator.setSelectedIndex(position)
            if (position == 3)
                mBinding.skipButton.text = getString(R.string._continue)
        }
    }
}