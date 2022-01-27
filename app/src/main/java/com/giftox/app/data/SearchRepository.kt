package com.giftox.app.data

import com.giftox.app.network.Api
import com.giftox.app.network.SearchParams
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(
    private val categoryDao: CategoryDao,
    private val retrofit: Retrofit
) {

    suspend fun getCategories() = categoryDao.getAll().also {
        val response = retrofit.create(Api::class.java).getCategories()
        if (response.status == 200) {
            categoryDao.insert(response.data)
        }
    }

    suspend fun search(
        keyword: String? = null,
        price: Int? = null,
        categoryIds: ArrayList<Int>? = null
    ): ArrayList<Celebrity>? {
        val params = SearchParams(keyword, price, categoryIds)
        val result = retrofit.create(Api::class.java).search(searchParams = params)
        if (result.status == 200) {
            return result.data
        }
        return null
    }

}