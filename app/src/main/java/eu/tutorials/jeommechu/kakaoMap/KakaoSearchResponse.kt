package eu.tutorials.jeommechu.kakaoMap

data class KakaoSearchResponse(
    val documents: List<PlaceDocument>
)

data class PlaceDocument(
    val placeName: String,
    val addressName: String,
    val distance: String
)