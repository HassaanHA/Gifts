package com.giftox.app.utils

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import com.giftox.app.R

fun EditText.togglePasswordVisibility(visibilityStatus: Boolean, visibilityButton: ImageView) {
    transformationMethod =
        if (visibilityStatus) HideReturnsTransformationMethod.getInstance()
        else PasswordTransformationMethod.getInstance()
    visibilityButton.setImageResource(
        if (visibilityStatus) R.drawable.ic_hide_password else R.drawable.ic_show_password
    )
    if (text.isNotEmpty())
        setSelection(text.length)
}