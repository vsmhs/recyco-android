package com.ukmprogramming.recyco.ui.fragments.forum

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ukmprogramming.recyco.databinding.FragmentForumBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForumFragment : Fragment() {
    private var _binding: FragmentForumBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModels<ForumViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}