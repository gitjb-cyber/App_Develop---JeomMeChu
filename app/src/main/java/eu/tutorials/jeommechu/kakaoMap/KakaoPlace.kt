package eu.tutorials.jeommechu.kakaoMap

data class KakaoSearchResponse(
    val documents: List<PlaceDocument>
)

data class PlaceDocument(
    val place_name: String,
    val address_name: String,
    val distance: String
)