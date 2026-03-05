package com.example.homeeats.pages.theme.chef

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.project.homeeats.pages.theme.Charcoal
import com.project.homeeats.pages.theme.Coral
import com.project.homeeats.pages.theme.Gray
import com.project.homeeats.pages.theme.WarmOffWhite
import com.project.homeeats.R


data class Dish(
    val id: Int,
    val name: String,
    val price: String,
    val imageRes: Int = 0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChefHomeScreen(
    onAddDish: () -> Unit = {},
    onEditDish: (Dish) -> Unit = {},
    onDeleteDish: (Dish) -> Unit = {},
    onBack: () -> Unit = {}
) {
    // Sample data — replace with your real data source
    val dishes = listOf(
        Dish(1, "Pepperoni Pizza", "$12.99", R.drawable.pepperoni_pizza),
        Dish(2, "Crispy Mozzarella", "$9.99", R.drawable.crispy_mozzarella)
    )

    Scaffold(
        containerColor = WarmOffWhite,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Dishes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Charcoal
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Charcoal
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = WarmOffWhite
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddDish,
                containerColor = Coral,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Dish")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(dishes) { dish ->
                DishCard(
                    dish = dish,
                    onEdit = { onEditDish(dish) },
                    onDelete = { onDeleteDish(dish) }
                )
            }
        }
    }
}

@Composable
fun DishCard(
    dish: Dish,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Dish image
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color(0xFFEEEEEE))
            ) {
                if (dish.imageRes != 0) {
                    Image(
                        painter = painterResource(id = dish.imageRes),
                        contentDescription = dish.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = dish.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Charcoal
                )
                Text(
                    text = dish.price,
                    fontSize = 14.sp,
                    color = Gray,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Edit button
                    TextButton(
                        onClick = onEdit,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Coral,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Edit", color = Coral, fontSize = 14.sp)
                    }

                    // Delete button
                    TextButton(
                        onClick = onDelete,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Coral,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Delete", color = Coral, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}