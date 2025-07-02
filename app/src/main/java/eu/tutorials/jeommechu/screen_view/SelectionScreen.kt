package eu.tutorials.jeommechu.screen_view


import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowRow
import eu.tutorials.jeommechu.R
import eu.tutorials.jeommechu.navigation.ScreenRoute
import eu.tutorials.jeommechu.ui.util.AppBarView
import eu.tutorials.jeommechu.ui.util.StatusBarView
import eu.tutorials.jeommechu.viewmodel.MainViewModel
import kotlinx.coroutines.delay


@Composable
fun SelectionScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    StatusBarView()
    var showWarning by remember { mutableStateOf(false) }
    var isAllSelected by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val buttonMappings = mapOf(
        "밥빵면" to listOf("밥🍚", "빵🍔", "면🍝", "떡", "탄수화물 X", "기타"),
        "국물" to listOf("국물⭕", "국물❌"),
        "온도" to listOf("뜨거움🔥", "차가움❄"),
        "맵기" to listOf("매움🥵", "안매움"),
        "국가" to listOf("한식", "양식", "중식", "일식", "아시안"),
        "고기" to listOf("돼지고기", "소고기", "양고기", "닭고기", "계란", "생선", "해산물", "비건")
    )

    val allButtons = buttonMappings.values.flatten().distinct()

    Scaffold(
        topBar = {
            AppBarView(navController = navController) { navController.navigateUp() }
        },
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(colorScheme.background)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = " 음식을 선택하세요",
                    color = colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = FontFamily(Font(R.font.jua_regular))
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        isAllSelected = !isAllSelected
                        allButtons.forEach { btn ->
                            mainViewModel.setButtonState(btn, isAllSelected)
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    border = BorderStroke(1.dp, colorScheme.primary),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = colorScheme.primaryContainer,
                        contentColor = colorScheme.onPrimaryContainer
                    )
                ) {
                    Text(if (isAllSelected) "전체 해제" else "전체 선택")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            buttonMappings.forEach { (row, buttons) ->
                item {
                    Text(
                        text = " $row",
                        style = MaterialTheme.typography.titleMedium,
                        color = colorScheme.onBackground,
                        modifier = Modifier.padding(vertical = 4.dp),
                        fontFamily = FontFamily(Font(R.font.jua_regular))
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        mainAxisSpacing = 8.dp,
                        crossAxisSpacing = 8.dp
                    ) {
                        buttons.forEach { button ->
                            ToggleButtonModern(
                                text = button,
                                isChecked = mainViewModel.buttonStates.value[button] ?: false,
                                onCheckedChange = { mainViewModel.toggleButton(button) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val isAnySelected = mainViewModel.buttonStates.value.values.any { it }
                        if (!isAnySelected) {
                            showWarning = true
                        } else {
                            mainViewModel.updateMatchingConditions()
                            navController.navigate(ScreenRoute.RecommendationScreen.route)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "선택 완료",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.jua_regular))
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        if (showWarning) {
            LaunchedEffect(Unit) {
                Toast.makeText(
                    context,
                    "카테고리를 선택하세요. 모든 음식을 보고싶으시다면 전체 선택을 고르세요!",
                    Toast.LENGTH_SHORT
                ).show()
                delay(2000)
                showWarning = false
            }
        }
    }
}

@Composable
fun ToggleButtonModern(
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val containerColor by animateColorAsState(
        targetValue = if (isChecked) colorScheme.primaryContainer else colorScheme.surface,
        label = "containerColorAnim"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isChecked) colorScheme.onPrimaryContainer else colorScheme.onSurface,
        label = "contentColorAnim"
    )
    OutlinedButton(
        onClick = { onCheckedChange(!isChecked) },
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = BorderStroke(1.dp, if (isChecked) colorScheme.primary else Color.LightGray),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(4.dp)
    ) {
        Text(
            text,
            fontFamily = FontFamily(Font(R.font.jua_regular)),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
