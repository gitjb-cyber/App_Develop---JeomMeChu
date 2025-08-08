package com.jbandroid.jeommechu.screen_view.selectionScreen

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.coroutines.delay
import java.time.LocalDate

@Composable
fun SelectionResultScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    StatusBarView()
    val context = LocalContext.current
    var showSaved by remember { mutableStateOf(false) }

    val selectedFood by remember { derivedStateOf { mainViewModel.selectedCondition } }

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
                    text = "오늘의 추천 음식은",
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = FontFamily(Font(R.font.jua_regular))
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = if (selectedFood == "❓") "음식을 찾을 수 없어요..." else selectedFood,
                    style = MaterialTheme.typography.displayMedium,
                    fontFamily = FontFamily(Font(R.font.jua_regular)),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        navController.navigate(ScreenRoute.UserMap.createRoute(selectedFood))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("주변 음식점 찾기")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val today = LocalDate.now().toString()
                        mainViewModel.insertMemo(today, selectedFood)
                        showSaved = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("메모에 추가하기")
                }
            }

            if (showSaved) {
                LaunchedEffect(Unit) {
                    Toast.makeText(context, "메모에 저장되었습니다!", Toast.LENGTH_SHORT).show()
                    delay(1500)
                    showSaved = false
                }
            }
        }
    }
}