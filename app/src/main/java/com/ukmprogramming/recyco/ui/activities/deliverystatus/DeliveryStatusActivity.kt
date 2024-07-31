package com.ukmprogramming.recyco.ui.activities.deliverystatus

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.ukmprogramming.recyco.R
import com.ukmprogramming.recyco.data.network.response.models.MarketItem
import com.ukmprogramming.recyco.databinding.ActivityDeliveryStatusBinding
import com.ukmprogramming.recyco.util.Helpers
import com.ukmprogramming.recyco.util.MarketTransactionStatuses
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.handleHttpException
import java.text.SimpleDateFormat
import java.util.Locale

class DeliveryStatusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeliveryStatusBinding
    private val viewModel by viewModels<DeliveryStatusViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDeliveryStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val marketItem = intent.getParcelableExtra<MarketItem>(EXTRA_MARKET_ITEM_KEY) ?: run {
            finish()
            return
        }

        binding.apply {
            viewModel.marketTransactionDataState.observe(this@DeliveryStatusActivity) { resultState ->
                progressBar.isVisible = resultState is ResultState.Loading

                if (resultState is ResultState.Success) {
                    tvProductName.text = marketItem.name
                    tvProductWeight.text = marketItem.weight.toString()
                    tvProductPrice.text = marketItem.price.toString()

                    val onProcessStatus = resultState.data.allStatus.firstOrNull {
                        it.status == MarketTransactionStatuses.ON_PROCESS.name
                    }
                    val onDeliverStatus = resultState.data.allStatus.firstOrNull {
                        it.status == MarketTransactionStatuses.ON_DELIVER.name
                    }
                    val finishedStatus = resultState.data.allStatus.firstOrNull {
                        it.status == MarketTransactionStatuses.FINISHED.name
                    }

                    tvOrderTime.text = Helpers.formatDate(onProcessStatus?.createdAt)
                    tvDeliver.text = Helpers.formatDate(onDeliverStatus?.createdAt)
                    tvComplete.text = Helpers.formatDate(finishedStatus?.createdAt)
                } else if (resultState is ResultState.Error) {
                    resultState.exception.getData()
                        ?.handleHttpException(this@DeliveryStatusActivity)
                        ?.let { message ->
                            Toast.makeText(this@DeliveryStatusActivity, message, Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            }
        }

        viewModel.getMarketTransactionById(marketItem.id)
    }

    companion object {
        const val EXTRA_MARKET_ITEM_KEY = "EXTRA_MARKET_ITEM_KEY"
    }
}