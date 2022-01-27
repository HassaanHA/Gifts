package com.giftox.app.data

data class Collection(
    val id:Long?,
    val title:String?,
    val icon:String?,
    val description:String?,
    val celebrities: ArrayList<Celebrity>?
)