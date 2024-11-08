package com.cammace.fetch.di

import android.content.Context
import com.cammace.fetch.BuildConfig
import com.cammace.fetch.data.network.HiringRemoteDataSource
import com.cammace.fetch.data.network.retrofit.RetrofitHiringRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Hilt module that provides network-related dependencies. Instances
 * created are available as long as the application is running.
 */
@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

    @Binds
    fun bindsHiringRemoteDataSource(hiringRemoteDataSource: RetrofitHiringRemoteDataSource): HiringRemoteDataSource

    companion object {
        // timeout network request after 10 seconds.
        private const val TIMEOUT_TIME = 10L

        // uses 10MB the application's cache directory.
        private const val CACHE_SIZE: Long = 10 * 1024 * 1024 // 10MB

        @Provides
        @Singleton
        fun providesNetworkJson(): Json = Json {
            // Don't throw error if JSON property don't match properties in data class.
            ignoreUnknownKeys = true
        }

        @Provides
        @Singleton
        fun okHttpCallFactory(@ApplicationContext context: Context): Call.Factory = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    if (BuildConfig.DEBUG) {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                },
            )
            .cache(Cache(directory = context.cacheDir, maxSize = CACHE_SIZE))
            .connectTimeout(TIMEOUT_TIME, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_TIME, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_TIME, TimeUnit.SECONDS)
            .build()
    }
}
