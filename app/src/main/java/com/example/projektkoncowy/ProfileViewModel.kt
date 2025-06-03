package com.example.projektkoncowy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projektkoncowy.datastore.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class ProfileUiState(
    val firstName: String = "",
    val lastName: String = "",
    val imagePath: String = ""
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStoreManager = DataStoreManager(application.applicationContext)

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {

        viewModelScope.launch {
            combine(
                dataStoreManager.firstNameFlow,
                dataStoreManager.lastNameFlow,
                dataStoreManager.imagePathFlow
            ) { firstName, lastName, imagePath ->
                ProfileUiState(firstName, lastName, imagePath)
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun saveFirstName(firstName: String) {
        viewModelScope.launch {
            dataStoreManager.saveFirstName(firstName)
        }
    }

    fun saveLastName(lastName: String) {
        viewModelScope.launch {
            dataStoreManager.saveLastName(lastName)
        }
    }

    fun saveImagePath(path: String) {
        viewModelScope.launch {
            dataStoreManager.saveImagePath(path)
        }
    }
}
