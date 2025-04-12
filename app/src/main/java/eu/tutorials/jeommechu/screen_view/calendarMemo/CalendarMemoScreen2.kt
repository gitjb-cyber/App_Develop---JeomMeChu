package eu.tutorials.jeommechu.screen_view.calendarMemo

/*
import android.os.Build
import android.widget.CalendarView
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import eu.tutorials.jeommechu.R
import eu.tutorials.jeommechu.viewmodel.MainViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarMemoScreen2(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    val isDarkMode = isSystemInDarkTheme()
    val iconColor = if (isDarkMode) Color.White else Color.Black

    // ViewModel의 상태 구독
    val memoMap = mainViewModel.memoMap


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
                }
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
                            // 날짜 포맷 변경 후 ViewModel 업데이트
                            val formattedDate =
                                "$year-${(month + 1).toString().padStart(2, '0')}-${dayOfMonth.toString().padStart(2, '0')}"
                            mainViewModel.updateSelectedDate(formattedDate)
                        }
                    }
                },
                modifier = Modifier.wrapContentSize()
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "선택한 날짜: ${mainViewModel.selectedDate}")

            Spacer(modifier = Modifier.height(8.dp))
            // AutoCompleteTextField의 상태 변경 시 ViewModel 업데이트
            MemoTextField2(
                label = "무엇을 드셨나요?",
                value = mainViewModel.currentMemo,
                onValueChanged = {mainViewModel.onMemoChange(it)}
            )

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    mainViewModel.saveCurrentMemo()
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
                // Room에서 가져온 memoMap을 리스트 형태로 표시
                items(memoMap.toList()) { (date, memoText) ->
                    Text(text = "$date: $memoText", modifier = Modifier.padding(4.dp))
                }
            }
        }
    }
}



@Composable
fun MemoTextField2(
    label: String,
    value: String,
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        label = { Text(text = label)},
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.Black,
            focusedBorderColor = colorResource(id = R.color.black),
            unfocusedBorderColor = colorResource(id = R.color.black),
            cursorColor = colorResource(id = R.color.black),
            focusedLabelColor = colorResource(id = R.color.black),
            unfocusedLabelColor = colorResource(id = R.color.black)
        )
        )
}
*/
