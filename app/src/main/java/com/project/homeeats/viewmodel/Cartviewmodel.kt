package com.project.homeeats.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.project.homeeats.data.model.Dish

/**
 * CartItem wraps a Dish with a quantity.
 * Kept in-memory (no Firestore) until the user places the order.
 */
data class CartItem(val dish: Dish, val quantity: Int)

/**
 * Shared ViewModel for HomeScreen (add to cart) and CartScreen (display/edit cart).
 * In MainActivity, create it once and pass it to both screens.
 */
class CartViewModel : ViewModel() {

    // mutableStateListOf triggers recomposition automatically
    private val _items = mutableStateListOf<CartItem>()
    val items: List<CartItem> get() = _items

    fun addItem(dish: Dish) {
        val index = _items.indexOfFirst { it.dish.id == dish.id }
        if (index >= 0) {
            _items[index] = _items[index].copy(quantity = _items[index].quantity + 1)
        } else {
            _items.add(CartItem(dish, 1))
        }
    }

    fun removeOne(dishId: String) {
        val index = _items.indexOfFirst { it.dish.id == dishId }
        if (index < 0) return
        val current = _items[index]
        if (current.quantity <= 1) _items.removeAt(index)
        else _items[index] = current.copy(quantity = current.quantity - 1)
    }

    fun clearCart() = _items.clear()

    fun totalPrice(): Double = _items.sumOf { it.dish.price * it.quantity }

    /** Convert cart items to OrderItems for OrderViewModel.placeOrder() */
    fun toOrderItems() = _items.map {
        com.project.homeeats.data.model.OrderItem(
            dishId   = it.dish.id,
            dishName = it.dish.name,
            price    = it.dish.price,
            quantity = it.quantity
        )
    }
}