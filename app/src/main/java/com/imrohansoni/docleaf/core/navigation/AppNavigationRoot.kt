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
import com.imrohansoni.docleaf.features.picturePreview.ui.PicturePreviewScreen
import com.imrohansoni.docleaf.features.scanner.ui.CameraScreen
import androidx.core.net.toUri


fun getSampleImageUris() : List<String> {

    val img1 = "file:///android_asset/IMG1.jpeg".toUri().toString()
    val img2 = "file:///android_asset/IMG2.jpeg".toUri().toString()
    val img3 = "file:///android_asset/IMG3.jpeg".toUri().toString()
    val img4 = "file:///android_asset/IMG4.jpeg".toUri().toString()
    val img5 = "file:///android_asset/IMG5.jpeg".toUri().toString()

    return listOf(img1, img2, img3, img4, img5)
}

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

                is Screen.PicturePreview -> {
                    NavEntry(key = key) {
                        PicturePreviewScreen(
                            backstack = backstack,
                            imageUri = key.imageUri
                        )
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

//                Screen.EdgeDetectionPipeline -> {
//                    NavEntry(key = key) {
//                        EdgeDetectionPipelineScreen(
//
//                        )
//                    }
//                }


                else -> throw Exception("Invalid screen ")
            }
        })
}