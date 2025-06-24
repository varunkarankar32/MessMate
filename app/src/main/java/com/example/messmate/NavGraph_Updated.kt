package com.example.messmate

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SignupScreen.route
    ) {
        composable(Screen.SignupScreen.route) {
            SignUpScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(Screen.LoginScreen.route) }
            )
        }

        composable(Screen.LoginScreen.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToSignUp = { navController.navigate(Screen.SignupScreen.route) }
            ) {
                // Navigate to MainScreen instead of HomeScreen after successful login
                navController.navigate(Screen.MainScreen.route) {
                    // Clear the back stack so user can't go back to login
                    popUpTo(Screen.SignupScreen.route) {
                        inclusive = true
                    }
                }
            }
        }

        composable(Screen.MainScreen.route) {
            MainScreen(
                onLogout = {
                    // Navigate back to login screen and clear the back stack
                    navController.navigate(Screen.LoginScreen.route) {
                        popUpTo(Screen.MainScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Keep the old HomeScreen for backward compatibility if needed
        composable(Screen.HomeScreen.route) {
            HomeScreen(
                onNotificationsClick = {},
                onProfileClick = {}
            )
        }
    }
}