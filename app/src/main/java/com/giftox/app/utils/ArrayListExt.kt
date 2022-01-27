package com.giftox.app.utils

import com.giftox.app.data.Category
import com.giftox.app.data.Product
import com.giftox.app.data.SocialLink
import com.giftox.app.data.Tweet
import java.lang.StringBuilder

fun ArrayList<Tweet>.toPlainString(): String {
    val toReturn = StringBuilder()
    for (i in 0 until size) {
        toReturn.append(get(i).text)
        if (i != size - 1)
            toReturn.append(", ")
    }
    return toReturn.toString()
}

fun ArrayList<Category>.getIdsArrayList(): ArrayList<Int> {
    val toReturn: ArrayList<Int> = ArrayList()
    forEach {
        toReturn.add(it.id.toInt())
    }
    return toReturn
}

fun ArrayList<Product>.toSocialLinksList():ArrayList<SocialLink>{
    val toReturn: ArrayList<SocialLink> = ArrayList()
    forEach {
        toReturn.add(SocialLink(it.id, it.link, null))
    }
    return toReturn
}