package com.project.homeeats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import com.project.homeeats.screens.CartScreen
import com.project.homeeats.screens.HomeScreen
import com.project.homeeats.screens.IntroductionScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                Surface {
                    AppEntry()
                }
            }
        }
    }
}

@Composable
fun AppEntry() {
    var currentScreen by remember { mutableStateOf("intro") }

    when (currentScreen) {
        "intro" -> IntroductionScreen(onFinish = { currentScreen = "home" })
        "home" -> HomeScreen(onCartClick = { currentScreen = "cart" })
        "cart" -> CartScreen()
    }
}