package eu.tutorials.jeommechu.view

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import eu.tutorials.jeommechu.navigation.ScreenRoute

@Composable
fun AppBarView(
    navController: NavController,
    onBackNavClicked: () -> Unit
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val iconColor = MaterialTheme.colorScheme.onBackground

    TopAppBar(
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
