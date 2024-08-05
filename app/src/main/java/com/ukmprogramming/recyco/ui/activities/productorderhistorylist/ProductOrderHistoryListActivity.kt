package com.ukmprogramming.recyco.ui.activities.productorderhistorylist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukmprogramming.recyco.R
import com.ukmprogramming.recyco.databinding.ActivityProductOrderHistoryListBinding
import com.ukmprogramming.recyco.ui.activities.deliverystatus.DeliveryStatusActivity
import com.ukmprogramming.recyco.ui.adapters.ProductOrderHistoryItemAdapter
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.handleHttpException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductOrderHistoryListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductOrderHistoryListBinding
    private val viewModel by viewModels<ProductOrderHistoryListViewModel>()

    private val dataAdapter = ProductOrderHistoryItemAdapter {
        startActivity(Intent(this, DeliveryStatusActivity::class.java).apply {
            putExtra(DeliveryStatusActivity.EXTRA_MARKET_ITEM_ID_KEY, it.item.id)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductOrderHistoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.order_history)
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        binding.apply {
            recyclerView.apply {
                adapter = dataAdapter
                layoutManager = LinearLayoutManager(this@ProductOrderHistoryListActivity)
            }

            viewModel.orderHistoryDataState.observe(this@ProductOrderHistoryListActivity) { resultState ->
                progressBar.isVisible = resultState is ResultState.Loading

                if (resultState is ResultState.Success) {
                    dataAdapter.submitList(resultState.data)
                } else if (resultState is ResultState.Error) {
                    resultState.exception.getData()?.handleHttpException()?.let { message ->
                        Toast.makeText(
                            this@ProductOrderHistoryListActivity,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        viewModel.getOrderHistory()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}