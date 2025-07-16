package com.jbandroid.jeommechu.kakaoMap

import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoSearchService {
    @GET("v2/local/search/keyword.json")
    // 코루틴에서 비동기로 호출
    suspend fun searchPlaces(
        @Query("query") query: String,
        @Query("x") x: Double,
        @Query("y") y: Double,
        @Query("radius") radius: Int = 1000,
        @Query("sort") sort: String = "distance"
    ): KakaoSearchResponse

    @GET("v2/local/geo/coord2address.json")
    suspend fun getAddress(
        @Query("x") x: Double,
        @Query("y") y: Double
    ): AddressResponse
}

