package com.ukmprogramming.recyco.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.ukmprogramming.recyco.BuildConfig
import com.ukmprogramming.recyco.R
import com.ukmprogramming.recyco.data.network.response.models.Article
import com.ukmprogramming.recyco.databinding.ItemArticleBinding
import com.ukmprogramming.recyco.util.Helpers

class ArticleItemAdapter(
    private val onItemCLick: (Article) -> Unit
) : ListAdapter<Article, ArticleItemAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Article, newItem: Article) =
            oldItem == newItem
    }
) {
    class ViewHolder(
        val binding: ItemArticleBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val context = holder.itemView.context
        val data = getItem(position)

        binding.apply {
            tvUserName.text = "Admin"
            tvDate.text = Helpers.formatDateNoTime(data.createdAt)

            Glide.with(context)
                .load(GlideUrl("${BuildConfig.BASE_URL}${data.thumbnailUrl}") {
                    mapOf(Pair("ngrok-skip-browser-warning", "ngrok-skip-browser-warning"))
                })
                .timeout(30000)
                .error(R.drawable.ic_broken_image)
                .placeholder(R.drawable.ic_broken_image)
                .into(binding.ivThumbnail)

            tvName.text = data.title
            tvDescription.text = data.description

            root.setOnClickListener {
                onItemCLick(data)
            }
        }
    }
}