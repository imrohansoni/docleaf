package com.imrohansoni.docleaf.core.components.input

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.imrohansoni.docleaf.core.components.IconResource
import com.imrohansoni.docleaf.core.components.Icons
import com.imrohansoni.docleaf.core.components.basic.Icon
import com.imrohansoni.docleaf.core.components.basic.Text
import com.imrohansoni.docleaf.core.theme.AppTheme
import com.imrohansoni.docleaf.core.theme.DocLeafScanner

@Immutable
data class TextFieldColors(
    val containerColor: Color,
    val contentColor: Color,
    val placeholderColor: Color,
    val cursorColor: Color,
    val borderColor: Color,
    val focusedBorderColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
    val disabledBorderColor: Color,
    val errorBorderColor: Color,
    val iconColor: Color,
)


@Stable
object TextFieldDefaults {
    @Composable
    @ReadOnlyComposable
    fun colors(
        enabled: Boolean,
        isError: Boolean,
    ): TextFieldColors {
        val colors = AppTheme.colors
        return TextFieldColors(
            containerColor = colors.surface,
            contentColor = colors.onSurface,
            placeholderColor = colors.onDisabled,
            cursorColor = colors.primary,
            borderColor = colors.outline,
            focusedBorderColor = colors.primary,
            disabledContainerColor = colors.surface,
            disabledContentColor = colors.onDisabled,
            disabledBorderColor = colors.disabled,
            errorBorderColor = colors.error,
            iconColor = colors.onSurface
        )
    }

    @Composable
    @ReadOnlyComposable
    fun borderWidth(
        focused: Boolean,
    ): Dp {

        return if (focused)
            2.dp
        else
            AppTheme.dimensions.borderThin
    }
}


@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: IconResource? = null,
    trailingIcon: IconResource? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {

    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()

    val colors = TextFieldDefaults.colors(
        enabled = enabled,
        isError = isError
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.disabledBorderColor
            isError -> colors.errorBorderColor
            focused -> colors.focusedBorderColor
            else -> colors.borderColor
        },
        animationSpec = tween(180),
        label = "BorderColor"
    )

    val borderWidth by animateDpAsState(
        targetValue = TextFieldDefaults.borderWidth(focused),
        animationSpec = tween(180),
        label = "BorderWidth"
    )

    Box(
        modifier = modifier
            .clip(AppTheme.shapes.medium)
            .background(
                if (enabled)
                    colors.containerColor
                else
                    colors.disabledContainerColor
            )
            .border(
                borderWidth,
                borderColor,
                AppTheme.shapes.medium
            )
            .height(AppTheme.dimensions.textFieldHeight),
        contentAlignment = Alignment.Center
    ) {

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            cursorBrush = SolidColor(colors.cursorColor),
            textStyle = AppTheme.typography.bodyMedium.copy(
                color = if (enabled)
                    colors.contentColor
                else
                    colors.disabledContentColor
            ),
            modifier = Modifier
                .fillMaxWidth(),
            decorationBox = { innerTextField ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = AppTheme.dimensions.textFieldHorizontalPadding,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        AppTheme.dimensions.space12
                    )
                ) {
                    leadingIcon?.let {
                        Icon(
                            icon = it,
                            tint = if(focused) colors.iconColor else colors.placeholderColor,
                            size = AppTheme.dimensions.iconMedium
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentHeight(Alignment.CenterVertically),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = value.isEmpty(),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Text(
                                text = placeholder,
                                color = colors.placeholderColor,
                                style = AppTheme.typography.bodyMedium
                            )
                        }
                        innerTextField()
                    }
                    when {
                        trailingIcon != null -> {
                            Icon(
                                icon = trailingIcon,
                                tint = colors.iconColor,
                                size = AppTheme.dimensions.iconMedium
                            )
                        }
                        enabled &&
                                !readOnly &&
                                value.isNotEmpty() -> {
                            IconButton(
                                icon = Icons.Cross,
                                contentDescription = "Clear text",
                                onClick = {
                                    onValueChange("")
                                }
                            )
                        }
                    }
                }
            }
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TextFieldPreview() {
    var email = remember { mutableStateOf("") }

    DocLeafScanner(darkTheme = true) {
        Box(Modifier.fillMaxSize().background(Color.Black).safeContentPadding()){
            TextField(value = email.value, onValueChange = {
                email.value = it
            }, placeholder = "Email", leadingIcon = Icons.User)
        }
    }
}