package com.jbandroid.jeommechu.screen_view.selectionScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.jbandroid.jeommechu.ui.design.AppBackdrop
import com.jbandroid.jeommechu.ui.design.AppPage
import com.jbandroid.jeommechu.ui.design.AppTopBar
import com.jbandroid.jeommechu.ui.design.JeomButtonPrimary
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
            AppTopBar(
                title = "",
                onBack = { navController.navigateUp() },
                onCalendar = { navController.navigate(ScreenRoute.CalendarMemoScreen.route) }
            )
        }
    ) { innerPadding ->
        AppBackdrop {
            AppPage(innerPadding = innerPadding) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    SelectionCard {
                        Text(
                            text = "🍽 오늘 뭐 먹을까?",
                            fontSize = 24.sp,
                            modifier = Modifier.padding(bottom = 32.dp)
                        )
                        JeomButtonPrimary(
                            text = "🎯 오늘의 기분으로 추천받기",
                            onClick = { navController.navigate(ScreenRoute.SelectionEmotion.route) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        JeomButtonPrimary(
                            text = "⚙ 조건 직접 선택",
                            onClick = { navController.navigate(ScreenRoute.SelectionType.route) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        JeomButtonPrimary(
                            text = "🎲 아무거나 추천해줘",
                            onClick = {
                                mainViewModel.setMatchingConditionsFromAllFoods()
                                navController.navigate(ScreenRoute.RouletteScreen.route)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}