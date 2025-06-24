package com.example.messmate

data class Attendance(
    val id: String = "",
    val userId: String = "",
    val userEmail: String = "",
    val date: String = "", // Format: yyyy-mm-dd
    val mealType: String = "", // breakfast, lunch, dinner
    val isPresent: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)