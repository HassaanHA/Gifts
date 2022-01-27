package com.giftox.app.data

import com.giftox.app.network.Api
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    private val homeDao:HomeDao,
    private val retrofit: Retrofit
){

    suspend fun getHomeData() = homeDao.getData().also {
        val response = retrofit.create(Api::class.java).getHomeData()
        if (response.status == 200){
            homeDao.insert(response.data)
        }
    }

}