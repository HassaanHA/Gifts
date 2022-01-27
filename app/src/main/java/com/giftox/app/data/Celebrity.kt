package com.giftox.app.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Celebrity(
    @PrimaryKey @ColumnInfo(name = "celebrity_id") val id: Long,
    @SerializedName("first_name") @ColumnInfo(name = "celebrity_first_name")val firstName: String?,
    @SerializedName("last_name") @ColumnInfo(name = "celebrity_last_name")val lastName: String?,
    @ColumnInfo(name = "celebrity_email") val email: String?,
    @ColumnInfo(name = "celebrity_avatar") val avatar: String?,
    @ColumnInfo(name = "celebrity_cover_image") @SerializedName("cover_image") val coverImage: String?,
    val video: String?,
    @ColumnInfo(name = "celebrity_phone") val phone: String?,
    @ColumnInfo(name = "celebrity_country_of_residence") @SerializedName("country_of_residence") val countryOfResidence: String?,
    @ColumnInfo(name = "celebrity_description") val description: String?,
    @SerializedName("avg_ratings") val avgRatings: Float?,
    @SerializedName("ratings_count") val ratingsCount: Int?,
    @SerializedName("celebrity_products") val celebrityProducts: ArrayList<Product>?,
    @SerializedName("ratings") val ratings: ArrayList<Rating>?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        arrayListOf<Product>().apply {
            parcel.readList(this, Product::class.java.classLoader)
        },
        arrayListOf<Rating>().apply {
            parcel.readList(this, Rating::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(email)
        parcel.writeString(avatar)
        parcel.writeString(coverImage)
        parcel.writeString(video)
        parcel.writeString(phone)
        parcel.writeString(countryOfResidence)
        parcel.writeString(description)
        parcel.writeValue(avgRatings)
        parcel.writeValue(ratingsCount)
        parcel.writeTypedList(celebrityProducts)
        parcel.writeTypedList(ratings)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Celebrity> {
        override fun createFromParcel(parcel: Parcel): Celebrity {
            return Celebrity(parcel)
        }

        override fun newArray(size: Int): Array<Celebrity?> {
            return arrayOfNulls(size)
        }
    }
}