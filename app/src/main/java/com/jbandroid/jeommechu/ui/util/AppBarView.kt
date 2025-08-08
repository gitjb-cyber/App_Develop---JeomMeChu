package com.jbandroid.jeommechu.ui.util

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.jbandroid.jeommechu.navigation.ScreenRoute
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.jbandroid.jeommechu.R

@Composable
fun AppBarView(
    navController: NavController,
    title: String = "",
    onBackNavClicked: () -> Unit
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val iconColor = MaterialTheme.colorScheme.onBackground

    TopAppBar(
        modifier = Modifier.statusBarsPadding(),
        title = { Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontFamily = FontFamily(Font(R.font.jua_regular)),
            color = iconColor
        )},
        backgroundColor = backgroundColor,
        navigationIcon = {
            IconButton(onClick = { onBackNavClicked() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    tint = iconColor,
                    contentDescription = "뒤로가기"
                )
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate(ScreenRoute.CalendarMemoScreen.route)} ) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    tint = iconColor,
                    contentDescription = "캘린더 화면 이동"
                )
            }
        }
    )
}
