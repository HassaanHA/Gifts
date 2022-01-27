package com.giftox.app.data

data class Response<T>(
    var status: Int? = null,
    val data: T? = null,
    var message: String? = null
)