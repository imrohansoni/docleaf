package com.imrohansoni.docleaf.core.components.input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imrohansoni.docleaf.core.components.Icons
import com.imrohansoni.docleaf.core.components.basic.Icon
import com.imrohansoni.docleaf.core.components.basic.Text
import com.imrohansoni.docleaf.core.theme.AppTheme
import com.imrohansoni.docleaf.core.theme.DocLeafScanner


@Composable
fun Dropdown(
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leading: (@Composable (() -> Unit))? = null,
) {

    val interactionSource = remember {
        MutableInteractionSource()
    }

    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) .98f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "DropdownScale"
    )

    Box(
        modifier = modifier
            .scale(scale)
            .clip(AppTheme.shapes.medium)
            .background(AppTheme.colors.surface)
            .border(
                width = AppTheme.dimensions.borderThin,
                color = AppTheme.colors.outline,
                shape = AppTheme.shapes.medium
            )
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                role = Role.DropdownList,
                onClick = onClick
            )
            .padding(
                horizontal = AppTheme.dimensions.space16,
                vertical = AppTheme.dimensions.space12
            ),
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            leading?.invoke()

            if (leading != null) {
                Spacer(
                    Modifier.width(
                        AppTheme.dimensions.space12
                    )
                )
            }

            Text(
                modifier = Modifier.weight(1f),
                text = value,
                style = AppTheme.typography.bodyMedium
            )

            Icon(
                icon = Icons.ChevronDown,
                size = AppTheme.dimensions.iconMedium
            )

        }
    }
}


@Composable
fun MenuItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selected: Boolean = false,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    headline: @Composable () -> Unit,
) {

    val background by animateColorAsState(
        targetValue =
            if (selected)
                AppTheme.colors.primary.copy(alpha = .12f)
            else
                Color.Transparent,
        label = "background"
    )

    val interaction = remember {
        MutableInteractionSource()
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(AppTheme.shapes.medium)
            .background(background)
            .clickable(
                enabled = enabled,
                interactionSource = interaction,
                indication = null,
                role = Role.Button,
                onClick = onClick
            )
            .padding(
                horizontal = AppTheme.dimensions.space16,
                vertical = AppTheme.dimensions.space12
            )
            .alpha(
                if (enabled) 1f else .5f
            )
            .semantics {
                role = Role.Button
                this.selected = selected
                if (!enabled) {
                    disabled()
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        leading?.invoke()

        if (leading != null) {
            Spacer(
                Modifier.width(
                    AppTheme.dimensions.space12
                )
            )
        }

        Box(
            modifier = Modifier.weight(1f)
        ) {
            headline()
        }
        if (trailing != null) {
            Spacer(
                Modifier.width(
                    AppTheme.dimensions.space12
                )
            )
            trailing()
        }
    }
}


@Composable
fun Menu(
    expanded: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {

    AnimatedVisibility(
        visible = expanded,
        enter = fadeIn(
            animationSpec = tween(180)
        ) + scaleIn(
            initialScale = .92f,
            animationSpec = tween(180)
        ),
        exit = fadeOut(
            animationSpec = tween(120)
        ) + scaleOut(
            targetScale = .92f,
            animationSpec = tween(120)
        )
    ) {

        Column(
            modifier = modifier
                .widthIn(min = 180.dp)
                .clip(AppTheme.shapes.large)
                .background(AppTheme.colors.surface)
                .border(
                    width = AppTheme.dimensions.borderThin,
                    color = AppTheme.colors.outline,
                    shape = AppTheme.shapes.large
                )
                .padding(
                    vertical = AppTheme.dimensions.space8
                )
        ) {

            content()

        }

    }

}



@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MenuPreview() {
    var expanded by remember {
        mutableStateOf(false)
    }

    var language by remember {
        mutableStateOf("English")
    }

    DocLeafScanner(darkTheme = true) {
        Box(Modifier
            .fillMaxSize()
            .background(color = Color.Gray)
            .safeContentPadding()
        ){
            Column {

                Dropdown(
                    value = language,
                    onClick = {
                        expanded = !expanded
                    },
                    leading = {
                        Icon(Icons.UserSquare)
                    }
                )

                Spacer(
                    Modifier.height(4.dp)
                )

                Menu(
                    expanded = expanded
                ) {
                    MenuItem(
                        selected = language == "English",
                        onClick = {
                            language = "English"
                            expanded = false
                        },
                        headline = {
                            Text("English")
                        }
                    )
                    MenuItem(
                        selected = language == "Hindi",
                        onClick = {
                            language = "Hindi"
                            expanded = false
                        },
                        headline = {
                            Text("Hindi")
                        }
                    )
                    MenuItem(
                        selected = language == "French",
                        onClick = {
                            language = "French"
                            expanded = false
                        },
                        headline = {
                            Text("French")
                        }
                    )
                }

            }
        }
    }
}