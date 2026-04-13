package com.project.homeeats.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.homeeats.data.model.Order
import com.project.homeeats.pages.theme.Charcoal
import com.project.homeeats.pages.theme.Coral
import com.project.homeeats.pages.theme.Gray
import com.project.homeeats.pages.theme.WarmOffWhite
import com.project.homeeats.viewmodel.OrderUiState
import com.project.homeeats.viewmodel.OrderViewModel

private val Accent = Coral
private val Background = WarmOffWhite

@Composable
fun OrdersScreen(
    orderViewModel: OrderViewModel,
    selectedTab: Int = 2,
    onTabChange: (Int) -> Unit = {}
) {
    val orders by orderViewModel.orders.collectAsStateWithLifecycle()
    val uiState by orderViewModel.orderState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Background,
        bottomBar = { BottomNavigationBar(selectedTab = selectedTab, onTabChange = onTabChange) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(padding)
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            Text("My Orders", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Charcoal)
            Spacer(Modifier.height(12.dp))

            when (uiState) {
                is OrderUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        CircularProgressIndicator(color = Accent)
                    }
                }
                is OrderUiState.Error -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Text(
                            text = (uiState as OrderUiState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                else -> {
                    if (orders.isEmpty()) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .padding(bottom = 70.dp),
                            Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.Receipt,
                                    contentDescription = "No orders",
                                    tint = Gray,
                                    modifier = Modifier.size(54.dp)
                                )
                                Spacer(Modifier.height(10.dp))
                                Text("No orders yet", color = Gray, fontSize = 14.sp)
                            }
                        }
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(orders, key = { it.id }) { order ->
                                OrderCard(order)
                            }
                            item { Spacer(Modifier.height(80.dp)) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderCard(order: Order) {
    val statusColor = when (order.status.lowercase()) {
        "delivered" -> Color(0xFF4CAF50)
        "cancelled" -> Color(0xFFE86A6A)
        "preparing"  -> Color(0xFFFF9800)
        else         -> Color(0xFF2196F3)   // pending / in progress
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Header row: order id + status badge
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Order #${order.id.takeLast(6).uppercase()}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Charcoal
                )
                Surface(
                    shape = RoundedCornerShape(50),
                    color = statusColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = order.status.replaceFirstChar { it.uppercase() },
                        color = statusColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))
            Divider(color = Color(0xFFEEEEEE))
            Spacer(Modifier.height(10.dp))

            // Items list
            order.items.forEach { item ->
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 3.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${item.quantity}× ${item.dishName}",
                        fontSize = 14.sp,
                        color = Charcoal,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${"%.2f".format(item.price * item.quantity)}",
                        fontSize = 14.sp,
                        color = Charcoal
                    )
                }
            }

            Spacer(Modifier.height(10.dp))
            Divider(color = Color(0xFFEEEEEE))
            Spacer(Modifier.height(8.dp))

            // Total
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Charcoal)
                Text(
                    text = "$${"%.2f".format(order.items.sumOf { it.price * it.quantity })}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Accent
                )
            }
        }
    }
}