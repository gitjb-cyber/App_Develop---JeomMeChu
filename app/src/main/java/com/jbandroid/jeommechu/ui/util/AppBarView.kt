package com.jbandroid.jeommechu.ui.util

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.jbandroid.jeommechu.navigation.ScreenRoute

@Composable
fun AppBarView(
    navController: NavController,
    onBackNavClicked: () -> Unit
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val iconColor = MaterialTheme.colorScheme.onBackground

    TopAppBar(
        modifier = Modifier.statusBarsPadding(),
        title = {},
        backgroundColor = backgroundColor,
        navigationIcon = {
            IconButton(onClick = { onBackNavClicked() }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    tint = iconColor,
                    contentDescription = "뒤로가기"
                )
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate(ScreenRoute.CalendarMemoScreen.route)} ) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "캘린더 화면 이동"
                )
            }
        }
    )
}
