package com.project.homeeats.pages.theme.chef

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.homeeats.data.model.Dish
import com.project.homeeats.pages.theme.Charcoal
import com.project.homeeats.pages.theme.Coral
import com.project.homeeats.pages.theme.Gray
import com.project.homeeats.pages.theme.WarmOffWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChefHomeScreen(
    dishes: List<Dish> = emptyList(),         // ← from DishViewModel.chefDishes
    onAddDish: () -> Unit = {},
    onEditDish: (Dish) -> Unit = {},
    onDeleteDish: (Dish) -> Unit = {},
    onBack: () -> Unit = {}
) {
    Scaffold(
        containerColor = WarmOffWhite,
        topBar = {
            TopAppBar(
                title = { Text("My Dishes", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Charcoal) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Charcoal)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmOffWhite)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddDish, containerColor = Coral, contentColor = Color.White, shape = CircleShape) {
                Icon(Icons.Default.Add, contentDescription = "Add Dish")
            }
        }
    ) { innerPadding ->
        if (dishes.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(innerPadding), Alignment.Center) {
                Text("No dishes yet. Tap + to add your first dish!", color = Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(dishes, key = { it.id }) { dish ->
                    DishCard(
                        dish     = dish,
                        onEdit   = { onEditDish(dish) },
                        onDelete = { onDeleteDish(dish) }
                    )
                }
            }
        }
    }
}

@Composable
fun DishCard(dish: Dish, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(100.dp).clip(RoundedCornerShape(15.dp)).background(Color(0xFFEEEEEE))) {
                if (dish.imageUrl.isNotEmpty()) {
                    val imageModel = remember(dish.imageUrl) { com.project.homeeats.utils.ImageUtils.base64ToByteArray(dish.imageUrl) ?: dish.imageUrl }
                    coil.compose.AsyncImage(
                        model = imageModel,
                        contentDescription = dish.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else if (dish.imageRes != 0) {
                    Image(
                        painter = painterResource(id = dish.imageRes),
                        contentDescription = dish.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(Modifier.fillMaxSize(), Alignment.Center) { Text("🍽", fontSize = 32.sp) }
                }
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.fillMaxWidth()) {
                Text(dish.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Charcoal)
                Text("$${"%.2f".format(dish.price)}", fontSize = 14.sp, color = Gray, modifier = Modifier.padding(top = 2.dp))
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    TextButton(onClick = onEdit, contentPadding = PaddingValues(0.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Coral, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Edit", color = Coral, fontSize = 14.sp)
                    }
                    TextButton(onClick = onDelete, contentPadding = PaddingValues(0.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Coral, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Delete", color = Coral, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}