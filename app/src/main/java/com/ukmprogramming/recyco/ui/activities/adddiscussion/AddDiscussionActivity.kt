package com.ukmprogramming.recyco.ui.activities.adddiscussion

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.ukmprogramming.recyco.R
import com.ukmprogramming.recyco.databinding.ActivityAddDiscussionBinding
import com.ukmprogramming.recyco.ui.fragments.forum.ForumFragment
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.handleHttpException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

        binding.apply {
            btnAdd.setOnClickListener {
                val title = etName.text.toString()
                val description = etDescription.text.toString()

                viewModel.addDiscussion(title, description)
            }

            viewModel.addDiscussionState.observe(this@AddDiscussionActivity) { resultState ->
                progressBar.isVisible = resultState is ResultState.Loading

                if (resultState is ResultState.Success) {
                    Toast.makeText(
                        this@AddDiscussionActivity,
                        "Add Discussion Success",
                        Toast.LENGTH_SHORT
                    ).show()
                    setResult(ForumFragment.RESULT_CODE_ADD_DISCUSSION)
                    finish()
                } else if (resultState is ResultState.Error) {
                    resultState.exception.getData()?.handleHttpException()?.let { message ->
                        Toast.makeText(this@AddDiscussionActivity, message, Toast.LENGTH_SHORT)
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