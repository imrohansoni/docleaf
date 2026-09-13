package com.imrohansoni.docleaf.core.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.imrohansoni.docleaf.R

val InterFontFamily = FontFamily(
    Font(R.font.inter_thin, FontWeight.Thin, FontStyle.Normal),
    Font(R.font.inter_extra_light, FontWeight.ExtraLight, FontStyle.Normal),
    Font(R.font.inter_light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.inter_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.inter_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.inter_semi_bold, FontWeight.SemiBold, FontStyle.Normal),
    Font(R.font.inter_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.inter_extra_bold, FontWeight.ExtraBold, FontStyle.Normal),
    Font(R.font.inter_black, FontWeight.Black, FontStyle.Normal),

    Font(R.font.inter_thin_italic, FontWeight.Thin, FontStyle.Italic),
    Font(R.font.inter_extra_light_italic, FontWeight.ExtraLight, FontStyle.Italic),
    Font(R.font.inter_light_italic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.inter_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.inter_medium_italic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.inter_semi_bold_italic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.inter_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.inter_extra_bold_italic, FontWeight.ExtraBold, FontStyle.Italic),
    Font(R.font.inter_black_italic, FontWeight.Black, FontStyle.Italic),
)


@Immutable
data class Typography(
    val titleLarge: TextStyle,
    val titleMedium: TextStyle,
    val body: TextStyle,
    val bodySecondary: TextStyle,
    val button: TextStyle,
    val caption: TextStyle
)