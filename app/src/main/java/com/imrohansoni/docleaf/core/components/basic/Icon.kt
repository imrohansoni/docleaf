package com.imrohansoni.docleaf.core.components.basic


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import com.imrohansoni.docleaf.core.components.IconResource
import com.imrohansoni.docleaf.core.theme.AppTheme


@Composable
fun Icon(
    icon: IconResource,
    modifier: Modifier = Modifier,
    size: Dp = AppTheme.dimensions.iconMedium,
    tint: Color = AppTheme.colors.onSurface,
    alpha: Float = 1f,
    rotation: Float = 0f,
    contentDescription: String? = null,
) {

    Image(
        modifier = modifier
            .size(size)
            .graphicsLayer { rotationZ = rotation }
            .alpha(alpha)
            .semantics {
                role = Role.Image
                if (contentDescription != null) {
                    this.contentDescription = contentDescription
                }
            },
        painter = painterResource(icon.resId),
        contentDescription = contentDescription,
        colorFilter = if (tint == Color.Unspecified) null else ColorFilter.tint(tint)
    )
}