package com.giftox.app.adapters

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.giftox.app.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, imageUrl: String?) {
    if (imageUrl.isNullOrEmpty().not()) {
        Glide.with(imageView.context).load(imageUrl).into(imageView)
    }
}

@BindingAdapter("imageUrlScaled")
fun loadImageAndScale(imageView: ImageView, imageUrl: String?) {
    if (imageUrl.isNullOrEmpty().not()) {
        Glide.with(imageView.context).load(imageUrl).into(imageView)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
    }
}

@Suppress("UNCHECKED_CAST")
@BindingAdapter("list")
fun <T> submitList(recyclerView: RecyclerView, list: ArrayList<T>?) {
    if (list != null)
        (recyclerView.adapter as ListAdapter<T, *>?)?.submitList(list)
}

@Suppress("UNCHECKED_CAST")
@BindingAdapter("list")
fun <T> submitList(recyclerView: RecyclerView, list: List<T>?) {
    val adapter = recyclerView.adapter as ListAdapter<T, *>?
    if (adapter?.itemCount == list?.size)
        adapter?.notifyDataSetChanged()
    else if (list != null)
        adapter?.submitList(list)
}

@Suppress("UNCHECKED_CAST")
@BindingAdapter("list")
fun <T> submitList(viewPager: ViewPager2, list: LiveData<ArrayList<T>>) {
    list.observeForever {
        (viewPager.adapter as ListAdapter<T, *>?)?.submitList(it)
    }
}

@BindingAdapter("isVisible")
fun setVisibility(view: View, status: MutableLiveData<Boolean>?) {
    status?.observeForever {
        view.isVisible = it
    }
}

@BindingAdapter("isVisible")
fun setVisibility(view: View, status: Boolean?) {
    if (status != null) {
        view.isVisible = status
    }
}

@BindingAdapter("isEnabled")
fun setEnabled(view: View, status: Boolean?) {
    status?.let {
        view.isEnabled = it
    }
}

@BindingAdapter("isChecked")
fun setChecked(textView: TextView, status: Boolean) {
    textView.isPressed = status
}

@BindingAdapter("drawableEnd")
fun setDrawableEnd(textView: TextView, drawable: Drawable) {
    textView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)
}

@BindingAdapter("error")
fun setError(editText: EditText, error: MutableLiveData<Int?>?) {
    error?.observeForever {
        it?.let { resId ->
            editText.error = editText.context.getString(resId)
            editText.requestFocus()
        }
    }
}

@BindingAdapter("date")
fun setDate(textView: TextView, date: String) {
    val sourceSdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    val destSdf = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
    val dateObject = sourceSdf.parse(date)
    val newDate = destSdf.format(dateObject ?: Date())
    textView.text = newDate
}

@BindingAdapter("dateTime")
fun setDateTime(textView: TextView, date: String) {
    val sourceSdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH)
    val destSdf = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
    val dateObject = sourceSdf.parse(date)
    val newDate = destSdf.format(dateObject ?: Date())
    textView.text = newDate
}