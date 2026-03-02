package com.project.homeeats.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.project.homeeats.R
import kotlinx.coroutines.delay

@Composable
fun IntroductionScreen(
    onFinish: () -> Unit = {}
) {
    // Navigate automatically after splash delay
    LaunchedEffect(Unit) {
        delay(2200)
        onFinish()
    }

    // Bounce animation for logo
    val scaleAnim = remember { Animatable(0.7f) }
    LaunchedEffect(Unit) {
        scaleAnim.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    // Fade animation for text
    val textAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 900, delayMillis = 350),
        label = stringResource(R.string.textalpha_text)
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFFF4F1)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // iOS-style rounded icon container
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .clip(RoundedCornerShape(40.dp)) // iPhone-style rounding
                    .background(Color(0xFF1C1C1E)),
                contentAlignment = Alignment.Center
            ) {
                // Logo image filling most of the container
                Image(
                    painter = painterResource(id = R.drawable.homeeats_logo),
                    contentDescription = stringResource(R.string.app_logo_text),
                    contentScale = ContentScale.Fit, // keeps logo + text visible
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp) // smaller padding = bigger logo
                        .scale(scaleAnim.value)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // App title
            Text(
                text = stringResource(R.string.homeeats_text),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.alpha(textAlpha),
                color = Color(0xFF1C1C1E)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // App subtitle
            Text(
                text = stringResource(R.string.every_hunger_matters_text),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.alpha(textAlpha),
                color = Color(0xFF8E8E93)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Smooth wave loading dots
            WaveDotsIndicator(
                totalDots = 3,
                modifier = Modifier.alpha(textAlpha)
            )
        }
    }
}

@Composable
fun WaveDotsIndicator(
    totalDots: Int = 3,
    modifier: Modifier = Modifier,
    activeColor: Color = Color(0xFFE86A6A),
    inactiveColor: Color = Color(0xFFF3A68A).copy(alpha = 0.35f),
    baseSizeDp: Float = 6f,
    waveSizeDp: Float = 10f,
    durationMs: Int = 1000,          // higher = smoother
    delayBetweenDotsMs: Int = 160     // spacing in time between dots
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalDots) { index ->
            val transition = rememberInfiniteTransition(label = "dotWave")

            // Each dot animates its size with a different start delay (wave effect)
            val sizeAnim by transition.animateFloat(
                initialValue = baseSizeDp,
                targetValue = waveSizeDp,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = durationMs
                        baseSizeDp at 0
                        waveSizeDp at (durationMs * 0.35f).toInt() with FastOutSlowInEasing
                        baseSizeDp at (durationMs * 0.75f).toInt() with FastOutSlowInEasing
                        baseSizeDp at durationMs
                    },
                    repeatMode = RepeatMode.Restart,
                    initialStartOffset = StartOffset(delayBetweenDotsMs * index)
                ),
                label = "sizeAnim"
            )

            // Use active color when dot is near peak size
            val isActive = sizeAnim > (baseSizeDp + 1f)
            val dotColor = if (isActive) activeColor else inactiveColor

            Box(
                modifier = Modifier
                    .size(sizeAnim.dp)
                    .clip(RoundedCornerShape(50))
                    .background(dotColor)
            )
        }
    }
}