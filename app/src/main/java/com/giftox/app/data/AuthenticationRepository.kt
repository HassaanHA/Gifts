package com.giftox.app.data

import android.content.SharedPreferences
import com.giftox.app.network.Api
import com.giftox.app.network.LoginParams
import com.giftox.app.network.RegisterParams
import com.giftox.app.utils.Constants
import org.json.JSONObject
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("BlockingMethodInNonBlockingContext")
@Singleton
class AuthenticationRepository @Inject constructor(
    private val userDao: UserDao,
    private val categoryDao: CategoryDao,
    private val sharedPreferences: SharedPreferences,
    private val retrofit: Retrofit
) {

    suspend fun getCategories() = categoryDao.getAll().also {
        val response = retrofit.create(Api::class.java).getCategories()
        if (response.status == 200) {
            categoryDao.insert(response.data)
        }
    }

    suspend fun register(
        firstName: String?, lastName: String?, dateOfBirth: String?, email: String?, password: String?,
        description: String?, categoryIds: ArrayList<Int>?, userType: String?
    ): Response<String> {
        val registerParams = RegisterParams(
            firstName, lastName, dateOfBirth, email, password,
            password, description, categoryIds, userType
        )
        return retrofit.create(Api::class.java).register(registerParams)
    }

    suspend fun socialLogin(
        firstName: String?, lastName: String?, email: String?,
        providerId: String?, provider: String? = "facebook"
    ): Response<User> {
        val registerParams = RegisterParams(
            firstName = firstName, lastName = lastName, email = email, providerId = providerId,
            userType = "customer", isSocial = true, provider = provider,
            deviceToken = sharedPreferences.getString(Constants.DEVICE_TOKEN, null)
        )
        val response = retrofit.create(Api::class.java).socialLogin(registerParams)
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

    suspend fun login(email: String?, password: String?): Response<User> {
        val loginParams = LoginParams(
            email = email, password = password,
            deviceToken = sharedPreferences.getString(Constants.DEVICE_TOKEN, null)
        )
        val response = retrofit.create(Api::class.java).login(loginParams)
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

    fun getLoggedInUser() = userDao.getLoggedInUser()

    suspend fun updateUser(user: User?) {
        userDao.insert(user)
    }

    suspend fun logout() {
        userDao.nuke()
    }

}