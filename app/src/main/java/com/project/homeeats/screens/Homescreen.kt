package com.project.homeeats.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.homeeats.data.model.Dish
import com.project.homeeats.pages.theme.Charcoal
import com.project.homeeats.pages.theme.Coral
import com.project.homeeats.pages.theme.Gray
import com.project.homeeats.pages.theme.WarmOffWhite
import com.project.homeeats.viewmodel.CartViewModel
import com.project.homeeats.viewmodel.DishViewModel
import java.util.Calendar

fun getGreeting(): String = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
    in 5..11  -> "Good morning 🌅"
    in 12..17 -> "Good afternoon ☀"
    else      -> "Good evening 🌙"
}

@Composable
fun HomeScreen(
    dishViewModel: DishViewModel,
    cartViewModel: CartViewModel,
    selectedTab: Int = 0,
    onTabChange: (Int) -> Unit = {}
) {
    val dishes by dishViewModel.allDishes.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    var selectedDishForDetails by remember { mutableStateOf<Dish?>(null) }

    val filteredDishes = remember(dishes, searchQuery) {
        if (searchQuery.isBlank()) dishes
        else dishes.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.chefName.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(selectedTab = selectedTab, onTabChange = onTabChange) },
        containerColor = WarmOffWhite
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(Modifier.height(16.dp))
                Text(getGreeting(), fontSize = 14.sp, color = Gray)
                Text("What would you like to eat?", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Charcoal)
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search dishes...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(Modifier.height(20.dp))
            }

            if (dishes.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(32.dp), Alignment.Center) {
                        CircularProgressIndicator(color = Coral)
                    }
                }
            } else {
                items(filteredDishes) { dish ->
                    DishFeedCard(
                        dish = dish, 
                        onAdd = { cartViewModel.addItem(dish) },
                        onClick = { selectedDishForDetails = dish }
                    )
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }

        if (selectedDishForDetails != null) {
            DishDetailsModal(
                dish = selectedDishForDetails!!,
                onDismiss = { selectedDishForDetails = null },
                onAddToCart = { cartViewModel.addItem(selectedDishForDetails!!) }
            )
        }
    }
}

@Composable
fun DishFeedCard(dish: Dish, onAdd: () -> Unit, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            if (dish.imageUrl.isNotEmpty()) {
                val imageModel = remember(dish.imageUrl) { com.project.homeeats.utils.ImageUtils.base64ToByteArray(dish.imageUrl) ?: dish.imageUrl }
                coil.compose.AsyncImage(
                    model = imageModel,
                    contentDescription = dish.name,
                    modifier = Modifier.size(84.dp).clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            } else if (dish.imageRes != 0) {
                Image(
                    painter = painterResource(id = dish.imageRes),
                    contentDescription = dish.name,
                    modifier = Modifier.size(84.dp).clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(Modifier.size(90.dp).clip(RoundedCornerShape(12.dp)), Alignment.Center) {
                    Text("🍽", fontSize = 32.sp)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(dish.name, fontWeight = FontWeight.Bold, color = Charcoal)
                Text("by ${dish.chefName}", color = Gray, fontSize = 12.sp)
                Spacer(Modifier.height(6.dp))
                Text("$${"%.2f".format(dish.price)}", color = Coral, fontWeight = FontWeight.Bold)
            }
            FloatingActionButton(onClick = onAdd, containerColor = Coral, modifier = Modifier.size(40.dp)) {
                Icon(Icons.Default.Add, contentDescription = "Add to cart", tint = Color.White)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedTab: Int, onTabChange: (Int) -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 4.dp
    ) {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabChange(0) },
            icon = {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_today),
                    contentDescription = "Home",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Coral,
                selectedTextColor = Coral,
                unselectedIconColor = Gray,
                unselectedTextColor = Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabChange(1) },
            icon = {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_add),
                    contentDescription = "Cart",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text("Cart") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Coral,
                selectedTextColor = Coral,
                unselectedIconColor = Gray,
                unselectedTextColor = Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onTabChange(2) },
            icon = {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_agenda),
                    contentDescription = "Orders",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text("Orders") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Coral,
                selectedTextColor = Coral,
                unselectedIconColor = Gray,
                unselectedTextColor = Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = selectedTab == 3,
            onClick = { onTabChange(3) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text("Profile") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Coral,
                selectedTextColor = Coral,
                unselectedIconColor = Gray,
                unselectedTextColor = Gray,
                indicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun DishDetailsModal(dish: Dish, onDismiss: () -> Unit, onAddToCart: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                // Image Header
                Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                    if (dish.imageUrl.isNotEmpty()) {
                        val imageModel = remember(dish.imageUrl) { com.project.homeeats.utils.ImageUtils.base64ToByteArray(dish.imageUrl) ?: dish.imageUrl }
                        coil.compose.AsyncImage(
                            model = imageModel,
                            contentDescription = dish.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else if (dish.imageRes != 0) {
                        Image(
                            painter = painterResource(id = dish.imageRes),
                            contentDescription = dish.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(Modifier.fillMaxSize().background(Color(0xFFEEEEEE)), Alignment.Center) {
                            Text("🍽", fontSize = 48.sp)
                        }
                    }
                    
                    // X Button
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(32.dp)
                            .background(Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(50))
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }
                
                // Content
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(dish.name, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Charcoal)
                    Text("by ${dish.chefName}", color = Gray, fontSize = 14.sp)
                    
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = dish.description.ifBlank { "No description available for this dish." },
                        color = Charcoal,
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    )
                    
                    Spacer(Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$${"%.2f".format(dish.price)}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Coral
                        )
                        
                        Button(
                            onClick = { 
                                onAddToCart()
                                onDismiss() 
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Coral),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                        ) {
                            Text("Add to Cart", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}