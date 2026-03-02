package com.project.homeeats.pages.theme.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.project.homeeats.R

// ─── Colors (shared with LoginScreen) ────────────────────────────────────────

private val BackgroundBeige  = Color(0xFFF2EDE8)
private val CardWhite        = Color(0xFFFAF7F5)
private val PrimaryRed       = Color(0xFFE05C60)
private val PrimaryRedDark   = Color(0xFFCB4448)
private val PrimaryRedLight  = Color(0xFFEA7E81)
private val TextDark         = Color(0xFF1C1A1A)
private val TextMedium       = Color(0xFF6B6462)
private val TextLight        = Color(0xFFA09896)
private val InputBorder      = Color(0xFFE5DDD9)
private val InputBackground  = Color(0xFFF5F0ED)
private val SegmentInactive  = Color(0xFFEDE6E2)

// ─── Register Screen ──────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterClick: (name: String, email: String, phone: String, password: String, isChef: Boolean) -> Unit = { _, _, _, _, _ -> },
    onLoginClick: () -> Unit = {}
) {
    var name            by remember { mutableStateOf("") }
    var email           by remember { mutableStateOf("") }
    var phone           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isChef          by remember { mutableStateOf(false) }
    var isLoading       by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    // Entrance animation
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    val alphaAnim by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "alpha"
    )
    val slideAnim by animateDpAsState(
        targetValue = if (visible) 0.dp else 20.dp,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "slide"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBeige),
        contentAlignment = Alignment.TopCenter
    ) {
        // Decorative blobs
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(PrimaryRedLight.copy(alpha = 0.10f), Color.Transparent),
                    center = Offset(size.width * 0.9f, size.height * 0.05f),
                    radius = size.width * 0.5f
                ),
                center = Offset(size.width * 0.9f, size.height * 0.05f),
                radius = size.width * 0.5f
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(PrimaryRedLight.copy(alpha = 0.07f), Color.Transparent),
                    center = Offset(size.width * 0.1f, size.height * 0.9f),
                    radius = size.width * 0.45f
                ),
                center = Offset(size.width * 0.1f, size.height * 0.9f),
                radius = size.width * 0.45f
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp)
                .offset(y = slideAnim)
                .graphicsLayer(alpha = alphaAnim),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // ── App Icon ──────────────────────────────────────────────────
            Image(
                painter = painterResource(R.drawable.homeeats_logo),
                contentDescription = "HomeEats Logo",
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Title ─────────────────────────────────────────────────────
            Text(
                text = "Create Account",
                style = TextStyle(
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark,
                    letterSpacing = (-0.5).sp
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ── Full Name ─────────────────────────────────────────────────
            InputField(
                label = "Full Name",
                value = name,
                onValueChange = { name = it },
                placeholder = "John Doe",
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                onImeAction = { focusManager.moveFocus(FocusDirection.Down) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Email ─────────────────────────────────────────────────────
            InputField(
                label = "Email",
                value = email,
                onValueChange = { email = it },
                placeholder = "your@email.com",
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                onImeAction = { focusManager.moveFocus(FocusDirection.Down) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Phone Number ──────────────────────────────────────────────
            InputField(
                label = "Phone Number",
                value = phone,
                onValueChange = { phone = it },
                placeholder = "+1 (555) 000-0000",
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next,
                onImeAction = { focusManager.moveFocus(FocusDirection.Down) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Password ──────────────────────────────────────────────────
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Password",
                    style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    placeholder = {
                        Text("••••••••", style = TextStyle(color = TextLight, fontSize = 14.sp))
                    },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Text(if (passwordVisible) "🙈" else "👁", fontSize = 18.sp)
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

            Spacer(modifier = Modifier.height(20.dp))

            // ── Role Toggle ───────────────────────────────────────────────
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "I am a",
                    style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(SegmentInactive)
                        .padding(4.dp)
                ) {
                    // Sliding indicator
                    val indicatorOffset by animateFloatAsState(
                        targetValue = if (isChef) 0.5f else 0f,
                        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
                        label = "toggle"
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.5f)
                            .offset(x = (indicatorOffset * 1000).dp * 0f) // handled via layout below
                    )

                    Row(modifier = Modifier.fillMaxSize()) {
                        // Customer
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (!isChef) PrimaryRed else Color.Transparent)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { isChef = false },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Customer",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (!isChef) Color.White else TextMedium
                                )
                            )
                        }
                        // Chef
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isChef) PrimaryRed else Color.Transparent)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { isChef = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Chef",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isChef) Color.White else TextMedium
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ── Create Account Button ─────────────────────────────────────
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
                    .background(Brush.horizontalGradient(listOf(PrimaryRed, PrimaryRedDark)))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        enabled = !isLoading
                    ) {
                        if (name.isNotBlank() && email.isNotBlank() && phone.isNotBlank() && password.isNotBlank()) {
                            isLoading = true
                            focusManager.clearFocus()
                            onRegisterClick(name, email, phone, password, isChef)
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
                        text = "Create Account",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Login Link ────────────────────────────────────────────────
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = TextMedium, fontSize = 14.sp, fontWeight = FontWeight.Normal)) {
                        append("Already have an account? ")
                    }
                    withStyle(SpanStyle(color = PrimaryRed, fontSize = 14.sp, fontWeight = FontWeight.Bold)) {
                        append("Log In")
                    }
                },
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onLoginClick
                )
            )

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

// ─── Reusable Input Field ─────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    onImeAction: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1C1A1A))
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            placeholder = {
                Text(placeholder, style = TextStyle(color = Color(0xFFA09896), fontSize = 14.sp))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            keyboardActions = KeyboardActions(
                onNext = { onImeAction() },
                onDone = { onImeAction() }
            ),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFFAF7F5),
                unfocusedContainerColor = Color(0xFFF5F0ED),
                focusedBorderColor = Color(0xFFE05C60),
                unfocusedBorderColor = Color(0xFFE5DDD9),
                cursorColor = Color(0xFFE05C60),
                focusedTextColor = Color(0xFF1C1A1A),
                unfocusedTextColor = Color(0xFF1C1A1A)
            ),
            textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium)
        )
    }
}

// ─── Preview ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun RegisterScreenPreview() {
    MaterialTheme {
        RegisterScreen()
    }
}