package com.cammace.fetch.data

import com.cammace.fetch.data.model.HiringItem
import com.cammace.fetch.data.network.HiringRemoteDataSource
import com.cammace.fetch.data.network.model.asExternalModel
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Singleton
class HiringRepository @Inject constructor(
    private val network: HiringRemoteDataSource
) {

    /**
     * Fetches the list of [HiringItem]s from the network. The list is filtered
     * to remove any null or blank names and sorted by listId and name.
     *
     * **Note:** Any network or other error for that matter is being handled
     * downstream in the viewmodel.
     */
    fun getHiringList(): Flow<List<HiringItem>> = flow {
        val items = network.getHiringList()

        // Process data: filter and sort
        val processedItems = items
            .filter { !it.name.isNullOrBlank() }
            .map { it.asExternalModel() }
            .sortedWith(compareBy<HiringItem> { it.listId }.thenBy { it.name })

        emit(processedItems)
    }
}
