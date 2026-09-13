package com.imrohansoni.docleaf.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imrohansoni.docleaf.core.theme.SquircleShape
import com.imrohansoni.docleaf.core.theme.darkGray
import com.imrohansoni.docleaf.core.theme.leafGreenMain


enum class ButtonType{
    PRIMARY, SECONDARY
}

enum class ButtonSize {
    SMALL, LARGE
}

@Composable
fun Button(
    modifier: Modifier = Modifier,
    text: String,
    buttonType : ButtonType = ButtonType.PRIMARY,
    buttonSize : ButtonSize = ButtonSize.LARGE,
    onClick: () -> Unit
) {
    val buttonHeight = if(buttonSize == ButtonSize.LARGE) 50.dp else 36.dp

    val backgroundColor = if(buttonType == ButtonType.PRIMARY) leafGreenMain else darkGray
    Box(
        modifier
            .fillMaxWidth()
            .height(buttonHeight)
            .clip(SquircleShape(0.6f))
            .background(backgroundColor)
            .clickable {
                onClick.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        BasicText(text.uppercase(), style = TextStyle(color = Color.White, fontSize = 14.sp))
    }
}
