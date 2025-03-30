package eu.tutorials.jeommechu.view

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import eu.tutorials.jeommechu.screen_view.ScreenRoute

@Composable
fun AppBarView(
    navController: NavController,
    onBackNavClicked: () -> Unit
) {
    val isDarkMode = isSystemInDarkTheme()
    val backgroundColor = if (isDarkMode) Color.Black else Color.White
    val iconColor = if (isDarkMode) Color.White else Color.Black

    TopAppBar(
        title = {},
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
                    contentDescription = "캘린더 화면 이동"
                )
            }
        }
    )
}
