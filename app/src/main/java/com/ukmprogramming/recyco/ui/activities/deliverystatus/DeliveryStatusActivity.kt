package com.ukmprogramming.recyco.ui.activities.deliverystatus

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.ukmprogramming.recyco.BuildConfig
import com.ukmprogramming.recyco.R
import com.ukmprogramming.recyco.databinding.ActivityDeliveryStatusBinding
import com.ukmprogramming.recyco.util.Helpers
import com.ukmprogramming.recyco.util.MarketTransactionStatuses
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.handleHttpException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeliveryStatusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeliveryStatusBinding
    private val viewModel by viewModels<DeliveryStatusViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDeliveryStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val marketItemId = intent.getStringExtra(EXTRA_MARKET_ITEM_ID_KEY) ?: run {
            finish()
            return
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.delivery_status)
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        binding.apply {
            viewModel.marketTransactionDataState.observe(this@DeliveryStatusActivity) { resultState ->
                progressBar.isVisible = resultState is ResultState.Loading

                if (resultState is ResultState.Success) {
                    tvProductName.text = resultState.data.item.name
                    tvProductWeight.text =
                        getString(R.string.weight_template, resultState.data.item.weight.toString())
                    tvProductPrice.text =
                        getString(R.string.price_template, resultState.data.item.price.toString())

                    val onProcessStatus = resultState.data.allStatus.firstOrNull {
                        it.status == MarketTransactionStatuses.ON_PROCESS.name
                    }
                    val onDeliverStatus = resultState.data.allStatus.firstOrNull {
                        it.status == MarketTransactionStatuses.ON_DELIVER.name
                    }
                    val finishedStatus = resultState.data.allStatus.firstOrNull {
                        it.status == MarketTransactionStatuses.FINISHED.name
                    }

                    Glide.with(this@DeliveryStatusActivity)
                        .load(
                            GlideUrl("${BuildConfig.BASE_URL}${resultState.data.item.thumbnailUrl}") {
                                mapOf(
                                    Pair(
                                        "ngrok-skip-browser-warning",
                                        "ngrok-skip-browser-warning"
                                    )
                                )
                            }
                        )
                        .timeout(30000)
                        .error(R.drawable.ic_broken_image)
                        .placeholder(R.drawable.ic_broken_image)
                        .into(binding.ivProductThumbnail)

                    tvOrderTime.text =
                        onProcessStatus?.createdAt?.let { Helpers.formatDate(it) } ?: "-"
                    tvDeliver.text =
                        onDeliverStatus?.createdAt?.let { Helpers.formatDate(it) } ?: "-"
                    tvComplete.text =
                        finishedStatus?.createdAt?.let { Helpers.formatDate(it) } ?: "-"
                } else if (resultState is ResultState.Error) {
                    resultState.exception.getData()?.handleHttpException()?.let { message ->
                        Toast.makeText(this@DeliveryStatusActivity, message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

        viewModel.getMarketTransactionById(marketItemId)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_MARKET_ITEM_ID_KEY = "EXTRA_MARKET_ITEM_ID_KEY"
    }
}