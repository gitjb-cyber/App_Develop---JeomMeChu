package eu.tutorials.jeommechu.screen_view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import eu.tutorials.jeommechu.R
import eu.tutorials.jeommechu.view.AppBarView
import eu.tutorials.jeommechu.view.StatusBarView
import eu.tutorials.jeommechu.viewmodel.MainViewModel
import kotlinx.coroutines.delay


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RouletteScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    StatusBarView()
    var isSpinning by remember { mutableStateOf(false) }
    var selectedValue by remember { mutableStateOf("❓") }
    val matchingConditions = mainViewModel.matchingConditions.value.toList()

    val transition = rememberInfiniteTransition()
    val randomIndex by transition.animateValue(
        initialValue = 0,
        targetValue = (matchingConditions.size - 1).coerceAtLeast(0),
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    LaunchedEffect(isSpinning) {
        if (isSpinning) {
            delay(2000) // 2초 동안 애니메이션 실행
            isSpinning = false
            selectedValue = matchingConditions.randomOrNull() ?: "❓"
            mainViewModel.updateSelectedCondition(selectedValue) // ViewModel에 저장
        }
    }
    Scaffold(
        // AppBarView 의 topBar 내부
        topBar = {
            AppBarView(navController = navController)
            { navController.navigateUp() }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (isSpinning) matchingConditions.getOrNull(randomIndex)
                    ?: "❓" else selectedValue,
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp),
                fontFamily = FontFamily(Font(R.font.jua_regular)),
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { isSpinning = true },
                enabled = !isSpinning
            ) {
                Text(text = "랜덤 선택!",
                    fontFamily = FontFamily(Font(R.font.jua_regular)))
            }
        }
    }
}
