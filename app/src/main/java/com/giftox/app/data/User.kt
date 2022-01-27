package com.giftox.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class User(
    @PrimaryKey val id: Long,
    @SerializedName("first_name") var firstName: String?,
    @SerializedName("last_name") var lastName: String?,
    var email: String?,
    var avatar: String?,
    var gender: String?,
    @SerializedName("is_social") val isSocial: Boolean = false,
    @SerializedName("cover_image") var coverImage: String?,
    @SerializedName("date_of_birth") var dateOfBirth: String?,
    @SerializedName("user_type") val userType: String?,
    @SerializedName("video_link") var videoLink: String?,
    var phone: String?,
    @SerializedName("country_of_residence") var countryOfResidence: String?,
    var description: String?,
    @SerializedName("short_description") var shortDescription: String?,
    @SerializedName("products") var products: ArrayList<Product>?,
    @SerializedName("category") var category: ArrayList<Category>?,
    @SerializedName("social_links") var socialLink: ArrayList<SocialLink>?,
    @SerializedName("access_token") var accessToken: String?
)