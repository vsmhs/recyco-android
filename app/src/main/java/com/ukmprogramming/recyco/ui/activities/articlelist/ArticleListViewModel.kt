package com.ukmprogramming.recyco.ui.activities.articlelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ukmprogramming.recyco.data.ArticleRepository
import com.ukmprogramming.recyco.data.network.response.models.Article
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val articleRepository: ArticleRepository
) : ViewModel() {
    private val _articleDataState = MutableLiveData<ResultState<List<Article>>>()
    val articleDataState: LiveData<ResultState<List<Article>>>
        get() = _articleDataState

    fun getArticles() = viewModelScope.launch {
        _articleDataState.value = ResultState.Loading

        try {
            val response = articleRepository.getArticles()

            if (response.success && response.data != null) {
                _articleDataState.value = ResultState.Success(response.data)
            } else {
                _articleDataState.value =
                    ResultState.Error(SingleEvent(Exception(response.message)))
            }
        } catch (e: Exception) {
            _articleDataState.value = ResultState.Error(SingleEvent(e))
        }
    }
}