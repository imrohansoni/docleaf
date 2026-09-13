package com.imrohansoni.docleaf.core.utils

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import java.io.File

fun assetToUri(
    context: Context,
    assetName: String
): Uri {

    val file = File(context.cacheDir, assetName.substringAfterLast('/'))

    if (!file.exists()) {
        context.assets.open(assetName).use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }

    return file.toUri()
}