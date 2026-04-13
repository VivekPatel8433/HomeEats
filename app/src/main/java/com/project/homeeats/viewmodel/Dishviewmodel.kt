package com.project.homeeats.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.homeeats.data.model.Dish
import com.project.homeeats.data.repository.DishRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class DishUiState {
    object Idle    : DishUiState()
    object Loading : DishUiState()
    data class Success(val message: String = "") : DishUiState()
    data class Error(val message: String)        : DishUiState()
}

class DishViewModel(
    private val repository: DishRepository = DishRepository()
) : ViewModel() {

    private val _allDishes = MutableStateFlow<List<Dish>>(emptyList())
    val allDishes: StateFlow<List<Dish>> = _allDishes.asStateFlow()

    private val _chefDishes = MutableStateFlow<List<Dish>>(emptyList())
    val chefDishes: StateFlow<List<Dish>> = _chefDishes.asStateFlow()

    private val _actionState = MutableStateFlow<DishUiState>(DishUiState.Idle)
    val actionState: StateFlow<DishUiState> = _actionState.asStateFlow()

    fun loadAllDishes() {
        viewModelScope.launch {
            try {
                repository.getAllDishes().collect { _allDishes.value = it }
            } catch (e: Exception) {
                // Ignore permission error on logout
            }
        }
    }

    fun loadChefDishes(chefId: String) {
        viewModelScope.launch {
            try {
                repository.getChefDishes(chefId).collect { _chefDishes.value = it }
            } catch (e: Exception) {
                // Ignore permission error on logout
            }
        }
    }

    fun addDish(
        chefId: String,
        chefName: String,
        name: String,
        description: String,
        price: Double,
        imageUrl: String = ""
    ) {
        if (price <= 0.0) {
            _actionState.value = DishUiState.Error("Price must be greater than 0")
            return
        }
        viewModelScope.launch {
            _actionState.value = DishUiState.Loading
            repository.addDish(chefId, chefName, name, description, price, imageUrl)
                .onSuccess { _actionState.value = DishUiState.Success("Dish added!") }
                .onFailure { e -> _actionState.value = DishUiState.Error(e.message ?: "Failed to add dish") }
        }
    }

    fun deleteDish(dishId: String) {
        viewModelScope.launch {
            repository.deleteDish(dishId)
                .onFailure { e -> _actionState.value = DishUiState.Error(e.message ?: "Delete failed") }
        }
    }

    fun updateDish(dish: Dish) {
        viewModelScope.launch {
            _actionState.value = DishUiState.Loading
            repository.updateDish(dish)
                .onSuccess { _actionState.value = DishUiState.Success("Dish updated!") }
                .onFailure { e -> _actionState.value = DishUiState.Error(e.message ?: "Update failed") }
        }
    }

    fun resetActionState() { _actionState.value = DishUiState.Idle }
}