package com.ukmprogramming.recyco.ui.activities.articledetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.ukmprogramming.recyco.BuildConfig
import com.ukmprogramming.recyco.R
import com.ukmprogramming.recyco.data.network.response.models.Article
import com.ukmprogramming.recyco.databinding.ActivityArticleDetailBinding
import com.ukmprogramming.recyco.util.Helpers
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticleDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityArticleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.article_detail)
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        val article = intent.getParcelableExtra<Article>(EXTRA_ARTICLE_ID_KEY) ?: run {
            finish()
            return
        }

        binding.apply {
            tvUserName.text = "Admin"
            tvDate.text = Helpers.formatDateNoTime(article.createdAt)

            Glide.with(this@ArticleDetailActivity)
                .load(GlideUrl("${BuildConfig.BASE_URL}${article.thumbnailUrl}") {
                    mapOf(Pair("ngrok-skip-browser-warning", "ngrok-skip-browser-warning"))
                })
                .timeout(30000)
                .error(R.drawable.ic_broken_image)
                .placeholder(R.drawable.ic_broken_image)
                .into(binding.ivThumbnail)

            tvDescription.text = article.description
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_ARTICLE_ID_KEY = "EXTRA_ARTICLE_ID_KEY"
    }
}