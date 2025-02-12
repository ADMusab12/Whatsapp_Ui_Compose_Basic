package com.codetech.composebasics.abstraction.sealed

sealed class Result<out T> {
    data class Success<T>(
        val data: T,
    ) : Result<T>()

    data class Failure(
        val exception: String,
    ) : Result<Nothing>()
}