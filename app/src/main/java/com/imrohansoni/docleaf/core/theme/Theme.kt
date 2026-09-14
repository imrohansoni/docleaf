package com.imrohansoni.docleaf.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf

// Composition Locals
private val LocalColors = staticCompositionLocalOf<AppColors> {
    error("No AppColors provided.")
}

private val LocalTypography = staticCompositionLocalOf<AppTypography> {
    error("No AppTypography provided.")
}

private val LocalDimensions = staticCompositionLocalOf<AppDimensions> {
    error("No AppDimensions provided.")
}

private val LocalShapes = staticCompositionLocalOf<AppShapes> {
    error("No AppShapes provided.")
}

// Theme Object


@Stable
object AppTheme {
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

    val dimensions: AppDimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalDimensions.current

    val shapes: AppShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalShapes.current
}


// Theme Provider
@Composable
fun DocLeafScanner(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colors: AppColors = if (darkTheme) DarkColors else LightColors,
    typography: AppTypography = DefaultTypography,
    dimens: AppDimensions = DefaultDimensions,
    shapes: AppShapes = DefaultShapes,
    content: @Composable () -> Unit,
) {

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTypography provides typography,
        LocalDimensions provides dimens,
        LocalShapes provides shapes,
    ) {
        SystemBarAppearance(darkTheme = darkTheme)
        content()
    }
}


