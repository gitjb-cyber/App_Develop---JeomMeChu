package eu.tutorials.jeommechu.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import eu.tutorials.jeommechu.R

@Composable
fun FoodCard(
    matchingConditions: Set<String>,
    toggleConditions: Map<String, List<String>>
) {
    val grouped: Map<String, List<String>> = matchingConditions.groupBy { conditionKey ->
        toggleConditions[conditionKey]?.getOrNull(4) ?: "기타"
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
                    OutlinedCard(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colors.surface,
                        ),
                        border = BorderStroke(1.dp, Color.Black),
                        modifier = Modifier
                            .padding(8.dp)
                            .size(200.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // 카드 상단에 해당 key 표시
                            Text(
                                text = conditionKey,
                                style = MaterialTheme.typography.h6,
                                fontFamily = FontFamily(Font(R.font.jua_regular))
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            // 인덱스 0 ~ 3까지 개별로 표시
                            val firstFourCount = minOf(4, values.size)
                            for (i in 0 until firstFourCount) {
                                Text(
                                    text = values[i],
                                    style = MaterialTheme.typography.body2,
                                    fontFamily = FontFamily(Font(R.font.jua_regular))
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            // 인덱스 5부터 끝까지 한 줄로 나열 (값이 있을 경우)
                            if (values.size > 5) {
                                val remaining = values.subList(5, values.size).joinToString(" ")
                                Text(
                                    text = remaining,
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
