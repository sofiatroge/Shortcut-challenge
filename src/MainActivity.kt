package com.shortcut.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shortcut.myapplication.ui.comic.ComicScreen
import com.shortcut.myapplication.ui.favorites.FavoritesScreen
import com.shortcut.myapplication.ui.theme.XkcdViewerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            XkcdViewerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    XkcdApp()
                }
            }
        }
    }
}

@Composable
fun XkcdApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "comic") {
        composable("comic") {
            ComicScreen(
                onNavigateToFavorites = {
                    navController.navigate("favorites")
                }
            )
        }

        composable("favorites") {
            FavoritesScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onComicClick = { comicNum ->
                    navController.popBackStack()
                    // Could navigate to specific comic here
                }
            )
        }
    }
}