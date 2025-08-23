package com.jbandroid.jeommechu.screen_view

import android.widget.CalendarView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jbandroid.jeommechu.data.FoodsData
import com.jbandroid.jeommechu.ui.design.AppBackdrop
import com.jbandroid.jeommechu.ui.design.AppTopBar
import com.jbandroid.jeommechu.ui.design.JeomButtonPrimary
import com.jbandroid.jeommechu.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun CalendarMemoScreen(
    navController: NavController,
    mainViewModel: MainViewModel = viewModel()
) {
    // 상태: 오늘을 기본 선택
    var selectedDate by rememberSaveable { mutableStateOf(LocalDate.now().toString()) }
    var currentMemo by rememberSaveable { mutableStateOf("") }

    // 추천 자동완성 목록
    val suggestions = remember { FoodsData.foodsList.map { it.title } }

    // Room 메모 스트림 관찰
    val memoList by mainViewModel.memoList.collectAsState()

    val focusManager = LocalFocusManager.current
    val calendarViewRef = remember { mutableStateOf<CalendarView?>(null) }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "메모",
                onBack = { navController.navigateUp() },
                onCalendar = null
            )
        },
        // 키보드가 올라오면 컨텐츠 인셋에 반영하여 자연스럽게 위로 올라오도록 함
        contentWindowInsets = WindowInsets.systemBars.union(WindowInsets.ime)
    ) { innerPadding ->
        AppBackdrop {
            // 전체 화면을 LazyColumn으로 구성 → bringIntoView가 스크롤을 제어할 수 있음
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                // 키보드(IME) 높이만큼 하단 패딩을 추가해 자동완성 영역이 가려지지 않게 함
                contentPadding = PaddingValues(
                    bottom = WindowInsets.ime.asPaddingValues().calculateBottomPadding()
                )
            ) {
                // CalendarView
                item {
                    AndroidView(
                        factory = { context ->
                            CalendarView(context).apply {
                                val today = LocalDate.now()
                                date = today.toEpochMillis()
                                calendarViewRef.value = this
                                setOnDateChangeListener { _, year, month, dayOfMonth ->
                                    val picked = "%04d-%02d-%02d".format(year, month + 1, dayOfMonth)
                                    selectedDate = picked
                                    currentMemo = memoList.find { it.date == picked }?.memo ?: ""
                                }
                            }
                        },
                        modifier = Modifier.wrapContentSize()
                    )
                }

                item { Spacer(Modifier.height(16.dp)) }

                item {
                    Text(
                        text = "선택한 날짜: $selectedDate",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                item { Spacer(Modifier.height(12.dp)) }

                // 입력 + 저장 (Row 구성)
                item {
                    AutoCompleteInputRow(
                        suggestions = suggestions,
                        value = currentMemo,
                        onValueChange = { currentMemo = it },
                        onSubmit = {
                            if (currentMemo.isNotBlank()) {
                                mainViewModel.insertMemo(selectedDate, currentMemo)
                                focusManager.clearFocus()
                            }
                        }
                    )
                }

                item { Spacer(Modifier.height(20.dp)) }

                item {
                    Text(
                        text = "메모 목록",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                item { Spacer(Modifier.height(8.dp)) }

                // 메모 리스트
                items(memoList) { memo ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedDate = memo.date
                                currentMemo = memo.memo
                                val target = LocalDate.parse(memo.date)
                                calendarViewRef.value?.date = target.toEpochMillis()
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${memo.date}: ${memo.memo}",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        IconButton(onClick = { mainViewModel.deleteMemo(memo) }) {
                            Icon(Icons.Default.Close, contentDescription = "메모 삭제")
                        }
                    }
                    Divider()
                }

                item { Spacer(Modifier.height(8.dp)) }
            }
        }
    }
}

@Composable
private fun AutoCompleteInputRow(
    suggestions: List<String>,
    value: String,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 필터된 추천 목록 (value 가 공백이 아닐 때만)
    val filtered = remember(value, suggestions) {
        if (value.isBlank()) emptyList() else suggestions.filter { it.contains(value, true) }
    }

    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val scope = rememberCoroutineScope()

    Column(modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .onFocusEvent { if (it.isFocused) scope.launch { bringIntoViewRequester.bringIntoView() } },
                singleLine = true,
                placeholder = { Text("메모 또는 음식 입력") }
            )
            Spacer(Modifier.width(8.dp))
            JeomButtonPrimary(
                text = "저장",
                onClick = onSubmit,
                enabled = value.isNotBlank()
            )
        }

        if (filtered.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 240.dp)
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                filtered.forEach { s ->
                    Text(
                        text = s,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onValueChange(s) }
                            .padding(10.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Divider(color = MaterialTheme.colorScheme.outlineVariant)
                }
            }
        }
    }
}

private fun LocalDate.toEpochMillis(): Long = this.toEpochDay() * 86_400_000
