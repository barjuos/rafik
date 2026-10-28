package com.rafiq.adhkar.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.rafiq.adhkar.RafiqApp
import com.rafiq.adhkar.data.AdhkarRepository
import com.rafiq.adhkar.data.db.AppDatabase
import com.rafiq.adhkar.data.db.CustomDhikr
import com.rafiq.adhkar.data.model.Dhikr
import com.rafiq.adhkar.data.settings.SettingsStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: AdhkarRepository,
    private val settingsStore: SettingsStore,
    database: AppDatabase,
) : ViewModel() {

    private val customDao = database.customDhikrDao()

    private val _adhkar = MutableStateFlow<Map<String, List<Dhikr>>>(emptyMap())
    val adhkar: StateFlow<Map<String, List<Dhikr>>> = _adhkar.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val theme: StateFlow<String> = settingsStore.theme
        .stateIn(viewModelScope, SharingStarted.Eagerly, "system")

    val customAdhkar: StateFlow<List<CustomDhikr>> = customDao.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch {
            _adhkar.value = runCatching { repository.load() }.getOrDefault(emptyMap())
            _isLoading.value = false
        }
    }

    fun setTheme(value: String) {
        viewModelScope.launch { settingsStore.setTheme(value) }
    }

    fun addCustom(content: String, count: Int, description: String, reference: String) {
        viewModelScope.launch {
            customDao.insert(
                CustomDhikr(
                    content = content,
                    count = count,
                    description = description,
                    reference = reference,
                ),
            )
        }
    }

    fun deleteCustom(item: CustomDhikr) {
        viewModelScope.launch { customDao.delete(item) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as RafiqApp
                MainViewModel(app.repository, app.settingsStore, app.database)
            }
        }
    }
}
