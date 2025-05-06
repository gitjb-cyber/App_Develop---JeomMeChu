package eu.tutorials.jeommechu.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import eu.tutorials.jeommechu.R
import eu.tutorials.jeommechu.viewmodel.MainViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FoodCardColumn(
    matchingConditions: Set<String>,
    toggleConditions: Map<String, List<String>>,
    navController: NavController,
    mainViewModel: MainViewModel
) {
    val grouped: Map<String, List<String>> = matchingConditions.groupBy { conditionKey ->
        toggleConditions[conditionKey]?.getOrNull(0) ?: "기타"
    }

    // 각 그룹별로 제목과 LazyRow를 표시
    Column(modifier = Modifier.fillMaxWidth()) {
        grouped.forEach { (groupTitle, keys) ->
            // 그룹 제목: 예를 들어 "한식", "일식" 등
            Text(
                text = groupTitle,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                fontFamily = FontFamily(Font(R.font.jua_regular))
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(keys) { conditionKey ->
                    val values = toggleConditions[conditionKey] ?: emptyList()

                    var menuExpanded by remember { mutableStateOf(false) }

                    OutlinedCard(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colors.surface,
                        ),
                        border = BorderStroke(1.dp, Color.Black),
                        modifier = Modifier
                            .padding(8.dp)
                            .size(200.dp),
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {

                            // 메뉴 버튼 (점 세 개)
                            IconButton(
                                onClick = { menuExpanded = true },
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Icon(
                                    Icons.Default.MoreVert,
                                    contentDescription = "More Options"
                                )
                            }

                            DropdownMenu(
                                expanded = menuExpanded,
                                onDismissRequest = { menuExpanded = false }
                            ) {
                                DropdownMenuItem(onClick = {
                                    menuExpanded = false
                                    navController.navigate("userMap/$conditionKey")
                                }) {
                                    Text("📍 주변 맛집 찾기")
                                }
                                DropdownMenuItem(onClick = {
                                    menuExpanded = false
                                    val today = LocalDate.now().toString()
                                    mainViewModel.insertMemo(today, conditionKey)
                                }) {
                                    Text("🍽 오늘 먹을 음식으로 추가")
                                }
                            }


                            // 카드 내부 콘텐츠
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .align(Alignment.CenterStart)
                            ) {
                                Text(
                                    text = conditionKey,
                                    style = MaterialTheme.typography.h6,
                                    fontFamily = FontFamily(Font(R.font.jua_regular))
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                val subValues = values.drop(1)
                                subValues.forEach { tag ->
                                    Text(
                                        text = tag,
                                        style = MaterialTheme.typography.body2,
                                        fontFamily = FontFamily(Font(R.font.jua_regular))
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}