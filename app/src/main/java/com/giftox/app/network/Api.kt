package com.giftox.app.network

import com.giftox.app.data.*
import okhttp3.RequestBody
import retrofit2.http.*

interface Api {

    @GET("home")
    suspend fun getHomeData(): Response<Home>

    @GET("occasions")
    suspend fun getOccasions(): Response<ArrayList<Occasion>>

    @GET("celebrity/{celebrityId}")
    suspend fun getCelebrity(@Path("celebrityId") celebrityId: Long): retrofit2.Response<Response<Celebrity>>

    @FormUrlEncoded
    @POST("celebrity/ratings")
    suspend fun submitReview(
        @Header("Authorization") authToken: String,
        @Field("celebrity_id") celebrityId: Long?,
        @Field("rating") rating: Int,
        @Field("review") review: String?,
    ): retrofit2.Response<Response<String>>

    @FormUrlEncoded
    @POST("categories")
    suspend fun getCategories(
        @Field("all") all: Boolean = true,
        @Field("is_detailed") isDetailed: Boolean = false
    ): Response<ArrayList<Category>>

    @POST("search")
    suspend fun search(
        @Query("limit") limit: Int = Int.MAX_VALUE,
        @Query("page") page: Int = 1,
        @Body searchParams: SearchParams
    ): Response<ArrayList<Celebrity>>

    @POST("auth/register")
    suspend fun register(
        @Body registerParams: RegisterParams
    ): Response<String>

    @POST("auth/register")
    suspend fun socialLogin(
        @Body registerParams: RegisterParams
    ): retrofit2.Response<Response<User>>

    @POST("auth/login")
    suspend fun login(
        @Body loginParams: LoginParams
    ): retrofit2.Response<Response<User>>

    @FormUrlEncoded
    @POST("change-password")
    suspend fun changePassword(
        @Header("Authorization") authToken: String,
        @Field("new_password") newPassword: String?,
        @Field("retype_new_password") retypeNewPassword: String?
    ): retrofit2.Response<Response<String>>

    @FormUrlEncoded
    @PUT("update-profile")
    suspend fun updateProfile(
        @Header("Authorization") authToken: String,
        @Field("first_name") firstName: String?,
        @Field("email") email: String?,
        @Field("phone") phone: String?,
        @Field("country") country: String?,
        @Field("birthday") birthday: String?,
        @Field("gender") gender: String?
    ): retrofit2.Response<Response<String>>

    @GET("profile")
    suspend fun getProfile(
        @Header("Authorization") authToken: String
    ): retrofit2.Response<Response<User>>

    @POST("portfolio")
    suspend fun updatePortfolio(
        @Header("Authorization") authToken: String,
        @Body body: RequestBody
    ): retrofit2.Response<Response<String>>

    @POST("orders")
    suspend fun placeOrder(
        @Header("Authorization") authToken: String,
        @Body orderParams: PlaceOrderParams
    ): retrofit2.Response<Unit>

    @GET("orders")
    suspend fun getOrders(
        @Header("Authorization") authToken: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = Int.MAX_VALUE
    ): Response<ArrayList<MyOrder>>

    @GET("orders/{orderId}")
    suspend fun getOrderDetails(
        @Header("Authorization") authToken: String,
        @Path("orderId") orderId: Long?,
    ): Response<MyOrder>

    @POST("order/update/{orderId}")
    suspend fun updateOrder(
        @Header("Authorization") authToken: String,
        @Path("orderId") orderId: Long?,
        @Body body: RequestBody
    ): retrofit2.Response<Response<String>>

    @GET("piggy-bank")
    suspend fun getPiggyBank(
        @Header("Authorization") authToken: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = Int.MAX_VALUE
    ): Response<PiggyBank>

}