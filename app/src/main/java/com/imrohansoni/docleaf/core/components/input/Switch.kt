package com.imrohansoni.docleaf.core.components.input

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.imrohansoni.docleaf.core.theme.AppTheme
import com.imrohansoni.docleaf.core.theme.DocLeafScanner

@Composable
fun Switch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {

    val interactionSource = remember { MutableInteractionSource() }

    val progress by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "SwitchProgress"
    )

    val travel =
        AppTheme.dimensions.switchWidth -
                AppTheme.dimensions.switchThumbSize -
                (AppTheme.dimensions.switchPadding * 2)

    val thumbOffset = AppTheme.dimensions.switchPadding + travel * progress

    val trackColor by animateColorAsState(
        targetValue = when {

            !enabled ->
                AppTheme.colors.disabled

            checked ->
                AppTheme.colors.primary

            else ->
                AppTheme.colors.outline

        },
        label = "track"
    )

    Box(
        modifier = modifier
            .size(
                width = AppTheme.dimensions.switchWidth,
                height = AppTheme.dimensions.switchHeight
            )
            .clip(shape = RoundedCornerShape(AppTheme.dimensions.switchHeight / 2))
            .background(trackColor)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    onCheckedChange(!checked)
                }
            )
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = thumbOffset)
                .size(AppTheme.dimensions.switchThumbSize)
                .clip(CircleShape)
                .background(Color.White)
        )
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SwitchPreview() {
    val checked = remember { mutableStateOf(false) }

    DocLeafScanner(darkTheme = true) {
        Box(Modifier.fillMaxSize().background(Color.Black).safeContentPadding()){
            Switch(checked = checked.value, onCheckedChange = {
                checked.value = it
            })
        }
    }
}