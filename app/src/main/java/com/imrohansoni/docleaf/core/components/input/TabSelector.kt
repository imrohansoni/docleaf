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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.imrohansoni.docleaf.core.components.IconResource
import com.imrohansoni.docleaf.core.components.basic.Icon
import com.imrohansoni.docleaf.core.components.basic.Text
import com.imrohansoni.docleaf.core.theme.AppTheme


data class TabItemData<T>(
    val value: T,
    val title: String,
    val icon: IconResource,
)

@Composable
internal fun <T> TabItem(
    tab: TabItemData<T>,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val interactionSource = remember {
        MutableInteractionSource()
    }

    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "TabScale"
    )

    val contentColor by animateColorAsState(
        targetValue = if (selected) {
            AppTheme.colors.primary
        } else {
            AppTheme.colors.onSurface.copy(alpha = 0.72f)
        },
        animationSpec = tween(180),
        label = "TabColor"
    )

    Row(
        modifier = modifier
            .height(32.dp)
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Tab,
                onClick = onClick
            )
            .padding(horizontal = 12.dp)
            .semantics {
                role = Role.Tab
                this.selected = selected
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Icon(
            icon = tab.icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(18.dp)
        )

        Spacer(Modifier.width(6.dp))

        Text(
            text = tab.title,
            style = AppTheme.typography.titleSmall,
            color = contentColor
        )
    }
}

@Composable
fun <T> TabSelector(
    tabs: List<TabItemData<T>>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabWidth = 110.dp
    val indicatorHeight = 3.dp

    val selectedIndex = tabs.indexOfFirst { it.value == selectedItem }
        .coerceAtLeast(0)

    val indicatorOffset by animateDpAsState(
        targetValue = tabWidth * selectedIndex,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "IndicatorOffset"
    )
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.wrapContentWidth()
        ) {
            tabs.forEach { tab ->
                TabItem(
                    modifier = Modifier
                        .width(tabWidth)
                        .height(44.dp),
                    tab = tab,
                    selected = tab.value == selectedItem,
                    onClick = {
                        onItemSelected(tab.value)
                    }
                )
            }
        }
        Spacer(Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
        ) {

            // Background divider
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        AppTheme.colors.outline.copy(alpha = 0.25f)
                    )
            )
            // Selected indicator
            Box(
                modifier = Modifier
                    .offset(x = indicatorOffset)
                    .width(tabWidth)
                    .fillMaxHeight()
                    .clip(AppTheme.shapes.pill)
                    .background(AppTheme.colors.primary)
            )
        }
    }
}