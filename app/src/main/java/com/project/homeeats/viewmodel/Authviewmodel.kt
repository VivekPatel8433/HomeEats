package com.project.homeeats.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.homeeats.data.model.User
import com.project.homeeats.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle    : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val user: User? = null) : AuthUiState()
    data class Error(val message: String)      : AuthUiState()
}

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // Persists the logged-in user independently of uiState transitions
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    val isLoggedIn: Boolean get() = repository.isLoggedIn

    init {
        if (repository.isLoggedIn) {
            viewModelScope.launch {
                _currentUser.value = repository.getCurrentUserProfile()
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            repository.login(email, password)
                .onSuccess {
                    val user = repository.getCurrentUserProfile()
                    _currentUser.value = user
                    _uiState.value = AuthUiState.Success(user)
                }
                .onFailure { e ->
                    _uiState.value = AuthUiState.Error(e.message ?: "Login failed")
                }
        }
    }

    fun register(name: String, email: String, phone: String, password: String, isChef: Boolean) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            repository.register(name, email, phone, password, isChef)
                .onSuccess {
                    val user = repository.getCurrentUserProfile()
                    _currentUser.value = user
                    _uiState.value = AuthUiState.Success(user)
                }
                .onFailure { e ->
                    _uiState.value = AuthUiState.Error(e.message ?: "Registration failed")
                }
        }
    }

    fun logout() {
        repository.logout()
        _currentUser.value = null
        _uiState.value = AuthUiState.Idle
    }

    fun switchToChef() {
        viewModelScope.launch {
            repository.switchToChef()
                .onSuccess {
                    // Only update currentUser silently — do NOT emit Success
                    // or the LaunchedEffect in MainActivity intercepts it and
                    // redirects back to Home
                    _currentUser.value = repository.getCurrentUserProfile()
                }
                .onFailure { e ->
                    _uiState.value = AuthUiState.Error(e.message ?: "Could not switch role")
                }
        }
    }

    // Only resets the transition state — currentUser stays intact
    fun resetState() { _uiState.value = AuthUiState.Idle }
}