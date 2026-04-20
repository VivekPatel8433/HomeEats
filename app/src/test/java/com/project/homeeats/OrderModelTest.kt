package com.project.homeeats

import com.project.homeeats.data.model.Order
import com.project.homeeats.data.model.OrderItem
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for the Order and OrderItem data models.
 */
class OrderModelTest {

    @Test
    fun defaultOrderStatus_isPending() {
        val order = Order()
        assertEquals("pending", order.status)
    }

    @Test
    fun defaultOrder_hasEmptyItemsList() {
        val order = Order()
        assertTrue(order.items.isEmpty())
        assertEquals(0.0, order.totalPrice, 0.001)
    }

    @Test
    fun orderItemLineTotal_calculatesCorrectly() {
        val item = OrderItem(
            dishId = "d1",
            dishName = "Pasta",
            price = 12.50,
            quantity = 3
        )
        val lineTotal = item.price * item.quantity
        assertEquals(37.50, lineTotal, 0.001)
    }

    @Test
    fun orderTotal_fromMultipleItems() {
        val items = listOf(
            OrderItem(dishId = "d1", dishName = "Pasta", price = 10.0, quantity = 2),
            OrderItem(dishId = "d2", dishName = "Pizza", price = 15.0, quantity = 1),
            OrderItem(dishId = "d3", dishName = "Salad", price = 8.50, quantity = 3)
        )
        val total = items.sumOf { it.price * it.quantity }
        // 20.0 + 15.0 + 25.5 = 60.5
        assertEquals(60.50, total, 0.001)
    }
}
