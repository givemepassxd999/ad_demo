package com.example.ad_demo.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ad_demo.databinding.AdItemBinding
import com.example.ad_demo.databinding.ContentItemBinding
import com.example.ad_demo.utils.ViewHolderType
import com.example.ad_demo.utils.ViewHolderType.Companion.AD
import com.example.ad_demo.utils.ViewHolderType.Companion.CONTENT
import com.example.ad_sdk.ad.AdData

class ItemAdapter : ListAdapter<ViewHolderType, RecyclerView.ViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CONTENT -> {
                val binding = ContentItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                ContentItemHolder(binding)
            }

            AD -> {
                val binding = AdItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                AdItemHolder(binding)
            }

            else -> {
                EmptyHolder(parent)
            }
        }
    }

    override fun getItemViewType(position: Int) = getItem(position).type

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        when (data.type) {
            CONTENT -> {
                (data as? ViewHolderType.Content)?.let {
                    (holder as? ContentItemHolder)?.bind(data.content)
                }
            }

            AD -> {
                (data as? ViewHolderType.Ad)?.let {
                    (holder as? AdItemHolder)?.bind(it.view, it.adData)
                }
            }
        }
    }

    inner class EmptyHolder(view: View) : RecyclerView.ViewHolder(view)
    inner class ContentItemHolder(
        private val binding: ContentItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("CheckResult", "SetTextI18n")
        fun bind(title: String) {
            binding.info.text = title
        }
    }

    inner class AdItemHolder(
        private val adBinding: AdItemBinding,
    ) : RecyclerView.ViewHolder(adBinding.root) {

        @SuppressLint("CheckResult", "SetTextI18n")
        fun bind(view: View?, adData: AdData) {
            Glide.with(itemView.context)
                .load(adData.adImageUrl)
                .into(adBinding.adImage)
            view?.let {
                (adBinding.root.parent as? ViewGroup)?.removeAllViews()
                adBinding.adContainer.removeAllViews()
                adBinding.adContainer.addView(view)
            }
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<ViewHolderType>() {
    override fun areItemsTheSame(oldItem: ViewHolderType, newItem: ViewHolderType): Boolean {
        (oldItem as? ViewHolderType.Content)?.let { old ->
            (newItem as? ViewHolderType.Content)?.let { new ->
                return old.content == new.content
            }
        }
        return false
    }

    override fun areContentsTheSame(oldItem: ViewHolderType, newItem: ViewHolderType): Boolean {
        (oldItem as? ViewHolderType.Ad)?.let { old ->
            (newItem as? ViewHolderType.Ad)?.let { new ->
                return old.adData.adName == new.adData.adName
            }
        }
        return false
    }
}