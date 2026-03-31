package com.project.homeeats.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.project.homeeats.data.model.Dish
import com.project.homeeats.data.model.Order
import com.project.homeeats.data.model.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * All Firestore CRUD in one place.
 *
 * Firestore structure:
 *   /users/{uid}
 *   /dishes/{dishId}
 *   /orders/{orderId}
 */
class FirestoreService {

    private val db = FirebaseFirestore.getInstance()

    // Collections
    private val usersCol  = db.collection("users")
    private val dishesCol = db.collection("dishes")
    private val ordersCol = db.collection("orders")

    // User
    suspend fun createUser(user: User) {
        usersCol.document(user.uid).set(user).await()
    }

    suspend fun getUser(uid: String): User? {
        val snap = usersCol.document(uid).get().await()
        return snap.toObject(User::class.java)
    }

    suspend fun updateUserRole(uid: String, role: String) {
        usersCol.document(uid).update("role", role).await()
    }

    // Dishes

    /** Real-time stream of ALL dishes (HomeScreen feed) */
    fun getAllDishesFlow(): Flow<List<Dish>> = callbackFlow {
        val listener = dishesCol.addSnapshotListener { snap, error ->
            if (error != null) { close(error); return@addSnapshotListener }
            val dishes = snap?.documents
                ?.mapNotNull { it.toObject(Dish::class.java)?.copy(id = it.id) }
                ?: emptyList()
            trySend(dishes)
        }
        awaitClose { listener.remove() }
    }

    /** Real-time stream of dishes belonging to one chef */
    fun getChefDishesFlow(chefId: String): Flow<List<Dish>> = callbackFlow {
        val listener = dishesCol
            .whereEqualTo("chefId", chefId)
            .addSnapshotListener { snap, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                val dishes = snap?.documents
                    ?.mapNotNull { it.toObject(Dish::class.java)?.copy(id = it.id) }
                    ?: emptyList()
                trySend(dishes)
            }
        awaitClose { listener.remove() }
    }

    suspend fun addDish(dish: Dish): String {
        val ref = dishesCol.add(dish).await()
        return ref.id
    }

    suspend fun updateDish(dish: Dish) {
        dishesCol.document(dish.id).set(dish).await()
    }

    suspend fun deleteDish(dishId: String) {
        dishesCol.document(dishId).delete().await()
    }

    // Orders
    suspend fun placeOrder(order: Order): String {
        val ref = ordersCol.add(order).await()
        return ref.id
    }

    /** Real-time stream of a customer's orders */
    fun getOrdersForCustomer(customerId: String): Flow<List<Order>> = callbackFlow {
        val listener = ordersCol
            .whereEqualTo("customerId", customerId)
            // No orderBy — avoids needing a Firestore composite index
            .addSnapshotListener { snap, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                val orders = snap?.documents
                    ?.mapNotNull { it.toObject(Order::class.java)?.copy(id = it.id) }
                    ?: emptyList()
                // Sort client-side by newest first instead
                trySend(orders.sortedByDescending { it.createdAt })
            }
        awaitClose { listener.remove() }
    }

    suspend fun updateOrderStatus(orderId: String, status: String) {
        ordersCol.document(orderId).update("status", status).await()
    }
}