package com.ukmprogramming.recyco.ui.activities.reward

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ukmprogramming.recyco.R
import com.ukmprogramming.recyco.databinding.ActivityRewardBinding

class RewardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRewardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRewardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.recyco_coins)
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}