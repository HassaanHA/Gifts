package com.giftox.app.network

import com.google.gson.annotations.SerializedName

data class RegisterParams(
    @SerializedName("first_name") val firstName: String? = null,
    @SerializedName("last_name") val lastName: String? = null,
    @SerializedName("date_of_birth") val dateOfBirth: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("password") val password: String? = null,
    @SerializedName("password_confirmation") val passwordConfirmation: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("category_id") val categoryIds: ArrayList<Int>? = null,
    @SerializedName("user_type") val userType: String? = null,
    @SerializedName("is_social") val isSocial: Boolean? = null,
    @SerializedName("provider") val provider: String? = null,
    @SerializedName("provider_id") val providerId: String? = null,
    @SerializedName("device_token") val deviceToken: String? = null,
    @SerializedName("device_type") val deviceType: String = "android"
)