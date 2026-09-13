package com.imrohansoni.docleaf.core.components//package com.imrohansoni.pdfapp.core.ui.components
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.systemBarsPadding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.text.BasicText
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//enum class IconPosition {
//    START, END, TEXT_BEFORE, TEXT_AFTER
//}
//
//@Composable
//fun TextIconButton(
//    modifier: Modifier = Modifier,
//    icon: AppIcon,
//    text: String,
//    iconPosition: IconPosition = IconPosition.TEXT_AFTER,
//    buttonStyle: ButtonStyle = ButtonStyle.FILLED,
//    buttonVariant: ButtonVariant = ButtonVariant.PRIMARY,
//    buttonSize: ButtonSize = ButtonSize.MEDIUM,
//    enabled: Boolean = true,
//    onClick: () -> Unit
//) {
//    val colors = AppTheme.colors
//    val shapes = AppTheme.shapes
//    val dimensions = AppTheme.dimensions
////    val typography = AppTheme.typography
//
//    val (iconButtonSize, iconSize) = when (buttonSize) {
//        ButtonSize.SMALL -> Pair(dimensions.iconButtonSmall, dimensions.iconSmall)
//        ButtonSize.MEDIUM -> Pair(dimensions.iconButtonMedium, dimensions.iconMedium)
//        ButtonSize.LARGE -> Pair(dimensions.iconButtonLarge, dimensions.iconLarge)
//    }
//
//    val buttonColors = resolveButtonColors(buttonStyle, buttonVariant, colors)
//
//    Row(
//        modifier
//            .fillMaxWidth()
//            .clip(shapes.large)
//            .background(buttonColors.background, shapes.large)
//            .border(1.dp, buttonColors.border, shapes.large)
//            .padding(8.dp)
//            .clickable { onClick.invoke() }, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
//    ) {
//        BasicText(
//            text = text,
//            style = TextStyle(fontFamily = InterFontFamily, fontWeight = FontWeight.Normal, fontSize = 16.sp, color = buttonColors.content)
//        )
//        Spacer(Modifier.width(8.dp))
//        Icon(icon = icon, color = buttonColors.content, size = iconSize)
//    }
//}
//
//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun TextIconButtonPreview(modifier: Modifier = Modifier) {
//    AppTheme(themeMode = ThemeMode.DARK) {
//        Box(
//            Modifier
//                .background(Color.White)
//                .fillMaxSize()
//                .systemBarsPadding(),
//            contentAlignment = Alignment.Center
//        ) {
//            Row(Modifier.padding(20.dp)) {
//                TextIconButton(modifier = Modifier.weight(1f), icon = Icons.Close, text = "CANCEL", buttonStyle = ButtonStyle.OUTLINE_SOFT,  buttonVariant = ButtonVariant.PRIMARY) { }
//                Spacer(Modifier.width(12.dp))
//                TextIconButton(modifier = Modifier.weight(1f),icon = Icons.Trash, text = "DELETE", buttonVariant = ButtonVariant.DANGER) { }
//            }
//        }
//    }
//}