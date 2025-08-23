package com.jbandroid.jeommechu.screen_view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jbandroid.jeommechu.R
import com.jbandroid.jeommechu.navigation.ScreenRoute
import com.jbandroid.jeommechu.ui.util.StatusBarView
import kotlinx.coroutines.delay

// 처음 화면
@Composable
fun StartScreen(
    navController: NavController
) {
    StatusBarView()
    // 이미지가 보이는지 여부를 제어하는 상태
    var visible1 by remember { mutableStateOf(false) }
    var visible2 by remember { mutableStateOf(false) }

    // 화면이 구성되면 딜레이 후에 visible을 true로 변경하여 애니메이션 실행
    LaunchedEffect(Unit) {
        delay(150)
        visible1 = true
        delay(300)
        visible2 = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.app_color))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            AnimatedVisibility(
                visible = visible1,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 2000, // 1초 동안 투명에서 선명해짐
                        easing = FastOutSlowInEasing
                    )
                )
            ) {
                // 메인 이미지
                Image(
                    painter = painterResource(id = R.drawable.main_img_contents),
                    contentDescription = "메인 이미지",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp)
                )
            }

            // 하단 버튼
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 64.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally // 버튼 중앙 정렬
            ) {
                AnimatedVisibility(
                    visible = visible2,
                    enter = fadeIn(
                        animationSpec = tween(
                            durationMillis = 1000, // 1초 동안 투명에서 선명해짐
                            easing = FastOutSlowInEasing
                        )
                    )
                ) {
                    ElevatedButton(
                        onClick = { navController.navigate(ScreenRoute.SelectionIntro.route) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = buttonColors(containerColor = Color.White)
                    )
                    {
                        Text(
                        text = "시작하기",
                        color = Color.Black,
                        fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

