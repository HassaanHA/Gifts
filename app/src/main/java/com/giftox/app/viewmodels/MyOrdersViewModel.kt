package com.giftox.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giftox.app.data.*
import com.giftox.app.utils.Constants
import com.giftox.app.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class MyOrdersViewModel @Inject constructor(
    private val myOrdersRepository: MyOrdersRepository,
    private val userDao: UserDao
) : ViewModel() {


    private val _user: MutableLiveData<User?> = MutableLiveData()
    val user: LiveData<User?> = _user
    val selectedTab: MutableLiveData<Int> = MutableLiveData(0)
    val inProgress: MutableLiveData<Boolean> = MutableLiveData(false)

    private val _pendingOrders: MutableLiveData<List<MyOrder>?> = MutableLiveData()
    val pendingOrders: LiveData<List<MyOrder>?> = _pendingOrders

    private val _completedOrders: MutableLiveData<List<MyOrder>?> = MutableLiveData()
    val completedOrders: LiveData<List<MyOrder>?> = _completedOrders

    private val _piggyBank: MutableLiveData<PiggyBank?> = MutableLiveData()
    val piggyBank: LiveData<PiggyBank?> = _piggyBank

    val selectedOrder: MutableLiveData<MyOrder?> = MutableLiveData()

    val updateOrderObservable: SingleLiveEvent<Response<String>> = SingleLiveEvent()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.getLoggedInUser().collect {
                _user.postValue(it)
                myOrdersRepository.getOrders(it?.accessToken).collect { list ->
                    _pendingOrders.postValue(getPendingOrders(list))
                    _completedOrders.postValue(getCompletedOrders(list))
                }
            }
        }
    }

    private fun getCompletedOrders(orders: List<MyOrder>): ArrayList<MyOrder> {
        val toReturn: ArrayList<MyOrder> = ArrayList()
        orders.forEach {
            if (it.status != Constants.orderStatusPending)
                toReturn.add(it)
        }
        return toReturn
    }

    private fun getPendingOrders(orders: List<MyOrder>): ArrayList<MyOrder> {
        val toReturn: ArrayList<MyOrder> = ArrayList()
        orders.forEach {
            if (it.status == Constants.orderStatusPending)
                toReturn.add(it)
        }
        return toReturn
    }

    fun setSelectedTab(position: Int) {
        selectedTab.postValue(position)
    }

    fun updateOrder(orderId: Long, orderType: String?, video: InputStream? = null, status: String? = null) {
        inProgress.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            val response = myOrdersRepository.updateOrder(
                user.value?.accessToken, orderId, orderType, video, status
            )
            updateOrderObservable.postValue(response)
            inProgress.postValue(false)
        }
    }

    fun fetchPiggyBank() {
        inProgress.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            _piggyBank.postValue(myOrdersRepository.getPiggyBank(user.value?.accessToken))
            inProgress.postValue(false)
        }
    }

    fun fetchOrderDetails(orderId: Long) {
        inProgress.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            selectedOrder.postValue(myOrdersRepository.getOrderDetails(user.value?.accessToken, orderId))
            inProgress.postValue(false)
        }
    }

    fun reset() {
        _user.postValue(null)
        _pendingOrders.postValue(null)
        _completedOrders.postValue(null)
        _piggyBank.postValue(null)
        selectedOrder.postValue(null)
    }
}