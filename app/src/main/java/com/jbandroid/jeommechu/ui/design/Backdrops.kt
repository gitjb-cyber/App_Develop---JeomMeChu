package com.jbandroid.jeommechu.ui.design

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.material3.MaterialTheme


@Composable
fun AppBackdrop(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val bg = MaterialTheme.colorScheme.background
    val overlay = Brush.linearGradient(
        0f to bg,
        1f to bg.copy(alpha = 0.98f) // 배경을 살짝만 깊게
    )
    Box(modifier.background(overlay)) { content() }
}