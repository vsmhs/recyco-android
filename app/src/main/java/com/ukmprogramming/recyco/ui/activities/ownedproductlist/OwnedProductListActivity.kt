package com.ukmprogramming.recyco.ui.activities.ownedproductlist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.ukmprogramming.recyco.databinding.ActivityOwnedProductListBinding
import com.ukmprogramming.recyco.ui.activities.editproduct.EditProductActivity
import com.ukmprogramming.recyco.ui.activities.productdetail.ProductDetailActivity
import com.ukmprogramming.recyco.ui.adapters.MarketItemAdapter
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.handleHttpException

class OwnedProductListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOwnedProductListBinding
    private val viewModel by viewModels<OwnedProductListViewModel>()

    private val dataAdapter = MarketItemAdapter {
        startActivity(Intent(this, EditProductActivity::class.java).apply {
            putExtra(EditProductActivity.EXTRA_MARKET_ITEM_KEY, it)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOwnedProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            recyclerView.apply {
                adapter = dataAdapter
                layoutManager = GridLayoutManager(this@OwnedProductListActivity, 2)
            }

            viewModel.marketDataState.observe(this@OwnedProductListActivity) { resultState ->
                progressBar.isVisible = resultState is ResultState.Loading

                if (resultState is ResultState.Success) {
                    dataAdapter.submitList(resultState.data)
                } else if (resultState is ResultState.Error) {
                    resultState.exception.getData()
                        ?.handleHttpException(this@OwnedProductListActivity)?.let { message ->
                            Toast.makeText(
                                this@OwnedProductListActivity,
                                message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
        }

        viewModel.getSelfMarketItems()
    }
}