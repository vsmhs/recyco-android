package com.ukmprogramming.recyco.ui.activities.ownedproductlist

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ukmprogramming.recyco.databinding.ActivityOwnedProductListBinding

class OwnedProductListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOwnedProductListBinding
    private val viewModel by viewModels<OwnedProductListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOwnedProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}