package com.ukmprogramming.recyco.ui.activities.adddiscussion

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ukmprogramming.recyco.R
import com.ukmprogramming.recyco.databinding.ActivityAddDiscussionBinding

class AddDiscussionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddDiscussionBinding
    private val viewModel by viewModels<AddDiscussionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddDiscussionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.add_discussion)
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}