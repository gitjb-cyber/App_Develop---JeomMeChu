package eu.tutorials.jeommechu.kakaoMap

import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoSearchService {
    @GET("v2/local/search/keyword.json")
    suspend fun searchPlaces(
        @Query("query") query: String,
        @Query("x") x: Double,
        @Query("y") y: Double,
        @Query("sort") sort: String = "distance"
    ): KakaoSearchResponse
}

