package com.imrohansoni.docleaf.features.picturePreview.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import coil.compose.AsyncImage
import com.imrohansoni.docleaf.core.components.Button
import com.imrohansoni.docleaf.core.components.IconButton
import com.imrohansoni.docleaf.core.components.Icons
import androidx.core.net.toUri

@Composable
fun PicturePreviewScreen(
    modifier: Modifier = Modifier,
    backstack: NavBackStack<NavKey>,
    imageUri: String) {

    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(icon = Icons.ArrowLeft) {

                }
                Spacer(Modifier.width(8.dp))

                BasicText("Doc-Leaf-Scanner-24-June-26", style = TextStyle(fontSize = 14.sp, color = Color.White, textDecoration = TextDecoration.Underline))
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth().aspectRatio(3f / 4f)
        ) {
            Box {
                AsyncImage(
                    model = imageUri.toUri(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
        BasicText(modifier = Modifier.padding(16.dp), text = "Drag the handle in the picture to adjust the trimming range, or use the clipping tool later to adjust", style = TextStyle(fontSize = 14.sp, color = Color.White))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
        ) {
            Spacer(Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(Modifier.weight(1f), text = "retake") {
                }
                Spacer(Modifier.width(24.dp))
                Button(Modifier.weight(1f), text = "Continue") {
                    //backstack.add(Screen.DocumentEdit(imageUri))
                }
            }
        }
    }
}
