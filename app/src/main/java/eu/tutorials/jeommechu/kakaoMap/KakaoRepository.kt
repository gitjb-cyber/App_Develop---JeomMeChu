package eu.tutorials.jeommechu.kakaoMap


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class KakaoRepository(
    private val kakaoApiKey: String
) {
    private val service: KakaoSearchService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(KakaoSearchService::class.java)
    }

    suspend fun searchNearbyPlaces(query: String, x: Double, y: Double): List<PlaceDocument> {
        return try {
            val response = service.searchPlaces(
                query = query,
                x = x,
                y = y,
                sort = "distance"
            )
            response.documents
        } catch (e: Exception) {
            emptyList()
        }
    }
}