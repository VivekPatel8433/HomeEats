package com.project.homeeats.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.BorderStroke
import com.project.homeeats.pages.theme.Charcoal
import com.project.homeeats.pages.theme.Coral
import com.project.homeeats.pages.theme.Gray
import com.project.homeeats.pages.theme.WarmOffWhite
import com.project.homeeats.viewmodel.AuthViewModel
import com.project.homeeats.viewmodel.CartItem
import com.project.homeeats.viewmodel.CartViewModel
import com.project.homeeats.viewmodel.OrderUiState
import com.project.homeeats.viewmodel.OrderViewModel

@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    orderViewModel: OrderViewModel,
    authViewModel: AuthViewModel,
    selectedTab: Int = 1,
    onTabChange: (Int) -> Unit = {},
    onOrderPlaced: () -> Unit = {}
) {
    val cartItems = cartViewModel.items
    val orderState by orderViewModel.orderState.collectAsStateWithLifecycle()
    val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()
    val accent = Coral

    // Navigate to Orders when order is placed successfully
    LaunchedEffect(orderState) {
        if (orderState is OrderUiState.Success) {
            cartViewModel.clearCart()
            orderViewModel.resetOrderState()
            onOrderPlaced()
        }
    }

    Scaffold(
        containerColor = WarmOffWhite,
        bottomBar = { BottomNavigationBar(selectedTab = selectedTab, onTabChange = onTabChange) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WarmOffWhite)
                .padding(padding)
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            Text("Your Cart", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Charcoal)
            Spacer(Modifier.height(12.dp))

            if (cartItems.isEmpty()) {
                Box(Modifier.fillMaxSize().padding(bottom = 70.dp), Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Outlined.ShoppingBag, contentDescription = "Empty cart",
                            tint = Gray, modifier = Modifier.size(54.dp))
                        Spacer(Modifier.height(10.dp))
                        Text("Your cart is empty", color = Gray, fontSize = 14.sp)
                    }
                }
                return@Scaffold
            }

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartItems) { item ->
                    CartItemCard(
                        cartItem    = item,
                        accent      = accent,
                        onAdd       = { cartViewModel.addItem(item.dish) },
                        onRemoveOne = { cartViewModel.removeOne(item.dish.id) }
                    )
                }

                item {
                    Spacer(Modifier.height(10.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total: $${"%.2f".format(cartViewModel.totalPrice())}",
                            fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        TextButton(onClick = { cartViewModel.clearCart() }) { Text("Clear") }
                    }
                    Spacer(Modifier.height(12.dp))

                    // Place Order button
                    val isLoading = orderState is OrderUiState.Loading
                    Button(
                        onClick = {
                            val uid = currentUser?.uid ?: ""
                            if (uid.isNotEmpty()) {
                                orderViewModel.placeOrder(uid, cartViewModel.toOrderItems())
                            }
                        },
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = accent)
                    ) {
                        if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp))
                        else Text("Place Order", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    // Error display
                    if (orderState is OrderUiState.Error) {
                        Text((orderState as OrderUiState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp))
                    }
                    Spacer(Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
private fun CartItemCard(cartItem: CartItem, accent: Color, onAdd: () -> Unit, onRemoveOne: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), 
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            if (cartItem.dish.imageUrl.isNotEmpty()) {
                val imageModel = remember(cartItem.dish.imageUrl) { com.project.homeeats.utils.ImageUtils.base64ToByteArray(cartItem.dish.imageUrl) ?: cartItem.dish.imageUrl }
                coil.compose.AsyncImage(
                    model = imageModel,
                    contentDescription = cartItem.dish.name,
                    modifier = Modifier.size(84.dp).clip(RoundedCornerShape(12.dp)),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            } else if (cartItem.dish.imageRes != 0) {
                Image(
                    painter = painterResource(id = cartItem.dish.imageRes),
                    contentDescription = cartItem.dish.name,
                    modifier = Modifier.size(84.dp).clip(RoundedCornerShape(12.dp)),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            } else {
                Box(Modifier.size(84.dp).clip(RoundedCornerShape(12.dp)), Alignment.Center) {
                    Text("🍽", fontSize = 28.sp)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(cartItem.dish.name, fontWeight = FontWeight.Bold, color = Charcoal)
                Text("by ${cartItem.dish.chefName}", color = Gray, fontSize = 12.sp)
                Spacer(Modifier.height(6.dp))
                Text("$${"%.2f".format(cartItem.dish.price)}", color = accent, fontWeight = FontWeight.Bold)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = onAdd, modifier = Modifier.size(34.dp), contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(999.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accent, contentColor = Color.White)) {
                    Text("+", fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(6.dp))
                Text(cartItem.quantity.toString(), fontWeight = FontWeight.Bold, color = Charcoal)
                Spacer(Modifier.height(6.dp))
                OutlinedButton(onClick = onRemoveOne, modifier = Modifier.size(34.dp),
                    contentPadding = PaddingValues(0.dp), shape = RoundedCornerShape(999.dp),
                    border = BorderStroke(1.dp, Gray),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Charcoal)) {
                    Text("-", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}