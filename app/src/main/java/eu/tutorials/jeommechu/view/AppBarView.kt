package eu.tutorials.jeommechu.view

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import eu.tutorials.jeommechu.R

@Composable
fun AppBarView(
    title: String,
    onBackNavClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = colorResource(id = R.color.white),
                modifier = Modifier
                    .padding(start = 4.dp)
                    .heightIn(max = 24.dp),
                fontFamily = FontFamily(Font(R.font.jua_regular))
            )
        },
        backgroundColor = colorResource(id = R.color.app_bar_color),
        elevation = 3.dp,
        navigationIcon = {
            IconButton(onClick = { onBackNavClicked() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    tint = Color.White,
                    contentDescription = "뒤로가기"
                )
            }
        }
    )
}
