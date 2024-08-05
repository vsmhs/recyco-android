package com.ukmprogramming.recyco.ui.activities.addproduct

import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.ukmprogramming.recyco.R
import com.ukmprogramming.recyco.databinding.ActivityAddProductBinding
import com.ukmprogramming.recyco.ui.fragments.market.MarketFragment
import com.ukmprogramming.recyco.util.Helpers
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.handleHttpException
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    private val viewModel by viewModels<AddProductViewModel>()

    private var imageFile: File? = null

    private val intentGalleryLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {
        if (it != null) {
            imageFile = Helpers.uriToFile(it, this)
            Glide.with(this)
                .load(imageFile)
                .placeholder(R.drawable.ic_broken_image)
                .error(R.drawable.ic_broken_image)
                .into(binding.ivPreview)
        } else {
            Toast.makeText(this, "Failed to get image", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.add_waste)
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        binding.apply {
            binding.clImagePicker.setOnClickListener {
                intentGalleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            binding.btnAdd.setOnClickListener {
                val name = etName.text.toString()
                val description = etDescription.text.toString()
                val price = etPrice.text.toString().toDoubleOrNull()
                val weight = etWeight.text.toString().toDoubleOrNull()

                imageFile?.let {
                    if (price != null && weight != null) {
                        viewModel.createMarketItem(
                            name,
                            price,
                            weight,
                            it,
                            description,
                        )
                    } else {
                        Toast.makeText(
                            this@AddProductActivity,
                            "Please fill price, weight, and image",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } ?: run {
                    Toast.makeText(
                        this@AddProductActivity,
                        "Please fill price, weight, and image",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            viewModel.createItemState.observe(this@AddProductActivity) { resultState ->
                progressBar.isVisible = resultState is ResultState.Loading

                if (resultState is ResultState.Success) {
                    setResult(MarketFragment.RESULT_CODE_ADD_PRODUCT)
                    finish()
                } else if (resultState is ResultState.Error) {
                    resultState.exception.getData()?.handleHttpException()?.let { message ->
                        Toast.makeText(this@AddProductActivity, message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}