package com.imrohansoni.docleaf.features.editor.ui.toolbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


data class Filter(
    val title : String,

)


@Composable
fun FilterAdjuster(modifier: Modifier = Modifier) {

}


@Composable
fun DocumentFilter(modifier: Modifier = Modifier) {
    Column(verticalArrangement = Arrangement.Center){
        Box(){

        }
    }
}

@Composable
fun FilterToolbar(modifier: Modifier = Modifier) {
    Box(Modifier.fillMaxWidth().height(300.dp)){

    }
}