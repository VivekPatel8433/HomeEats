package com.project.homeeats

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.homeeats.pages.theme.chef.AddDishScreen
import com.project.homeeats.pages.theme.customer.ProfileScreen
import com.project.homeeats.pages.theme.auth.LoginScreen
import com.project.homeeats.pages.theme.auth.RegisterScreen
import com.project.homeeats.pages.theme.chef.ChefHomeScreen
import com.project.homeeats.screens.CartScreen
import com.project.homeeats.screens.HomeScreen
import com.project.homeeats.screens.IntroductionScreen
import com.project.homeeats.screens.OrdersScreen
import com.project.homeeats.viewmodel.AuthUiState
import com.project.homeeats.viewmodel.AuthViewModel
import com.project.homeeats.viewmodel.CartViewModel
import com.project.homeeats.viewmodel.DishViewModel
import com.project.homeeats.viewmodel.OrderViewModel

private enum class Screen {
    Intro, Login, Register,
    Home, Cart, Orders, Profile,
    ChefHome, AddDish
}

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private val dishViewModel: DishViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()
    private val orderViewModel: OrderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val authState   by authViewModel.uiState.collectAsStateWithLifecycle()
            // currentUser persists even after resetState() — safe to use for uid/name
            val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()

            var currentScreen by remember {
                mutableStateOf(if (authViewModel.isLoggedIn) Screen.Home else Screen.Intro)
            }
            var dishToEdit by remember { mutableStateOf<com.project.homeeats.data.model.Dish?>(null) }

            // React to auth transitions
            LaunchedEffect(authState) {
                when (authState) {
                    is AuthUiState.Success -> {
                        currentScreen = Screen.Home
                        authViewModel.resetState()
                    }
                    is AuthUiState.Error -> {
                        Toast.makeText(
                            this@MainActivity,
                            (authState as AuthUiState.Error).message,
                            Toast.LENGTH_SHORT
                        ).show()
                        authViewModel.resetState()
                    }
                    else -> {}
                }
            }

            LaunchedEffect(currentScreen) {
                if (currentScreen == Screen.Home) dishViewModel.loadAllDishes()
            }

            when (currentScreen) {

                Screen.Intro -> IntroductionScreen(
                    onFinish = { currentScreen = Screen.Login }
                )

                Screen.Login -> LoginScreen(
                    onLoginClick    = { email, password -> authViewModel.login(email, password) },
                    onRegisterClick = { currentScreen = Screen.Register }
                )

                Screen.Register -> RegisterScreen(
                    onRegisterClick = { name, email, phone, password, isChef ->
                        authViewModel.register(name, email, phone, password, isChef)
                    },
                    onLoginClick = { currentScreen = Screen.Login }
                )

                Screen.Home -> HomeScreen(
                    dishViewModel = dishViewModel,
                    cartViewModel = cartViewModel,
                    selectedTab   = 0,
                    onTabChange   = { tab ->
                        currentScreen = when (tab) {
                            1 -> Screen.Cart
                            2 -> Screen.Orders
                            3 -> Screen.Profile
                            else -> Screen.Home
                        }
                    }
                )

                Screen.Cart -> CartScreen(
                    cartViewModel  = cartViewModel,
                    orderViewModel = orderViewModel,
                    authViewModel  = authViewModel,
                    selectedTab    = 1,
                    onTabChange    = { tab ->
                        currentScreen = when (tab) {
                            0 -> Screen.Home
                            2 -> Screen.Orders
                            3 -> Screen.Profile
                            else -> Screen.Cart
                        }
                    },
                    onOrderPlaced = { currentScreen = Screen.Orders }
                )

                Screen.Orders -> {
                    val uid = currentUser?.uid ?: ""
                    LaunchedEffect(uid) {
                        if (uid.isNotEmpty()) orderViewModel.loadOrders(uid)
                    }
                    OrdersScreen(
                        orderViewModel = orderViewModel,
                        selectedTab    = 2,
                        onTabChange    = { tab ->
                            currentScreen = when (tab) {
                                0 -> Screen.Home
                                1 -> Screen.Cart
                                3 -> Screen.Profile
                                else -> Screen.Orders
                            }
                        }
                    )
                }

                Screen.Profile -> ProfileScreen(
                    userName = currentUser?.name?.takeIf { it.isNotBlank() } ?: "Unknown User",
                    userRole = currentUser?.role?.replaceFirstChar { it.uppercase() } ?: "Customer",
                    onBecomeChef = {
                        authViewModel.switchToChef()
                        Toast.makeText(this@MainActivity, "You are now a Chef! You can switch to the Chef View.", Toast.LENGTH_SHORT).show()
                    },
                    onNavigateToChef = {
                        currentScreen = Screen.ChefHome
                    },
                    onHelpSupport = { /* TODO */ },
                    onLogOut = {
                        cartViewModel.clearCart()
                        orderViewModel.resetOrderState()
                        authViewModel.logout()
                        currentScreen = Screen.Login
                    },
                    onHome   = { currentScreen = Screen.Home },
                    onCart   = { currentScreen = Screen.Cart },
                    onOrders = { currentScreen = Screen.Orders }
                )

                Screen.ChefHome -> {
                    val chefId = currentUser?.uid ?: ""
                    LaunchedEffect(chefId) {
                        if (chefId.isNotEmpty()) dishViewModel.loadChefDishes(chefId)
                    }
                    val chefDishes by dishViewModel.chefDishes.collectAsStateWithLifecycle()

                    ChefHomeScreen(
                        dishes       = chefDishes,
                        onAddDish    = { 
                            dishToEdit = null
                            currentScreen = Screen.AddDish 
                        },
                        onEditDish   = { dish -> 
                            dishToEdit = dish
                            currentScreen = Screen.AddDish 
                        },
                        onDeleteDish = { dish -> dishViewModel.deleteDish(dish.id) },
                        onBack       = { currentScreen = Screen.Profile }
                    )
                }

                Screen.AddDish -> {
                    AddDishScreen(
                        dishToEdit = dishToEdit,
                        onBack = { currentScreen = Screen.ChefHome },
                        onSave = { name, description, price, imageUrl ->
                            if (dishToEdit == null) {
                                dishViewModel.addDish(
                                    chefId      = currentUser?.uid ?: "",
                                    chefName    = currentUser?.name ?: "",
                                    name        = name,
                                    description = description,
                                    price       = price.toDoubleOrNull() ?: 0.0,
                                    imageUrl    = imageUrl
                                )
                            } else {
                                dishViewModel.updateDish(
                                    dishToEdit!!.copy(
                                        name = name,
                                        description = description,
                                        price = price.toDoubleOrNull() ?: 0.0,
                                        imageUrl = if (imageUrl.isNotEmpty()) imageUrl else dishToEdit!!.imageUrl
                                    )
                                )
                            }
                            currentScreen = Screen.ChefHome
                        }
                    )
                }
            }
        }
    }
}