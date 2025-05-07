package eu.tutorials.jeommechu.kakaoMap

import com.google.gson.annotations.SerializedName

data class KakaoSearchResponse(
    @SerializedName("documents") val documents: List<PlaceDocument>
)

data class PlaceDocument(
    @SerializedName("place_name") val placeName: String,
    @SerializedName("distance") val distance: String,
    @SerializedName("address_name") val addressName: String,
    @SerializedName("road_address_name") val roadAddressName: String?, // 도로명 주소
    // 좌표
    @SerializedName("x") val longitude: String,
    @SerializedName("y") val latitude: String

)