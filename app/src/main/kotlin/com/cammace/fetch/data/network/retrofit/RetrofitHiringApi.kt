package com.cammace.fetch.data.network.retrofit

import com.cammace.fetch.data.network.HiringRemoteDataSource
import com.cammace.fetch.data.network.model.HiringResponse
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET

/**
 * Retrofit API declaration for Fetch's hiring JSON API.
 */
private interface RetrofitHiringApi {

    @GET
    suspend fun getHiringList(): List<HiringResponse>
}

private const val BASE_URL = "https://fetch-hiring.s3.amazonaws.com/hiring.json"

/**
 * [Retrofit] backed [HiringRemoteDataSource]
 */
@Singleton
class RetrofitHiringRemoteDataSource @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: Call.Factory,
) : HiringRemoteDataSource {

    private val networkApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .callFactory(okhttpCallFactory)
        .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(RetrofitHiringApi::class.java)

    override suspend fun getHiringList(): List<HiringResponse> = networkApi.getHiringList()
}
