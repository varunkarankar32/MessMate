package com.example.messmate

data class Feedback(
    val id: String = "",
    val userId: String = "",
    val userEmail: String = "",
    val title: String = "",
    val description: String = "",
    val rating: Int = 0, // 1-5 stars
    val category: String = "", // food_quality, service, cleanliness, other
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "pending" // pending, reviewed, resolved
)