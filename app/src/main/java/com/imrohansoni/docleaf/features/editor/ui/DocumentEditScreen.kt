package com.imrohansoni.docleaf.features.editor.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.imrohansoni.docleaf.core.components.AppIcon
import com.imrohansoni.docleaf.core.components.Icon
import com.imrohansoni.docleaf.core.components.IconButton
import com.imrohansoni.docleaf.core.components.Icons
import androidx.core.net.toUri
import com.imrohansoni.docleaf.core.components.Slider
import com.imrohansoni.docleaf.features.editor.rememberDocumentPagerState


enum class EditTool(
    val title: String,
    val icon: AppIcon
) {
    NEW("New", Icons.CropPlus),
    FILTERS("Filters", Icons.MagicWand),
    CROP("Crop", Icons.Crop),
    ROTATE("Rotate", Icons.Rotate),
    ADJUST("Adjust", Icons.Controller),
    WATERMARK("Watermark", Icons.EditText),
    DRAW("Draw", Icons.Pen),
    HIGHLIGHTER("Highlighter", Icons.Highlighter),
    TEXT("Text", Icons.TextSquare),
    IMAGE("Image", Icons.Image),
    SIGNATURE("Signature", Icons.Signature),
    REARRANGE("Rearrange", Icons.ArrowAllDirection),
}


@Composable
fun EditToolItem(
    tool: EditTool,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(72.dp)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            icon = tool.icon,
            color = Color.White
        )

        Spacer(Modifier.height(6.dp))

        BasicText(
            text = tool.title,
            style = TextStyle(color = Color.White)
        )
    }
}


@Composable
fun EditToolBar(
    selectedTool: EditTool,
    onToolSelected: (EditTool) -> Unit
) {

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {

        items(EditTool.entries) { tool ->

            EditToolItem(
                tool = tool,
                selected = tool == selectedTool,
                onClick = {
                    onToolSelected(tool)
                }
            )
        }
    }
}


@Composable
fun DocumentEditScreen(
    modifier: Modifier = Modifier,
    backstack: NavBackStack<NavKey>,
    imageUris : List<String>
) {
    var selectedTool by remember {
        mutableStateOf(EditTool.REARRANGE)
    }

    val value = remember { mutableStateOf(0.5f) }


    var currentImageUriIndex: Int by remember {
        mutableIntStateOf(0)
    }

    val pagerState = rememberDocumentPagerState()


    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(icon = Icons.ArrowLeft) {

                }
                Spacer(Modifier.width(8.dp))

                BasicText(
                    "Doc-Leaf-Scanner-24-June-26",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.White,
                        textDecoration = TextDecoration.Underline
                    )
                )
            }


            IconButton(icon = Icons.Download) {

            }
        }

//        Slider(value = value.value, onValueChange = {
//            value.value = it
//        }, onValueChangeFinished = { })

        Slider(value = value.value, onValueChange = {
            value.value = it
        })


        DocumentPager(
            pageCount = imageUris.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->

            AsyncImage(
                model = imageUris[currentImageUriIndex].toUri(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )

        }

        Column() {
            Spacer(Modifier.height(24.dp))
            Row{




            }
            Row() {
                IconButton(icon = Icons.Undo) {}
                IconButton(icon = Icons.Redo) { }
            }
            Spacer(Modifier.height(24.dp))
            EditToolBar(selectedTool = selectedTool) {
                selectedTool = it
            }
        }

    }
}