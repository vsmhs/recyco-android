package com.ukmprogramming.recyco.ui.activities.requestdelivery

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
import com.ukmprogramming.recyco.databinding.ActivityRequestDeliveryBinding
import com.ukmprogramming.recyco.ui.activities.productdetail.ProductDetailActivity
import com.ukmprogramming.recyco.ui.activities.productdetail.ProductDetailActivity.Companion
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.handleHttpException

class RequestDeliveryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRequestDeliveryBinding
    private val viewModel by viewModels<RequestDeliveryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRequestDeliveryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val marketItem =
            intent.getParcelableExtra<MarketItem>(ProductDetailActivity.EXTRA_MARKET_ITEM_KEY)
                ?: run {
                    finish()
                    return
                }

        binding.apply {
            Glide.with(this@RequestDeliveryActivity)
                .load(marketItem.thumbnailUrl)
                .placeholder(R.drawable.ic_broken_image)
                .error(R.drawable.ic_broken_image)
                .into(ivProductThumbnail)
            tvProductName.text = marketItem.name
            tvProductWeight.text = marketItem.weight.toString()
            tvProductPrice.text = marketItem.price.toString()

            val servicePrice = 5000
            val deliveryPrice = if(marketItem.price <= 100) (100/10)*2500 else (100/10*2000)
            tvFee.text = getString(R.string.price_template, (servicePrice + deliveryPrice).toString())

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
                    resultState.exception.getData()
                        ?.handleHttpException(this@RequestDeliveryActivity)
                        ?.let { message ->
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

    companion object {
        const val EXTRA_MARKET_ITEM_KEY = "EXTRA_MARKET_ITEM_KEY"
    }
}