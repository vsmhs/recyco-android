package com.ukmprogramming.recyco.ui.fragments.market

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.ukmprogramming.recyco.databinding.FragmentMarketBinding
import com.ukmprogramming.recyco.ui.activities.productdetail.ProductDetailActivity
import com.ukmprogramming.recyco.ui.adapters.MarketItemAdapter
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.UserRoles
import com.ukmprogramming.recyco.util.handleHttpException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarketFragment : Fragment() {
    private var _binding: FragmentMarketBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModels<MarketViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity()

        val dataAdapter = MarketItemAdapter {
            startActivity(Intent(activity, ProductDetailActivity::class.java).apply {
                putExtra(ProductDetailActivity.EXTRA_MARKET_ITEM_KEY, it)
            })
        }

        binding.apply {
            recyclerView.apply {
                adapter = dataAdapter
                layoutManager = GridLayoutManager(activity, 2)
            }

            viewModel.loadingState.observe(viewLifecycleOwner) {
                progressBar.isVisible = it
            }

            viewModel.marketDataState.observe(viewLifecycleOwner) { resultState ->
                if (resultState is ResultState.Success) {
                    dataAdapter.submitList(resultState.data)
                } else if (resultState is ResultState.Error) {
                    resultState.exception.getData()?.handleHttpException(activity)?.let { message ->
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            viewModel.userDataState.observe(viewLifecycleOwner) { resultState ->
                if (resultState is ResultState.Success) {
                    btnHistory.isVisible = resultState.data.role == UserRoles.C_SMALL.name ||
                            resultState.data.role == UserRoles.C_LARGE.name
                    btnOwnedProduct.isVisible = resultState.data.role == UserRoles.P_SMALL.name ||
                            resultState.data.role == UserRoles.P_LARGE.name
                } else if (resultState is ResultState.Error) {
                    resultState.exception.getData()?.handleHttpException(activity)?.let { message ->
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewModel.getUserProfile()
        viewModel.getMarketItems()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}