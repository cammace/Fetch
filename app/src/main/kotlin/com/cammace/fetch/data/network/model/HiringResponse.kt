package com.cammace.fetch.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class HiringResponse(
    val id: Int,
    val listId: Int,
    val name: String? = null
)
