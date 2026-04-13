package com.project.homeeats.pages.theme.chef

import com.project.homeeats.data.model.Dish

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.homeeats.pages.theme.Charcoal
import com.project.homeeats.pages.theme.Coral
import com.project.homeeats.pages.theme.Gray
import com.project.homeeats.pages.theme.Peach
import com.project.homeeats.pages.theme.WarmOffWhite
import coil.compose.AsyncImage
import com.project.homeeats.utils.ImageUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDishScreen(
    dishToEdit: Dish? = null,
    onBack: () -> Unit = {},
    onSave: (name: String, description: String, price: String, imageUrl: String) -> Unit = { _, _, _, _ -> }
) {
    var dishName by remember(dishToEdit) { mutableStateOf(dishToEdit?.name ?: "") }
    var description by remember(dishToEdit) { mutableStateOf(dishToEdit?.description ?: "") }
    var price by remember(dishToEdit) { mutableStateOf(if (dishToEdit?.price != null && dishToEdit.price > 0) dishToEdit.price.toString() else "") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var base64Image by remember { mutableStateOf("") }
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            val encoded = ImageUtils.uriToBase64(context, it)
            if (encoded != null) base64Image = encoded
        }
    }

    Scaffold(
        containerColor = WarmOffWhite,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (dishToEdit != null) "Edit Dish" else "Add New Dish",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Charcoal
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Charcoal
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = WarmOffWhite
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // Upload Photo Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .border(
                        width = 1.5.dp,
                        color = Peach,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(
                        color = Color(0xFFFDE8E0),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = "Selected Dish Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else if (dishToEdit?.imageUrl?.isNotEmpty() == true) {
                    val imageModel = remember(dishToEdit.imageUrl) { com.project.homeeats.utils.ImageUtils.base64ToByteArray(dishToEdit.imageUrl) ?: dishToEdit.imageUrl }
                    AsyncImage(
                        model = imageModel,
                        contentDescription = "Dish Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_camera),
                            contentDescription = "Upload",
                            tint = Coral,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Tap to Upload Photo", color = Charcoal, fontSize = 14.sp)
                    }
                }
            }

            // Dish Name
            Column {
                Text(
                    text = "Dish Name",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Charcoal,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                OutlinedTextField(
                    value = dishName,
                    onValueChange = { dishName = it },
                    placeholder = { Text("e.g. Butter Chicken", color = Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedBorderColor = Coral,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    singleLine = true
                )
            }

            // Description
            Column {
                Text(
                    text = "Description",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Charcoal,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Describe your dish...", color = Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedBorderColor = Coral,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    maxLines = 5
                )
            }

            // Price
            Column {
                Text(
                    text = "Price (\$)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Charcoal,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    placeholder = { Text("0.00", color = Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedBorderColor = Coral,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Save Button
            Button(
                onClick = { onSave(dishName, description, price, base64Image) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Coral)
            ) {
                Text(
                    text = "Save Dish",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}