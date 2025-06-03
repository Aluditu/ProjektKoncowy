package com.example.projektkoncowy

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projektkoncowy.ui.screens.HomeScreen
import com.example.projektkoncowy.ui.screens.PostDetailScreen
import com.example.projektkoncowy.ui.screens.ProfileScreen
import com.example.projektkoncowy.ui.screens.UserDetailScreen

@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("postDetail/{postId}") { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId")?.toIntOrNull() ?: return@composable
            PostDetailScreen(navController, postId)
        }
        composable("userDetail/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?: return@composable
            UserDetailScreen(navController, userId)
        }
        composable("profile") {
            ProfileScreen(navController)
        }
    }
}
