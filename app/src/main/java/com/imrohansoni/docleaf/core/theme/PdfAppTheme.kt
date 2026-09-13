package com.imrohansoni.docleaf.core.theme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp


val defaultDimensions = Dimensions(
    iconSmall = 16.dp,
    iconMedium = 20.dp,
    iconLarge = 24.dp,

    iconButtonSmall = 36.dp,
    iconButtonMedium = 44.dp,
    iconButtonLarge = 52.dp,

    borderThin = 1.dp,
    borderNormal = 2.dp,
)

internal val LocalColors = compositionLocalOf<Colors> {
    error("colors not provided")
}

internal val LocalDimensions = staticCompositionLocalOf<Dimensions> {
    error("dimensions not provided")
}

internal val LocalTypography = staticCompositionLocalOf<Typography> {
    error("typography not provided")
}

internal val LocalShapes = staticCompositionLocalOf<Shapes> {
    error("shapes not provided")
}

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}


@Composable
internal fun isDarkMode(themeMode: ThemeMode): Boolean {
    return when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }
}

@Composable
fun PdfAppTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val isDark = isDarkMode(themeMode)
    val colors = if (isDark) {
        Colors(
            background = Color(0xFFF5F6F7),
            surface = Color(0xFFFFFFFF),
            primary = leafGreenMain,
            secondary = Color(0xFF42B72A),
            accent = leafGreenMain,
            textPrimary = Color(0xFF1C1E21),
            textSecondary = Color(0xFF65676B),
            textDisabled = Color(0xFFBCC0C4),
            border = Color(0xFFDADDE1),
            divider = Color(0xFFE4E6EB),
            success = Color(0xFF42B72A),
            warning = Color(0xFFF7B928),
            danger = Color(0xFFE41E3F),
        )
    } else {
        Colors(
            background = Color(0xFF18191A),
            surface = Color(0xFF242526),
            primary = leafGreenMain,
            secondary = Color(0xFF31A24C),
            accent = leafGreenMain,
            textPrimary = Color(0xFFE4E6EB),
            textSecondary = Color(0xFFB0B3B8),
            textDisabled = Color(0xFF6A6D71),
            border = Color(0xFF3A3B3C),
            divider = Color(0xFF303031),
            success = Color(0xFF31A24C),
            warning = Color(0xFFE4B23C),
            danger = Color(0xFFF02849),
        )
    }

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalDimensions provides defaultDimensions,
    ) {
        content()
    }
}


object AppTheme {
    val colors: Colors
        @Composable get() = LocalColors.current

    val typography: Typography
        @Composable get() = LocalTypography.current

    val dimensions: Dimensions
        @Composable get() = LocalDimensions.current

    val shapes: Shapes
        @Composable get() = LocalShapes.current
}