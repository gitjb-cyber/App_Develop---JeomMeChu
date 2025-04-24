package eu.tutorials.jeommechu.kakaoMap

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import eu.tutorials.jeommechu.BuildConfig

object KakaoMapUtils {
    private const val BASE_URL = "https://dapi.kakao.com/"
    private const val API_KEY = BuildConfig.KAKAO_REST_API_KEY

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", API_KEY) // api 키 보안
                .build()
            chain.proceed(request)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client) // ← client 추가
        .build()

    private val service = retrofit.create(KakaoSearchService::class.java)

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


interface KakaoSearchService {
    @GET("v2/local/search/keyword.json")
    suspend fun searchPlaces(
        @Query("query") query: String,
        @Query("x") x: Double,
        @Query("y") y: Double,
        @Query("sort") sort: String = "distance"
    ): KakaoSearchResponse
}

