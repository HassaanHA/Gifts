package com.giftox.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Home(
    @PrimaryKey val id: Int = 1,
    val banners: ArrayList<Celebrity>?,
    val tweets: ArrayList<Tweet>?,
    val collections: ArrayList<Collection>?
)

data class Tweet(
    val text: String
)