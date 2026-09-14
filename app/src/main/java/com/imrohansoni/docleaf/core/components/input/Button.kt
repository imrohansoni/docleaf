package com.imrohansoni.docleaf.core.components.input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import com.imrohansoni.docleaf.core.components.feedback.LoadingIndicator
import com.imrohansoni.docleaf.core.theme.AppTheme

enum class ButtonVariant { Filled, Outlined, Text }

@Immutable
data class AppButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val borderColor: Color,
)

object ButtonDefaults {
    @Composable
    @ReadOnlyComposable
    fun colors(
        variant: ButtonVariant,
        enabled: Boolean,
    ): AppButtonColors {

        val colors = AppTheme.colors

        return when (variant) {
            ButtonVariant.Filled -> {
                if (enabled) {
                    AppButtonColors(
                        containerColor = colors.primary,
                        contentColor = colors.onPrimary,
                        borderColor = Color.Transparent
                    )
                } else {
                    AppButtonColors(
                        containerColor = colors.disabled,
                        contentColor = colors.onDisabled,
                        borderColor = Color.Transparent
                    )
                }
            }

            ButtonVariant.Outlined -> {
                if (enabled) {
                    AppButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = colors.onSurface,
                        borderColor = colors.outline
                    )

                } else {
                    AppButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = colors.onDisabled,
                        borderColor = colors.disabled
                    )
                }
            }

            ButtonVariant.Text -> {
                if (enabled) {
                    AppButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = colors.primary,
                        borderColor = Color.Transparent
                    )
                } else {
                    AppButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = colors.onDisabled,
                        borderColor = Color.Transparent
                    )
                }
            }
        }
    }
}

@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    variant: ButtonVariant = ButtonVariant.Filled,
    content: @Composable RowScope.() -> Unit,
) {

    val interactionSource = remember {
        MutableInteractionSource()
    }

    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) .97f else 1f,
        animationSpec = spring(
            stiffness = Spring.StiffnessMediumLow,
            dampingRatio = Spring.DampingRatioMediumBouncy
        ),
        label = "buttonScale"
    )

    val colors = ButtonDefaults.colors(
        variant = variant,
        enabled = enabled
    )

    val contentAlpha by animateFloatAsState(
        targetValue = if (loading) 0f else 1f,
        animationSpec = tween(150),
        label = "contentAlpha"
    )

    Box(
        modifier = modifier
            .scale(scale)
            .clip(AppTheme.shapes.medium)
            .background(colors.containerColor)
            .border(
                BorderStroke(
                    width = AppTheme.dimensions.borderThin,
                    color = colors.borderColor
                ),
                shape = AppTheme.shapes.medium
            )
            .clickable(
                enabled = enabled && !loading,
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = onClick
            )
            .defaultMinSize(
                minHeight = AppTheme.dimensions.buttonHeight
            )
            .semantics {
                role = Role.Button

                if (!enabled)
                    disabled()
            }
            .padding(
                horizontal = AppTheme.dimensions.space20,
                vertical = AppTheme.dimensions.space12
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.alpha(contentAlpha),
                horizontalArrangement = Arrangement.spacedBy(
                    AppTheme.dimensions.space8
                ),
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )

            AnimatedVisibility(
                visible = loading,
                enter = fadeIn(animationSpec = tween(150)),
                exit = fadeOut(animationSpec = tween(100))
            ) {
                LoadingIndicator(
                    modifier = Modifier.size(
                        AppTheme.dimensions.loadingMedium
                    ),
                    color = colors.contentColor
                )
            }
        }
    }
}
