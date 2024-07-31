package com.ukmprogramming.recyco.ui.fragments.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ukmprogramming.recyco.databinding.FragmentHomeBinding
import com.ukmprogramming.recyco.ui.activities.articlelist.ArticleListActivity
import com.ukmprogramming.recyco.ui.activities.community.CommunityActivity
import com.ukmprogramming.recyco.ui.activities.registercommunity.RegisterCommunityActivity
import com.ukmprogramming.recyco.ui.activities.reward.RewardActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity()

        binding.apply {
            cvCoins.setOnClickListener {
                startActivity(Intent(activity, RewardActivity::class.java))
            }

            cvRegisterCommunity.setOnClickListener {
                startActivity(Intent(activity, RegisterCommunityActivity::class.java))
            }

            btnLocation.setOnClickListener {
                // TODO: to location activity
            }

            btnArticle.setOnClickListener {
                startActivity(Intent(activity, ArticleListActivity::class.java))
            }

            btnCommunity.setOnClickListener {
                startActivity(Intent(activity, CommunityActivity::class.java))
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}