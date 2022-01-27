package com.giftox.app.viewmodels

import android.os.Bundle
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.AccessToken
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import com.giftox.app.R
import com.giftox.app.data.AuthenticationRepository
import com.giftox.app.data.Category
import com.giftox.app.utils.Constants
import com.giftox.app.utils.SingleLiveEvent
import com.giftox.app.utils.getIdsArrayList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    val firstName: MutableLiveData<String> = MutableLiveData()
    val lastName: MutableLiveData<String> = MutableLiveData()
    val dateOfBirth: MutableLiveData<String?> = MutableLiveData(null)
    val repeatPassword: MutableLiveData<String> = MutableLiveData()
    val description: MutableLiveData<String?> = MutableLiveData(null)
    val termsAndConditionsChecked: MutableLiveData<Boolean> = MutableLiveData(false)

    private val selectedCategories: ArrayList<Category> = ArrayList()

    val email: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()

    val emailError: MutableLiveData<Int?> = MutableLiveData()
    val passwordError: MutableLiveData<Int?> = MutableLiveData()

    val firstNameError: MutableLiveData<Int?> = MutableLiveData()
    val lastNameError: MutableLiveData<Int?> = MutableLiveData()
    val repeatPasswordError: MutableLiveData<Int?> = MutableLiveData()
    val descriptionError: MutableLiveData<Int?> = MutableLiveData()
    val validationErrors: MutableLiveData<Int?> = MutableLiveData()

    val loginErrorMessage: MutableLiveData<String?> = MutableLiveData()
    val registerErrorMessage: MutableLiveData<String?> = MutableLiveData()

    val inProgress: MutableLiveData<Boolean> = MutableLiveData(false)

    val isPasswordVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    val isRepeatPasswordVisible: MutableLiveData<Boolean> = MutableLiveData(false)

    val loginObservable: SingleLiveEvent<Void> = SingleLiveEvent()
    val registerObservable: SingleLiveEvent<Void> = SingleLiveEvent()

    val registrationType: SingleLiveEvent<String> = SingleLiveEvent()

    private val _categories: MutableLiveData<List<Category>?> = MutableLiveData()
    val categories: LiveData<List<Category>?> = _categories

    init {
        viewModelScope.launch(Dispatchers.IO) {
            authenticationRepository.getCategories().collect {
                _categories.postValue(it)
            }
        }
    }

    fun setRegistrationType(type: String) {
        registrationType.value = type
    }

    fun togglePasswordVisibility() {
        isPasswordVisible.value = isPasswordVisible.value?.not()
    }

    fun toggleRepeatPasswordVisibility() {
        isRepeatPasswordVisible.value = isRepeatPasswordVisible.value?.not()
    }

    fun onCategorySelectionChanged(category: Category) {
        if (selectedCategories.contains(category))
            selectedCategories.remove(category)
        else
            selectedCategories.add(category)
    }

    fun login() {
        when {
            email.value.isNullOrEmpty() -> emailError.value = R.string.error_email_required
            Patterns.EMAIL_ADDRESS.matcher(email.value.toString()).matches().not() -> emailError.value =
                R.string.error_email_invalid
            password.value.isNullOrEmpty() -> passwordError.value = R.string.error_password_required
            password.value!!.length < 8 -> passwordError.value = R.string.error_password_length
            else -> {
                inProgress.postValue(true)
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        val response = authenticationRepository.login(email.value, password.value)
                        if (response.status == Constants.SUCCESS) {
                            val user = response.data
                            authenticationRepository.updateUser(user)
                            withContext(Dispatchers.Main) { loginObservable.call() }
                        } else {
                            loginErrorMessage.postValue(response.message ?: "")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        loginErrorMessage.postValue(e.localizedMessage)
                    } finally {
                        inProgress.postValue(false)
                    }
                }
            }
        }
    }

    fun register() {
        when {
            firstName.value.isNullOrEmpty() -> firstNameError.value = R.string.error_required
            lastName.value.isNullOrEmpty() -> lastNameError.value = R.string.error_required
            registrationType.value == Constants.userTypeCustomer &&
                    dateOfBirth.value.isNullOrEmpty() -> validationErrors.value = R.string.error_birthday_required
            email.value.isNullOrEmpty() -> emailError.value = R.string.error_email_required
            Patterns.EMAIL_ADDRESS.matcher(email.value.toString()).matches().not() -> emailError.value =
                R.string.error_email_invalid
            password.value.isNullOrEmpty() -> passwordError.value = R.string.error_password_required
            password.value!!.length < 8 -> passwordError.value = R.string.error_password_length
            repeatPassword.value.isNullOrEmpty() -> repeatPasswordError.value = R.string.error_required
            password.value != repeatPassword.value -> repeatPasswordError.value = R.string.error_passwords_do_not_match
            registrationType.value == Constants.userTypeCelebrity &&
                    description.value.isNullOrEmpty() -> descriptionError.value = R.string.error_required
            registrationType.value == Constants.userTypeCelebrity &&
                    selectedCategories.isEmpty() -> validationErrors.value = R.string.error_category_required
            termsAndConditionsChecked.value == false -> validationErrors.value =
                R.string.error_terms_and_conditions_not_checked
            else -> {
                inProgress.value = true
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        val response = authenticationRepository.register(
                            firstName.value, lastName.value, dateOfBirth.value,
                            email.value, password.value, description.value,
                            selectedCategories.getIdsArrayList(), registrationType.value
                        )
                        if (response.status == Constants.SUCCESS) {
                            withContext(Dispatchers.Main) {
                                login()
                            }
                        } else {
                            registerErrorMessage.postValue(response.message ?: "")
                            inProgress.postValue(false)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        registerErrorMessage.postValue(e.localizedMessage)
                        inProgress.postValue(false)
                    }
                }
            }
        }
    }

    fun socialLogin(accessToken: AccessToken?) {
        val infoRequest =
            GraphRequest.newMeRequest(
                accessToken
            ) { jsonObject, _ ->
                if (jsonObject != null) {
                    val id = if (jsonObject.has("id")) jsonObject["id"] as String else ""
                    val name = if (jsonObject.has("name")) jsonObject["name"] as String else ""
                    val nameSplits = name.split(" ")
                    val email = if (jsonObject.has("email")) jsonObject["email"] as String else ""
                    inProgress.value = true
                    viewModelScope.launch(Dispatchers.IO) {
                        try {
                            val response = authenticationRepository.socialLogin(
                                firstName = nameSplits[0], lastName = if (nameSplits.size > 1) nameSplits[1] else "",
                                email = email, providerId = id
                            )
                            if (response.status == Constants.SUCCESS) {
                                val user = response.data
                                authenticationRepository.updateUser(user)
                                loginObservable.call()
                            } else {
                                loginErrorMessage.postValue(response.message ?: "")
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            loginErrorMessage.postValue(e.localizedMessage)
                            inProgress.postValue(false)
                        }
                    }
                }
            }
        val params = Bundle()
        params.putString("fields", "id, name, email")
        infoRequest.parameters = params
        infoRequest.executeAsync()
    }

    val facebookLoginCallback = object : FacebookCallback<LoginResult> {
        override fun onSuccess(result: LoginResult?) {
            socialLogin(result?.accessToken)
        }

        override fun onCancel() {
            //do nothing
        }

        override fun onError(error: FacebookException?) {
            loginErrorMessage.postValue(error?.localizedMessage ?: "")
        }
    }

    fun reset() {
        try {
            email.value = ""
            password.value = ""
            firstName.value = ""
            lastName.value = ""
            dateOfBirth.value = null
            repeatPassword.value = ""
            description.value = null
            loginErrorMessage.value = null
            registerErrorMessage.value = null
            emailError.value = null
            passwordError.value = null
            firstNameError.value = null
            lastNameError.value = null
            repeatPasswordError.value = null
            descriptionError.value = null
            validationErrors.value = null
            inProgress.value = false
            termsAndConditionsChecked.value = false
            selectedCategories.clear()
            isPasswordVisible.value = false
            isRepeatPasswordVisible.value = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}