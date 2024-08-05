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
import com.ukmprogramming.recyco.data.network.response.models.MarketItem
import com.ukmprogramming.recyco.databinding.ItemMarketBinding

class MarketItemAdapter(
    private val onItemCLick: (MarketItem) -> Unit
) : ListAdapter<MarketItem, MarketItemAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<MarketItem>() {
        override fun areItemsTheSame(oldItem: MarketItem, newItem: MarketItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MarketItem, newItem: MarketItem) =
            oldItem == newItem
    }
) {
    class ViewHolder(
        val binding: ItemMarketBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemMarketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val context = holder.itemView.context
        val data = getItem(position)

        binding.apply {
            tvTitle.text = data.name
            tvWeight.text = context.getString(R.string.weight_template, data.weight.toString())
            tvPrice.text = context.getString(R.string.price_template, data.price.toString())
            Glide.with(context)
                .load(GlideUrl("${BuildConfig.BASE_URL}${data.thumbnailUrl}") {
                    mapOf(Pair("ngrok-skip-browser-warning", "ngrok-skip-browser-warning"))
                })
                .timeout(30000)
                .placeholder(R.drawable.ic_broken_image)
                .error(R.drawable.ic_broken_image)
                .into(ivThumbnail)
            root.setOnClickListener {
                onItemCLick(data)
            }
        }
    }
}