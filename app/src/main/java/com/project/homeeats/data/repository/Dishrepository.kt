package com.project.homeeats.data.repository

import com.project.homeeats.data.model.Dish
import com.project.homeeats.data.remote.FirestoreService
import kotlinx.coroutines.flow.Flow

/**
 * Dish repository — used by both HomeScreen (all dishes) and ChefHomeScreen (chef's dishes).
 */
class DishRepository(
    private val firestoreService: FirestoreService = FirestoreService()
) {
    /** Live feed of every dish — used on HomeScreen */
    fun getAllDishes(): Flow<List<Dish>> = firestoreService.getAllDishesFlow()

    /** Live feed of dishes for a specific chef — used on ChefHomeScreen */
    fun getChefDishes(chefId: String): Flow<List<Dish>> =
        firestoreService.getChefDishesFlow(chefId)

    /** Called from AddDishScreen */
    suspend fun addDish(
        chefId: String,
        chefName: String,
        name: String,
        description: String,
        price: Double,
        imageUrl: String = ""
    ): Result<String> = runCatching {
        val dish = Dish(
            chefId      = chefId,
            chefName    = chefName,
            name        = name,
            description = description,
            price       = price,
            imageUrl    = imageUrl
        )
        firestoreService.addDish(dish)
    }

    suspend fun updateDish(dish: Dish): Result<Unit> = runCatching {
        firestoreService.updateDish(dish)
    }

    suspend fun deleteDish(dishId: String): Result<Unit> = runCatching {
        firestoreService.deleteDish(dishId)
    }
}