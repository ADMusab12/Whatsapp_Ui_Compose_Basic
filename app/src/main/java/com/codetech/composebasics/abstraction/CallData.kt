package com.codetech.composebasics.abstraction

data class CallData(
    val name: String,
    val avatar: Int,
    val isFavorite: Boolean,
    val timestamp: String? = null
)