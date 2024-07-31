package com.ukmprogramming.recyco.ui.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ukmprogramming.recyco.R
import com.ukmprogramming.recyco.data.network.response.models.Community
import com.ukmprogramming.recyco.databinding.ItemCommunityBinding
import com.ukmprogramming.recyco.util.Constants

class CommunityItemAdapter(
    private val onItemCLick: (Community) -> Unit
) : ListAdapter<Community, CommunityItemAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<Community>() {
        override fun areItemsTheSame(oldItem: Community, newItem: Community) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Community, newItem: Community) =
            oldItem == newItem
    }
) {
    class ViewHolder(
        val binding: ItemCommunityBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemCommunityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val context = holder.itemView.context
        val data = getItem(position)

        binding.apply {
            Glide.with(context)
                .load("${Constants.BASE_URL}${data.thumbnailUrl}")
                .error(R.drawable.ic_broken_image)
                .placeholder(R.drawable.ic_broken_image)
                .into(binding.ivThumbnail)

            tvName.text = data.name
            tvDescription.text = data.description

            btnMoreInfo.setOnClickListener {
                Intent(Intent.ACTION_VIEW, Uri.parse(data.communityUrl))
            }

            root.setOnClickListener {
                onItemCLick(data)
            }
        }
    }
}