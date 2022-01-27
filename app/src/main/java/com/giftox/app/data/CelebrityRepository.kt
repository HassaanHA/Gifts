package com.giftox.app.data

import com.giftox.app.network.Api
import com.giftox.app.network.PlaceOrderParams
import org.json.JSONObject
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("BlockingMethodInNonBlockingContext")
@Singleton
class CelebrityRepository @Inject constructor(
    private val occasionDao: OccasionDao,
    private val retrofit: Retrofit
) {

    suspend fun getCelebrity(celebrityId: Long): Response<Celebrity> {
        val response = retrofit.create(Api::class.java).getCelebrity(celebrityId)
        return if (response.isSuccessful && response.body() != null) {
            response.body()!!
        } else {
            val errorResponse = Response<Celebrity>()
            val errorBody = response.errorBody()?.string()
            val error = JSONObject(errorBody ?: "")
            errorResponse.status = 401
            errorResponse.message = error.getString("message")
            errorResponse
        }
    }

    suspend fun submitReview(accessToken: String?, celebrityId: Long?, rating: Int, review: String?): Response<String> {
        val bearerToken = "Bearer $accessToken"
        val response = retrofit.create(Api::class.java).submitReview(bearerToken, celebrityId, rating, review)
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

    suspend fun getOccasions() = occasionDao.getAll().also {
        val response = retrofit.create(Api::class.java).getOccasions()
        if (response.status == 200) {
            occasionDao.insert(response.data)
        }
    }

    suspend fun placeOrder(accessToken: String?, orderParams: PlaceOrderParams): Response<String> {
        val bearerToken = "Bearer $accessToken"
        val response = retrofit.create(Api::class.java).placeOrder(bearerToken, orderParams)
        return if (response.isSuccessful && response.body() != null) {
            Response(200, "", "Order Placed!")
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