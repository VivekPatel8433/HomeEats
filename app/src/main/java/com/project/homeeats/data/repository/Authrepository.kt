package com.project.homeeats.data.repository

import com.google.firebase.auth.FirebaseUser
import com.project.homeeats.data.model.User
import com.project.homeeats.data.remote.FirebaseAuthService
import com.project.homeeats.data.remote.FirestoreService

/**
 * Single source of truth for auth operations.
 * Combines FirebaseAuth + Firestore user-document creation.
 */
class AuthRepository(
    private val authService: FirebaseAuthService = FirebaseAuthService(),
    private val firestoreService: FirestoreService = FirestoreService()
) {
    val currentUserId: String? get() = authService.currentUserId
    val isLoggedIn: Boolean get() = authService.currentUser != null

    // Register
    suspend fun register(
        name: String,
        email: String,
        phone: String,
        password: String,
        isChef: Boolean
    ): Result<FirebaseUser> = runCatching {
        val firebaseUser = authService.register(email, password)
        val user = User(
            uid   = firebaseUser.uid,
            name  = name,
            email = email,
            phone = phone,
            role  = if (isChef) "chef" else "customer"
        )
        firestoreService.createUser(user)
        firebaseUser
    }

    // Login
    suspend fun login(email: String, password: String): Result<FirebaseUser> = runCatching {
        authService.login(email, password)
    }

    // Logout
    fun logout() = authService.logout()

    // Fetch current user profile from Firestore
    suspend fun getCurrentUserProfile(): User? {
        val uid = authService.currentUserId ?: return null
        return firestoreService.getUser(uid)
    }

    // Switch role
    suspend fun switchToChef(): Result<Unit> = runCatching {
        val uid = authService.currentUserId ?: error("Not logged in")
        firestoreService.updateUserRole(uid, "chef")
    }
}