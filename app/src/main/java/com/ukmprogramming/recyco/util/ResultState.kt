package com.ukmprogramming.recyco.util

sealed class ResultState<out R> private constructor() {
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(val exception: SingleEvent<Exception>) : ResultState<Nothing>()
    data object Loading : ResultState<Nothing>()
}