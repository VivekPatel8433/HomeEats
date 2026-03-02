package com.example.homeeats.pages.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Coral,
    secondary = Peach,
    background = WarmOffWhite,
    surface = WarmOffWhite,
    onPrimary = WarmOffWhite,
    onSecondary = WarmOffWhite,
    onBackground = Charcoal,
    onSurface = Charcoal,
)

@Composable
fun HomeEatsTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}