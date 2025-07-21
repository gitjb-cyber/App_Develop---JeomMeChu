package com.jbandroid.jeommechu.screen_view


import android.widget.CalendarView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jbandroid.jeommechu.data.FoodsData
import com.jbandroid.jeommechu.viewmodel.MainViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarMemoScreen(
    navController: NavController,
    mainViewModel: MainViewModel = viewModel()
) {
    val isDarkMode = isSystemInDarkTheme()
    val iconColor = if (isDarkMode) Color.White else Color.Black

    // 현재 선택한 날짜 및 메모 상태
    var selectedDate by remember { mutableStateOf(LocalDate.now().toString()) }
    var currentMemo by remember { mutableStateOf("") }
    // 음식 제목 추천 리스트
    val suggestions = FoodsData.foodsList.map { food -> food.title }
    // Room에서 가져온 메모 목록을 Compose State로 관찰
    val memoList by mainViewModel.memoList.collectAsState()

    val focusManager = LocalFocusManager.current
    rememberScrollState()
    val calendarViewRef = remember { mutableStateOf<CalendarView?>(null) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    androidx.compose.material.IconButton(onClick = { navController.navigateUp() }) {
                        androidx.compose.material.Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            tint = iconColor,
                            contentDescription = "뒤로가기"
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ) {
            // CalendarView
            item {
                AndroidView(
                    factory = { context ->
                        CalendarView(context).apply {
                            val today = LocalDate.now()
                            date = today.toEpochDay() * 24 * 60 * 60 * 1000
                            calendarViewRef.value = this

                            setOnDateChangeListener { _, year, month, dayOfMonth ->
                                selectedDate = "$year-${
                                    (month + 1).toString().padStart(2, '0')
                                }-${dayOfMonth.toString().padStart(2, '0')}"
                                currentMemo = memoList.find { it.date == selectedDate }?.memo ?: ""
                            }
                        }
                    },
                    modifier = Modifier.wrapContentSize()
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                Text(
                    text = "선택한 날짜: $selectedDate",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                AutoCompleteTextField(
                    suggestions = suggestions,
                    currentMemo = currentMemo,
                    onTextChange = { newText -> currentMemo = newText },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                Button(
                    onClick = {
                        mainViewModel.insertMemo(selectedDate, currentMemo)
                        focusManager.clearFocus()
                    },
                    modifier = Modifier
                        .padding(end = 16.dp)
                ) {
                    Text("저장")
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                Text(
                    text = "메모 목록",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // 메모 리스트 항목들
            items(memoList) { memo ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedDate = memo.date
                            currentMemo = memo.memo
                            val targetDate = LocalDate.parse(memo.date)
                            val millis = targetDate.toEpochDay() * 24 * 60 * 60 * 1000
                            calendarViewRef.value?.date = millis
                        }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${memo.date}: ${memo.memo}",
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                        mainViewModel.deleteMemo(memo)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "메모 삭제"
                        )
                    }
                }
                Divider()
            }
        }
    }
}

@Composable
fun AutoCompleteTextField(
    suggestions: List<String>,
    currentMemo: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf(currentMemo) }

    // 부모 상태(currentMemo)가 변경되면 query 업데이트
    LaunchedEffect(currentMemo) {
        query = currentMemo
    }

    // 입력한 query에 따라 필터링된 추천 리스트
    val filteredSuggestions = suggestions.filter {
        it.contains(query, ignoreCase = true) && query.isNotBlank()
    }

    Column(modifier) {
        // 텍스트 필드
        TextField(
            value = query,
            onValueChange = {
                query = it
                onTextChange(it)
            },
            label = { Text("메모") },
            modifier = Modifier.fillMaxWidth()
        )

        // 텍스트 필드 아래에 추천 리스트 표시
        if (filteredSuggestions.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                filteredSuggestions.forEach { suggestion ->
                    Text(
                        text = suggestion,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                query = suggestion
                                onTextChange(suggestion)
                            }
                            .padding(8.dp)
                    )
                    Divider()
                }
            }
        }
    }
}