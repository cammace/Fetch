package com.cammace.fetch.data

import com.cammace.fetch.data.network.HiringRemoteDataSource
import com.cammace.fetch.data.network.model.HiringResponse
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

@Singleton
class HiringRepository @Inject constructor(
    private val network: HiringRemoteDataSource
) {

    fun getHiringList(): Flow<Result<List<HiringResponse>>> = flow {
        try {
            val response = network.getHiringList()
            // TODO emit response mapped to domain model.
        } catch (e: Exception) {
            Timber.e(e, "Error getting nearby restaurants")
            emit(Result.failure(e))
        }
    }
}
