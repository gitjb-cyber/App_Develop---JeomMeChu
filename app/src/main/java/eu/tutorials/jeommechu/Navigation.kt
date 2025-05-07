package eu.tutorials.jeommechu

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import eu.tutorials.jeommechu.screen_view.RecommendationScreen
import eu.tutorials.jeommechu.screen_view.RouletteScreen
import eu.tutorials.jeommechu.screen_view.ScreenRoute
import eu.tutorials.jeommechu.screen_view.SelectionScreen
import eu.tutorials.jeommechu.screen_view.StartScreen
import eu.tutorials.jeommechu.screen_view.CalendarMemoScreen
import eu.tutorials.jeommechu.screen_view.UserMapScreen
import eu.tutorials.jeommechu.viewmodel.MainViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    mainViewModel: MainViewModel = viewModel()
) {

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

        composable(
            route = ScreenRoute.UserMap.route,
            arguments = listOf(navArgument("conditionKey") { type = NavType.StringType })
        ) { backStackEntry ->
            val conditionKey = backStackEntry.arguments?.getString("conditionKey") ?: ""
            UserMapScreen(conditionKey, mainViewModel)
        }

        composable(ScreenRoute.RouletteScreen.route) {
            RouletteScreen(navController, mainViewModel)
        }

        composable(ScreenRoute.CalendarMemoScreen.route) {
            CalendarMemoScreen(navController)
        }
    }
}
