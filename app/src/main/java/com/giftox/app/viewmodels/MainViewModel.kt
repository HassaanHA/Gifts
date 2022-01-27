package com.giftox.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giftox.app.data.AuthenticationRepository
import com.giftox.app.data.User
import com.giftox.app.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    val logoutObservable: SingleLiveEvent<Unit> = SingleLiveEvent()
    val inProgress: SingleLiveEvent<Boolean?> = SingleLiveEvent()

    private val _user: MutableLiveData<User?> = MutableLiveData(null)
    val user: LiveData<User?> = _user

    init {
        inProgress.value = false
        viewModelScope.launch(Dispatchers.IO) {
            authenticationRepository.getLoggedInUser().collect {
                _user.postValue(it)
            }
        }
    }

    fun logout() {
        inProgress.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            authenticationRepository.logout()
            withContext(Dispatchers.Main) {
                inProgress.postValue(false)
                logoutObservable.call()
            }
        }
    }

}