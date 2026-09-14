package com.imrohansoni.docleaf.core.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class AppDimensions(
    // Spacing
    val space2: Dp,
    val space4: Dp,
    val space8: Dp,
    val space12: Dp,
    val space16: Dp,
    val space20: Dp,
    val space24: Dp,
    val space32: Dp,
    val space40: Dp,
    val space48: Dp,
    val space64: Dp,

    // Border
    val borderThin: Dp,
    val borderMedium: Dp,
    val borderThick: Dp,

    // Icon
    val iconExtraSmall: Dp,
    val iconSmall: Dp,
    val iconMedium: Dp,
    val iconLarge: Dp,
    val iconExtraLarge: Dp,
    val iconUltraLarge : Dp,
    val iconExtraUltraLarge : Dp,

    // Components
    val buttonHeight: Dp,
    val buttonHeightSmall : Dp,
    val textFieldHeight: Dp,
    val switchWidth: Dp,
    val switchHeight: Dp,
    val checkboxSize: Dp,
    val dropdownMinHeight: Dp,

    // Loading
    val loadingSmall: Dp,
    val loadingMedium: Dp,
    val loadingLarge: Dp,

    // Elevation
    val elevationSmall: Dp,
    val elevationMedium: Dp,
    val elevationLarge: Dp,


    val textFieldHorizontalPadding: Dp,
    val textFieldVerticalPadding: Dp,


    // Checkbox
    val checkboxIconSize: Dp,

// Switch
    val switchThumbSize: Dp,
    val switchPadding: Dp,
    val topBarHeight: Dp
)


internal val DefaultDimensions = AppDimensions(
    // Spacing
    space2 = 2.dp,
    space4 = 4.dp,
    space8 = 8.dp,
    space12 = 12.dp,
    space16 = 16.dp,
    space20 = 20.dp,
    space24 = 24.dp,
    space32 = 32.dp,
    space40 = 40.dp,
    space48 = 48.dp,
    space64 = 64.dp,

    // Border
    borderThin = 1.dp,
    borderMedium = 1.5.dp,
    borderThick = 2.dp,

    // Icons

    iconExtraSmall = 16.dp,
    iconSmall = 20.dp,
    iconMedium = 24.dp,
    iconLarge = 32.dp,
    iconExtraLarge = 40.dp,
    iconUltraLarge = 48.dp,
    iconExtraUltraLarge = 56.dp,

    // Components
    buttonHeight = 52.dp,
    buttonHeightSmall = 38.dp,
    textFieldHeight = 56.dp,

    switchWidth = 52.dp,
    switchHeight = 32.dp,

    checkboxSize = 20.dp,

    dropdownMinHeight = 56.dp,

    // Loading
    loadingSmall = 16.dp,
    loadingMedium = 24.dp,
    loadingLarge = 32.dp,

    // Elevation
    elevationSmall = 2.dp,
    elevationMedium = 6.dp,
    elevationLarge = 12.dp,

    textFieldVerticalPadding = 14.dp,
    textFieldHorizontalPadding = 16.dp,

    checkboxIconSize = 14.dp,
    switchThumbSize = 28.dp,
    switchPadding = 2.dp,

    topBarHeight = 64.dp
)