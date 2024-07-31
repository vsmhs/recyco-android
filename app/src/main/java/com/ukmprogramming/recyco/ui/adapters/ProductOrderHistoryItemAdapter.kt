package com.ukmprogramming.recyco.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ukmprogramming.recyco.R
import com.ukmprogramming.recyco.data.network.response.models.MarketTransactionsItem
import com.ukmprogramming.recyco.databinding.ItemProductOrderHistoryBinding
import com.ukmprogramming.recyco.util.Helpers
import com.ukmprogramming.recyco.util.MarketTransactionStatuses

class ProductOrderHistoryItemAdapter(
    private val onItemCLick: (MarketTransactionsItem) -> Unit
) : ListAdapter<MarketTransactionsItem, ProductOrderHistoryItemAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<MarketTransactionsItem>() {
        override fun areItemsTheSame(
            oldItem: MarketTransactionsItem,
            newItem: MarketTransactionsItem
        ) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: MarketTransactionsItem,
            newItem: MarketTransactionsItem
        ) =
            oldItem == newItem
    }
) {
    class ViewHolder(
        val binding: ItemProductOrderHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemProductOrderHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val context = holder.itemView.context
        val data = getItem(position)

        binding.apply {
            Glide.with(context)
                .load(data.item.thumbnailUrl)
                .placeholder(R.drawable.ic_broken_image)
                .error(R.drawable.ic_broken_image)
                .into(ivThumbnail)
            tvName.text = data.item.name
            tvWeight.text = context.getString(R.string.weight_template, data.item.weight.toString())
            tvDate.text = if (data.item.postedAt != null) {
                Helpers.formatDate(data.item.postedAt)
            } else {
                "-"
            }
            tvStatus.text = when (data.lastStatus.status) {
                MarketTransactionStatuses.ON_PROCESS.name -> "On Process"
                MarketTransactionStatuses.ON_DELIVER.name -> "On Deliver"
                MarketTransactionStatuses.FINISHED.name -> "Finished"
                MarketTransactionStatuses.CANCELLED.name -> "Cancelled"
                else -> "Unknown"
            }

            root.setOnClickListener {
                onItemCLick(data)
            }
        }
    }
}