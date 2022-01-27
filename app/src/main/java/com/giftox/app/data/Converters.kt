package com.giftox.app.data

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun celebrityListToString(celebrityList: ArrayList<Celebrity>?): String? {
        if (celebrityList == null) return null
        return Gson().toJson(celebrityList)
    }

    @TypeConverter
    fun stringToCelebrityList(string: String?): ArrayList<Celebrity>? {
        if (string == null) return null
        return ArrayList(Gson().fromJson(string, Array<Celebrity>::class.java).asList())
    }

    @TypeConverter
    fun collectionListToString(collectionList: ArrayList<Collection>?): String? {
        if (collectionList == null) return null
        return Gson().toJson(collectionList)
    }

    @TypeConverter
    fun stringToCollectionList(string: String?): ArrayList<Collection>? {
        if (string == null) return null
        return ArrayList(Gson().fromJson(string, Array<Collection>::class.java).asList())
    }

    @TypeConverter
    fun tweetListToString(tweetList: ArrayList<Tweet>?): String? {
        if (tweetList == null) return null
        return Gson().toJson(tweetList)
    }

    @TypeConverter
    fun stringToTweetList(string: String?): ArrayList<Tweet>? {
        if (string == null) return null
        return ArrayList(Gson().fromJson(string, Array<Tweet>::class.java).asList())
    }

    @TypeConverter
    fun productListToString(productList: ArrayList<Product>?): String? {
        if (productList == null) return null
        return Gson().toJson(productList)
    }

    @TypeConverter
    fun stringToProductList(string: String?): ArrayList<Product>? {
        if (string == null) return null
        return ArrayList(Gson().fromJson(string, Array<Product>::class.java).asList())
    }

    @TypeConverter
    fun socialLinksListToString(socialLinksList: ArrayList<SocialLink>?): String? {
        if (socialLinksList == null) return null
        return Gson().toJson(socialLinksList)
    }

    @TypeConverter
    fun stringToSocialLinksList(string: String?): ArrayList<SocialLink>? {
        if (string == null) return null
        return ArrayList(Gson().fromJson(string, Array<SocialLink>::class.java).asList())
    }

    @TypeConverter
    fun categoryListToString(categoryList: ArrayList<Category>?): String? {
        if (categoryList == null) return null
        return Gson().toJson(categoryList)
    }

    @TypeConverter
    fun stringToCategoryList(string: String?): ArrayList<Category>? {
        if (string == null) return null
        return ArrayList(Gson().fromJson(string, Array<Category>::class.java).asList())
    }

    @TypeConverter
    fun ratingListToString(ratingList: ArrayList<Rating>?): String? {
        if (ratingList == null) return null
        return Gson().toJson(ratingList)
    }

    @TypeConverter
    fun stringToRatingList(string: String?): ArrayList<Rating>? {
        if (string == null) return null
        return ArrayList(Gson().fromJson(string, Array<Rating>::class.java).asList())
    }

    @TypeConverter
    fun productToString(product: Product?): String? {
        if (product == null) return null
        return Gson().toJson(product)
    }

    @TypeConverter
    fun stringToProduct(string: String?): Product? {
        if (string == null) return null
        return Gson().fromJson(string, Product::class.java)
    }

    @TypeConverter
    fun productDetailsToString(productDetails: ProductDetails?): String? {
        if (productDetails == null) return null
        return Gson().toJson(productDetails)
    }

    @TypeConverter
    fun stringToProductDetails(string: String?): ProductDetails? {
        if (string == null) return null
        return Gson().fromJson(string, ProductDetails::class.java)
    }

    @TypeConverter
    fun occasionToString(occasion: Occasion?): String? {
        if (occasion == null) return null
        return Gson().toJson(occasion)
    }

    @TypeConverter
    fun stringToOccasion(string: String?): Occasion? {
        if (string == null) return null
        return Gson().fromJson(string, Occasion::class.java)
    }

}