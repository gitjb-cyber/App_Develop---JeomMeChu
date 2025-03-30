package eu.tutorials.jeommechu.screen_view

import android.widget.CalendarView
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarMemoScreen(
    navController: NavController,
    //mainViewModel: MainViewModel = viewModel()
) {
    val isDarkMode = isSystemInDarkTheme()
    val backgroundColor = if (isDarkMode) Color.Black else Color.White
    val iconColor = if (isDarkMode) Color.White else Color.Black

    // 날짜를 "yyyy-MM-dd" 형식으로 저장하기 위한 맵 (날짜 -> 메모)
    val memoMap = remember { mutableStateMapOf<String, String>() }
    var selectedDate by remember { mutableStateOf("") }
    var currentMemo by remember { mutableStateOf("") }

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
            // Android의 CalendarView를 Compose에 포함
            AndroidView(
                factory = { context ->
                    CalendarView(context).apply {
                        // 날짜가 선택되면 콜백을 통해 선택한 날짜를 업데이트
                        setOnDateChangeListener { _, year, month, dayOfMonth ->
                            // CalendarView의 month는 0부터 시작하므로 1을 더해줍니다.
                            selectedDate = "$year-${
                                (month + 1).toString().padStart(2, '0')
                            }-${dayOfMonth.toString().padStart(2, '0')}"
                            // 해당 날짜의 메모를 불러옵니다.
                            currentMemo = memoMap[selectedDate] ?: ""
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "선택한 날짜: ${if (selectedDate.isEmpty()) "없음" else selectedDate}")

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = currentMemo,
                onValueChange = { currentMemo = it },
                label = { Text("음식 메모") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    if (selectedDate.isNotEmpty()) {
                        memoMap[selectedDate] = currentMemo
                    }
                },
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