package com.project.homeeats.pages.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.homeeats.R
import java.util.Calendar

/**
 * Returns a greeting message based on the current device time.
 * Morning: 5AM–11AM
 * Afternoon: 12PM–5PM
 * Evening: All other hours
 */
fun getGreeting(): String {
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

    return when (currentHour) {
        in 5..11 -> "Good morning 🌅"
        in 12..17 -> "Good afternoon ☀"
        else -> "Good evening 🌙"
    }
}

/**
 * HomeScreen
 *
 * Displays:
 * - Dynamic greeting
 * - Search bar
 * - Food list
 * - Bottom navigation
 */
@Composable
fun HomeScreen() {

    Scaffold(
        bottomBar = { BottomNavigationBar() },
        containerColor = Color(0xFFF4EDEA)
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {

            // Header Section
            item {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = getGreeting(),
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Text(
                    text = "What would you like to eat?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Search dishes...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Food Items
            item {
                FoodCard(
                    title = "Beef Rendang",
                    chef = "Chef Arif",
                    price = "$14.99",
                    imageRes = R.drawable.beef_rendang
                )
            }

            item {
                FoodCard(
                    title = "Strawberry Bombom",
                    chef = "Chef Ana",
                    price = "$6.99",
                    imageRes = R.drawable.bombom_chocolates_with_strawberry
                )
            }

            item {
                FoodCard(
                    title = "Colorful Gelatin Dessert",
                    chef = "Chef Maria",
                    price = "$5.99",
                    imageRes = R.drawable.colorful_gelatin_dessert_with_cream_
                )
            }

            item {
                FoodCard(
                    title = "Crispy Mozzarella",
                    chef = "Chef Luca",
                    price = "$8.99",
                    imageRes = R.drawable.crispy_mozzarella
                )
            }

            item {
                FoodCard(
                    title = "Dal Makhani",
                    chef = "Chef Raj",
                    price = "$11.99",
                    imageRes = R.drawable.dal_makhani
                )
            }

            item {
                FoodCard(
                    title = "Golden Martabak",
                    chef = "Chef Putri",
                    price = "$9.49",
                    imageRes = R.drawable.golden_martabak
                )
            }

            item {
                FoodCard(
                    title = "Paneer Butter Masala",
                    chef = "Chef Singh",
                    price = "$12.49",
                    imageRes = R.drawable.paneer_butter_masala
                )
            }

            item {
                FoodCard(
                    title = "Pepperoni Pizza",
                    chef = "Chef Marco",
                    price = "$13.99",
                    imageRes = R.drawable.pepperoni_pizza
                )
            }
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

/**
 * Reusable Food Card component
 */
@Composable
fun FoodCard(
    title: String,
    chef: String,
    price: String,
    imageRes: Int
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold)
                Text("by $chef", color = Color.Gray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(price, color = Color(0xFFE86A6A), fontWeight = FontWeight.Bold)
            }

            FloatingActionButton(
                onClick = {},
                containerColor = Color(0xFFE86A6A),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Item")
            }
        }
    }
}

/**
 * Bottom Navigation Bar
 */
@Composable
fun BottomNavigationBar() {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
            label = { Text("Cart") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Receipt, contentDescription = "Orders") },
            label = { Text("Orders") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}