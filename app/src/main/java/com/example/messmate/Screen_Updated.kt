package com.example.messmate

sealed class Screen(val route: String) {
    object LoginScreen : Screen("loginscreen")
    object SignupScreen : Screen("signupscreen")
    object HomeScreen : Screen("homescreen")
    object MainScreen : Screen("mainscreen")
    object MessMenuScreen : Screen("mess_menu")
    object AttendanceScreen : Screen("attendance")
    object FeedbackScreen : Screen("feedback")
    object DiscussScreen : Screen("discuss")
}