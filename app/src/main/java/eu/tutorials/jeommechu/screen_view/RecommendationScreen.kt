package eu.tutorials.jeommechu.screen_view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import eu.tutorials.jeommechu.R
import eu.tutorials.jeommechu.view.AppBarView
import eu.tutorials.jeommechu.view.FoodCard
import eu.tutorials.jeommechu.viewmodel.MainViewModel

@SuppressLint("ResourceAsColor")
@Composable
fun RecommendationScreen(
    navController: NavController,
    mainViewModel: MainViewModel = viewModel()
) {
    val matchingConditions by mainViewModel.matchingConditions
    val toggleConditions by mainViewModel.toggleConditions


    Scaffold(
        // AppBarView 의 topBar 내부
        topBar = {
            AppBarView(
                title = "추전 음식"
            )
            // ← 아이콘 버튼을 누르면 뒤로 돌아감
            //  navigateUp : 사용자를 이전에 있던 화면으로 돌아가게 하는 것
            { navController.navigateUp() }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(colorResource(id = R.color.app_background_color))
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Spacer(modifier = Modifier.height(32.dp))
                    // "모두 일치" 버튼
                    Button(
                        onClick = { mainViewModel.setSelectedMode("모두 일치") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (mainViewModel.selectedMode.value == "모두 일치")
                                MaterialTheme.colorScheme.primary else Color(R.color.light_gray)
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text("모든 조건 일치",
                            fontFamily = FontFamily(Font(R.font.jua_regular)))
                    }

                    Button(
                        onClick = { mainViewModel.setSelectedMode("하나라도 일치") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (mainViewModel.selectedMode.value == "하나라도 일치")
                                MaterialTheme.colorScheme.primary else Color(R.color.light_gray)
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text("하나라도 일치",
                            fontFamily = FontFamily(Font(R.font.jua_regular)))
                    }
                }
            }
            item {
                Card(
                    modifier = Modifier.padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White) // 배경색 흰색 적용
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp) // 내부 요소 패딩 추가
                    ) {
                        Text(
                            text = "조건 일치 미리보기",
                            style = MaterialTheme.typography.headlineSmall,
                            fontFamily = FontFamily(Font(R.font.jua_regular)),
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (matchingConditions.isEmpty())
                                "해당 조건에 모두 일치하는 메뉴가 없습니다"
                            else
                                matchingConditions.joinToString(", "),
                            fontFamily = FontFamily(Font(R.font.jua_regular)),
                            color = Color.Black
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                FoodCard(
                    matchingConditions = matchingConditions,
                    toggleConditions = toggleConditions
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFA5A5))
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp) // 내부 요소 패딩 추가
                    ) {
                        Text(
                            text = "오늘의 추천 메뉴",
                            style = MaterialTheme.typography.headlineSmall,
                            fontFamily = FontFamily(Font(R.font.jua_regular)),
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = matchingConditions.shuffled().take(5).joinToString(", "),
                            fontFamily = FontFamily(Font(R.font.jua_regular)),
                            color = Color.Black
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
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 64.dp)
                ) {
                    Text("랜덤 선택",
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.jua_regular))
                    )
                }
            }
        }
    }
}