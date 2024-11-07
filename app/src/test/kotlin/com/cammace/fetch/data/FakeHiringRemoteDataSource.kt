package com.cammace.fetch.data

import com.cammace.fetch.data.network.HiringRemoteDataSource
import com.cammace.fetch.data.network.model.HiringResponse

class FakeHiringRemoteDataSource(
    private val itemsToReturn: List<HiringResponse>? = null,
    private val exceptionToThrow: Exception? = null
) : HiringRemoteDataSource {

    override suspend fun getHiringList(): List<HiringResponse> {
        exceptionToThrow?.let { throw it }
        return itemsToReturn ?: emptyList()
    }
}
