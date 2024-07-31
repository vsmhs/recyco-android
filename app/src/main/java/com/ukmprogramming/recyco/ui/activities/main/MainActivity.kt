package com.ukmprogramming.recyco.ui.activities.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ukmprogramming.recyco.R
import com.ukmprogramming.recyco.databinding.ActivityMainBinding
import com.ukmprogramming.recyco.ui.fragments.forum.ForumFragment
import com.ukmprogramming.recyco.ui.fragments.home.HomeFragment
import com.ukmprogramming.recyco.ui.fragments.market.MarketFragment
import com.ukmprogramming.recyco.ui.fragments.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        switchFragment(getCurrentFragment())

        binding.apply {
            bottomNavigationView.apply {
                setOnItemSelectedListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.home -> {
                            switchFragment(HomeFragment())
                            true
                        }

                        R.id.market -> {
                            switchFragment(MarketFragment())
                            true
                        }

                        R.id.forum -> {
                            switchFragment(ForumFragment())
                            true
                        }

                        R.id.profile -> {
                            switchFragment(ProfileFragment())
                            true
                        }

                        else -> false
                    }
                }
            }
        }
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.contentFrame.id, fragment)
            .commit()
    }

    private fun getCurrentFragment(): Fragment {
        return supportFragmentManager.findFragmentById(binding.contentFrame.id) ?: HomeFragment()
    }
}