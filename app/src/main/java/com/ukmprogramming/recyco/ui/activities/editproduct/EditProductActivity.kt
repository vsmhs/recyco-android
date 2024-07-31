package com.ukmprogramming.recyco.ui.activities.editproduct

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.ukmprogramming.recyco.data.network.response.models.MarketItem
import com.ukmprogramming.recyco.databinding.ActivityEditProductBinding
import com.ukmprogramming.recyco.ui.activities.main.MainActivity
import com.ukmprogramming.recyco.ui.activities.productdetail.ProductDetailActivity
import com.ukmprogramming.recyco.ui.activities.productdetail.ProductDetailActivity.Companion
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

        val marketItem = intent.getParcelableExtra<MarketItem>(EXTRA_MARKET_ITEM_KEY) ?: run {
            finish()
            return
        }

        binding.apply {
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
                    resultState.exception.getData()?.handleHttpException(this@EditProductActivity)
                        ?.let { message ->
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
                    resultState.exception.getData()?.handleHttpException(this@EditProductActivity)
                        ?.let { message ->
                            Toast.makeText(this@EditProductActivity, message, Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            }
        }

        viewModel.getUserProfile()
    }

    companion object {
        const val EXTRA_MARKET_ITEM_KEY = "EXTRA_MARKET_ITEM_KEY"
    }
}