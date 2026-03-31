package com.project.homeeats.data.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val role: String = "customer"   // "customer" | "chef"
)