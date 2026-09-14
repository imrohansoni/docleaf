package com.imrohansoni.docleaf.core.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color


internal val LightColors = AppColors(

    primary = Color(0xFF2563EB),
    onPrimary = Color(0xFFFFFFFF),

    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF020817),

    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0F172A),

    outline = Color(0xFFE2E8F0),

    error = Color(0xFFDC2626),
    success = Color(0xFF16A34A),
    warning = Color(0xFFF59E0B),

    disabled = Color(0xFFE5E7EB),
    onDisabled = Color(0xFF94A3B8),
)


internal val DarkColors = AppColors(

    primary = Color(0xFF60A5FA),
    onPrimary = Color(0xFF001B3D),

    background = Color(0xFF020817),
    onBackground = Color(0xFFF8FAFC),

    surface = Color(0xFF0F172A),
    onSurface = Color(0xFFF8FAFC),

    outline = Color(0xFF334155),

    error = Color(0xFFF87171),
    success = Color(0xFF4ADE80),
    warning = Color(0xFFFBBF24),

    disabled = Color(0xFF334155),
    onDisabled = Color(0xFF64748B),
)


@Immutable
data class AppColors(

    // Brand
    val primary: Color,
    val onPrimary: Color,

    // Background
    val background: Color,
    val onBackground: Color,

    // Surface
    val surface: Color,
    val onSurface: Color,

    // Borders
    val outline: Color,

    // States
    val error: Color,
    val success: Color,
    val warning: Color,

    // Disabled
    val disabled: Color,
    val onDisabled: Color,
)