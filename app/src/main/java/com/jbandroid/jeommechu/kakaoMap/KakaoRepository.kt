package com.jbandroid.jeommechu.kakaoMap


import android.util.Log
import com.google.gson.annotations.SerializedName
import com.jbandroid.jeommechu.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Kakao REST API를 통해 장소 검색
class KakaoRepository {
    private val service: KakaoSearchService

    init {
        // Kakao 인증 헤더용 Interceptor
        val interceptor = Interceptor { chain ->
            val kakaoKey = BuildConfig.KAKAO_REST_API_KEY
            Log.d("KakaoKeyCheck", "🔑 REST API Key: $kakaoKey")

            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "KakaoAK $kakaoKey")
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
            Log.d("KakaoRepository", "검색 결과 개수: ${response.documents.size}")
            response.documents
        } catch (e: Exception) {
            Log.e("KakaoRepository", "카카오 장소 검색 실패: ${e.localizedMessage}", e)
            emptyList()
        }
    }

    suspend fun getAddressFromCoordinates(x: Double, y: Double): String? {
        return try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(
                    OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val newRequest = chain.request().newBuilder()
                            .addHeader(
                                "Authorization",
                                "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"
                            )
                            .build()
                        chain.proceed(newRequest)
                    }
                    .build()
                )
                .build()

            val service = retrofit.create(KakaoSearchService::class.java)

            val response = service.getAddress(x, y)
            response.documents.firstOrNull()?.address?.addressName
        } catch (e: Exception) {
            null
        }
    }
}

data class AddressResponse(
    @SerializedName("documents") val documents: List<AddressDocument>
)

data class AddressDocument(
    @SerializedName("address") val address: AddressInfo
)

data class AddressInfo(
    @SerializedName("address_name") val addressName: String
)
