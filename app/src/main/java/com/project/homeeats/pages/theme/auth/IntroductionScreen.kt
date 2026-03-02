//package com.project.homeeats.pages.theme.auth
//
//import androidx.compose.animation.core.Animatable
//import androidx.compose.animation.core.Spring
//import androidx.compose.animation.core.spring
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.alpha
//import androidx.compose.ui.draw.scale
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import kotlinx.coroutines.delay
//import com.example.homeeats.R
//
///**
// * IntroductionScreen
// *
// * This composable displays an animated splash screen when the app starts.
// * It shows the app logo with a fade-in and bounce scale animation.
// *
// * The onFinish callback can be used to navigate to the next screen.
// */
//@Composable
//fun IntroductionScreen(
//    onFinish: () -> Unit = {}
//) {
//
//    // Animatable values for scale and transparency
//    val scale = remember { Animatable(0.2f) }   // starts very small
//    val alpha = remember { Animatable(0f) }     // starts invisible
//
//    // Runs animation when the screen is first displayed
//    LaunchedEffect(Unit) {
//
//        // Fade-in animation
//        alpha.animateTo(
//            targetValue = 1f,
//            animationSpec = spring(dampingRatio = 0.9f)
//        )
//
//        // Bounce scale animation
//        scale.animateTo(
//            targetValue = 1f,
//            animationSpec = spring(
//                dampingRatio = Spring.DampingRatioMediumBouncy,
//                stiffness = Spring.StiffnessLow
//            )
//        )
//
//        // Small delay before finishing splash
//        delay(900)
//
//        // Trigger navigation callback
//        onFinish()
//    }
//
//    // Main container centered on screen
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White),
//        contentAlignment = Alignment.Center
//    ) {
//        // App logo image
//        Image(
//            painter = painterResource(id = R.drawable.homeeats_logo),
//            contentDescription = "App Logo",
//            modifier = Modifier
//                .size(180.dp)
//                .alpha(alpha.value)
//                .scale(scale.value)
//        )
//    }
//}