package com.ukmprogramming.recyco.ui.activities.editproduct

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
import com.ukmprogramming.recyco.databinding.ActivityEditProductBinding
import com.ukmprogramming.recyco.util.MarketTransactionStatuses
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.UserRoles
import com.ukmprogramming.recyco.util.handleHttpException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProductBinding
    private val viewModel by viewModels<EditProductViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.edit_product)
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        val marketItem = intent.getParcelableExtra<MarketItem>(EXTRA_MARKET_ITEM_KEY) ?: run {
            finish()
            return
        }

        binding.apply {
            Glide.with(this@EditProductActivity)
                .load(GlideUrl("${BuildConfig.BASE_URL}${marketItem.thumbnailUrl}") {
                    mapOf(Pair("ngrok-skip-browser-warning", "ngrok-skip-browser-warning"))
                })
                .timeout(30000)
                .placeholder(R.drawable.ic_broken_image)
                .error(R.drawable.ic_broken_image)
                .into(ivThumbnail)

            tvProductName.text = marketItem.name
            tvPrice.text = marketItem.price.toString()
            tvWeight.text = marketItem.weight.toString()
            tvDescription.text = marketItem.description.toString()

            btnProcessOrder.setOnClickListener {
                viewModel.updateOrder(marketItem.id)
            }

            btnFinishOrder.setOnClickListener {
                viewModel.completeOrder(marketItem)
            }

            viewModel.userDataState.observe(this@EditProductActivity) { userDataState ->
                if (userDataState is ResultState.Success) {
                    viewModel.orderState.observe(this@EditProductActivity) {
                        if (it == MarketTransactionStatuses.FINISHED || it == MarketTransactionStatuses.CANCELLED) {
                            btnFinishOrder.isVisible = false
                            btnProcessOrder.isVisible = false
                        } else {
                            btnFinishOrder.isVisible =
                                userDataState.data.role == UserRoles.P_SMALL.name
                            btnProcessOrder.isVisible =
                                userDataState.data.role == UserRoles.P_LARGE.name
                        }
                    }
                    viewModel.getOrderStatus(marketItem.id)
                }
            }

            viewModel.completeOrderState.observe(this@EditProductActivity) { resultState ->
                progressBar.isVisible = resultState is ResultState.Loading

                if (resultState is ResultState.Success) {
                    resultState.data.getData()?.let {
                        Toast.makeText(this@EditProductActivity, it, Toast.LENGTH_SHORT).show()
                    }
                } else if (resultState is ResultState.Error) {
                    resultState.exception.getData()?.handleHttpException()?.let { message ->
                        Toast.makeText(this@EditProductActivity, message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            viewModel.updateOrderState.observe(this@EditProductActivity) { resultState ->
                progressBar.isVisible = resultState is ResultState.Loading

                if (resultState is ResultState.Success) {
                    resultState.data.getData()?.let {
                        Toast.makeText(this@EditProductActivity, it, Toast.LENGTH_SHORT).show()
                    }
                } else if (resultState is ResultState.Error) {
                    resultState.exception.getData()?.handleHttpException()?.let { message ->
                        Toast.makeText(this@EditProductActivity, message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

        viewModel.getUserProfile()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_MARKET_ITEM_KEY = "EXTRA_MARKET_ITEM_KEY"
    }
}