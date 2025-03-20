package eu.tutorials.jeommechu

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import eu.tutorials.jeommechu.screen_view.RecommendationScreen
import eu.tutorials.jeommechu.screen_view.RouletteScreen
import eu.tutorials.jeommechu.screen_view.ScreenRoute
import eu.tutorials.jeommechu.screen_view.SelectionScreen
import eu.tutorials.jeommechu.screen_view.StartScreen
import eu.tutorials.jeommechu.viewmodel.MainViewModel

@Composable
fun Navigation(navController: NavHostController) {
    val mainViewModel: MainViewModel = viewModel()

    NavHost(navController = navController, startDestination = ScreenRoute.StartScreen.route) {
        composable(ScreenRoute.StartScreen.route) {
            StartScreen(navController)
        }
        composable(ScreenRoute.SelectionScreen.route) {
            SelectionScreen(navController, mainViewModel)
        }
        composable(ScreenRoute.RecommendationScreen.route) {
            RecommendationScreen(navController, mainViewModel)
        }
        /*
        composable(ScreenRoute.UserMap.route) {
            UserMapScreen(navController)
        }
        */
        composable(ScreenRoute.RouletteScreen.route) {
            RouletteScreen(navController, mainViewModel)
        }
    }
}
