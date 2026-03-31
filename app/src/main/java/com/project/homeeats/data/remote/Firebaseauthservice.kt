package com.project.homeeats.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

/**
 * Thin wrapper around Firebase Authentication.
 *
 * Required dependencies in build.gradle (app):
 *   implementation("com.google.firebase:firebase-auth-ktx")
 *   implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services")
 */
class FirebaseAuthService {
    private val auth: FirebaseAuth = Firebase.auth

    val currentUser: FirebaseUser? get() = auth.currentUser
    val currentUserId: String?     get() = auth.currentUser?.uid

    // Sign Up
    suspend fun register(email: String, password: String): FirebaseUser {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        return result.user ?: error("Registration failed: user is null")
    }

    // Sign In
    suspend fun login(email: String, password: String): FirebaseUser {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user ?: error("Login failed: user is null")
    }

    // Sign Out
    fun logout() {
        auth.signOut()
    }

    // Password Reset
    suspend fun sendPasswordReset(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }
}