package com.example.messmate

data class MenuItem(
    val id: String = "",
    val name: String = "",
    val category: String = "", // breakfast, lunch, dinner
    val price: Double = 0.0,
    val description: String = "",
    val isAvailable: Boolean = true,
    val date: String = "" // Format: yyyy-mm-dd
)