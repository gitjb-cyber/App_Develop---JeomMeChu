package com.jbandroid.jeommechu.kakaoMap

import kotlin.math.ceil

data class PlaceModel(
    val placeName: String,
    val distance: Double, // meter
    val address: String,
    val latitude: Double,
    val longitude: Double
) {
    // 도보 1분당 약 66m
    val walkingTime: Int
        get() = ceil(distance / 66).toInt().coerceAtLeast(1)
    // 차로 1분당 약 500m
    val drivingTime: Int
        get() = ceil(distance / 500).toInt().coerceAtLeast(1) // 500m = 1분 가정

}