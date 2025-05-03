package eu.tutorials.jeommechu.kakaoMap


import eu.tutorials.jeommechu.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class KakaoRepository {
    private val service: KakaoSearchService

    init {
        // Kakao 인증 헤더용 Interceptor
        val interceptor = Interceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}")
                .build()
            chain.proceed(newRequest)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client) // ← client 설정 추가
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
