package com.ukmprogramming.recyco.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ukmprogramming.recyco.data.network.response.models.ForumPostReply
import com.ukmprogramming.recyco.databinding.ItemCommentBinding

class CommentItemAdapter(
    private val onItemCLick: (ForumPostReply) -> Unit
) : ListAdapter<ForumPostReply, CommentItemAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<ForumPostReply>() {
        override fun areItemsTheSame(oldItem: ForumPostReply, newItem: ForumPostReply) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ForumPostReply, newItem: ForumPostReply) =
            oldItem == newItem
    }
) {
    class ViewHolder(
        val binding: ItemCommentBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val data = getItem(position)

        binding.apply {
            tvUserName.text = data.repliedBy.name
            tvDescription.text = data.description

            root.setOnClickListener {
                onItemCLick(data)
            }
        }
    }
}