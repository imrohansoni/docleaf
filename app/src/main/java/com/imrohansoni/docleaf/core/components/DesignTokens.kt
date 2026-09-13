package com.imrohansoni.docleaf.core.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object AppColors {
    val Primary = Color(0xFF3D5AFE)
    val OnPrimary = Color(0xFFFFFFFF)
    val PrimaryDisabled = Color(0xFFB9C2F2)

    val Surface = Color(0xFFFFFFFF)
    val SurfaceVariant = Color(0xFFF1F2F6)
    val OnSurface = Color(0xFF1B1C1F)
    val OnSurfaceVariant = Color(0xFF5B5D66)
    val OnSurfaceDisabled = Color(0xFFB0B1B8)

    val Outline = Color(0xFFD5D6DC)
    val OutlineFocused = Primary

    val PressOverlayLight = Color(0x14000000)
    val PressOverlayOnPrimary = Color(0x1FFFFFFF)

    val Error = Color(0xFFE53935)
}

object AppShapes {
    val ButtonLargeCorner = 14.dp
    val ButtonSmallCorner = 10.dp
    val CheckboxCorner = 6.dp
    val SwitchTrackCorner = 999.dp // fully rounded
}

object AppMotion {
    // Snappy but soft — mimics Material's emphasized-decelerate feel without
    // pulling in the Material library.
    val PressSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )

    val StateSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMediumLow
    )

    const val PressedScaleDefault = 0.96f
    const val PressedScaleSubtle = 0.98f
}
