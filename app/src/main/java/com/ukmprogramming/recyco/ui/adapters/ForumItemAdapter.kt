package com.ukmprogramming.recyco.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ukmprogramming.recyco.data.network.response.models.ForumPost
import com.ukmprogramming.recyco.databinding.ItemForumBinding
import com.ukmprogramming.recyco.util.Helpers

class ForumItemAdapter(
    private val onItemCLick: (ForumPost) -> Unit
) : ListAdapter<ForumPost, ForumItemAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<ForumPost>() {
        override fun areItemsTheSame(oldItem: ForumPost, newItem: ForumPost) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ForumPost, newItem: ForumPost) =
            oldItem == newItem
    }
) {
    class ViewHolder(
        val binding: ItemForumBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemForumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val data = getItem(position)

        binding.apply {
            tvUserName.text = data.createdBy.name
            tvDate.text = Helpers.formatDateNoTime(data.createdAt)
            tvTitle.text = data.title
            tvDescription.text = data.description

            root.setOnClickListener {
                onItemCLick(data)
            }
        }
    }
}