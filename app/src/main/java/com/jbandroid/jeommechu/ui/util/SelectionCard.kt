package com.jbandroid.jeommechu.ui.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.jbandroid.jeommechu.R
import com.jbandroid.jeommechu.screen_view.selectionScreen.ToggleButtonModern

@Composable
fun SelectionCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .wrapContentHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            content = content
        )
    }
}

@Composable
fun FeatureGroup(
    title: String,
    options: List<String>,
    buttonStates: Map<String, Boolean>,
    setButtonState: (String, Boolean) -> Unit
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium
    )

    Spacer(modifier = Modifier.height(8.dp))

    FlowRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEach { option ->
            val isChecked = buttonStates[option] ?: false
            ToggleButtonModern(
                text = option,
                isChecked = isChecked,
                onCheckedChange = {
                    if (isChecked) {
                        // 다시 누르면 해제
                        setButtonState(option, false)
                    } else {
                        // 전부 해제 후 선택
                        options.forEach { setButtonState(it, false) }
                        setButtonState(option, true)
                    }
                }
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
}