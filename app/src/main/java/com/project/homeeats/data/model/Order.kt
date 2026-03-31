package com.project.homeeats.data.model

data class OrderItem(
    val dishId: String = "",
    val dishName: String = "",
    val price: Double = 0.0,
    val quantity: Int = 1
)

data class Order(
    val id: String = "",
    val customerId: String = "",
    val items: List<OrderItem> = emptyList(),
    val totalPrice: Double = 0.0,
    val status: String = "pending",   // "pending" | "preparing" | "delivered"
    val createdAt: Long = System.currentTimeMillis()
)