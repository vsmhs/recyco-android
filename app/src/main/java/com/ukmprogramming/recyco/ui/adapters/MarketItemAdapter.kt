package com.ukmprogramming.recyco.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ukmprogramming.recyco.data.network.response.models.MarketItem
import com.ukmprogramming.recyco.databinding.ItemMarketBinding

class MarketItemAdapter(
    private val bearerToken: String,
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
    }
}