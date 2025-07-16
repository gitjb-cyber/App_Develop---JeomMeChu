package com.jbandroid.jeommechu.kakaoMap

import com.google.gson.annotations.SerializedName

// 장소 정보
data class KakaoSearchResponse(
    @SerializedName("documents") val documents: List<PlaceDocument>
)

// 표시할 기본 정보
data class PlaceDocument(
    @SerializedName("place_name") val placeName: String,
    @SerializedName("distance") val distance: String,
    @SerializedName("address_name") val addressName: String,
    @SerializedName("road_address_name") val roadAddressName: String?, // 도로명 주소
    // 좌표
    @SerializedName("x") val longitude: Double,
    @SerializedName("y") val latitude: Double

)