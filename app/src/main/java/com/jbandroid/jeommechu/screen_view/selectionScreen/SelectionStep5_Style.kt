package com.jbandroid.jeommechu.screen_view.selectionScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.jbandroid.jeommechu.viewmodel.SelectMode

@Composable
fun SelectionStyleScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    StatusBarView()
    val buttonStates = mainViewModel.buttonStates.value

    val countryOptions = listOf("한식", "일식", "중식", "양식", "아시안")
    val meatOptions = listOf("돼지고기", "소고기", "양고기", "닭고기", "계란", "생선", "해산물")

    Scaffold(
        topBar = {
            AppTopBar(
                title = "어떤 스타일이 좋아요?",
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
                            title = "국가",
                            options = countryOptions,
                            buttonStates,
                            mainViewModel::setButtonState
                        )
                        FeatureGroup(
                            title = "고기",
                            options = meatOptions,
                            buttonStates,
                            mainViewModel::setButtonState
                        )

                        Spacer(modifier = Modifier.height(8.dp))


                        JeomButtonPrimary(
                            text = "추천받기 🎉",
                            onClick = {
                                mainViewModel.setSelectedMode(SelectMode.ALL_MATCH)
                                mainViewModel.updateMatchingConditions()
                                navController.navigate(ScreenRoute.RecommendationScreen.route)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}