package com.codetech.composebasics.abstraction

data class ChatData(
    val name: String,
    val lastMessage: String,
    val timestamp: String,
    val avatar: Int,
    val unreadCount: Int = 0,
    val isTyping: Boolean = false
)