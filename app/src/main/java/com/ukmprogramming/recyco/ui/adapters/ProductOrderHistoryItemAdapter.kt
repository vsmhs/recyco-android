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
                .load(GlideUrl("${BuildConfig.BASE_URL}${data.item.thumbnailUrl}") {
                    mapOf(Pair("ngrok-skip-browser-warning", "ngrok-skip-browser-warning"))
                })
                .timeout(30000)
                .placeholder(R.drawable.ic_broken_image)
                .error(R.drawable.ic_broken_image)
                .into(ivThumbnail)
            tvName.text = data.item.name
            tvWeight.text = context.getString(R.string.weight_template, data.item.weight.toString())
            tvDate.text = Helpers.formatDate(data.item.postedAt)
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