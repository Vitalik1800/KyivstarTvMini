package com.vs18.kyivstartvmini.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vs18.kyivstartvmini.data.model.ProgramItem
import com.vs18.kyivstartvmini.data.model.isNow
import com.vs18.kyivstartvmini.data.repository.EpgRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

class EpgViewModel : ViewModel() {

    private val repository = EpgRepository()

    var programs by mutableStateOf<List<ProgramItem>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var currentIndex by mutableStateOf<Int?>(null)
        private set

    private var refreshJob: Job? = null

    fun loadEpg(tvgId: String) {
        isLoading = true
        viewModelScope.launch {
            programs = repository.getForChannel(tvgId)
            updateCurrentIndex()
            isLoading = false
        }
        startAutoRefresh(tvgId)
    }

    private fun updateCurrentIndex() {
        currentIndex = programs.indexOfFirst {
            isNow(it.startTime, it.endTime)
        }.takeIf { it >= 0 }
    }

    private fun startAutoRefresh(tvgId: String) {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            while (isActive) {
                delay(5.minutes)
                programs = repository.getForChannel(tvgId)
                updateCurrentIndex()
            }
        }
    }

    override fun onCleared() {
        refreshJob?.cancel()
        super.onCleared()
    }
}
