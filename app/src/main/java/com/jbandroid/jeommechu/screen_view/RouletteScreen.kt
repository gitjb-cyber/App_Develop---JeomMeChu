package com.jbandroid.jeommechu.screen_view

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jbandroid.jeommechu.navigation.ScreenRoute
import com.jbandroid.jeommechu.ui.design.AppBackdrop
import com.jbandroid.jeommechu.ui.design.AppPage
import com.jbandroid.jeommechu.ui.design.AppTopBar
import com.jbandroid.jeommechu.ui.design.JeomButtonOutline
import com.jbandroid.jeommechu.ui.design.JeomButtonPrimary
import com.jbandroid.jeommechu.ui.design.JeomButtonTonal
import com.jbandroid.jeommechu.ui.util.SelectionCard
import com.jbandroid.jeommechu.ui.util.StatusBarView
import com.jbandroid.jeommechu.viewmodel.MainViewModel
import kotlinx.coroutines.delay


@Composable
fun RouletteScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    StatusBarView()

    // 상태
    var isSpinning by remember { mutableStateOf(false) }
    var selectedValue by remember { mutableStateOf("❓") }
    var isCustomMode by remember { mutableStateOf(false) }
    var newItem by remember { mutableStateOf("") }

    val rouletteList =
        if (isCustomMode) mainViewModel.customRouletteItems
        else mainViewModel.matchingConditions.value.toList()

    val transition = rememberInfiniteTransition(label = "")
    val randomIndex by transition.animateValue(
        initialValue = 0,
        targetValue = (rouletteList.size - 1).coerceAtLeast(0),
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(animation = tween(100, easing = LinearEasing)),
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
            AppTopBar(
                title = "룰렛",
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
                        Text(
                            text = "* 커스텀 룰렛은 직접 항목을 넣어 돌릴 수 있어요.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(Modifier.height(12.dp))


                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (isCustomMode) {
                                JeomButtonOutline(
                                    text = "설정된 메뉴",
                                    onClick = { isCustomMode = false; selectedValue = "❓" },
                                    modifier = Modifier.weight(1f)
                                )
                                JeomButtonTonal(
                                    text = "커스텀",
                                    onClick = { /* 현재 선택 */ },
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                JeomButtonTonal(
                                    text = "설정된",
                                    onClick = { /* 현재 선택 */ },
                                    modifier = Modifier.weight(1f)
                                )
                                JeomButtonOutline(
                                    text = "커스텀 룰렛",
                                    onClick = { isCustomMode = true; selectedValue = "❓" },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        // 커스텀 모드 입력 섹션
                        if (isCustomMode) {
                            Spacer(Modifier.height(12.dp))
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
                                Spacer(Modifier.width(8.dp))
                                JeomButtonPrimary(
                                    text = "추가",
                                    onClick = {
                                        if (newItem.isNotBlank()) {
                                            mainViewModel.addCustomItem(newItem)
                                            newItem = ""
                                            focusManager.clearFocus()
                                        }
                                    }
                                )
                            }

                            Spacer(Modifier.height(8.dp))

                            mainViewModel.customRouletteItems.forEach { item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(onClick = { mainViewModel.removeCustomItem(item) }) {
                                        Icon(Icons.Default.Close, contentDescription = "삭제")
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(24.dp))

                        // 결과 표시
                        Text(
                            text = if (isSpinning) rouletteList.getOrNull(randomIndex)
                                ?: "❓" else selectedValue,
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                                .align(Alignment.CenterHorizontally)
                        )

                        Spacer(Modifier.height(24.dp))

                        // 시작 버튼 (Primary)
                        JeomButtonPrimary(
                            text = if (isSpinning) "돌리는 중..." else "랜덤 선택!",
                            onClick = { isSpinning = true },
                            enabled = !isSpinning && rouletteList.isNotEmpty(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(8.dp))

                        // 보조 액션 (예: 결과로 지도 이동)
                        JeomButtonOutline(
                            text = "이 결과로 주변 맛집 찾기",
                            onClick = {
                                val key = if (selectedValue == "❓") rouletteList.firstOrNull()
                                    ?: return@JeomButtonOutline
                                else selectedValue
                                navController.navigate(ScreenRoute.UserMap.createRoute(key))
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}
