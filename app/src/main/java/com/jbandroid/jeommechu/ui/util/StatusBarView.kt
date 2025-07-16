package com.jbandroid.jeommechu.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.google.accompanist.systemuicontroller.rememberSystemUiController

// 상단바 색상 변경
@Composable
fun StatusBarView(backgroundColor: Color = Color.White) {
    val systemUiController = rememberSystemUiController()

    // true이면 검은 아이콘 (밝은 배경에 어울림), false이면 흰 아이콘 (어두운 배경에 어울림)
    val useDarkIcons = backgroundColor.luminance() > 0.5f

    // 상태바 색상 및 아이콘 색 설정
    systemUiController.setStatusBarColor(
        color = backgroundColor,
        darkIcons = useDarkIcons
    )
}