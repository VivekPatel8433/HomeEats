package com.project.homeeats

import com.project.homeeats.data.model.Dish
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for the Dish data model.
 */
class DishModelTest {

    @Test
    fun defaultDish_hasZeroPrice() {
        val dish = Dish()
        assertEquals(0.0, dish.price, 0.001)
    }

    @Test
    fun dishFields_areSetCorrectly() {
        val dish = Dish(
            id = "d1",
            chefId = "c1",
            chefName = "Chef Bob",
            name = "Butter Chicken",
            description = "Creamy and delicious",
            price = 14.99,
            imageUrl = "data:image/jpeg;base64,abc",
            imageRes = 0
        )
        assertEquals("d1", dish.id)
        assertEquals("c1", dish.chefId)
        assertEquals("Chef Bob", dish.chefName)
        assertEquals("Butter Chicken", dish.name)
        assertEquals("Creamy and delicious", dish.description)
        assertEquals(14.99, dish.price, 0.001)
        assertEquals("data:image/jpeg;base64,abc", dish.imageUrl)
    }

    @Test
    fun dishCopy_preservesUnchangedFields() {
        val original = Dish(id = "d1", name = "Pasta", price = 10.0, chefName = "Chef A")
        val updated = original.copy(price = 18.50)
        assertEquals("d1", updated.id)
        assertEquals("Pasta", updated.name)
        assertEquals("Chef A", updated.chefName)
        assertEquals(18.50, updated.price, 0.001)
    }

    @Test
    fun defaultDish_hasEmptyStrings() {
        val dish = Dish()
        assertEquals("", dish.id)
        assertEquals("", dish.chefId)
        assertEquals("", dish.chefName)
        assertEquals("", dish.name)
        assertEquals("", dish.description)
        assertEquals("", dish.imageUrl)
        assertEquals(0, dish.imageRes)
    }
}
