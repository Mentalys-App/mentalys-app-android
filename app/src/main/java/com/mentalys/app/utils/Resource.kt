package com.mentalys.app.utils

sealed class Resource<out R> private constructor() {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val error: String) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
}