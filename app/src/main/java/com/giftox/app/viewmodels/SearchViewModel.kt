package com.giftox.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giftox.app.data.Category
import com.giftox.app.data.Celebrity
import com.giftox.app.data.SearchRepository
import com.giftox.app.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    val inProgress: SingleLiveEvent<Boolean?> = SingleLiveEvent()

    val searchQuery: MutableLiveData<String?> = MutableLiveData()
    val priceRange: MutableLiveData<Int?> = MutableLiveData()
    val country: MutableLiveData<String?> = MutableLiveData()

    private val _celebrities: MutableLiveData<ArrayList<Celebrity>?> = MutableLiveData()
    val celebrities: LiveData<ArrayList<Celebrity>?> = _celebrities

    private val _categories: MutableLiveData<List<Category>?> = MutableLiveData()
    val categories: LiveData<List<Category>?> = _categories

    val selectedCategories: ArrayList<Int> = ArrayList()

    init {
        inProgress.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            searchRepository.getCategories().collect {
                _categories.postValue(it)
                inProgress.postValue(false)
            }
        }
    }

    fun search() {
        inProgress.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            _celebrities.postValue(searchRepository.search(searchQuery.value, priceRange.value, selectedCategories))
            inProgress.postValue(false)
        }
    }

}