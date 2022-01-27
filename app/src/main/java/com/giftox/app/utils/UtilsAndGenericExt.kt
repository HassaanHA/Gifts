package com.giftox.app.utils

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.preference.PreferenceManager
import com.giftox.app.BuildConfig
import com.giftox.app.R
import com.giftox.app.data.Product
import com.giftox.app.data.ProductDetails
import com.giftox.app.data.SocialLink
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

fun Context.isFirstLaunch(): Boolean {
    val prefs = PreferenceManager.getDefaultSharedPreferences(this)
    return prefs.getBoolean(Constants.IS_FIRST_LAUNCH, true).also {
        prefs.edit().putBoolean(Constants.IS_FIRST_LAUNCH, false).apply()
    }
}

fun getListOfCountries(): Array<String> {
    val locales = Locale.getAvailableLocales()
    val countries: ArrayList<String> = ArrayList()
    locales.forEach {
        val country = it.displayCountry
        if (country.trim().isNotEmpty() && countries.contains(country).not())
            countries.add(country)
    }
    countries.sort()
    return countries.toTypedArray()
}

fun Context.showCountriesDialog(onCountrySelected: (String) -> Unit, selectedCountry: String? = null) {
    val listOfCountries = getListOfCountries()
    val selectedPosition = listOfCountries.indexOf(selectedCountry)
    AlertDialog.Builder(this).setTitle(getString(R.string.select_country))
        .setSingleChoiceItems(listOfCountries, selectedPosition) { dialog, which ->
            dialog.dismiss()
            onCountrySelected(listOfCountries[which])
        }.show()
}

fun Context.showDatePickerDialog(listener: DatePickerDialog.OnDateSetListener) {
    val calendar = Calendar.getInstance()
    DatePickerDialog(
        this, listener, calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}

fun Context.showSocialLinkTypesDialog(
    currentList: MutableList<SocialLink>,
    onTypeSelected: (String) -> Unit,
    selectedType: String? = null
) {
    val types = arrayListOf("Instagram", "Facebook", "Twitter", "Youtube", "Tiktok")
    currentList.forEach {
        types.remove(it.type)
    }
    val selectedPosition = types.indexOf(selectedType)
    AlertDialog.Builder(this).setTitle(getString(R.string.select_platform))
        .setSingleChoiceItems(types.toTypedArray(), selectedPosition) { dialog, which ->
            dialog.dismiss()
            onTypeSelected(types[which])
        }.show()
}

fun Context.getServicesList(): ArrayList<Product> {
    return arrayListOf(
        Product(1, 0, null, ProductDetails(0L, getString(R.string.giftox_video), "", "", "")),
        Product(2, 0, null, ProductDetails(0L, getString(R.string.insta_like_comment), "", "", "")),
        Product(3, 0, null, ProductDetails(0L, getString(R.string.fb_like_comment), "", "", "")),
        Product(4, 0, null, ProductDetails(0L, getString(R.string.twitter_like_comment), "", "", "")),
        Product(5, 0, null, ProductDetails(0L, getString(R.string.follow_me), "", "", ""))
    )
}

fun AppCompatActivity.showMediaSourceChooserDialog(onOptionSelected: (Int) -> Unit) {
    val options = arrayOf("Camera", "Gallery")
    AlertDialog.Builder(this).setTitle("Choose Source")
        .setSingleChoiceItems(options, -1) { dialog, which ->
            dialog.dismiss()
            onOptionSelected(which)
        }.show()
}

fun AppCompatActivity.getImageFromCamera(photoFile: File, cameraResultLauncher: ActivityResultLauncher<Intent>) {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    intent.flags = Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
    intent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, true)
    val photoURI: Uri = FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.provider", photoFile)
    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
    val chooser = Intent.createChooser(intent, "Capture Image")
    cameraResultLauncher.launch(chooser)
}

fun getImageFromGallery(galleryResultLauncher: ActivityResultLauncher<Intent>) {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.type = "image/*"
    intent.flags = Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    val chooser = Intent.createChooser(intent, "Choose Image")
    galleryResultLauncher.launch(chooser)
}

fun getVideoFromGallery(galleryResultLauncher: ActivityResultLauncher<Intent>) {
    val intent = Intent()
    intent.type = "video/*"
    intent.action = Intent.ACTION_GET_CONTENT
    val chooser = Intent.createChooser(intent, "Choose Video")
    galleryResultLauncher.launch(chooser)
}

fun Context.getOutputMediaFile(): File {
    val mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val mediaFile = File(mediaStorageDir?.absolutePath, "${Date().time}.jpg")
    mediaFile.createNewFile()
    return mediaFile
}

fun Context.showOrderStatusOptionsDialog(onOptionSelected: (String) -> Unit) {
    val options = arrayOf("Pending", "Completed", "In Progress", "Reject")
    AlertDialog.Builder(this).setTitle("Edit Order")
        .setSingleChoiceItems(options, -1) { dialog, which ->
            dialog.dismiss()
            onOptionSelected(options[which].lowercase().replace(" ", "_"))
        }.show()
}