package com.giftox.app.views.on_boarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnBoardingPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnBoardingPageOneFragment()
            1 -> OnBoardingPageTwoFragment()
            2 -> OnBoardingPageThreeFragment()
            else -> OnBoardingPageFourFragment()
        }
    }
}