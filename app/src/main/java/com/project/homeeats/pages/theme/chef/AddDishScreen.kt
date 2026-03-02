//package com.example.homeeats.pages.theme.chef
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.homeeats.pages.theme.Charcoal
//import com.example.homeeats.pages.theme.Coral
//import com.example.homeeats.pages.theme.Gray
//import com.example.homeeats.pages.theme.Peach
//import com.example.homeeats.pages.theme.WarmOffWhite
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AddDishScreen(
//    onBack: () -> Unit = {},
//    onSave: (name: String, description: String, price: String) -> Unit = { _, _, _ -> }
//) {
//    var dishName by remember { mutableStateOf("") }
//    var description by remember { mutableStateOf("") }
//    var price by remember { mutableStateOf("") }
//
//    Scaffold(
//        containerColor = WarmOffWhite,
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = "Add New Dish",
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 20.sp,
//                        color = Charcoal
//                    )
//                },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(
//                            imageVector = Icons.Default.ArrowBack,
//                            contentDescription = "Back",
//                            tint = Charcoal
//                        )
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = WarmOffWhite
//                )
//            )
//        }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .padding(horizontal = 16.dp)
//                .verticalScroll(rememberScrollState()),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            Spacer(modifier = Modifier.height(4.dp))
//
//            // Upload Photo Box
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(140.dp)
//                    .border(
//                        width = 1.5.dp,
//                        color = Peach,
//                        shape = RoundedCornerShape(12.dp)
//                    )
//                    .background(
//                        color = Color(0xFFFDE8E0),
//                        shape = RoundedCornerShape(12.dp)
//                    ),
//                contentAlignment = Alignment.Center
//            ) {
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    Icon(
//                        painter = painterResource(id = android.R.drawable.ic_menu_upload),
//                        contentDescription = "Upload",
//                        tint = Gray,
//                        modifier = Modifier.size(32.dp)
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(text = "Upload Photo", color = Gray, fontSize = 14.sp)
//                }
//            }
//
//            // Dish Name
//            Column {
//                Text(
//                    text = "Dish Name",
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Medium,
//                    color = Charcoal,
//                    modifier = Modifier.padding(bottom = 6.dp)
//                )
//                OutlinedTextField(
//                    value = dishName,
//                    onValueChange = { dishName = it },
//                    placeholder = { Text("e.g. Butter Chicken", color = Gray) },
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(10.dp),
//                    colors = OutlinedTextFieldDefaults.colors(
//                        unfocusedBorderColor = Color(0xFFE0E0E0),
//                        focusedBorderColor = Coral,
//                        unfocusedContainerColor = Color.White,
//                        focusedContainerColor = Color.White
//                    ),
//                    singleLine = true
//                )
//            }
//
//            // Description
//            Column {
//                Text(
//                    text = "Description",
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Medium,
//                    color = Charcoal,
//                    modifier = Modifier.padding(bottom = 6.dp)
//                )
//                OutlinedTextField(
//                    value = description,
//                    onValueChange = { description = it },
//                    placeholder = { Text("Describe your dish...", color = Gray) },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(120.dp),
//                    shape = RoundedCornerShape(10.dp),
//                    colors = OutlinedTextFieldDefaults.colors(
//                        unfocusedBorderColor = Color(0xFFE0E0E0),
//                        focusedBorderColor = Coral,
//                        unfocusedContainerColor = Color.White,
//                        focusedContainerColor = Color.White
//                    ),
//                    maxLines = 5
//                )
//            }
//
//            // Price
//            Column {
//                Text(
//                    text = "Price (\$)",
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Medium,
//                    color = Charcoal,
//                    modifier = Modifier.padding(bottom = 6.dp)
//                )
//                OutlinedTextField(
//                    value = price,
//                    onValueChange = { price = it },
//                    placeholder = { Text("0.00", color = Gray) },
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(10.dp),
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
//                    colors = OutlinedTextFieldDefaults.colors(
//                        unfocusedBorderColor = Color(0xFFE0E0E0),
//                        focusedBorderColor = Coral,
//                        unfocusedContainerColor = Color.White,
//                        focusedContainerColor = Color.White
//                    ),
//                    singleLine = true
//                )
//            }
//
//            Spacer(modifier = Modifier.height(4.dp))
//
//            // Save Button
//            Button(
//                onClick = { onSave(dishName, description, price) },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(52.dp),
//                shape = RoundedCornerShape(12.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = Coral)
//            ) {
//                Text(
//                    text = "Save Dish",
//                    color = Color.White,
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.SemiBold
//                )
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//        }
//    }
//}