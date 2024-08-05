package com.ukmprogramming.recyco.ui.activities.requestdelivery

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.ukmprogramming.recyco.BuildConfig
import com.ukmprogramming.recyco.R
import com.ukmprogramming.recyco.data.network.response.models.MarketItem
import com.ukmprogramming.recyco.databinding.ActivityRequestDeliveryBinding
import com.ukmprogramming.recyco.ui.activities.productdetail.ProductDetailActivity
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.handleHttpException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RequestDeliveryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRequestDeliveryBinding
    private val viewModel by viewModels<RequestDeliveryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRequestDeliveryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.request_delivery)
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        val marketItem =
            intent.getParcelableExtra<MarketItem>(ProductDetailActivity.EXTRA_MARKET_ITEM_KEY)
                ?: run {
                    finish()
                    return
                }

        binding.apply {
            Glide.with(this@RequestDeliveryActivity)
                .load(
                    GlideUrl("${BuildConfig.BASE_URL}${marketItem.thumbnailUrl}") {
                        mapOf(Pair("ngrok-skip-browser-warning", "ngrok-skip-browser-warning"))
                    }
                )
                .timeout(30000)
                .placeholder(R.drawable.ic_broken_image)
                .error(R.drawable.ic_broken_image)
                .into(ivProductThumbnail)
            tvProductName.text = marketItem.name
            tvProductWeight.text = getString(R.string.weight_template, marketItem.weight.toString())
            tvProductPrice.text = getString(R.string.price_template, marketItem.price.toString())

            val servicePrice = 5000
            val deliveryPrice =
                if (marketItem.price <= 100) (100 / 10) * 1000 else (100 / 10 * 1500)
            tvFee.text =
                getString(R.string.price_template, (servicePrice + deliveryPrice).toString())

            btnRequestDelivery.setOnClickListener {
                val address = etAddress.text.toString()
                val recipientName = etRecipientName.text.toString()
                val recipientPhone = etPhoneNumber.text.toString()
                val note = etNote.text.toString()

                viewModel.requestDelivery(
                    marketItem.id,
                    recipientName,
                    recipientPhone,
                    "",
                    address,
                    note
                )
            }

            viewModel.requestDeliveryState.observe(this@RequestDeliveryActivity) { resultState ->
                progressBar.isVisible = resultState is ResultState.Loading

                if (resultState is ResultState.Success) {
                    Toast.makeText(
                        this@RequestDeliveryActivity,
                        "Request Delivery Success",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else if (resultState is ResultState.Error) {
                    resultState.exception.getData()?.handleHttpException()?.let { message ->
                        Toast.makeText(
                            this@RequestDeliveryActivity,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_MARKET_ITEM_KEY = "EXTRA_MARKET_ITEM_KEY"
    }
}