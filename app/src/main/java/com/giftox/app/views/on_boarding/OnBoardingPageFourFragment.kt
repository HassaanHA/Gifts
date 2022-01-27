package com.giftox.app.views.on_boarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.giftox.app.R
import com.giftox.app.databinding.FragmentOnBoardingPageFourBinding

class OnBoardingPageFourFragment : Fragment() {

    private lateinit var mBinding: FragmentOnBoardingPageFourBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentOnBoardingPageFourBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.playBtn.setOnClickListener {

        }
    }

}