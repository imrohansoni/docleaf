package com.imrohansoni.docleaf.core.components.layout

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.imrohansoni.docleaf.core.components.IconResource
import com.imrohansoni.docleaf.core.components.basic.Icon
import com.imrohansoni.docleaf.core.components.basic.Text
import com.imrohansoni.docleaf.core.theme.AppTheme

data class BottomNavItem<T>(
    val id: T,
    val label: String,
    val icon: IconResource,
    val selectedIcon: IconResource = icon,
)

@Composable
fun <T> BottomNavBar(
    items: List<BottomNavItem<T>>,
    selected: T,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (items.isEmpty()) return

    val barHeight = 68.dp
    val outerPadding = 6.dp

    BoxWithConstraints(
        modifier = modifier
            .widthIn(
            max = 360.dp,
        )
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(
                horizontal = AppTheme.dimensions.space12,
                vertical = AppTheme.dimensions.space8,
            )
            .height(barHeight)
            .clip(CircleShape)
            .background(
                AppTheme.colors.surface,
            )
            .border(
                width = 1.dp,
                color = AppTheme.colors.onSurface.copy(
                    alpha = 0.08f,
                ),
                shape = CircleShape,
            )
            .padding(
                horizontal = outerPadding,
                vertical = 5.dp,
            ),
    ) {
        val itemWidth = maxWidth / items.size

        val selectedIndex = items.indexOfFirst {
            it.id == selected
        }.coerceAtLeast(0)

        val selectedOffset by animateDpAsState(
            targetValue = itemWidth * selectedIndex,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMedium,
            ),
            label = "BottomNavSelectedOffset",
        )

        Box(
            modifier = Modifier
                .offset(x = selectedOffset)
                .width(itemWidth)
                .fillMaxHeight()
                .clip(CircleShape)
                .background(
                    AppTheme.colors.onSurface.copy(
                        alpha = 0.12f,
                    ),
                ),
        )

        Row(
            modifier = Modifier.fillMaxSize(),
        ) {
            items.forEach { item ->
                BottomNavItem(
                    item = item,
                    selected = item.id == selected,
                    onClick = {
                        onSelect(item.id)
                    },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}


@Composable
private fun <T> BottomNavItem(
    item: BottomNavItem<T>,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = tween(
            durationMillis = 100,
            easing = FastOutSlowInEasing,
        ),
        label = "BottomNavPressScale",
    )

    val iconColor by animateColorAsState(
        targetValue = if (selected) {
            AppTheme.colors.onSurface
        } else {
            AppTheme.colors.onSurface.copy(
                alpha = 0.55f,
            )
        },
        animationSpec = tween(
            durationMillis = 180,
        ),
        label = "BottomNavIconColor",
    )

    val textColor by animateColorAsState(
        targetValue = if (selected) {
            AppTheme.colors.onSurface
        } else {
            AppTheme.colors.onSurface.copy(
                alpha = 0.55f,
            )
        },
        animationSpec = tween(
            durationMillis = 180,
        ),
        label = "BottomNavTextColor",
    )

    Column(
        modifier = modifier
            .fillMaxHeight()
            .scale(scale)
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Tab,
                onClick = onClick,
            )
            .padding(
                horizontal = AppTheme.dimensions.space8,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            icon = if (selected) {
                item.selectedIcon
            } else {
                item.icon
            },
            contentDescription = item.label,
            size = AppTheme.dimensions.iconMedium,
            tint = iconColor,
        )

        Spacer(
            modifier = Modifier.height(
                AppTheme.dimensions.space2,
            ),
        )

        Text(
            text = item.label,
            style = if (selected) {
                AppTheme.typography.labelMedium
            } else {
                AppTheme.typography.labelSmall
            },
            color = textColor,
            maxLines = 1,
        )
    }
}