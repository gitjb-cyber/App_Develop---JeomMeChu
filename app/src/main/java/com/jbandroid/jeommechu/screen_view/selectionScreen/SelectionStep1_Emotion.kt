package com.jbandroid.jeommechu.screen_view.selectionScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jbandroid.jeommechu.R
import com.jbandroid.jeommechu.navigation.ScreenRoute
import com.jbandroid.jeommechu.ui.util.AppBarView
import com.jbandroid.jeommechu.ui.util.SelectionCard
import com.jbandroid.jeommechu.ui.util.StatusBarView
import com.jbandroid.jeommechu.viewmodel.MainViewModel

@Composable
fun SelectionEmotionScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    val emotions = MainViewModel.Emotion.entries
    StatusBarView()
    Scaffold(
        topBar = {
            AppBarView(navController = navController) { navController.navigateUp() }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            SelectionCard(modifier = Modifier.padding(innerPadding)) {
                Text(
                    text = "오늘 어떤 느낌이에요?",
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = FontFamily(Font(R.font.jua_regular))
                )
                Spacer(modifier = Modifier.height(16.dp))

                emotions.forEach { emotion ->
                    Button(
                        onClick = {
                            val selectedFood = mainViewModel.selectFoodByEmotion(emotion)
                            mainViewModel.updateSelectedCondition(selectedFood)
                            navController.navigate(ScreenRoute.SelectionResult.route)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Text(emotion.label, fontFamily = FontFamily(Font(R.font.jua_regular)))
                    }
                }
            }
        }
    }
}