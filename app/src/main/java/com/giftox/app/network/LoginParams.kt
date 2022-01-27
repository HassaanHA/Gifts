package com.giftox.app.network

import com.google.gson.annotations.SerializedName

data class LoginParams(
    @SerializedName("email") val email: String? = null,
    @SerializedName("password") val password: String? = null,
    @SerializedName("device_type") val deviceType: String = "android",
    @SerializedName("device_token") val deviceToken: String? = null
)