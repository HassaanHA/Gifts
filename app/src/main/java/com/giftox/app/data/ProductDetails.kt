package com.giftox.app.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ProductDetails(
    val id: Long,
    val title: String?,
    val description: String?,
    val icon: String?,
    @SerializedName("hover_icon") val hoverIcon: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(icon)
        parcel.writeString(hoverIcon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductDetails> {
        override fun createFromParcel(parcel: Parcel): ProductDetails {
            return ProductDetails(parcel)
        }

        override fun newArray(size: Int): Array<ProductDetails?> {
            return arrayOfNulls(size)
        }
    }
}