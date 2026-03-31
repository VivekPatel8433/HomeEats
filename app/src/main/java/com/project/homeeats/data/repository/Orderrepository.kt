package com.project.homeeats.data.repository

import com.project.homeeats.data.model.Order
import com.project.homeeats.data.model.OrderItem
import com.project.homeeats.data.remote.FirestoreService
import kotlinx.coroutines.flow.Flow

class OrderRepository(
    private val firestoreService: FirestoreService = FirestoreService()
) {
    /** Place an order from the cart */
    suspend fun placeOrder(
        customerId: String,
        items: List<OrderItem>
    ): Result<String> = runCatching {
        val total = items.sumOf { it.price * it.quantity }
        val order = Order(
            customerId = customerId,
            items      = items,
            totalPrice = total
        )
        firestoreService.placeOrder(order)
    }

    /** Real-time orders stream for the Orders screen */
    fun getMyOrders(customerId: String): Flow<List<Order>> =
        firestoreService.getOrdersForCustomer(customerId)

    suspend fun updateStatus(orderId: String, status: String): Result<Unit> = runCatching {
        firestoreService.updateOrderStatus(orderId, status)
    }
}