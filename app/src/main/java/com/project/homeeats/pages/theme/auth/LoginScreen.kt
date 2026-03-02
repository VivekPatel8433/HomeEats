package com.project.homeeats.pages.theme.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import com.project.homeeats.R

// ─── Colors ───────────────────────────────────────────────────────────────────

private val BackgroundBeige   = Color(0xFFF2EDE8)
private val CardWhite         = Color(0xFFFAF7F5)
private val PrimaryRed        = Color(0xFFE05C60)
private val PrimaryRedDark    = Color(0xFFCB4448)
private val PrimaryRedLight   = Color(0xFFEA7E81)
private val TextDark          = Color(0xFF1C1A1A)
private val TextMedium        = Color(0xFF6B6462)
private val TextLight         = Color(0xFFA09896)
private val InputBorder       = Color(0xFFE5DDD9)
private val InputBackground   = Color(0xFFF5F0ED)

// ─── Login Screen ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginClick: (email: String, password: String) -> Unit = { _, _ -> },
    onRegisterClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    // Subtle entrance animation
    val alphaAnim by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "alpha"
    )
    val slideAnim by animateDpAsState(
        targetValue = 0.dp,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "slide"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBeige),
        contentAlignment = Alignment.Center
    ) {
        // Decorative soft blobs
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(PrimaryRedLight.copy(alpha = 0.12f), Color.Transparent),
                    center = Offset(size.width * 0.85f, size.height * 0.12f),
                    radius = size.width * 0.5f
                ),
                center = Offset(size.width * 0.85f, size.height * 0.12f),
                radius = size.width * 0.5f
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(PrimaryRedLight.copy(alpha = 0.08f), Color.Transparent),
                    center = Offset(size.width * 0.1f, size.height * 0.85f),
                    radius = size.width * 0.45f
                ),
                center = Offset(size.width * 0.1f, size.height * 0.85f),
                radius = size.width * 0.45f
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp)
                .offset(y = slideAnim)
                .graphicsLayer(alpha = alphaAnim),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ── App Icon ──────────────────────────────────────────────────
            Image(
                painter = painterResource(R.drawable.applogo),
                contentDescription = "HomeEats Logo",
                modifier = Modifier
                    .size(140.dp)
//                    .clip(RoundedCornerShape(150.dp))
            )

            // ── Brand Name ────────────────────────────────────────────────
            Text(
                text = "HomeEats",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark,
                    letterSpacing = (-0.5).sp
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Every Hunger Matters",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextLight,
                    letterSpacing = 0.2.sp
                )
            )

            Spacer(modifier = Modifier.height(40.dp))

            // ── Email Field ───────────────────────────────────────────────
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Email",
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextDark
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    placeholder = {
                        Text(
                            "your@email.com",
                            style = TextStyle(color = TextLight, fontSize = 14.sp)
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardWhite,
                        unfocusedContainerColor = InputBackground,
                        focusedBorderColor = PrimaryRed,
                        unfocusedBorderColor = InputBorder,
                        cursorColor = PrimaryRed,
                        focusedTextColor = TextDark,
                        unfocusedTextColor = TextDark
                    ),
                    textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ── Password Field ────────────────────────────────────────────
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Password",
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextDark
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    placeholder = {
                        Text(
                            "••••••••",
                            style = TextStyle(color = TextLight, fontSize = 14.sp)
                        )
                    },
                    singleLine = true,
                    visualTransformation = if (passwordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            if (email.isNotBlank() && password.isNotBlank()) {
                                isLoading = true
                                onLoginClick(email, password)
                            }
                        }
                    ),
                    trailingIcon = {
                        val icon = if (passwordVisible) "🙈" else "👁"
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Text(icon, fontSize = 18.sp)
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardWhite,
                        unfocusedContainerColor = InputBackground,
                        focusedBorderColor = PrimaryRed,
                        unfocusedBorderColor = InputBorder,
                        cursorColor = PrimaryRed,
                        focusedTextColor = TextDark,
                        unfocusedTextColor = TextDark
                    ),
                    textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ── Login Button ──────────────────────────────────────────────
            val buttonScale by animateFloatAsState(
                targetValue = if (isLoading) 0.97f else 1f,
                animationSpec = spring(stiffness = Spring.StiffnessMedium),
                label = "buttonScale"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .graphicsLayer(scaleX = buttonScale, scaleY = buttonScale)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = PrimaryRed.copy(alpha = 0.35f),
                        spotColor = PrimaryRed.copy(alpha = 0.45f)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(PrimaryRed, PrimaryRedDark)
                        )
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        if (!isLoading && email.isNotBlank() && password.isNotBlank()) {
                            isLoading = true
                            focusManager.clearFocus()
                            onLoginClick(email, password)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text(
                        text = "Log In",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // ── Register Link ─────────────────────────────────────────────
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = TextMedium,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    ) {
                        append("Don't have an account? ")
                    }
                    withStyle(
                        SpanStyle(
                            color = PrimaryRed,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Register")
                    }
                },
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onRegisterClick
                    )
            )
        }
    }
}

// ─── Preview ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen()
    }
}