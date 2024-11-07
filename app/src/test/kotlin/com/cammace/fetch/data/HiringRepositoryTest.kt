package com.cammace.fetch.data

import com.cammace.fetch.data.model.HiringItem
import com.cammace.fetch.data.network.model.HiringResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HiringRepositoryTest {

    @Test
    fun `getHiringList returns processed items when API call is successful`() = runTest {
        val apiItems = listOf(
            HiringResponse(id = 1, listId = 1, name = "Item B"),
            HiringResponse(id = 2, listId = 1, name = "Item A"),
            HiringResponse(id = 3, listId = 2, name = null),
            HiringResponse(id = 4, listId = 2, name = ""),
            HiringResponse(id = 5, listId = 2, name = "Item C"),
        )
        val expectedItems = listOf(
            HiringItem(id = 2, listId = 1, name = "Item A"),
            HiringItem(id = 1, listId = 1, name = "Item B"),
            HiringItem(id = 5, listId = 2, name = "Item C"),
        )
        val fakeApiService = FakeHiringRemoteDataSource(itemsToReturn = apiItems)
        val repository = HiringRepository(network = fakeApiService)

        val result = repository.getHiringList().first()

        assertEquals(expectedItems, result)
    }

    @Test
    fun `getHiringList throws exception when API call fails`() = runTest {
        val exception = Exception("Network error")
        val fakeApiService = FakeHiringRemoteDataSource(exceptionToThrow = exception)
        val repository = HiringRepository(network = fakeApiService)

        try {
            repository.getHiringList().first()
            fail("Expected an exception to be thrown")
        } catch (e: Exception) {
            assertEquals(exception, e)
        }
    }
}
