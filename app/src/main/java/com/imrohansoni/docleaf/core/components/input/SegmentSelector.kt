package com.imrohansoni.docleaf.core.components.input

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imrohansoni.docleaf.core.components.basic.Text
import com.imrohansoni.docleaf.core.theme.AppTheme
import com.imrohansoni.docleaf.core.theme.DocLeafScanner

@Composable
fun <T> SegmentedSelector(
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    label: (T) -> String,
) {

    val dimensions = AppTheme.dimensions

    BoxWithConstraints(
        modifier = modifier
            .height(42.dp)
            .clip(AppTheme.shapes.pill)
            .background(AppTheme.colors.surface)

    ) {
        val padding = 3.dp

        val selectorWidth = (maxWidth - padding * 2) / items.size

        val selectedIndex = items.indexOf(selectedItem)

        val selectorOffset by animateDpAsState(
            targetValue = padding + selectorWidth * selectedIndex,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            ),
            label = "SelectorOffset"
        )

        // Sliding selector
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(padding)
        ) {

            Box(
                modifier = Modifier
                    .offset(x = selectorOffset - padding)
                    .width(selectorWidth)
                    .fillMaxHeight()
                    .clip(AppTheme.shapes.pill)
                    .background(AppTheme.colors.primary)
            )
        }

        Row(modifier = Modifier.fillMaxSize()) {
            items.forEach { item ->
                SegmentItem(
                    modifier = Modifier.weight(1f),
                    text = label(item),
                    selected = item == selectedItem,
                    onClick = {
                        onItemSelected(item)
                    }
                )
            }
        }
    }
}

@Composable
private fun SegmentItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val interactionSource = remember {
        MutableInteractionSource()
    }

    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) .97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "Scale"
    )

    val textColor by animateColorAsState(
        targetValue = if (selected)
            AppTheme.colors.onPrimary
        else
            AppTheme.colors.onSurface,
        animationSpec = tween(180),
        label = "TextColor"
    )

    Box(
        modifier = modifier
            .fillMaxHeight()
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Tab,
                onClick = onClick
            )
            .semantics {
                role = Role.Tab
                this.selected = selected
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = AppTheme.typography.labelMedium,
            color = textColor
        )
    }
}

enum class CaptureMode {
    AUTO,
    MANUAL
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SegmentSelectorPreview() {
    var captureMode by remember {
        mutableStateOf(CaptureMode.AUTO)
    }

    DocLeafScanner(darkTheme = true) {
        Box(
            Modifier
                .fillMaxSize()
                .background(color = Color.Gray)
                .safeContentPadding()
        ) {
            SegmentedSelector(
                items = CaptureMode.entries,
                selectedItem = captureMode,
                onItemSelected = {
                    captureMode = it
                },
                label = {
                    when (it) {
                        CaptureMode.AUTO -> "Automatic"
                        CaptureMode.MANUAL -> "Manual"
                    }
                }
            )
        }
    }
}