package com.giftox.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.giftox.app.data.SocialLink
import com.giftox.app.databinding.ItemAddRemoveSocialLinkBinding
import com.giftox.app.utils.showSocialLinkTypesDialog

class AddRemoveSocialLinksAdapter(
    private val onAddNewSocialLinkClicked: (SocialLink) -> Unit,
    private val onRemoveSocialLinkClicked: (SocialLink) -> Unit
) : ListAdapter<SocialLink, AddRemoveSocialLinksAdapter.ViewHolder>(
    SocialLinkDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAddRemoveSocialLinkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val mBinding: ItemAddRemoveSocialLinkBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun bind(socialLink: SocialLink) {
            mBinding.socialLink = socialLink
            mBinding.isFirstItem = bindingAdapterPosition == 0
            mBinding.addRemoveBtn.setOnClickListener {
                if (bindingAdapterPosition == 0) {
                    val id = (getItem(itemCount - 1).id ?: -1) + 1
                    val newSocialLink = SocialLink(id)
                    onAddNewSocialLinkClicked(newSocialLink)
                } else {
                    onRemoveSocialLinkClicked(socialLink)
                }
            }
            mBinding.socialLinkTitleTv.setOnClickListener {
                mBinding.socialLinkTitleTv.context.showSocialLinkTypesDialog(currentList,
                    { type ->
                        socialLink.type = type
                        mBinding.socialLinkTitleTv.text = type
                    }, socialLink.type
                )
            }
            mBinding.linkEt.doAfterTextChanged {
                socialLink.link = mBinding.linkEt.text?.toString()
            }
        }
    }
}

class SocialLinkDiffCallback : DiffUtil.ItemCallback<SocialLink>() {
    override fun areItemsTheSame(oldItem: SocialLink, newItem: SocialLink): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SocialLink, newItem: SocialLink): Boolean {
        return oldItem == newItem
    }
}