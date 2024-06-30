package com.rumpel.rumpelandroid.ui.activities.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rumpel.rumpelandroid.utils.threads.BackgroundTask
import com.rumpel.rumpelandroid.utils.threads.TaskRunner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Helper class to load stuff using a background thread and update the UI using a swipe refresh mechanism
 */
class HomeViewModel: ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun loadStuff(runnable: Runnable) {
        viewModelScope.launch {
            _isLoading.value = true
            TaskRunner.executeAsync(BackgroundTask(
                BackgroundTask(runnable) {}) {
                _isLoading.value = false
            })
        }
    }
}