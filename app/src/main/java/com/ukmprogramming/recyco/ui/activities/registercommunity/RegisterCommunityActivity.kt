package com.ukmprogramming.recyco.ui.activities.registercommunity

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.ukmprogramming.recyco.R
import com.ukmprogramming.recyco.databinding.ActivityRegisterCommunityBinding

class RegisterCommunityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterCommunityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.register_community)
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        binding.webView.apply {
            webViewClient = WebViewClient()

            loadUrl(FORM_URL)

            settings.javaScriptEnabled = true

            settings.setSupportZoom(false)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val FORM_URL =
            "https://docs.google.com/forms/d/e/1FAIpQLSfYxD3Spl-_n_zK9KL2s8UgeAEcGgflVS-V2RyCqYrrzM50aA/viewform?usp=sf_link"
    }
}