package com.jbandroid.jeommechu.screen_view.selectionScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jbandroid.jeommechu.navigation.ScreenRoute
import com.jbandroid.jeommechu.ui.util.AppBarView
import com.jbandroid.jeommechu.ui.util.FeatureGroup
import com.jbandroid.jeommechu.ui.util.SelectionCard
import com.jbandroid.jeommechu.ui.util.StatusBarView
import com.jbandroid.jeommechu.viewmodel.MainViewModel

@Composable
fun SelectionFeatureScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    StatusBarView()
    val buttonStates = mainViewModel.buttonStates.value

    val temperatureOptions = listOf("뜨거움🔥","살짝 매움", "차가움❄")
    val soupOptions = listOf("국물⭕", "국물❌")
    val spicyOptions = listOf("매움🌶", "안매움")

    Scaffold(
        topBar = {
            AppBarView(navController = navController, title = "음식의 특성을 골라보세요")
            { navController.navigateUp() }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            SelectionCard(modifier = Modifier.padding(innerPadding)) {

                FeatureGroup(
                    title = "온도",
                    options = temperatureOptions,
                    buttonStates,
                    mainViewModel::setButtonState
                )
                FeatureGroup(
                    title = "국물",
                    options = soupOptions,
                    buttonStates,
                    mainViewModel::setButtonState
                )
                FeatureGroup(
                    title = "맵기",
                    options = spicyOptions,
                    buttonStates,
                    mainViewModel::setButtonState
                )


                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        navController.navigate(ScreenRoute.SelectionStyle.route)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("다음 ➡")
                }
            }
        }
    }
}