package com.imrohansoni.docleaf.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.imrohansoni.docleaf.features.login.ui.LoginScreen
import com.imrohansoni.docleaf.features.onboarding.ui.OnboardingScreen
import com.imrohansoni.docleaf.features.scanner.ui.CameraScreen

@Composable
fun AppNavigationRoot(modifier: Modifier = Modifier) {

    val backstack: NavBackStack<NavKey> = rememberNavBackStack(Screen.Camera)

    NavDisplay(
        modifier = modifier,
        onBack = { backstack.removeLastOrNull() },
        backStack = backstack,
        entryProvider = { key ->
            when (key) {
                Screen.Login -> {
                    NavEntry(key = key) {
                        LoginScreen(
                            backstack = backstack
                        )
                    }
                }

                Screen.Onboarding -> {
                    NavEntry(key = key) {
                        OnboardingScreen(
                            backstack = backstack
                        )
                    }
                }

                Screen.Main -> {
                    NavEntry(key = key) {
                        MainScreen(
                            backstack = backstack
                        )
                    }
                }

                Screen.Camera -> {
                    NavEntry(key = key) {
                    CameraScreen(
//                        backstack = backstack,
                        onPagesReady = {},
                        onPayload = {}
                    )
//                        DocumentScannerScreen({}, {})
                    }
                }


                is Screen.DocumentEdit -> {
                    NavEntry(key = key) {
//                        DocumentEditScreen(
//                            backstack = backstack,
//                            imageUris = key.imageUris
//                        )
//                        CameraScreen(backstack = backstack)
                    }
                }



                else -> throw Exception("Invalid screen ")
            }
        })
}