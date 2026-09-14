package com.imrohansoni.docleaf.core.components.feedback

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imrohansoni.docleaf.core.components.IconResource
import com.imrohansoni.docleaf.core.theme.AppTheme
import com.imrohansoni.docleaf.core.theme.DocLeafScanner
import kotlinx.coroutines.delay
import com.imrohansoni.docleaf.core.components.Icons
import com.imrohansoni.docleaf.core.components.basic.Icon
import com.imrohansoni.docleaf.core.components.basic.Text
import com.imrohansoni.docleaf.core.components.input.Button


@Immutable
enum class MessageType {
    Success,
    Error,
    Warning,
    Info
}

@Immutable
private data class MessageStyle(
    val icon: IconResource,
    val color: Color,
    val duration: Long,
)

@Composable
private fun messageStyle(
    type: MessageType,
): MessageStyle {

    val colors = AppTheme.colors

    return when (type) {

        MessageType.Success -> MessageStyle(
            icon = Icons.Check,
            color = colors.success,
            duration = 2500L
        )

        MessageType.Error -> MessageStyle(
            icon = Icons.Alert,
            color = colors.error,
            duration = 5000L
        )

        MessageType.Warning -> MessageStyle(
            icon = Icons.Warning,
            color = colors.warning,
            duration = 4000L
        )

        MessageType.Info -> MessageStyle(
            icon = Icons.Bell,
            color = colors.primary,
            duration = 3000L
        )

    }

}

@Composable
fun MessageBar(
    visible: Boolean,
    message: String,
    messageType: MessageType,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val style = messageStyle(messageType)
    val dimensions = AppTheme.dimensions

    LaunchedEffect(visible, message) {
        if (visible) {
            delay(style.duration)
            onDismiss()
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {

        AnimatedVisibility(
            visible = visible,
            enter =
                slideInVertically(
                    initialOffsetY = { fullHeight -> fullHeight },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                ) + fadeIn(),
            exit =
                slideOutVertically(
                    targetOffsetY = { fullHeight -> fullHeight },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) + fadeOut()
        ) {

            Row(
                modifier = Modifier
                    .padding(
                        horizontal = dimensions.space24,
                        vertical = dimensions.space24
                    )
                    .fillMaxWidth()
                    .widthIn(max = 520.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = AppTheme.shapes.large
                    )
                    .clip(AppTheme.shapes.large)
                    .background(AppTheme.colors.surface)
                    .border(
                        width = dimensions.borderThin,
                        color = AppTheme.colors.outline,
                        shape = AppTheme.shapes.large
                    )
                    .semantics {
                        liveRegion = LiveRegionMode.Polite
                        contentDescription = message
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .width(8.dp)
                        .height(52.dp)
                        .background(style.color)
                )

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            horizontal = dimensions.space16,
                            vertical = dimensions.space12
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        dimensions.space12
                    )
                ) {

                    Icon(
                        icon = style.icon,
                        tint = style.color,
                        size = dimensions.iconMedium
                    )

                    Text(
                        modifier = Modifier.weight(1f),
                        text = message,
                        style = AppTheme.typography.bodyMedium,
                        color = AppTheme.colors.onSurface
                    )

                }

            }

        }

    }

}


@Preview(
    showBackground = true,
    showSystemUi = true,
    backgroundColor = 0xFF111111
)
@Composable
private fun MessageBarPreview() {

    var visible by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(MessageType.Success) }

    DocLeafScanner(darkTheme = true) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.background)
                .safeContentPadding()
        ) {

            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Button(
                    onClick = {
                        type = MessageType.Success
                        message = "Download completed successfully."
                        visible = true
                    }
                ) {
                    Text("Success")
                }

                Button(
                    onClick = {
                        type = MessageType.Error
                        message = "Unable to download the file."
                        visible = true
                    }
                ) {
                    Text("Error")
                }

                Button(
                    onClick = {
                        type = MessageType.Warning
                        message = "Your storage is almost full."
                        visible = true
                    }
                ) {
                    Text("Warning")
                }

                Button(
                    onClick = {
                        type = MessageType.Info
                        message = "Checking for updates..."
                        visible = true
                    }
                ) {
                    Text("Info")
                }

            }

            MessageBar(
                modifier = Modifier
                    .align(Alignment.BottomCenter),
                visible = visible,
                message = message,
                messageType = type,
                onDismiss = {
                    visible = false
                }
            )
        }
    }
}