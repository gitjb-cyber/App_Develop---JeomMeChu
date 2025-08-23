package com.jbandroid.jeommechu.screen_view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jbandroid.jeommechu.R
import com.jbandroid.jeommechu.navigation.ScreenRoute
import com.jbandroid.jeommechu.ui.design.AppBackdrop
import com.jbandroid.jeommechu.ui.design.AppTopBar
import com.jbandroid.jeommechu.ui.design.JeomButtonPrimary
import com.jbandroid.jeommechu.ui.util.FoodCardColumn
import com.jbandroid.jeommechu.ui.util.StatusBarView
import com.jbandroid.jeommechu.viewmodel.MainViewModel
import com.jbandroid.jeommechu.viewmodel.SelectMode


@SuppressLint("ResourceAsColor")
@Composable
fun RecommendationScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    StatusBarView()
    val matchingConditions by mainViewModel.matchingConditions
    val toggleConditions by mainViewModel.toggleConditions
    val sliderValue by mainViewModel.sliderDaysAgo

    LaunchedEffect(Unit) {
        mainViewModel.setSelectedMode(SelectMode.ALL_MATCH)
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "추천",
                onBack = { navController.navigateUp() },
                onCalendar = { navController.navigate(ScreenRoute.CalendarMemoScreen.route) }
            )
        }
    ) { innerPadding ->
        AppBackdrop {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {

                // 슬라이더 영역
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = null,
                                    tint = colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "최근 $sliderValue 일간 먹은 음식 제외",
                                    style = MaterialTheme.typography.titleMedium,
                                )
                            }

                            Slider(
                                value = sliderValue.toFloat(),
                                onValueChange = { mainViewModel.setSliderDaysAgo(it.toInt()) },
                                valueRange = 0f..7f,
                                steps = 6,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp)
                            )
                        }
                    }
                }

                item {
                    if (matchingConditions.isEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3F3)),
                            elevation = CardDefaults.cardElevation(6.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "해당 조건을 만족하는 음식을 찾을 수 없습니다.",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = { navController.navigate(ScreenRoute.SelectionType.route) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 32.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = colorScheme.primary,
                                        contentColor = colorScheme.onPrimary
                                    )
                                ) {
                                    Text("조건 다시 선택하기")
                                }
                            }
                        }
                    }
                }

                if (matchingConditions.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        FoodCardColumn(
                            matchingConditions = matchingConditions,
                            toggleConditions = toggleConditions,
                            navController = navController,
                            mainViewModel = mainViewModel
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE2E2)),
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "추천",
                                        tint = Color(0xFFE91E63),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "오늘의 추천 메뉴",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = Color.Black
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = matchingConditions.shuffled().take(5).joinToString(", "),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }

                    item {
                        Column(modifier = Modifier.padding(16.dp)) {

                            Spacer(modifier = Modifier.height(12.dp))

                            JeomButtonPrimary(
                                text = "조건 다시 선택하기",
                                onClick = { navController.navigate(ScreenRoute.SelectionType.route) },
                                modifier = Modifier.fillMaxWidth()
                            )

                            JeomButtonPrimary(
                                text = "랜덤 선택",
                                onClick = { navController.navigate(ScreenRoute.RouletteScreen.route) },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }
            }
        }
    }
}