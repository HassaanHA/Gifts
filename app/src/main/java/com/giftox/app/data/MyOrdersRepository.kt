package com.giftox.app.data

import com.giftox.app.network.Api
import com.giftox.app.utils.Constants
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("BlockingMethodInNonBlockingContext")
@Singleton
class MyOrdersRepository @Inject constructor(
    private val myOrderDao: MyOrderDao,
    private val retrofit: Retrofit
) {

    suspend fun getOrders(accessToken: String?) = myOrderDao.getAll().also {
        val bearerToken = "Bearer $accessToken"
        val response = retrofit.create(Api::class.java).getOrders(bearerToken)
        if (response.status == Constants.SUCCESS) {
            myOrderDao.nuke()
            myOrderDao.insert(response.data)
        }
    }

    suspend fun updateOrder(
        accessToken: String?, orderId: Long, orderType: String?,
        video: InputStream?, status: String?
    ): Response<String> {
        val bearerToken = "Bearer $accessToken"
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("type", orderType ?: "")
        if (video != null) {
            val part = MultipartBody.Part.createFormData(
                "video", "videoFile",
                video.readBytes().toRequestBody("application/octet-stream".toMediaTypeOrNull())
            )
            builder.addPart(part)
        }
        if (status != null) {
            builder.addFormDataPart("status", status)
        }
        val response = retrofit.create(Api::class.java).updateOrder(bearerToken, orderId, builder.build())
        return if (response.isSuccessful && response.body() != null) {
            val ordersResponse = retrofit.create(Api::class.java).getOrders(bearerToken)
            if (ordersResponse.status == Constants.SUCCESS) {
                myOrderDao.nuke()
                myOrderDao.insert(ordersResponse.data)
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

    suspend fun getPiggyBank(accessToken: String?): PiggyBank? {
        val bearerToken = "Bearer $accessToken"
        return retrofit.create(Api::class.java).getPiggyBank(bearerToken).data
    }

    suspend fun getOrderDetails(accessToken: String?, orderId: Long): MyOrder? {
        val bearerToken = "Bearer $accessToken"
        return retrofit.create(Api::class.java).getOrderDetails(bearerToken, orderId).data
    }

}