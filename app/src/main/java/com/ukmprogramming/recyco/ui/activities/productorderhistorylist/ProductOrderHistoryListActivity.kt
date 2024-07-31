package com.ukmprogramming.recyco.ui.activities.productorderhistorylist

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ukmprogramming.recyco.R
import com.ukmprogramming.recyco.databinding.ActivityProductOrderHistoryListBinding

class ProductOrderHistoryListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductOrderHistoryListBinding
    private val viewModel by viewModels<ProductOrderHistoryListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductOrderHistoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}