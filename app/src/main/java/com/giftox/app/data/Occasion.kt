package com.giftox.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Occasion(
    @PrimaryKey val id: Long,
    val title: String?,
    val icon: String?
)