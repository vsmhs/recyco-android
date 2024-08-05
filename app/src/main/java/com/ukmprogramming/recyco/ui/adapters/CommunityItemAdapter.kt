package com.ukmprogramming.recyco.ui.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.ukmprogramming.recyco.BuildConfig
import com.ukmprogramming.recyco.R
import com.ukmprogramming.recyco.data.network.response.models.Community
import com.ukmprogramming.recyco.databinding.ItemCommunityBinding

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
                .load(GlideUrl("${BuildConfig.BASE_URL}${data.thumbnailUrl}") {
                    mapOf(Pair("ngrok-skip-browser-warning", "ngrok-skip-browser-warning"))
                })
                .timeout(30000)
                .error(R.drawable.ic_broken_image)
                .placeholder(R.drawable.ic_broken_image)
                .into(binding.ivThumbnail)

            tvName.text = data.name
            tvDescription.text = data.description

            btnMoreInfo.setOnClickListener {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(data.communityUrl)))
            }

            root.setOnClickListener {
                onItemCLick(data)
            }
        }
    }
}