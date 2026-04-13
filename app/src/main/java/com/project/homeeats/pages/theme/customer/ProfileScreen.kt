package com.project.homeeats.pages.theme.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.homeeats.R
import com.project.homeeats.pages.theme.Charcoal
import com.project.homeeats.pages.theme.Coral
import com.project.homeeats.pages.theme.Gray
import com.project.homeeats.pages.theme.Peach
import com.project.homeeats.pages.theme.WarmOffWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userName: String = "John Doe",
    userRole: String = "Customer",
    onBecomeChef: () -> Unit = {},
    onNavigateToChef: () -> Unit = {},
    onHelpSupport: () -> Unit = {},
    onLogOut: () -> Unit = {},
    onHome: () -> Unit = {},
    onCart: () -> Unit = {},
    onOrders: () -> Unit = {}
) {
    Scaffold(
        containerColor = WarmOffWhite,
        bottomBar = {
            BottomNavBar(
                onHome = onHome,
                onCart = onCart,
                onOrders = onOrders
            )
        }
    ) { innerPadding ->
        var showChefModal by remember { mutableStateOf(false) }
        var showTermsModal by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Title
            Text(
                text = "Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Charcoal
            )

            Spacer(modifier = Modifier.height(24.dp))

            // User Info Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF5D5CC)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = Coral,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = userName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Charcoal
                    )
                    Text(
                        text = userRole,
                        fontSize = 14.sp,
                        color = Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Set up business profile banner (Only show for customers)
            if (!userRole.equals("Chef", ignoreCase = true)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFDE8E0))
                        .border(1.dp, Peach, RoundedCornerShape(12.dp))
                        .clickable { showTermsModal = true }
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF5D5CC)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = android.R.drawable.ic_menu_myplaces),
                                contentDescription = "Chef",
                                tint = Coral,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Set up your business profile",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                color = Charcoal
                            )
                            Text(
                                text = "Start selling your homemade dishes",
                                fontSize = 13.sp,
                                color = Gray
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Switch to Chef View
            ProfileMenuItem(
                icon = {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_preferences),
                        contentDescription = "Chef View",
                        tint = Charcoal,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = "Switch to Chef View",
                textColor = Charcoal,
                onClick = {
                    if (userRole.equals("Chef", ignoreCase = true)) {
                        onNavigateToChef()
                    } else {
                        showChefModal = true
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Help & Support
            ProfileMenuItem(
                icon = {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_help),
                        contentDescription = "Help",
                        tint = Charcoal,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = "Help & Support",
                textColor = Charcoal,
                onClick = onHelpSupport
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Log Out
            ProfileMenuItem(
                icon = {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_revert),
                        contentDescription = "Log Out",
                        tint = Coral,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = "Log Out",
                textColor = Coral,
                onClick = onLogOut
            )
        }

        if (showChefModal) {
            AlertDialog(
                onDismissRequest = { showChefModal = false },
                title = { Text("Access Denied", fontWeight = FontWeight.Bold, color = Charcoal) },
                text = { Text("You need to set up a business profile before accessing the Chef View. Please tap the 'Set up your business profile' banner to get started.", color = Charcoal) },
                confirmButton = {
                    TextButton(onClick = { showChefModal = false }) {
                        Text("OK", color = Coral, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = WarmOffWhite
            )
        }

        if (showTermsModal) {
            AlertDialog(
                onDismissRequest = { showTermsModal = false },
                title = { Text("Terms & Conditions", fontWeight = FontWeight.Bold, color = Charcoal) },
                text = { Text("By setting up a business profile, you agree to our terms of service, including maintaining food safety guidelines, adhering to payment fees, and following our community standards as a Chef.", color = Charcoal) },
                confirmButton = {
                    TextButton(onClick = { 
                        showTermsModal = false
                        onBecomeChef()
                    }) {
                        Text("Accept", color = Coral, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showTermsModal = false }) {
                        Text("Decline", color = Gray, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = WarmOffWhite
            )
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: @Composable () -> Unit,
    label: String,
    textColor: Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
        }
    }
}

@Composable
fun BottomNavBar(
    onHome: () -> Unit,
    onCart: () -> Unit,
    onOrders: () -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 4.dp
    ) {
        NavigationBarItem(
            selected = false,
            onClick = onHome,
            icon = {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_today),
                    contentDescription = "Home",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Coral,
                selectedTextColor = Coral,
                unselectedIconColor = Gray,
                unselectedTextColor = Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = onCart,
            icon = {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_add),
                    contentDescription = "Cart",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text("Cart") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Coral,
                selectedTextColor = Coral,
                unselectedIconColor = Gray,
                unselectedTextColor = Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = onOrders,
            icon = {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_agenda),
                    contentDescription = "Orders",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text("Orders") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Coral,
                selectedTextColor = Coral,
                unselectedIconColor = Gray,
                unselectedTextColor = Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text("Profile") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Coral,
                selectedTextColor = Coral,
                unselectedIconColor = Gray,
                unselectedTextColor = Gray,
                indicatorColor = Color.Transparent
            )
        )
    }
}