package com.ukmprogramming.recyco.util

open class SingleEvent<T>(
    private val data: T
) {
    var isHandled = false
        private set

    fun getData() = if (isHandled) {
        null
    } else {
        isHandled = true
        data
    }
}