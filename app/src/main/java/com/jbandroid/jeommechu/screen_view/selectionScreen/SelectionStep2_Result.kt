package com.jbandroid.jeommechu.screen_view.selectionScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
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
import kotlinx.coroutines.delay
import java.time.LocalDate

@Composable
fun SelectionResultScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    StatusBarView()
    val selectedFood = mainViewModel.selectedCondition
    val currentEmotion = mainViewModel.currentEmotion
    val context = LocalContext.current

    var showSaved by remember { mutableStateOf(false) }

    val emotionList: List<String> = currentEmotion?.let { e ->
        mainViewModel.getFoodsByEmotion(e)
    } ?: emptyList()

    // 현재 표시 중인 메뉴를 제외한 "다른 메뉴" 후보
    val otherOptions = remember(selectedFood, currentEmotion, emotionList) {
        emotionList.filter { it != selectedFood }
    }

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
                            text = "오늘의 추천 음식은",
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Text(
                            text = if (selectedFood == "❓") "음식을 찾을 수 없어요..." else selectedFood,
                            style = MaterialTheme.typography.displayMedium,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        JeomButtonPrimary(
                            text = "다른 메뉴 추천",
                            onClick = {
                                val next = otherOptions.randomOrNull() ?: selectedFood
                                mainViewModel.updateSelectedCondition(next)   // VM 값만 바꿔도 화면이 갱신됨
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        JeomButtonPrimary(
                            text = "주변 음식점 찾기",
                            onClick = {
                                navController.navigate(ScreenRoute.UserMap.createRoute(selectedFood))
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        JeomButtonPrimary(
                            text = "메모에 추가하기",
                            onClick = {
                                val today = LocalDate.now().toString()
                                mainViewModel.insertMemo(today, selectedFood)
                                showSaved = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    if (showSaved) {
                        LaunchedEffect(Unit) {
                            Toast.makeText(context, "메모에 저장되었습니다!", Toast.LENGTH_SHORT).show()
                            delay(1500)
                            showSaved = false
                        }
                    }
                }
            }
        }
    }
}