package com.project.homeeats.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.ShoppingBag
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
import androidx.compose.foundation.BorderStroke

@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    selectedTab: Int,
    onTabChange: (Int) -> Unit
) {
    val cartItems = cartViewModel.items
    val pageBg = Color(0xFFF4EDEA)
    val accent = Color(0xFFE86A6A)

    Scaffold(
        containerColor = pageBg,
        bottomBar = { BottomNavigationBar(selectedTab = selectedTab, onTabChange = onTabChange) }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(pageBg)
                .padding(padding)
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {

            Text(
                text = "Your Cart",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // EMPTY STATE
            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 70.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingBag,
                            contentDescription = "Empty cart",
                            tint = Color(0xFF8A8A8A),
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Your cart is empty",
                            color = Color(0xFF8A8A8A),
                            fontSize = 14.sp
                        )
                    }
                }
                return@Scaffold
            }

            // ITEMS LIST
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 70.dp)
            ) {
                items(cartItems) { item ->
                    CartItemCard(
                        cartItem = item,
                        accent = accent,
                        onAdd = { cartViewModel.addItem(item.food) },
                        onRemoveOne = { cartViewModel.removeOne(item.food.id) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    TotalRow(
                        total = cartViewModel.totalPrice(),
                        onClear = { cartViewModel.clearCart() }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
private fun CartItemCard(
    cartItem: CartItem,
    accent: Color,
    onAdd: () -> Unit,
    onRemoveOne: () -> Unit
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
                painter = painterResource(id = cartItem.food.imageRes),
                contentDescription = cartItem.food.title,
                modifier = Modifier
                    .size(84.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(cartItem.food.title, fontWeight = FontWeight.Bold)
                Text("by ${cartItem.food.chef}", color = Color.Gray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "$${"%.2f".format(cartItem.food.price)}",
                    color = accent,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = onAdd,
                    modifier = Modifier.size(34.dp),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(999.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accent, contentColor = Color.White)
                ) {
                    Text("+", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(cartItem.quantity.toString(), fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedButton(
                    onClick = onRemoveOne,
                    modifier = Modifier.size(34.dp),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(999.dp),
                    border = BorderStroke(1.dp, Color(0xFFB8B8B8)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF333333))
                ) {
                    Text("-", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun TotalRow(
    total: Double,
    onClear: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Total: $${"%.2f".format(total)}",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        TextButton(onClick = onClear) { Text("Clear") }
    }
}

@Composable
fun BottomNavigationBar(
    selectedTab: Int,
    onTabChange: (Int) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabChange(0) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabChange(1) },
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
            label = { Text("Cart") }
        )
        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onTabChange(2) },
            icon = { Icon(Icons.Default.Receipt, contentDescription = "Orders") },
            label = { Text("Orders") }
        )
        NavigationBarItem(
            selected = selectedTab == 3,
            onClick = { onTabChange(3) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}