package com.imrohansoni.docleaf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.imrohansoni.docleaf.core.navigation.AppNavigationRoot
import com.imrohansoni.docleaf.core.theme.DocLeafScanner


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DocLeafScanner {
                Box(modifier = Modifier.fillMaxSize().background(Color.Black).safeDrawingPadding()) {
                    AppNavigationRoot()
                }
            }
        }
    }
}