package com.ukmprogramming.recyco.ui.activities.discussion

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukmprogramming.recyco.R
import com.ukmprogramming.recyco.databinding.ActivityDiscussionBinding
import com.ukmprogramming.recyco.ui.adapters.CommentItemAdapter
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.handleHttpException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscussionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiscussionBinding
    private val viewModel by viewModels<DiscussionViewModel>()

    private val commentItemAdapter = CommentItemAdapter {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDiscussionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.discussion)
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        val forumPostId = intent.getStringExtra(EXTRA_FORUM_POST_ID) ?: run {
            finish()
            return
        }

        binding.apply {
            recyclerView.apply {
                adapter = commentItemAdapter
                layoutManager = LinearLayoutManager(this@DiscussionActivity)
            }

            viewModel.dataState.observe(this@DiscussionActivity) { resultState ->
                progressBar.isVisible = resultState is ResultState.Loading

                if (resultState is ResultState.Success) {
                    tvTitle.text = resultState.data.title
                    tvDescription.text = resultState.data.description

                    commentItemAdapter.submitList(resultState.data.replies)
                } else if (resultState is ResultState.Error) {
                    resultState.exception.getData()?.handleHttpException()?.let { message ->
                        Toast.makeText(this@DiscussionActivity, message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            viewModel.addReplyState.observe(this@DiscussionActivity) { resultState ->
                progressBar.isVisible = resultState is ResultState.Loading

                if (resultState is ResultState.Success) {
                    Toast.makeText(this@DiscussionActivity, "Add Reply Success", Toast.LENGTH_SHORT)
                        .show()
                    viewModel.getForumPostWithReply(forumPostId)
                } else if (resultState is ResultState.Error) {
                    resultState.exception.getData()?.handleHttpException()?.let { message ->
                        Toast.makeText(this@DiscussionActivity, message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            fab.setOnClickListener {
                val description = etComment.text.toString()
                viewModel.addReply(
                    forumPostId,
                    description
                )
            }
        }

        viewModel.getForumPostWithReply(forumPostId)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_FORUM_POST_ID = "EXTRA_FORUM_POST_ID"
    }
}