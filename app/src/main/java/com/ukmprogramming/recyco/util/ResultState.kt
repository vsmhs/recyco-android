package com.ukmprogramming.recyco.util

sealed class ResultState<out R> {
    data class Success<T>(val data: T) : ResultState<T>()
    data class Error(val exception: SingleEvent<Exception>) : ResultState<Nothing>()
    data object Loading : ResultState<Nothing>()
}