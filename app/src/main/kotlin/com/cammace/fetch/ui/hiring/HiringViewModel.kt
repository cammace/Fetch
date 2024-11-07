package com.cammace.fetch.ui.hiring

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cammace.fetch.data.HiringRepository
import com.cammace.fetch.data.model.HiringItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HiringViewModel @Inject constructor(
    repository: HiringRepository
) : ViewModel() {

    private val retryTrigger = MutableSharedFlow<Unit>(replay = 1).apply {
        // Triggers the first data fetch when viewmodel first created.
        tryEmit(Unit)
    }

    val uiState: StateFlow<HiringUiState> = retryTrigger
        .flatMapLatest {
            repository.getHiringList()
                .map<List<HiringItem>, HiringUiState> { items ->
                    if (items.isEmpty()) {
                        HiringUiState.Empty
                    } else {
                        HiringUiState.Success(items)
                    }
                }
                .catch { exception ->
                    emit(HiringUiState.LoadFailed(exception))
                }
                .onStart {
                    emit(HiringUiState.Loading)
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HiringUiState.Loading
        )

    fun retry() {
        retryTrigger.tryEmit(Unit)
    }
}
