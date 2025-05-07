package eu.tutorials.jeommechu.screen_view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import eu.tutorials.jeommechu.viewmodel.MainViewModel
import kotlinx.coroutines.tasks.await

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("MissingPermission", "SetJavaScriptEnabled")
@Composable
fun UserMapScreen(
    conditionKey: String, // 검색 키워드
    mainViewModel: MainViewModel
) {
    val context = LocalContext.current // 현재 context(위치 요청 등)
    val places by mainViewModel.places.collectAsState() // 검색된 장소 목록(거리순)
    val error by mainViewModel.error.collectAsState() // 오류 메시지


    val webViewRef = remember { mutableStateOf<WebView?>(null) }

    // 위치 권한 요청 런처 추가
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val fine = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarse = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            if (!fine && !coarse) {
                mainViewModel.setError("위치 권한이 필요합니다.")
            }
        }
    )

    // 최초 진입 시 위치 권한 요청
    LaunchedEffect(Unit) {
        val fineGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarseGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (!fineGranted || !coarseGranted) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // 위치 요청 및 장소 검색
    LaunchedEffect(conditionKey) {
        val fusedClient = LocationServices.getFusedLocationProviderClient(context)
        try {
            val location = fusedClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()
            mainViewModel.updateLocation(location)
            mainViewModel.searchNearbyPlaces(conditionKey)
            mainViewModel.startLocationUpdates(context, conditionKey)
        } catch (e: SecurityException) {
            mainViewModel.updateLocation(null)
            mainViewModel.setError("위치 권한이 필요합니다.")
        }
    }

    // 화면에서 벗어날 때 위치 갱신 중단
    DisposableEffect(Unit) {
        onDispose {
            mainViewModel.stopLocationUpdates()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        // WebView
        AndroidView(factory = {
            WebView(it).apply {
                webViewClient = object : WebViewClient() {
                    @Deprecated("Deprecated in Java")
                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                        url?.let {
                            if (url.startsWith("intent://")) {
                                try {
                                    val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                                    if (intent != null) {
                                        context.startActivity(intent)
                                        return true
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                return true
                            }
                        }
                        return false // 나머지는 WebView가 직접 처리
                    }
                }
                settings.javaScriptEnabled = true
                loadUrl("https://map.kakao.com/?q=$conditionKey")
            }
        })

        // 오류 메시지가 있을 경우
        error?.let {
            Text(
                text = "⚠ $it",
                color = Color.Red,
                style = MaterialTheme.typography.body2,
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color(0xFFFFE0E0), shape = RoundedCornerShape(8.dp))
                    .padding(12.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

       /* Text(
            text = "📍 주변 추천 리스트(거리순)",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(horizontal = 16.dp)
        )*/

        Spacer(modifier = Modifier.height(4.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            items(places) { place ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            webViewRef.value?.loadUrl("https://map.kakao.com/link/search/${place.placeName}")
                        }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "• ${place.placeName}", style = MaterialTheme.typography.body1)
                        Text(
                            text = "${place.distance}m 거리",
                            style = MaterialTheme.typography.body2,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}