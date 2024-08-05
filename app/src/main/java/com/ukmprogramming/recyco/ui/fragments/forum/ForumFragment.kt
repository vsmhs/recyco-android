package com.ukmprogramming.recyco.ui.fragments.forum

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukmprogramming.recyco.databinding.FragmentForumBinding
import com.ukmprogramming.recyco.ui.activities.adddiscussion.AddDiscussionActivity
import com.ukmprogramming.recyco.ui.activities.discussion.DiscussionActivity
import com.ukmprogramming.recyco.ui.adapters.ForumItemAdapter
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.handleHttpException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForumFragment : Fragment() {
    private var _binding: FragmentForumBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModels<ForumViewModel>()

    private val launcherAddDiscussionActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_CODE_ADD_DISCUSSION) {
            viewModel.getForumPosts()
        }
    }

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
        val forumItemAdapter = ForumItemAdapter {
            launcherAddDiscussionActivity.launch(
                Intent(
                    activity,
                    DiscussionActivity::class.java
                ).apply {
                    putExtra(DiscussionActivity.EXTRA_FORUM_POST_ID, it.id)
                }
            )
        }

        binding.apply {
            recyclerView.apply {
                adapter = forumItemAdapter
                layoutManager = LinearLayoutManager(activity)
            }

            fab.setOnClickListener {
                startActivity(
                    Intent(
                        activity,
                        AddDiscussionActivity::class.java
                    )
                )
            }

            viewModel.forumDataState.observe(viewLifecycleOwner) { resultState ->
                progressBar.isVisible = resultState is ResultState.Loading

                if (resultState is ResultState.Success) {
                    forumItemAdapter.submitList(resultState.data)
                } else if (resultState is ResultState.Error) {
                    resultState.exception.getData()?.handleHttpException()?.let { message ->
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewModel.getForumPosts()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val RESULT_CODE_ADD_DISCUSSION = 12345678
    }
}