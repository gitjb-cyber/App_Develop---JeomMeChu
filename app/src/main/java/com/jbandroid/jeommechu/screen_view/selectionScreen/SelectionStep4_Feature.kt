package com.jbandroid.jeommechu.screen_view.selectionScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jbandroid.jeommechu.navigation.ScreenRoute
import com.jbandroid.jeommechu.ui.design.AppBackdrop
import com.jbandroid.jeommechu.ui.design.AppPage
import com.jbandroid.jeommechu.ui.design.AppTopBar
import com.jbandroid.jeommechu.ui.design.JeomButtonPrimary
import com.jbandroid.jeommechu.ui.util.FeatureGroup
import com.jbandroid.jeommechu.ui.util.SelectionCard
import com.jbandroid.jeommechu.ui.util.StatusBarView
import com.jbandroid.jeommechu.viewmodel.MainViewModel

@Composable
fun SelectionFeatureScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    StatusBarView()
    val buttonStates = mainViewModel.buttonStates.value

    val temperatureOptions = listOf("뜨거움🔥", "차가움❄")
    val soupOptions = listOf("국물⭕", "국물❌")
    val spicyOptions = listOf("매움🌶", "살짝 매움", "안매움")

    Scaffold(
        topBar = {
            AppTopBar(
                title = "음식의 특성을 골라보세요",
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

                        FeatureGroup(
                            title = "온도",
                            options = temperatureOptions,
                            buttonStates,
                            mainViewModel::setButtonState
                        )
                        FeatureGroup(
                            title = "국물",
                            options = soupOptions,
                            buttonStates,
                            mainViewModel::setButtonState
                        )
                        FeatureGroup(
                            title = "맵기",
                            options = spicyOptions,
                            buttonStates,
                            mainViewModel::setButtonState
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        JeomButtonPrimary(
                            text = "다음 ➡",
                            onClick = {
                                navController.navigate(ScreenRoute.SelectionStyle.route)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}