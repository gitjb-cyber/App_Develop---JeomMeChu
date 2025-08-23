package com.jbandroid.jeommechu.ui.design

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.jbandroid.jeommechu.R
import com.jbandroid.jeommechu.ui.theme.AppColor

private val Jua = FontFamily(Font(R.font.jua_regular))

@Composable
fun JeomButtonPrimary(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppColor,
            contentColor = Color.Black
        ),
        modifier = modifier
    ) {
        Text(
        text,
        style = MaterialTheme.typography.titleMedium,
        fontFamily = Jua
        )
    }
}

// 선택된 버튼
@Composable
fun JeomButtonTonal(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    FilledTonalButton(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.filledTonalButtonColors(),
        modifier = modifier
    ) {
        Text(
        text,
        style = MaterialTheme.typography.titleMedium,
        fontFamily = Jua
        )
    }
}

// 선택되지 않은 버튼
@Composable
fun JeomButtonOutline(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        border = ButtonDefaults.outlinedButtonBorder,
        colors = ButtonDefaults.outlinedButtonColors(),
        modifier = modifier
    ) {
        Text(
            text,
            style = MaterialTheme.typography.titleMedium,
            fontFamily = Jua
        )
    }
}