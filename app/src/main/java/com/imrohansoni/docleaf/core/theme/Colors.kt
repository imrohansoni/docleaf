package com.imrohansoni.docleaf.core.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

val leafGreenMain = Color(0xFF68BB3E)
val leafGreenDark = Color(0xFF4A8A2B)
val darkGreen = Color(0xFF165B34)
val primaryBlue = Color(0xFF008EFF)

val lightGray = Color(0xFFEAEAEA)
val gray = Color(0xFF9C9C9C)
val regularGray = Color(0xFF515151)
val gray2 = Color(0xFF575757)
val darkGray = Color(0xFF1C1C1C)
val slateBlack = Color(0xFF0F0F0F) // Edit text background
val slatBlack2 = Color(0XFF2F2F2F) // Edit text border color

val blackGray = Color(0xFF252525)
val slateBlue = Color(0xFF202932)


@Immutable
data class Colors(
    val background: Color,
    val surface: Color,

    val primary: Color,
    val secondary: Color,
    val accent: Color,

    val textPrimary: Color,
    val textSecondary: Color,
    val textDisabled: Color,

    val border: Color,
    val divider: Color,

    val success: Color,
    val danger: Color,
    val warning: Color,
)