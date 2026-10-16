package com.example.mobiledev_maat.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DayLogFactory(private val repository: DayLogRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DayLogViewModel(repository) as T
    }
}