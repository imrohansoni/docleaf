package com.imrohansoni.docleaf.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen : NavKey {
    @Serializable
    data object Login : Screen(), NavKey

    @Serializable
    data object Onboarding : Screen(), NavKey

    @Serializable
    data object Main : Screen(), NavKey

    @Serializable
    data object Camera : Screen(), NavKey

    @Serializable
    data class PicturePreview(val imageUri: String) : Screen(), NavKey

    @Serializable
    data class DocumentEdit(val imageUris: List<String>) : Screen(), NavKey

    @Serializable
    data object EdgeDetectionPipeline : Screen(), NavKey
}