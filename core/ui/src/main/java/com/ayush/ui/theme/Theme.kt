package com.ayush.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val LedgeDarkColorScheme = darkColorScheme(
    primary = LedgeMd3Primary,
    onPrimary = LedgeMd3OnPrimary,
    primaryContainer = LedgeMd3PrimaryContainer,
    onPrimaryContainer = LedgeMd3OnPrimaryContainer,
    background = LedgeMd3Background,
    onBackground = LedgeMd3OnBackground,
    surface = LedgeMd3Surface,
    onSurface = LedgeMd3OnSurface,
    surfaceVariant = LedgeMd3SurfaceVariant,
    onSurfaceVariant = LedgeMd3OnSurfaceVariant,
    error = LedgeMd3Error,
    onError = LedgeMd3OnError,
    errorContainer = LedgeMd3ErrorContainer,
    onErrorContainer = LedgeMd3OnErrorContainer,
    outline = LedgeMd3Outline,
    outlineVariant = LedgeMd3OutlineVariant,
)

@Composable
fun LedgeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LedgeDarkColorScheme,
        typography = LedgeTypography,
        shapes = LedgeShapes,
        content = content,
    )
}