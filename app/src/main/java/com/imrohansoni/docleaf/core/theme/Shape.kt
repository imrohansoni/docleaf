package com.imrohansoni.docleaf.core.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Immutable
data class AppShapes(

    val extraSmall: Shape,
    val small: Shape,
    val medium: Shape,
    val large: Shape,
    val extraLarge: Shape,
    val pill : Shape
)

internal val DefaultShapes = AppShapes(
    extraSmall = SquircleShape(radius = 8.dp),
    small = SquircleShape(radius = 12.dp),
    medium = SquircleShape(radius = 16.dp),
    large = SquircleShape(radius = 24.dp),
    extraLarge = SquircleShape(radius = 32.dp),
    pill = RoundedCornerShape(percent = 50)
)