package com.jbandroid.jeommechu.screen_view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.jbandroid.jeommechu.BuildConfig
import com.jbandroid.jeommechu.ui.util.AppBarView
import com.jbandroid.jeommechu.ui.util.StatusBarView
import com.jbandroid.jeommechu.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.math.abs

// 장소 정보 화면
@SuppressLint("MissingPermission", "SetJavaScriptEnabled")
@Composable
fun UserMapScreen(
    conditionKey: String,
    navController: NavController,
    mainViewModel: MainViewModel
) {
    StatusBarView()
    val context = LocalContext.current
    val fusedClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val location by mainViewModel.location.collectAsState()
    val address by mainViewModel.currentAddress.collectAsState()
    val places by mainViewModel.places.collectAsState()
    val error by mainViewModel.error.collectAsState()
    val sliderValue by mainViewModel.searchRadius.collectAsState()

    val showPermissionDialog = remember { mutableStateOf(true) }

    // 권한 요청 런처
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fine = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarse = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fine || coarse) {
            showPermissionDialog.value = false
            CoroutineScope(Dispatchers.Main).launch {
                val loc = fusedClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()
                mainViewModel.updateLocation(loc)
                mainViewModel.fetchCurrentAddress()
                mainViewModel.searchNearbyPlaces(conditionKey)
            }
        } else {
            mainViewModel.setError("위치 권한이 필요합니다.")
        }
    }

    // 진입 시 권한 확인
    LaunchedEffect(Unit) {
        val fineGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarseGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (!fineGranted && !coarseGranted) {
            // 권한 요청 필요
            showPermissionDialog.value = true
        } else {
            showPermissionDialog.value = false
            val loc = fusedClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()
            mainViewModel.updateLocation(loc)
            mainViewModel.fetchCurrentAddress()
            mainViewModel.searchNearbyPlaces(conditionKey)
        }
    }

    // 사용자 동의 UI 표시
    if (showPermissionDialog.value) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("위치 제공 동의") },
            text = { Text("정확한 추천을 위해 위치 정보를 제공해 주세요.") },
            confirmButton = {
                TextButton(onClick = {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }) {
                    Text("동의하고 계속")
                }
            }
        )
        return
    }

    // 위치 가져오기 진단
    if (location != null) {
        Log.d("Location", "✅ 위치 가져오기 성공: ${location!!.latitude}, ${location!!.longitude}")
        mainViewModel.updateLocation(location)
        mainViewModel.fetchCurrentAddress()
        mainViewModel.searchNearbyPlaces(conditionKey)
    } else {
        Log.w("Location", "⚠ 위치 가져오기 실패: null 반환됨")
        mainViewModel.setError("위치를 가져올 수 없습니다. GPS를 확인해주세요.")
    }
    Log.d("Kakao", "REST API KEY: ${BuildConfig.KAKAO_REST_API_KEY}")

    Scaffold(
        topBar = {
            AppBarView(navController = navController) { navController.navigateUp() }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // 현재 내 위치
            val locationText = address?.let {
                "현재 내 위치: $it"
            } ?: "현재 내 위치: 가져오는 중..."

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(text = locationText, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }

            // 내 위치 및 반경 + 위치 새로고침
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("📍 내 위치 기준 반경 ${sliderValue}km", style = MaterialTheme.typography.bodyLarge)
                TextButton(onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        val loc = fusedClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()
                        mainViewModel.updateLocation(loc)
                        mainViewModel.fetchCurrentAddress()
                        mainViewModel.searchNearbyPlaces(conditionKey)
                    }
                }) {
                    Text("위치 새로고침")
                }
            }

            // 단계별 슬라이더
            val distanceSteps = listOf(0.5f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f)

            Slider(
                value = sliderValue,
                onValueChange = { value ->
                    // 가장 가까운 값으로 스냅
                    val nearest = distanceSteps.minByOrNull { abs(it - value) } ?: value
                    mainViewModel.setSearchRadius(nearest)
                },
                valueRange = 0.5f..5.0f,
                steps = distanceSteps.size - 2,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Text(
                text = "📋 해당 맛집을 거리순으로 정렬합니다",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                color = Color.DarkGray
            )

            // 에러 메시지
            error?.let {
                Text(
                    text = "⚠ $it",
                    color = Color.Red,
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color(0xFFFFE0E0), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                )
            }

            // 장소가 없을 때 안내 메시지 표시
            if (places.isEmpty() && error == null) {
                Text(
                    text = "🔎 조건에 맞는 장소를 찾을 수 없어요.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                // 장소 카드 리스트
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    items(places) { place ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            elevation = CardDefaults.cardElevation(6.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {

                                // 1. 가게 이름
                                Text(
                                    text = "🏪 ${place.placeName}",
                                    style = MaterialTheme.typography.headlineSmall
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // 2. 거리(m)
                                Text(text = "📏 거리: ${place.distance.toInt()}m")

                                // 3. 도보 시간
                                Text(text = "🚶 도보 약 ${place.walkingTime}분")

                                // 4. 차량 시간 (도보보다 1.5배 빠르다고 가정)
                                Text(text = "🚗 차로 약 ${place.drivingTime}분")

                                // 5. 주소
                                Text(
                                    text = "📍 ${place.address}",
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // 6. 지도에서 보기
                                Button(
                                    onClick = {
                                        val mapUrl =
                                            "https://map.kakao.com/link/map/${place.placeName},${place.latitude},${place.longitude}"
                                        val intent = Intent(Intent.ACTION_VIEW, mapUrl.toUri())
                                        context.startActivity(intent)
                                    },
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Text("지도에서 보기")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}