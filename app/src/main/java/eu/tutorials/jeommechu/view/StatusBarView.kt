package eu.tutorials.jeommechu.view

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

// 상단바 색상 변경
@Composable
fun StatusBarView() {
    val systemUiController = rememberSystemUiController()

    systemUiController.setStatusBarColor(
        color = Color.Transparent, // 투명 색상 적용
        darkIcons = true // 상태바 아이콘을 검은색으로 (다크 모드면 false로 변경)
    )
}