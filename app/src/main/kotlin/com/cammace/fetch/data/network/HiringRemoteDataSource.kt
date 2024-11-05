package com.cammace.fetch.data.network

import com.cammace.fetch.data.network.model.HiringResponse

/**
 * Interface representing network calls to the Fetch hiring API.
 */
interface HiringRemoteDataSource {
    suspend fun getHiringList(): List<HiringResponse>
}
