package com.giftox.app.data

import com.giftox.app.network.Api
import com.giftox.app.utils.Constants
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import java.io.File
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("BlockingMethodInNonBlockingContext")
@Singleton
class ProfileRepository @Inject constructor(
    private val userDao: UserDao,
    private val categoryDao: CategoryDao,
    private val retrofit: Retrofit
) {

    suspend fun getCategories() = categoryDao.getAll().also {
        val response = retrofit.create(Api::class.java).getCategories()
        if (response.status == 200) {
            categoryDao.insert(response.data)
        }
    }

    suspend fun changePassword(accessToken: String?, newPassword: String?): Response<String> {
        val bearerToken = "Bearer $accessToken"
        val response = retrofit.create(Api::class.java).changePassword(bearerToken, newPassword, newPassword)
        return if (response.isSuccessful && response.body() != null) {
            response.body()!!
        } else {
            val errorResponse = Response<String>()
            val errorBody = response.errorBody()?.string()
            val error = JSONObject(errorBody ?: "")
            errorResponse.status = 401
            errorResponse.message = error.getString("message")
            errorResponse
        }
    }

    suspend fun updateProfile(
        accessToken: String?, firstName: String?, email: String?, phone: String?,
        country: String?, birthday: String?, gender: String?
    ): Response<String> {
        val bearerToken = "Bearer $accessToken"
        val response = retrofit.create(Api::class.java).updateProfile(
            bearerToken, firstName, email, phone, country, birthday, gender
        )
        return if (response.isSuccessful && response.body() != null) {
            val userResponse = getProfile(bearerToken)
            if (userResponse.status == Constants.SUCCESS) {
                val userData = userResponse.data
                userData?.accessToken = accessToken
                userDao.insert(userData)
            }
            response.body()!!
        } else {
            val errorResponse = Response<String>()
            val errorBody = response.errorBody()?.string()
            val error = JSONObject(errorBody ?: "")
            errorResponse.status = 401
            errorResponse.message = error.getString("message")
            errorResponse
        }
    }

    private suspend fun getProfile(accessToken: String?): Response<User> {
        val bearerToken = "Bearer $accessToken"
        val response = retrofit.create(Api::class.java).getProfile(bearerToken)
        return if (response.isSuccessful && response.body() != null) {
            response.body()!!
        } else {
            val errorResponse = Response<User>()
            val errorBody = response.errorBody()?.string()
            val error = JSONObject(errorBody ?: "")
            errorResponse.status = error.getInt("status")
            errorResponse.message = error.getString("message")
            errorResponse
        }
    }

    suspend fun updatePortfolio(
        accessToken: String?, avatar: String?, promoVideo: InputStream?, coverImage: String?,
        categories: ArrayList<Category>, socialLinks: ArrayList<SocialLink>, products: ArrayList<Product>
    ): Response<String> {
        val bearerToken = "Bearer $accessToken"
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        if (avatar != null) {
            val file = File(avatar)
            builder.addFormDataPart(
                "image", file.name,
                file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
            )
        }
        if (promoVideo != null) {
            val part = MultipartBody.Part.createFormData(
                "video", "videoFile",
                promoVideo.readBytes().toRequestBody("application/octet-stream".toMediaTypeOrNull())
            )
            builder.addPart(part)
        }
        if (coverImage != null) {
            val file = File(coverImage)
            builder.addFormDataPart(
                "cover_image", file.name,
                file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
            )
        }
        for (i in categories.indices) {
            builder.addFormDataPart("category_ids[$i]", categories[i].id.toString())
        }
        for (i in socialLinks.indices) {
            builder.addFormDataPart("social_types[$i]", socialLinks[i].type ?: "")
            builder.addFormDataPart("social_values[$i]", socialLinks[i].link ?: "")
        }
        products.forEach {
            builder.addFormDataPart("products[${it.id}]", it.price.toString())
        }
        val response = retrofit.create(Api::class.java).updatePortfolio(bearerToken, builder.build())
        return if (response.isSuccessful && response.body() != null) {
            val userResponse = getProfile(bearerToken)
            if (userResponse.status == Constants.SUCCESS) {
                val userData = userResponse.data
                userData?.accessToken = accessToken
                userDao.insert(userData)
            }
            response.body()!!
        } else {
            val errorResponse = Response<String>()
            val errorBody = response.errorBody()?.string()
            val error = JSONObject(errorBody ?: "")
            errorResponse.status = 401
            errorResponse.message = error.getString("message")
            errorResponse
        }
    }

}