package com.ukmprogramming.recyco.ui.activities.community

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukmprogramming.recyco.R
import com.ukmprogramming.recyco.databinding.ActivityCommunityBinding
import com.ukmprogramming.recyco.ui.adapters.CommunityItemAdapter
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.handleHttpException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommunityBinding
    private val viewModel by viewModels<CommunityViewModel>()

    private val communityItemAdapter = CommunityItemAdapter {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.community)
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        binding.apply {
            recyclerView.apply {
                adapter = communityItemAdapter
                layoutManager = LinearLayoutManager(this@CommunityActivity)
            }
            viewModel.communityDataState.observe(this@CommunityActivity) { resultState ->
                progressBar.isVisible = resultState is ResultState.Loading

                if (resultState is ResultState.Success) {
                    communityItemAdapter.submitList(resultState.data)
                } else if (resultState is ResultState.Error) {
                    resultState.exception.getData()?.handleHttpException()?.let { message ->
                        Toast.makeText(this@CommunityActivity, message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

        viewModel.getCommunities()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}