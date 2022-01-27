package com.giftox.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giftox.app.data.Celebrity
import com.giftox.app.data.Collection
import com.giftox.app.data.HomeRepository
import com.giftox.app.utils.toPlainString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() {

    private val _tweets: MutableLiveData<String?> = MutableLiveData()
    val tweets: LiveData<String?> = _tweets

    private val _banners: MutableLiveData<ArrayList<Celebrity>?> = MutableLiveData()
    val banners: LiveData<ArrayList<Celebrity>?> = _banners

    private val _collections: MutableLiveData<ArrayList<Collection>?> = MutableLiveData()
    val collections: LiveData<ArrayList<Collection>?> = _collections

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val homeData = repository.getHomeData()
            homeData.collect {
                _tweets.postValue(it.tweets?.toPlainString())
                _banners.postValue(it.banners)
                _collections.postValue(it.collections)
            }
        }
    }

}