package com.jbandroid.jeommechu.screen_view.selectionScreen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowRow
import com.jbandroid.jeommechu.R
import com.jbandroid.jeommechu.navigation.ScreenRoute
import com.jbandroid.jeommechu.ui.design.AppBackdrop
import com.jbandroid.jeommechu.ui.design.AppPage
import com.jbandroid.jeommechu.ui.design.AppTopBar
import com.jbandroid.jeommechu.ui.design.JeomButtonPrimary
import com.jbandroid.jeommechu.ui.util.SelectionCard
import com.jbandroid.jeommechu.ui.util.StatusBarView
import com.jbandroid.jeommechu.viewmodel.MainViewModel

@Composable
fun SelectionTypeScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    StatusBarView()
    val options = listOf("밥🍚", "빵🍞", "면🍝", "떡", "기타")
    val selectedStates = mainViewModel.buttonStates.value

    Scaffold(
        topBar = {
            AppTopBar(
                title = "밥 / 빵 / 면 중 골라볼까요?",
                onBack = { navController.navigateUp() },
                onCalendar = { navController.navigate(ScreenRoute.CalendarMemoScreen.route) }
            )
        }
    ) { innerPadding ->
        AppBackdrop {
            AppPage(innerPadding = innerPadding) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    SelectionCard {

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            mainAxisSpacing = 12.dp,
                            crossAxisSpacing = 12.dp
                        ) {
                            options.forEach { option ->
                                val isSelected = selectedStates[option] ?: false
                                ToggleButtonModern(
                                    text = option,
                                    isChecked = isSelected,
                                    onCheckedChange = {
                                        val current = selectedStates[option] ?: false
                                        if (current) {
                                            mainViewModel.setButtonState(option, false)
                                        } else {
                                            options.forEach {
                                                mainViewModel.setButtonState(
                                                    it,
                                                    false
                                                )
                                            }
                                            mainViewModel.setButtonState(option, true)
                                        }
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        JeomButtonPrimary(
                            text = "다음 ➡",
                            onClick = {
                                navController.navigate(ScreenRoute.SelectionFeature.route)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun ToggleButtonModern(
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val containerColor by animateColorAsState(
        targetValue = if (isChecked) colorScheme.primaryContainer else colorScheme.surface,
        label = "containerColorAnim"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isChecked) colorScheme.onPrimaryContainer else colorScheme.onSurface,
        label = "contentColorAnim"
    )
    OutlinedButton(
        onClick = { onCheckedChange(!isChecked) },
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = BorderStroke(1.dp, if (isChecked) colorScheme.primary else Color.LightGray),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(4.dp)
    ) {
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
