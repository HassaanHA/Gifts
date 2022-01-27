package com.giftox.app.network

import com.google.gson.annotations.SerializedName

data class PlaceOrderParams(
    @SerializedName("celebrity_id") var celebrityId: String? = null,
    @SerializedName("product_users") var productUsers: HashMap<String, String?>? = null,
    @SerializedName("to") var to: String? = null,
    @SerializedName("receiver") var receiver: String? = null,
    @SerializedName("occasion_id") var occasionId: String? = null,
    @SerializedName("special_wishes") var specialWishes: String? = null,
    @SerializedName("links") var links: HashMap<String, String?>? = null,
    @SerializedName("agree") var agree: String? = null,
    @SerializedName("is_public") var isPublic: String? = null,
    @SerializedName("payment_info") var paymentInfo: PaymentInfo? = null,
)

data class PaymentInfo(
    @SerializedName("paymentId") var paymentId: String? = null,
    @SerializedName("token") var token: String? = null,
    @SerializedName("payerId") var payerId: String? = null
)