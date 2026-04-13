package com.project.homeeats.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.homeeats.data.model.Order
import com.project.homeeats.data.model.OrderItem
import com.project.homeeats.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ── UI State ──────────────────────────────────────────────────────────────────

sealed class OrderUiState {
    object Idle    : OrderUiState()
    object Loading : OrderUiState()
    data class Success(val orderId: String = "") : OrderUiState()
    data class Error(val message: String)        : OrderUiState()
}

// ── ViewModel ─────────────────────────────────────────────────────────────────

class OrderViewModel(
    private val repository: OrderRepository = OrderRepository()
) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _orderState = MutableStateFlow<OrderUiState>(OrderUiState.Idle)
    val orderState: StateFlow<OrderUiState> = _orderState.asStateFlow()

    // ── Orders screen ─────────────────────────────────────────────────────────
    // Call once when OrdersScreen enters composition
    fun loadOrders(customerId: String) {
        viewModelScope.launch {
            try {
                repository.getMyOrders(customerId).collect { _orders.value = it }
            } catch (e: Exception) {
                // Ignore permission error on logout
            }
        }
    }

    // ── CartScreen "Place Order" button ───────────────────────────────────────
    // Wire: Button(onClick = { orderViewModel.placeOrder(uid, cartViewModel.toOrderItems()) })
    fun placeOrder(customerId: String, items: List<OrderItem>) {
        if (items.isEmpty()) {
            _orderState.value = OrderUiState.Error("Cart is empty")
            return
        }
        viewModelScope.launch {
            _orderState.value = OrderUiState.Loading
            repository.placeOrder(customerId, items)
                .onSuccess { id -> _orderState.value = OrderUiState.Success(id) }
                .onFailure { e -> _orderState.value = OrderUiState.Error(e.message ?: "Order failed") }
        }
    }

    fun resetOrderState() { _orderState.value = OrderUiState.Idle }
}