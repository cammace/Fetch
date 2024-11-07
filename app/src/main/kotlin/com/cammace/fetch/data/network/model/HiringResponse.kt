package com.cammace.fetch.data.network.model

import com.cammace.fetch.data.model.HiringItem
import kotlinx.serialization.Serializable

@Serializable
data class HiringResponse(
    val id: Int,
    val listId: Int,
    val name: String? = null
)

fun HiringResponse.asExternalModel(): HiringItem = HiringItem(
    id = id,
    listId = listId,
    name = name ?: ""
)
