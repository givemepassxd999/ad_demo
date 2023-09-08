package com.example.ad_demo.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ad_demo.utils.ViewHolderType
import com.example.ad_demo.utils.ViewHolderType.Companion.AD
import com.example.ad_demo.utils.ViewHolderType.Companion.CONTENT
import com.example.ad_demo.databinding.AdItemBinding
import com.example.ad_demo.databinding.ContentItemBinding

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
                    (holder as? AdItemHolder)?.bind(data.url)
                }
            }
        }
    }

    inner class ContentItemHolder(
        private val binding: ContentItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("CheckResult", "SetTextI18n")
        fun bind(title: String) {
            binding.info.text = title
        }
    }

    inner class AdItemHolder(
        private val binding: AdItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("CheckResult", "SetTextI18n")
        fun bind(url: String) {
        }
    }

    inner class EmptyHolder(view: View) : RecyclerView.ViewHolder(view)
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
                return old.url == new.url
            }
        }
        return false
    }
}