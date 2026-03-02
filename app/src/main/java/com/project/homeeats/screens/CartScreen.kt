package com.project.homeeats.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Cart") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Your cart items will appear here.", style = MaterialTheme.typography.bodyLarge)
        }
    }
}