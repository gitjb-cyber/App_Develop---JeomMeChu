package com.jbandroid.jeommechu.ui.design

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jbandroid.jeommechu.R
import com.jbandroid.jeommechu.ui.theme.AppColor
import com.jbandroid.jeommechu.ui.theme.OnBackground
import com.jbandroid.jeommechu.ui.theme.OnSecondaryContainer
import com.jbandroid.jeommechu.ui.theme.OnSurfaceVariant
import com.jbandroid.jeommechu.ui.theme.OutlineVariant
import com.jbandroid.jeommechu.ui.theme.Pink50
import com.jbandroid.jeommechu.ui.theme.SecondaryContainer
import com.jbandroid.jeommechu.ui.theme.SurfaceVariant


private val Jua = FontFamily(Font(R.font.jua_regular))


private val AppColorScheme = lightColorScheme(
    primary = AppColor,
    onPrimary = Color.White,
    background = Pink50,
    onBackground = OnBackground,
    surface = Color.White,
    onSurface = OnBackground,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    outlineVariant = OutlineVariant
)


private val AppShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)


private val AppTypography = Typography(
    headlineLarge  = TextStyle(fontFamily = Jua, fontWeight = FontWeight.SemiBold, fontSize = 28.sp, lineHeight = 34.sp),
    headlineMedium = TextStyle(fontFamily = Jua, fontWeight = FontWeight.SemiBold, fontSize = 24.sp, lineHeight = 30.sp),
    headlineSmall  = TextStyle(fontFamily = Jua, fontWeight = FontWeight.Medium,   fontSize = 20.sp, lineHeight = 26.sp),

    titleLarge  = TextStyle(fontFamily = Jua, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 24.sp),
    titleMedium = TextStyle(fontFamily = Jua, fontWeight = FontWeight.Medium,   fontSize = 16.sp, lineHeight = 20.sp),
    titleSmall  = TextStyle(fontFamily = Jua, fontWeight = FontWeight.Medium,   fontSize = 14.sp, lineHeight = 18.sp),

    bodyLarge  = TextStyle(fontFamily = Jua, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 22.sp),
    bodyMedium = TextStyle(fontFamily = Jua, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp),
    bodySmall  = TextStyle(fontFamily = Jua, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 16.sp),
)


@Composable
fun JeomTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        shapes = AppShapes,
        typography = AppTypography,
        content = content
    )
}