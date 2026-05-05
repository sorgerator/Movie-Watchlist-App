package com.example.movie_watchlist_app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class HomeShortcutViewModel : ViewModel() {
    private val _searchTip = MutableStateFlow<String>("")
    val searchTip: StateFlow<String> = _searchTip

    init {
        startSearchTips()
    }

    private fun observeSearchTips(): Flow<String> = flow {
        emit("Tip 1: Use search to find movies.")
        delay(1000)
        emit("Tip 2: Filter by genre for better results.")
        delay(1000)
        emit("Tip 3: Mark movies as favorites.")
    }

    private fun startSearchTips() {
        viewModelScope.launch {
            observeSearchTips().collect { tip ->
                _searchTip.value = tip
            }
        }
    }
}
