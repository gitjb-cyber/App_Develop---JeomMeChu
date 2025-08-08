package com.jbandroid.jeommechu.screen_view.selectionScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jbandroid.jeommechu.R
import com.jbandroid.jeommechu.navigation.ScreenRoute
import com.jbandroid.jeommechu.ui.util.AppBarView
import com.jbandroid.jeommechu.ui.util.SelectionCard
import com.jbandroid.jeommechu.ui.util.StatusBarView
import com.jbandroid.jeommechu.viewmodel.MainViewModel


@Composable
fun SelectionIntroScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    StatusBarView()
    Scaffold(
        topBar = {
            AppBarView(navController = navController) { navController.navigateUp() }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            SelectionCard {
                Text(
                    text = "🍽 오늘 뭐 먹을까?",
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.jua_regular)),
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                Button(
                    onClick = { navController.navigate(ScreenRoute.SelectionEmotion.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text("🎯 오늘의 기분으로 추천받기")
                }

                Button(
                    onClick = { navController.navigate(ScreenRoute.SelectionType.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text("⚙ 조건 직접 선택")
                }

                Button(
                    onClick = {
                        mainViewModel.setMatchingConditionsFromAllFoods()
                        navController.navigate(ScreenRoute.RouletteScreen.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text("🎲 아무거나 추천해줘")
                }
            }
        }
    }
}