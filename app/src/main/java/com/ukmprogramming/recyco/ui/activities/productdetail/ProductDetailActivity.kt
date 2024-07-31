package com.ukmprogramming.recyco.ui.activities.productdetail

import android.content.Intent
import android.net.Uri
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
import com.ukmprogramming.recyco.databinding.ActivityProductDetailBinding
import com.ukmprogramming.recyco.ui.activities.requestdelivery.RequestDeliveryActivity
import com.ukmprogramming.recyco.util.Helpers
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.UserRoles
import com.ukmprogramming.recyco.util.handleHttpException

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    private val viewModel by viewModels<ProductDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val marketItem = intent.getParcelableExtra<MarketItem>(EXTRA_MARKET_ITEM_KEY) ?: run {
            finish()
            return
        }

        binding.apply {
            viewModel.userDataState.observe(this@ProductDetailActivity) { resultState ->
                progressBar.isVisible = resultState is ResultState.Loading

                if (resultState is ResultState.Success) {
                    btnRequestDelivery.isVisible = resultState.data.role == UserRoles.C_LARGE.name
                    tvUserName.text = resultState.data.name
                } else if (resultState is ResultState.Error) {
                    resultState.exception.getData()?.handleHttpException(this@ProductDetailActivity)
                        ?.let { message ->
                            Toast.makeText(this@ProductDetailActivity, message, Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            }

            Glide.with(this@ProductDetailActivity)
                .load(marketItem.thumbnailUrl)
                .placeholder(R.drawable.ic_broken_image)
                .error(R.drawable.ic_broken_image)
                .into(ivThumbnail)
            tvDate.text = Helpers.formatDate(marketItem.postedAt.toString())
            tvProductName.text = marketItem.name
            tvWeight.text = getString(R.string.weight_template, marketItem.weight.toString())
            tvPrice.text = getString(R.string.price_template, marketItem.price.toString())
            tvDescription.text = marketItem.description

            btnRequestDelivery.setOnClickListener {
                startActivity(
                    Intent(
                        this@ProductDetailActivity,
                        RequestDeliveryActivity::class.java
                    ).apply {
                        putExtra(RequestDeliveryActivity.EXTRA_MARKET_ITEM_KEY, marketItem)
                    }
                )
            }
            btnContact.setOnClickListener {
                startActivity(
                    Intent(Intent.ACTION_DIAL, Uri.parse("tel:${marketItem.postedBy.phoneNumber}"))
                )
            }
        }

        viewModel.getUserProfile()
    }

    companion object {
        const val EXTRA_MARKET_ITEM_KEY = "EXTRA_MARKET_ITEM_KEY"
    }
}