package com.imrohansoni.docleaf.core.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.imrohansoni.docleaf.core.components.Icons
import com.imrohansoni.docleaf.core.components.basic.Text
import com.imrohansoni.docleaf.core.components.input.RoundIconButton
import com.imrohansoni.docleaf.core.theme.AppTheme


@Composable
fun TopBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    navigationIcon: (@Composable (() -> Unit))? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .height(AppTheme.dimensions.topBarHeight)
            .padding(horizontal = AppTheme.dimensions.space16)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            when {
                navigationIcon != null -> {
                    navigationIcon()
                    Spacer(Modifier.width(AppTheme.dimensions.space12))
                }
                onBack != null -> {
                    RoundIconButton(
                        icon = Icons.ArrowLeft,
                        contentDescription = "Back",
                        size = AppTheme.dimensions.iconLarge,
                        onClick = onBack,
                    )
                    Spacer(Modifier.width(AppTheme.dimensions.space12))
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = title, style = AppTheme.typography.titleLarge, maxLines = 1)
                if (subtitle != null) {
                    Spacer(Modifier.height(AppTheme.dimensions.space2))
                    Text(
                        text = subtitle,
                        style = AppTheme.typography.bodySmall,
                        color = AppTheme.colors.onDisabled,
                        maxLines = 1
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.space4),
                verticalAlignment = Alignment.CenterVertically,
                content = actions
            )
        }
    }
}

//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//private fun TopBarPreview() {
//    AppTheme(darkTheme = true) {
//        Box(Modifier
//            .fillMaxSize()
//            .background(Color.Black)
//            .safeDrawingPadding()) {
//            TopBar(
//                title = "Downloads",
//                navigationIcon = {
//                    IconButton(
//                        icon = Icons.ArrowLeft,
//                        contentDescription = "Back",
//                        onClick = {
//
//                        }
//                    )
//                },
//                actions = {
//
//                    IconButton(
//                        icon = Icons.Magnifier,
//                        contentDescription = "Search",
//                        onClick = {}
//                    )
//
//                    Spacer(Modifier.width(AppTheme.dimensions.space12))
//
//
//                    IconButton(
//                        icon = Icons.Settings,
//                        contentDescription = "Setting",
//                        onClick = {}
//                    )
//                }
//            )
//        }
//    }
//}