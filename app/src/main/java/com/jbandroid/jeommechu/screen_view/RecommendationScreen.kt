package com.jbandroid.jeommechu.screen_view

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jbandroid.jeommechu.ui.util.AppBarView
import com.jbandroid.jeommechu.ui.util.FoodCardColumn
import com.jbandroid.jeommechu.R
import com.jbandroid.jeommechu.navigation.ScreenRoute
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

    val context = LocalContext.current

    // 조건 재확인 로직: 모두 일치가 비어있을 경우 자동 전환
    LaunchedEffect(matchingConditions, mainViewModel.selectedMode.value) {
        if (mainViewModel.selectedMode.value == SelectMode.ALL_MATCH && matchingConditions.isEmpty()) {
            mainViewModel.setSelectedMode(SelectMode.ANY_MATCH)
            Toast.makeText(context, "중복될 수 없는 항목이 선택되어 하나만 포함되더라도 불러옵니다.", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        // AppBarView 의 topBar 내부
        topBar = {
            AppBarView(navController)
            // ← 아이콘 버튼을 누르면 뒤로 돌아감
            //  navigateUp : 사용자를 이전에 있던 화면으로 돌아가게 하는 것
            {
                navController.navigateUp()
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                                fontFamily = FontFamily(Font(R.font.jua_regular))
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Button(
                        onClick = { mainViewModel.setSelectedMode(SelectMode.ALL_MATCH) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (mainViewModel.selectedMode.value == SelectMode.ALL_MATCH)
                                colorScheme.primary else Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                    ) {
                        Text("모든 조건 일치", fontFamily = FontFamily(Font(R.font.jua_regular)))
                    }

                    Button(
                        onClick = { mainViewModel.setSelectedMode(SelectMode.ANY_MATCH) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (mainViewModel.selectedMode.value == SelectMode.ANY_MATCH)
                                colorScheme.primary else Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                    ) {
                        Text("하나라도 일치", fontFamily = FontFamily(Font(R.font.jua_regular)))
                    }
                }
            }

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
                                fontFamily = FontFamily(Font(R.font.jua_regular)),
                                color = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = matchingConditions.shuffled().take(5).joinToString(", "),
                            style = MaterialTheme.typography.bodyLarge,
                            fontFamily = FontFamily(Font(R.font.jua_regular)),
                            color = Color.DarkGray
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        navController.navigate(ScreenRoute.RouletteScreen.route)
                    },
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
                    Text(
                        "랜덤 선택",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.jua_regular))
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
