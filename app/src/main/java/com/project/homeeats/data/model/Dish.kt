package com.project.homeeats.data.model

data class Dish(
    val id: String = "",
    val chefId: String = "",
    val chefName: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val imageRes: Int = 0          // local drawable fallback (for existing UI)
)