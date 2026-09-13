package com.imrohansoni.docleaf.features.login.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.imrohansoni.docleaf.core.navigation.Screen

@Composable
fun LoginScreen(
    backstack: NavBackStack<NavKey>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column {
            LoginWithGoogleButton {

            }
            Spacer(Modifier.height(12.dp))
            BasicText(
                "By Logging in you are agree to our terms and privacy policy",
                style = TextStyle(color = Color.White, textAlign = TextAlign.Center)
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    val backstack: NavBackStack<NavKey> = rememberNavBackStack(Screen.Login)
    LoginScreen(backstack)
}