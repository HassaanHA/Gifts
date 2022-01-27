package com.giftox.app.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class MyOrder(
    @PrimaryKey @ColumnInfo(name = "my_order_id") val id: Long,
    @SerializedName("order_id") val orderId: Long,
    @ColumnInfo(name = "my_order_price") val price: Int,
    @SerializedName("special_wishes") val specialWishes: String?,
    var status: String?,
    val type: String?,
    @ColumnInfo(name = "my_order_video")val video: String?,
    @SerializedName("created_at") val createdAt: String?,
    @Embedded @SerializedName("celebrity_details") val celebrity: Celebrity?,
    @SerializedName("producd_details") val productDetails: ProductDetails?,
    @SerializedName("occasion_details") val occasion: Occasion?,
    @Embedded @SerializedName("customer_details") val customer: User?
)