package com.example.messmate

data class Discussion(
    val id: String = "",
    val userId: String = "",
    val userEmail: String = "",
    val userName: String = "",
    val title: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val replies: List<DiscussionReply> = emptyList(),
    val likes: Int = 0,
    val category: String = "general" // general, complaints, suggestions, announcements
)

data class DiscussionReply(
    val id: String = "",
    val userId: String = "",
    val userEmail: String = "",
    val userName: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis()
)