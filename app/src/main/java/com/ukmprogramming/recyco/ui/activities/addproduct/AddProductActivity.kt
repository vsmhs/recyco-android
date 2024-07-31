package com.ukmprogramming.recyco.ui.activities.addproduct

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ThemedSpinnerAdapter.Helper
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.ukmprogramming.recyco.R
import com.ukmprogramming.recyco.databinding.ActivityAddProductBinding
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
                .into(binding.ivPreview)
        } else {
            Toast.makeText(this, "Failed to get image", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            binding.clImagePicker.setOnClickListener {
                intentGalleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            binding.btnAdd.setOnClickListener {
                val name = etName.text.toString()
                val description = etDescription.text.toString()
                val price = etPrice.text.toString().toDoubleOrNull()
                val weight = etWeight.text.toString().toDoubleOrNull()

                if (price != null && weight != null) {
                    viewModel.createMarketItem(
                        name,
                        price,
                        weight,
                        description,
                        imageFile
                    )
                } else {
                    Toast.makeText(
                        this@AddProductActivity,
                        "Please fill all data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            viewModel.createItemState.observe(this@AddProductActivity) { resultState ->
                progressBar.isVisible = resultState is ResultState.Loading

                if (resultState is ResultState.Success) {
                    finish()
                } else if (resultState is ResultState.Error) {
                    resultState.exception.getData()?.handleHttpException(this@AddProductActivity)
                        ?.let { message ->
                            Toast.makeText(this@AddProductActivity, message, Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            }
        }
    }
}