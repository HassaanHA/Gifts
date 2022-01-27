package com.giftox.app.data

import com.google.gson.annotations.SerializedName

data class PiggyBank(
    val total: Float?,
    @SerializedName("item") val items: ArrayList<PiggyBankItem>
)

data class PiggyBankItem(
    val price: Int?,
    val date: String?
)