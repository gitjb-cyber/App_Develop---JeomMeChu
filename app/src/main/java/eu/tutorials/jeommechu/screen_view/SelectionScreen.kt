package eu.tutorials.jeommechu.screen_view


import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowRow
import eu.tutorials.jeommechu.R
import eu.tutorials.jeommechu.view.AppBarView
import eu.tutorials.jeommechu.view.StatusBarView
import eu.tutorials.jeommechu.viewmodel.MainViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ResourceAsColor")
@Composable
fun SelectionScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    StatusBarView()
    val isDarkMode = isSystemInDarkTheme()
    val backgroundColor = if (isDarkMode) Color.Black else Color.White
    val textColor = if (isDarkMode) Color.White else Color.Black

    val buttonMappings = mapOf(
        "밥빵면" to listOf("밥🍚", "빵🍔", "면🍝", "떡", "탄수화물 X", "기타"),
        "국물" to listOf("국물⭕", "국물❌"),
        "온도" to listOf("뜨거움🔥", "차가움❄"),
        "맵기" to listOf("매움🥵", "안매움"),
        "국가" to listOf("한식", "양식", "중식", "일식", "아시안"),
        "고기" to listOf("돼지고기", "소고기", "양고기", "닭고기", "계란", "생선", "해산물", "비건")
    )

    Scaffold(
        // AppBarView 의 topBar 내부
        topBar = {
            AppBarView(navController = navController)
            // ← 아이콘 버튼을 누르면 뒤로 돌아감
            //  navigateUp : 사용자를 이전에 있던 화면으로 돌아가게 하는 것
            { navController.navigateUp() }
        },
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = backgroundColor)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = " 음식을 선택하세요",
                    color = textColor,
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = FontFamily(Font(R.font.jua_regular),
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            buttonMappings.forEach { (row, buttons) ->
                item {
                    Text(
                        text = " $row",
                        style = MaterialTheme.typography.titleMedium,
                        color = textColor,
                        modifier = Modifier.padding(vertical = 4.dp),
                        fontFamily = FontFamily(Font(R.font.jua_regular))
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        buttons.forEach { button ->
                            ToggleButton(
                                text = button,
                                isChecked = mainViewModel.buttonStates.value[button] ?: false,
                                onCheckedChange = { mainViewModel.toggleButton(button) },

                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // "선택 완료" 버튼
            item {
                Spacer(modifier = Modifier.height(16.dp))
                ElevatedButton(
                    onClick = {
                        mainViewModel.updateMatchingConditions()
                        navController.navigate(ScreenRoute.RecommendationScreen.route)
                    },
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 64.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(R.color.app_color))
                ) {
                    Text(
                        text = "선택 완료",
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.jua_regular)
                        )
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

}

@SuppressLint("ResourceAsColor")
@Composable
fun ToggleButton(text: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Button(
        onClick = { onCheckedChange(!isChecked) },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSystemInDarkTheme()) Color.White else Color.LightGray,
            disabledContainerColor = Color.LightGray
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, if (isChecked) Color.Red else Color.Transparent),
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text,
            fontFamily = FontFamily(Font(R.font.jua_regular)))
    }
}
