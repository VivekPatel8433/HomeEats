package com.project.homeeats.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.homeeats.data.model.Dish
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

    val filteredDishes = remember(dishes, searchQuery) {
        if (searchQuery.isBlank()) dishes
        else dishes.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.chefName.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(selectedTab = selectedTab, onTabChange = onTabChange) },
        containerColor = Color(0xFFF4EDEA)
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(Modifier.height(16.dp))
                Text(getGreeting(), fontSize = 14.sp, color = Color.Gray)
                Text("What would you like to eat?", fontSize = 22.sp, fontWeight = FontWeight.Bold)
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
                        CircularProgressIndicator(color = Color(0xFFE86A6A))
                    }
                }
            } else {
                items(filteredDishes) { dish ->
                    DishFeedCard(dish = dish, onAdd = { cartViewModel.addItem(dish) })
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun DishFeedCard(dish: Dish, onAdd: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            if (dish.imageRes != 0) {
                Image(
                    painter = painterResource(id = dish.imageRes),
                    contentDescription = dish.name,
                    modifier = Modifier.size(90.dp).clip(RoundedCornerShape(12.dp))
                )
            } else {
                Box(Modifier.size(90.dp).clip(RoundedCornerShape(12.dp)), Alignment.Center) {
                    Text("🍽", fontSize = 32.sp)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(dish.name, fontWeight = FontWeight.Bold)
                Text("by ${dish.chefName}", color = Color.Gray, fontSize = 12.sp)
                Spacer(Modifier.height(6.dp))
                Text("$${"%.2f".format(dish.price)}", color = Color(0xFFE86A6A), fontWeight = FontWeight.Bold)
            }
            FloatingActionButton(onClick = onAdd, containerColor = Color(0xFFE86A6A), modifier = Modifier.size(40.dp)) {
                Icon(Icons.Default.Add, contentDescription = "Add to cart")
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedTab: Int, onTabChange: (Int) -> Unit) {
    NavigationBar {
        listOf("Home" to Icons.Default.Home, "Cart" to Icons.Default.ShoppingCart,
            "Orders" to Icons.Default.Receipt, "Profile" to Icons.Default.Person)
            .forEachIndexed { index, (label, icon) ->
                NavigationBarItem(
                    selected = selectedTab == index,
                    onClick = { onTabChange(index) },
                    icon = { Icon(icon, contentDescription = label) },
                    label = { Text(label) }
                )
            }
    }
}