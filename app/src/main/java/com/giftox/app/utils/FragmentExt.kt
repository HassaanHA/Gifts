package com.giftox.app.utils

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(message: String?, length: Int = Toast.LENGTH_LONG) {
    if (message != null)
        context?.let { Toast.makeText(it, message, length).show() }
}

fun Fragment.showToast(stringResId: Int?, length: Int = Toast.LENGTH_LONG) {
    if (stringResId != null)
        context?.let { Toast.makeText(it, getString(stringResId), length).show() }
}