package com.giftox.app.viewmodels

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giftox.app.R
import com.giftox.app.data.*
import com.giftox.app.network.PaymentInfo
import com.giftox.app.network.PlaceOrderParams
import com.giftox.app.utils.Constants
import com.giftox.app.utils.SingleLiveEvent
import com.paypal.checkout.approve.ApprovalData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CelebrityViewModel @Inject constructor(
    private val celebrityRepository: CelebrityRepository,
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private val _user: MutableLiveData<User?> = MutableLiveData()
    val user: LiveData<User?> = _user

    val to: MutableLiveData<String> = MutableLiveData()
    val email: MutableLiveData<String> = MutableLiveData()
    val specialWishes: MutableLiveData<String> = MutableLiveData()

    val inProgress: MutableLiveData<Boolean> = MutableLiveData(false)

    val tAndCsAgree: MutableLiveData<Boolean> = MutableLiveData(false)
    val isPublic: MutableLiveData<Boolean> = MutableLiveData(false)

    val toError: MutableLiveData<Int?> = MutableLiveData()
    val emailError: MutableLiveData<Int?> = MutableLiveData()

    private val _occasions: MutableLiveData<List<Occasion>> = MutableLiveData()
    val occasions: LiveData<List<Occasion>> = _occasions

    private val _celebrity: MutableLiveData<Celebrity?> = MutableLiveData()
    val celebrity: LiveData<Celebrity?> = _celebrity

    var selectedOccasion: Occasion? = null
    val selectedProducts: ArrayList<Product> = ArrayList()
    val selectedSocialOptions: ArrayList<Product> = ArrayList()
    val socialLinks: ArrayList<SocialLink> = ArrayList()
    var isVideoOptionSelected = false
    val total: Int
        get() {
            var total = 0
            selectedProducts.forEach {
                total += it.price
            }
            return total
        }

    val validationErrors: SingleLiveEvent<Int?> = SingleLiveEvent()
    val apiErrors: SingleLiveEvent<String?> = SingleLiveEvent()
    val giftoxVideoObservable: SingleLiveEvent<Unit> = SingleLiveEvent()
    val socialLinksObservable: SingleLiveEvent<Unit> = SingleLiveEvent()
    val placeOrderObservable: SingleLiveEvent<Response<String>?> = SingleLiveEvent()
    val submitRatingObservable: SingleLiveEvent<Response<String>?> = SingleLiveEvent()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            celebrityRepository.getOccasions().collect {
                _occasions.postValue(it)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            authenticationRepository.getLoggedInUser().collect {
                _user.postValue(it)
            }
        }
    }

    fun getCelebrity(celebrity: Celebrity?) {
        celebrity?.let {
            inProgress.postValue(true)
            viewModelScope.launch(Dispatchers.IO) {
                val response = celebrityRepository.getCelebrity(it.id)
                inProgress.postValue(false)
                if (response.status == Constants.SUCCESS)
                    _celebrity.postValue(response.data)
                else
                    apiErrors.postValue(response.message)
            }
        }
    }

    fun onProductSelectionChanged(product: Product) {
        if (selectedProducts.contains(product)) {
            selectedProducts.remove(product)
            product.productDetails?.title?.let {
                if (isVideoOptionSelected)
                    isVideoOptionSelected = it.contains("video", true).not()
            }
        } else {
            selectedProducts.add(product)
            product.productDetails?.title?.let {
                if (isVideoOptionSelected.not())
                    isVideoOptionSelected = it.contains("video", true)
            }
        }
    }

    fun continueFromVideo() {
        when {
            to.value.isNullOrEmpty() -> toError.value = R.string.error_required
            email.value.isNullOrEmpty() -> emailError.value = R.string.error_required
            Patterns.EMAIL_ADDRESS.matcher(email.value.toString()).matches().not() -> emailError.value =
                R.string.error_email_invalid
            selectedOccasion == null -> validationErrors.value = R.string.error_select_occasion
            else -> {
                resetErrors()
                giftoxVideoObservable.call()
            }
        }
    }

    fun continueFromSocialLinks() {
        var shouldProceed = true
        socialLinks.forEach {
            if (it.link.isNullOrEmpty()) {
                validationErrors.value = R.string.error_enter_social_media_link
                shouldProceed = false
            }
        }
        if (shouldProceed) {
            resetErrors()
            socialLinksObservable.call()
        }
    }

    fun placeOrder(approvalData: ApprovalData) {
        val orderParams = PlaceOrderParams()
        orderParams.celebrityId = celebrity.value?.id?.toString()
        orderParams.to = to.value
        orderParams.receiver = email.value
        orderParams.occasionId = selectedOccasion?.id?.toString()
        orderParams.specialWishes = specialWishes.value
        orderParams.agree = if (tAndCsAgree.value == true) "on" else "off"
        orderParams.isPublic = if (isPublic.value == true) "on" else "off"
        val products: HashMap<String, String?> = HashMap()
        val links: HashMap<String, String?> = HashMap()
        selectedProducts.forEach {
            if (it.productDetails?.title?.contains("video", true) == false) {
                products[it.id.toString()] = "other"
            } else {
                products[it.id.toString()] = "video"
            }
        }
        orderParams.productUsers = products
        socialLinks.forEach {
            links[it.id.toString()] = it.link
        }
        orderParams.links = links
        orderParams.paymentInfo = PaymentInfo(
            approvalData.orderId, approvalData.orderId, approvalData.payerId
        )
        viewModelScope.launch(Dispatchers.IO) {
            inProgress.postValue(true)
            val response = celebrityRepository.placeOrder(user.value?.accessToken, orderParams)
            placeOrderObservable.postValue(response)
            inProgress.postValue(false)
        }
    }

    fun submitRating(rating: Int, review: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            inProgress.postValue(true)
            val response = celebrityRepository.submitReview(
                user.value?.accessToken, celebrity.value?.id, rating, review
            )
            getCelebrity(celebrity.value)
            submitRatingObservable.postValue(response)
            inProgress.postValue(false)
        }
    }

    private fun resetErrors() {
        toError.value = null
        emailError.value = null
        validationErrors.value = null
    }

    fun reset() {
        to.value = ""
        email.value = ""
        specialWishes.value = ""
        selectedOccasion = null
        validationErrors.value = null
        _celebrity.postValue(null)
        socialLinks.clear()
        selectedProducts.clear()
    }
}