package com.giftox.app.views

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.giftox.app.adapters.AddRemoveSocialLinksAdapter
import com.giftox.app.adapters.CategoriesAdapter
import com.giftox.app.adapters.ServicesPricesAdapter
import com.giftox.app.data.Category
import com.giftox.app.data.SocialLink
import com.giftox.app.databinding.FragmentPortfolioBinding
import com.giftox.app.utils.*
import com.giftox.app.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class PortfolioFragment : Fragment() {

    private val categoriesAdapter = CategoriesAdapter { category -> onCategorySelectionChanged(category) }
    private val socialLinksAdapter = AddRemoveSocialLinksAdapter(
        { socialLink -> onAddNewSocialLinkClicked(socialLink) },
        { socialLink -> onRemoveSocialLinkClicked(socialLink) }
    )
    private val servicesPricesAdapter = ServicesPricesAdapter()
    private var photoFile: File? = null
    private lateinit var mBinding: FragmentPortfolioBinding
    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentPortfolioBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.categoriesRv.adapter = categoriesAdapter
        mBinding.socialLinksRv.adapter = socialLinksAdapter
        mBinding.servicePricesRv.adapter = servicesPricesAdapter
        servicesPricesAdapter.submitList(context?.getServicesList())
        mBinding.viewModel = viewModel
        setupObservers()
        initView()
    }

    private fun setupObservers() {
        viewModel.user.observe(viewLifecycleOwner, {
            it?.category?.let { list ->
                categoriesAdapter.selectedCategories.clear()
                categoriesAdapter.selectedCategories.addAll(list)
                viewModel.selectedCategories.addAll(list)
            }
            it?.products?.let { list ->
                servicesPricesAdapter.userServices.clear()
                servicesPricesAdapter.userServices.addAll(list)
                servicesPricesAdapter.notifyItemRangeChanged(0, servicesPricesAdapter.itemCount)
            }
        })
        viewModel.validationErrors.observe(this, {
            showToast(it)
        })
        viewModel.updatePortfolioObservable.observe(this, {
            showToast(it.message)
        })
    }

    private fun initView() {
        mBinding.profilePictureIv.setOnClickListener {
            (activity as AppCompatActivity?)?.let {
                it.showMediaSourceChooserDialog { choice ->
                    if (choice == 0) {
                        photoFile = it.getOutputMediaFile()
                        it.getImageFromCamera(photoFile!!, cameraResultLauncherForAvatar)
                    } else {
                        if (ActivityCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED
                        ) {
                            getImageFromGallery(galleryResultLauncherForAvatar)
                        } else {
                            readExternalStoragePermissionLauncherForAvatar.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    }

                }
            }
        }
        mBinding.promoVideoContainer.setOnClickListener {
            (activity as AppCompatActivity?)?.let {
                it.showMediaSourceChooserDialog { choice ->
                    if (choice == 0) {
                        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                        val chooser = Intent.createChooser(intent, "Capture Video")
                        resultLauncherForVideo.launch(chooser)
                    } else {
                        if (ActivityCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED
                        ) {
                            getVideoFromGallery(resultLauncherForVideo)
                        } else {
                            readExternalStoragePermissionLauncherForVideo.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    }
                }
            }
        }
        mBinding.coverPhotoIv.setOnClickListener {
            (activity as AppCompatActivity?)?.let {
                it.showMediaSourceChooserDialog { choice ->
                    if (choice == 0) {
                        photoFile = it.getOutputMediaFile()
                        it.getImageFromCamera(photoFile!!, cameraResultLauncherForCover)
                    } else {
                        if (ActivityCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED
                        ) {
                            getImageFromGallery(galleryResultLauncherForCover)
                        } else {
                            readExternalStoragePermissionLauncherForCover.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    }

                }
            }
        }
        mBinding.saveBtn.setOnClickListener {
            viewModel.socialLinks = ArrayList(socialLinksAdapter.currentList)
            viewModel.services = ArrayList(servicesPricesAdapter.currentList)
            viewModel.updatePortfolio()
        }
        val layoutManager = GridLayoutManager(requireContext(), 6)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position < 3) 2 else 3
            }
        }
        mBinding.categoriesRv.layoutManager = layoutManager
    }

    private fun onCategorySelectionChanged(category: Category) {
        viewModel.onCategorySelectionChanged(category)
    }

    private val readExternalStoragePermissionLauncherForAvatar =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                getImageFromGallery(galleryResultLauncherForAvatar)
            }
        }

    private val cameraResultLauncherForAvatar =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                viewModel.avatar = photoFile?.absolutePath
                context?.let { context ->
                    Glide.with(context).load(viewModel.avatar).into(mBinding.profilePictureIv)
                }
            }
        }

    private val galleryResultLauncherForAvatar =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data != null && it.data!!.data != null) {
                context?.let { context ->
                    viewModel.avatar = UriHelper.getPath(context, it.data!!.data!!)
                    Glide.with(context).load(viewModel.avatar).into(mBinding.profilePictureIv)
                }
            }
        }

    private val readExternalStoragePermissionLauncherForVideo =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                getVideoFromGallery(resultLauncherForVideo)
            }
        }

    private val resultLauncherForVideo =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                context?.let { context ->
                    val videoInputStream = context.contentResolver.openInputStream(it.data!!.data!!)
                    viewModel.video = videoInputStream
                    Glide.with(context).load(it.data!!.data!!).into(mBinding.promoVideoIv)
                    mBinding.promoVideoIv.scaleType = ImageView.ScaleType.CENTER_CROP
                }
            }
        }

    private val readExternalStoragePermissionLauncherForCover =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                getImageFromGallery(galleryResultLauncherForCover)
            }
        }

    private val cameraResultLauncherForCover =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                viewModel.cover = photoFile?.absolutePath
                context?.let { context ->
                    Glide.with(context).load(viewModel.cover).into(mBinding.coverPhotoIv)
                }
            }
        }

    private val galleryResultLauncherForCover =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data != null && it.data!!.data != null) {
                context?.let { context ->
                    viewModel.cover = UriHelper.getPath(context, it.data!!.data!!)
                    Glide.with(context).load(viewModel.cover).into(mBinding.coverPhotoIv)
                }
            }
        }

    private fun onAddNewSocialLinkClicked(socialLink: SocialLink) {
        viewModel.socialLinks.add(socialLink)
        socialLinksAdapter.submitList(viewModel.socialLinks)
        socialLinksAdapter.notifyItemInserted(socialLinksAdapter.itemCount)
    }

    private fun onRemoveSocialLinkClicked(socialLink: SocialLink) {
        val index = viewModel.socialLinks.indexOf(socialLink)
        viewModel.socialLinks.remove(socialLink)
        socialLinksAdapter.submitList(viewModel.socialLinks)
        socialLinksAdapter.notifyItemRemoved(index)
    }
}