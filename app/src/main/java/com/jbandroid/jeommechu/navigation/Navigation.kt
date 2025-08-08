package com.jbandroid.jeommechu.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jbandroid.jeommechu.screen_view.RecommendationScreen
import com.jbandroid.jeommechu.screen_view.RouletteScreen
import com.jbandroid.jeommechu.screen_view.StartScreen
import com.jbandroid.jeommechu.screen_view.CalendarMemoScreen
import com.jbandroid.jeommechu.screen_view.UserMapScreen
import com.jbandroid.jeommechu.screen_view.selectionScreen.SelectionEmotionScreen
import com.jbandroid.jeommechu.screen_view.selectionScreen.SelectionFeatureScreen
import com.jbandroid.jeommechu.screen_view.selectionScreen.SelectionIntroScreen
import com.jbandroid.jeommechu.screen_view.selectionScreen.SelectionResultScreen
import com.jbandroid.jeommechu.screen_view.selectionScreen.SelectionStyleScreen
import com.jbandroid.jeommechu.screen_view.selectionScreen.SelectionTypeScreen
import com.jbandroid.jeommechu.viewmodel.MainViewModel


@Composable
fun Navigation(
    navController: NavHostController,
    mainViewModel: MainViewModel = viewModel()
) {

    NavHost(navController = navController, startDestination = ScreenRoute.StartScreen.route)
    {
        composable(ScreenRoute.StartScreen.route) {
            StartScreen(navController)
        }

        composable(ScreenRoute.SelectionIntro.route) {
            SelectionIntroScreen(navController, mainViewModel)
        }
        composable(ScreenRoute.SelectionEmotion.route) {
            SelectionEmotionScreen(navController, mainViewModel)
        }
        composable(ScreenRoute.SelectionResult.route) {
            SelectionResultScreen(navController, mainViewModel)
        }
        composable(ScreenRoute.SelectionType.route) {
            SelectionTypeScreen(navController, mainViewModel)
        }
        composable(ScreenRoute.SelectionFeature.route) {
            SelectionFeatureScreen(navController, mainViewModel)
        }
        composable(ScreenRoute.SelectionStyle.route) {
            SelectionStyleScreen(navController, mainViewModel)
        }

        composable(ScreenRoute.RecommendationScreen.route) {
            RecommendationScreen(navController, mainViewModel)
        }

        composable(
            route = ScreenRoute.UserMap.route,
            arguments = listOf(navArgument("conditionKey") { type = NavType.StringType })
        ) { backStackEntry ->
            val conditionKey = backStackEntry.arguments?.getString("conditionKey") ?: ""
            UserMapScreen(conditionKey, navController, mainViewModel)
        }

        composable(ScreenRoute.RouletteScreen.route) {
            RouletteScreen(navController, mainViewModel)
        }

        composable(ScreenRoute.CalendarMemoScreen.route) {
            CalendarMemoScreen(navController)
        }
    }
}
