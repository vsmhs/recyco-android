package com.ukmprogramming.recyco.ui.activities.articlelist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukmprogramming.recyco.R
import com.ukmprogramming.recyco.databinding.ActivityArticleListBinding
import com.ukmprogramming.recyco.ui.activities.articledetail.ArticleDetailActivity
import com.ukmprogramming.recyco.ui.adapters.ArticleItemAdapter
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.handleHttpException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticleListBinding
    private val viewModel by viewModels<ArticleListViewModel>()

    private val articleItemAdapter = ArticleItemAdapter {
        startActivity(
            Intent(
                this,
                ArticleDetailActivity::class.java
            ).apply {
                putExtra(ArticleDetailActivity.EXTRA_ARTICLE_ID_KEY, it)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityArticleListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.article)
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        binding.apply {
            recyclerView.apply {
                adapter = articleItemAdapter
                layoutManager = LinearLayoutManager(this@ArticleListActivity)
            }

            viewModel.articleDataState.observe(this@ArticleListActivity) { resultState ->
                progressBar.isVisible = resultState is ResultState.Loading

                if (resultState is ResultState.Success) {
                    articleItemAdapter.submitList(resultState.data)
                } else if (resultState is ResultState.Error) {
                    resultState.exception.getData()?.handleHttpException()?.let { message ->
                        Toast.makeText(this@ArticleListActivity, message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

        viewModel.getArticles()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}