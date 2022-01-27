package com.giftox.app.network

import com.google.gson.annotations.SerializedName

data class SearchParams(
    val keyword: String? = null,
    val price: Int? = null,
    @SerializedName("category_id") val categoryIds: ArrayList<Int>? = null
)