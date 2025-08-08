package com.jbandroid.jeommechu.screen_view

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jbandroid.jeommechu.R
import com.jbandroid.jeommechu.ui.util.AppBarView
import com.jbandroid.jeommechu.ui.util.StatusBarView
import com.jbandroid.jeommechu.viewmodel.MainViewModel
import kotlinx.coroutines.delay


@Composable
fun RouletteScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    StatusBarView()
    val isDarkMode = isSystemInDarkTheme()
    val backgroundColor = if (isDarkMode) Color.Black else Color.White
    val textColor = if (isDarkMode) Color.White else Color.Black

    var isSpinning by remember { mutableStateOf(false) }
    var selectedValue by remember { mutableStateOf("❓") }
    var isCustomMode by remember { mutableStateOf(false) }
    var newItem by remember { mutableStateOf("") }

    val rouletteList =
        if (isCustomMode)
        mainViewModel.customRouletteItems
        else mainViewModel.matchingConditions.value.toList()

    val transition = rememberInfiniteTransition(label = "")
    val randomIndex by transition.animateValue(
        initialValue = 0,
        targetValue = (rouletteList.size - 1).coerceAtLeast(0),
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    val focusManager = LocalFocusManager.current

    LaunchedEffect(isSpinning) {
        if (isSpinning) {
            delay(2000)
            isSpinning = false
            selectedValue = rouletteList.randomOrNull() ?: "❓"
            mainViewModel.updateSelectedCondition(selectedValue)
        }
    }

    Scaffold(
        topBar = {
            AppBarView(navController = navController) { navController.navigateUp() }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(backgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "* 커스텀 룰렛은 직접 설정하여 룰렛을 실행할 수 있습니다.\n"
                        + "원하는 음식이나 계산 할 사람의 이름을 넣어 실행해 보세요!",
                color = Color.Red,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 모드 선택 버튼
            Row(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = {
                        isCustomMode = false
                        selectedValue = "❓"
                              },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isCustomMode) MaterialTheme.colorScheme.primary else Color.LightGray
                    )
                ) {
                    Text(
                        text = "설정 메뉴 룰렛 돌리기",
                        fontFamily = FontFamily(Font(R.font.jua_regular))
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        isCustomMode = true
                        selectedValue = "❓"
                              },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCustomMode) MaterialTheme.colorScheme.primary else Color.LightGray
                    )
                ) {
                    Text(
                        text = "커스텀 룰렛 돌리기",
                        fontFamily = FontFamily(Font(R.font.jua_regular))
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isCustomMode) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextField(
                            value = newItem,
                            onValueChange = { newItem = it },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            placeholder = { Text("항목 입력") },
                            keyboardActions = KeyboardActions(onDone = {
                                if (newItem.isNotBlank()) {
                                    mainViewModel.addCustomItem(newItem)
                                    newItem = ""
                                }
                                focusManager.clearFocus()
                            })
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            if (newItem.isNotBlank()) {
                                mainViewModel.addCustomItem(newItem)
                                newItem = ""
                                focusManager.clearFocus()
                            }
                        }) {
                            Icon(Icons.Default.Add, contentDescription = "추가")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    mainViewModel.customRouletteItems.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item,
                                modifier = Modifier.weight(1f),
                                fontFamily = FontFamily(Font(R.font.jua_regular)),
                                fontSize = 16.sp
                            )
                            IconButton(onClick = { mainViewModel.removeCustomItem(item) }) {
                                Icon(Icons.Default.Close, contentDescription = "삭제")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = if (isSpinning) rouletteList.getOrNull(randomIndex) ?: "❓" else selectedValue,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.jua_regular)),
                color = textColor,
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            val spinButtonColors = ButtonDefaults.buttonColors(
                containerColor = if (isSpinning)
                    MaterialTheme.colorScheme.secondaryContainer
                else
                    MaterialTheme.colorScheme.primary,
                contentColor = if (isSpinning)
                    MaterialTheme.colorScheme.onSecondaryContainer
                else
                    MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(
                onClick = { isSpinning = true },
                enabled = !isSpinning && rouletteList.isNotEmpty(),
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth(),
                colors = spinButtonColors
            ) {
                Text(
                    text = "랜덤 선택!",
                    fontFamily = FontFamily(Font(R.font.jua_regular)),
                    fontSize = 20.sp
                )
            }
        }
    }
}
