package com.project.homeeats

import com.project.homeeats.data.model.Dish
import com.project.homeeats.viewmodel.CartViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for CartViewModel — the in-memory shopping cart logic.
 * CartViewModel is purely synchronous (no coroutines), so it can be
 * tested directly without needing coroutine test dispatchers.
 */
class CartViewModelTest {

    private lateinit var cartVM: CartViewModel

    private val pasta = Dish(id = "d1", name = "Pasta", price = 10.0, chefName = "Chef A")
    private val pizza = Dish(id = "d2", name = "Pizza", price = 15.0, chefName = "Chef B")

    @Before
    fun setUp() {
        cartVM = CartViewModel()
    }

    // ── addItem ────────────────────────────────────────────

    @Test
    fun addItem_addsNewDishToCart() {
        cartVM.addItem(pasta)
        assertEquals(1, cartVM.items.size)
        assertEquals("d1", cartVM.items[0].dish.id)
        assertEquals(1, cartVM.items[0].quantity)
    }

    @Test
    fun addItem_incrementsQuantityForSameDish() {
        cartVM.addItem(pasta)
        cartVM.addItem(pasta)
        assertEquals(1, cartVM.items.size)
        assertEquals(2, cartVM.items[0].quantity)
    }

    // ── removeOne ──────────────────────────────────────────

    @Test
    fun removeOne_decreasesQuantity() {
        cartVM.addItem(pasta)
        cartVM.addItem(pasta) // qty = 2
        cartVM.removeOne("d1")
        assertEquals(1, cartVM.items.size)
        assertEquals(1, cartVM.items[0].quantity)
    }

    @Test
    fun removeOne_removesItemWhenQuantityIsOne() {
        cartVM.addItem(pasta)
        cartVM.removeOne("d1")
        assertTrue(cartVM.items.isEmpty())
    }

    // ── clearCart ───────────────────────────────────────────

    @Test
    fun clearCart_emptiesAllItems() {
        cartVM.addItem(pasta)
        cartVM.addItem(pizza)
        cartVM.clearCart()
        assertTrue(cartVM.items.isEmpty())
    }

    // ── totalPrice ─────────────────────────────────────────

    @Test
    fun totalPrice_calculatesCorrectly() {
        cartVM.addItem(pasta)  // 10.0
        cartVM.addItem(pasta)  // 10.0 × 2 = 20.0
        cartVM.addItem(pizza)  // 15.0
        assertEquals(35.0, cartVM.totalPrice(), 0.001)
    }

    // ── toOrderItems ───────────────────────────────────────

    @Test
    fun toOrderItems_convertsCartToOrderItems() {
        cartVM.addItem(pasta)
        cartVM.addItem(pasta) // qty = 2
        cartVM.addItem(pizza) // qty = 1

        val orderItems = cartVM.toOrderItems()
        assertEquals(2, orderItems.size)

        val pastaItem = orderItems.first { it.dishId == "d1" }
        assertEquals("Pasta", pastaItem.dishName)
        assertEquals(10.0, pastaItem.price, 0.001)
        assertEquals(2, pastaItem.quantity)

        val pizzaItem = orderItems.first { it.dishId == "d2" }
        assertEquals("Pizza", pizzaItem.dishName)
        assertEquals(15.0, pizzaItem.price, 0.001)
        assertEquals(1, pizzaItem.quantity)
    }
}
