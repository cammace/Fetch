package com.cammace.fetch.ui.hiring

import com.cammace.fetch.data.model.HiringItem

sealed interface HiringUiState {
    data object Loading : HiringUiState
    data class LoadFailed(val exception: Throwable) : HiringUiState
    data object Empty : HiringUiState
    data class Success(val items: List<HiringItem>) : HiringUiState
}
