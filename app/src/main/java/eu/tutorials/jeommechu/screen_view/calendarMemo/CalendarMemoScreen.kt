package eu.tutorials.jeommechu.screen_view.calendarMemo


import android.os.Build
import android.widget.CalendarView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import eu.tutorials.jeommechu.data.FoodsData
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarMemoScreen(navController: NavController) {
    val isDarkMode = isSystemInDarkTheme()
    val iconColor = if (isDarkMode) Color.White else Color.Black

    val memoMap = remember { mutableStateMapOf<String, String>() }
    var selectedDate by remember { mutableStateOf(LocalDate.now().toString()) } // 오늘 날짜로 초기화
    var currentMemo by remember { mutableStateOf("") }

    // 음식 제목과 마지막 태그를 포함한 추천 리스트
    val suggestions = FoodsData.foodsList.map { food -> food.title }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AndroidView(
                factory = { context ->
                    CalendarView(context).apply {
                        val today = LocalDate.now()
                        date = today.toEpochDay() * 24 * 60 * 60 * 1000 // 오늘 날짜로 이동

                        setOnDateChangeListener { _, year, month, dayOfMonth ->
                            selectedDate = "$year-${(month + 1).toString().padStart(2, '0')}-${dayOfMonth.toString().padStart(2, '0')}"
                            currentMemo = memoMap[selectedDate] ?: ""
                        }
                    }
                },
                modifier = Modifier.wrapContentSize()
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "선택한 날짜: $selectedDate") // 초기 값이 오늘 날짜로 설정됨

            Spacer(modifier = Modifier.height(8.dp))
            AutoCompleteTextField(
                suggestions = suggestions,
                currentMemo = currentMemo,
                onTextChange = { newText -> currentMemo = newText },
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { memoMap[selectedDate] = currentMemo },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("저장")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "메모 목록", style = MaterialTheme.typography.titleMedium)
            LazyColumn(
                modifier = Modifier.padding(8.dp)
            ) {
                items(memoMap.toList()) { (date, memoText) ->
                    Text(text = "$date: $memoText", modifier = Modifier.padding(4.dp))
                }
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
