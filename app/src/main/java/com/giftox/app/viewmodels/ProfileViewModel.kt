package com.giftox.app.viewmodels

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giftox.app.R
import com.giftox.app.data.*
import com.giftox.app.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val userDao: UserDao
) : ViewModel() {

    private val _user: MutableLiveData<User?> = MutableLiveData()
    val user: LiveData<User?> = _user

    val selectedTab: MutableLiveData<Int> = MutableLiveData(0)
    val inProgress: SingleLiveEvent<Boolean?> = SingleLiveEvent()

    val newPassword: MutableLiveData<String> = MutableLiveData()
    val repeatNewPassword: MutableLiveData<String> = MutableLiveData()
    val changePasswordObservable: SingleLiveEvent<Response<String>> = SingleLiveEvent()

    val firstName: MutableLiveData<String> = MutableLiveData()
    val email: MutableLiveData<String> = MutableLiveData()
    val mobile: MutableLiveData<String> = MutableLiveData()
    val country: MutableLiveData<String> = MutableLiveData()
    val birthday: MutableLiveData<String> = MutableLiveData()
    val gender: MutableLiveData<String> = MutableLiveData()
    val updatePersonalInfoObservable: SingleLiveEvent<Response<String>> = SingleLiveEvent()

    val firstNameError: MutableLiveData<Int?> = MutableLiveData()
    val emailError: MutableLiveData<Int?> = MutableLiveData()
    val mobileError: MutableLiveData<Int?> = MutableLiveData()
    val validationErrors: SingleLiveEvent<Int?> = SingleLiveEvent()

    val newPasswordError: MutableLiveData<Int?> = MutableLiveData()
    val repeatNewPasswordError: MutableLiveData<Int?> = MutableLiveData()

    val isNewPasswordVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    val isRepeatNewPasswordVisible: MutableLiveData<Boolean> = MutableLiveData(false)

    private val _categories: MutableLiveData<List<Category>?> = MutableLiveData()
    val categories: LiveData<List<Category>?> = _categories

    val selectedCategories: ArrayList<Category> = ArrayList()
    var socialLinks: ArrayList<SocialLink> = ArrayList()
    var services: ArrayList<Product> = ArrayList()
    val updatePortfolioObservable: SingleLiveEvent<Response<String>> = SingleLiveEvent()

    var avatar: String? = null
    var video: InputStream? = null
    var cover: String? = null

    init {
        inProgress.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.getCategories().collect {
                _categories.postValue(it)
                inProgress.postValue(false)
            }
        }
    }

    fun fetchUser() {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.getLoggedInUser().collect {
                _user.postValue(it)
                if (it?.socialLink.isNullOrEmpty()) {
                    socialLinks.add(SocialLink(-1L))
                } else {
                    socialLinks.clear()
                    it?.socialLink?.let { list ->
                        socialLinks.addAll(list)
                    }
                }
            }
        }
    }

    fun setSelectedTab(position: Int) {
        selectedTab.postValue(position)
    }

    fun toggleNewPasswordVisibility() {
        isNewPasswordVisible.value = isNewPasswordVisible.value?.not()
    }

    fun toggleRepeatNewPasswordVisibility() {
        isRepeatNewPasswordVisible.value = isRepeatNewPasswordVisible.value?.not()
    }

    fun onCategorySelectionChanged(category: Category) {
        if (selectedCategories.contains(category))
            selectedCategories.remove(category)
        else
            selectedCategories.add(category)
    }

    fun changePassword() {
        when {
            newPassword.value.isNullOrEmpty() -> newPasswordError.value = R.string.error_required
            newPassword.value!!.length < 8 -> newPasswordError.value = R.string.error_password_length
            repeatNewPassword.value.isNullOrEmpty() -> repeatNewPasswordError.value = R.string.error_required
            repeatNewPassword.value!!.length < 8 -> repeatNewPasswordError.value = R.string.error_password_length
            newPassword.value != repeatNewPassword.value -> repeatNewPasswordError.value =
                R.string.error_passwords_do_not_match
            else -> {
                inProgress.postValue(true)
                viewModelScope.launch(Dispatchers.IO) {
                    val response = profileRepository.changePassword(user.value?.accessToken, newPassword.value)
                    changePasswordObservable.postValue(response)
                    newPassword.postValue("")
                    repeatNewPassword.postValue("")
                    inProgress.postValue(false)
                }
            }
        }
    }

    fun updateProfile() {
        when {
            firstName.value.isNullOrEmpty() -> firstNameError.value = R.string.error_required
            email.value.isNullOrEmpty() -> emailError.value = R.string.error_required
            Patterns.EMAIL_ADDRESS.matcher(email.value.toString()).matches().not() -> emailError.value =
                R.string.error_email_invalid
            mobile.value.isNullOrEmpty() -> mobileError.value = R.string.error_required
            country.value.isNullOrEmpty() -> validationErrors.value = R.string.error_country_required
            birthday.value.isNullOrEmpty() -> validationErrors.value = R.string.error_birthday_required
            gender.value.isNullOrEmpty() -> validationErrors.value = R.string.error_gender_required
            else -> {
                inProgress.postValue(true)
                viewModelScope.launch(Dispatchers.IO) {
                    val response = profileRepository.updateProfile(
                        user.value?.accessToken, firstName.value, email.value, mobile.value,
                        country.value, birthday.value, gender.value
                    )
                    updatePersonalInfoObservable.postValue(response)
                    inProgress.postValue(false)
                }
            }
        }
    }

    fun updatePortfolio() {
        var shouldProceed = true
        if (selectedCategories.isNullOrEmpty()) {
            validationErrors.value = R.string.error_category_required
            shouldProceed = false
        }
        socialLinks.forEach {
            if (it.id != -1L && it.type.isNullOrEmpty()) {
                validationErrors.value = R.string.error_select_social_media_type
                shouldProceed = false
            } else if (it.id != -1L && it.link.isNullOrEmpty()) {
                validationErrors.value = R.string.error_enter_social_media_link
                shouldProceed = false
            }
        }
        if (shouldProceed) {
            inProgress.postValue(true)
            viewModelScope.launch(Dispatchers.IO) {
                val response = profileRepository.updatePortfolio(
                    user.value?.accessToken, avatar, video, cover,
                    selectedCategories, socialLinks, services
                )
                updatePortfolioObservable.postValue(response)
                inProgress.postValue(false)
            }
        }
    }

    fun reset() {
        emailError.value = null
        firstNameError.value = null
        mobileError.value = null
        validationErrors.value = null
        newPasswordError.value = null
        repeatNewPasswordError.value = null
        isNewPasswordVisible.value = false
        isRepeatNewPasswordVisible.value = false
    }
}