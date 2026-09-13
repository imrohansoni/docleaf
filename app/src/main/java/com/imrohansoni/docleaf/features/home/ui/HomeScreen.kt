package com.imrohansoni.docleaf.features.home.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat


private fun hasCameraPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val listState = rememberSaveable(
        saver = LazyListState.Saver
    ) {
        LazyListState()
    }

    var showCamera by remember {
        mutableStateOf(false)
    }

    var hasPermission by remember {
        mutableStateOf(
            hasCameraPermission(context)
        )
    }

    val launcher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            hasPermission = granted
        }

    Box(Modifier.fillMaxSize().background(Color.Black).padding(12.dp), contentAlignment = Alignment.Center){
        LazyRow(state = listState) {
            items(100){
                Column{
                    BasicText("item $it")
                }
            }
        }
    }
}


