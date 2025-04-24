package eu.tutorials.jeommechu.screen_view

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.tutorials.jeommechu.viewmodel.MainViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("MissingPermission")
@Composable
fun UserMapScreen(
    conditionKey: String,
    mainViewModel: MainViewModel
) {
    val places by mainViewModel.places.collectAsState()
    val error by mainViewModel.error.collectAsState()

    LaunchedEffect(conditionKey) {
        mainViewModel.searchNearbyPlaces(conditionKey)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "주변 장소 ($conditionKey)", style = MaterialTheme.typography.h3)

        if (error != null) {
            Text(text = error ?: "", color = MaterialTheme.colors.error)
        }

        if (places.isEmpty()) {
            Text(text = "결과가 없습니다.")
        } else {
            places.forEach { place ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(place.place_name, style = MaterialTheme.typography.body1)
                        Text(place.address_name, style = MaterialTheme.typography.body2)
                    }
                }
            }
        }
    }
}
