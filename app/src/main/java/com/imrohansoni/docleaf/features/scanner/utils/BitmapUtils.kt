package com.imrohansoni.docleaf.features.scanner.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface

fun Bitmap.rotate(degrees: Float): Bitmap {
    if (degrees == 0f) return this
    val m = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, m, true)
}

/**
 * Loads a gallery image safely:
 *  1. Downsamples big photos to ~2500px max (a 50MP photo would crash the app otherwise)
 *  2. Applies the EXIF rotation (phones often store photos sideways + a rotation tag)
 */
object GalleryImageLoader {

    private const val MAX_DIMENSION = 2500

    fun load(context: Context, uri: Uri): Bitmap? {
        return try {
            // Pass 1: read only the dimensions, no pixels
            val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            context.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, bounds)
            }
            if (bounds.outWidth <= 0) return null

            // Work out how much to shrink (powers of 2)
            var sampleSize = 1
            while (maxOf(bounds.outWidth, bounds.outHeight) / (sampleSize * 2) >= MAX_DIMENSION) {
                sampleSize *= 2
            }

            // Pass 2: actually decode, shrunk
            val opts = BitmapFactory.Options().apply { inSampleSize = sampleSize }
            val bitmap = context.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, opts)
            } ?: return null

            // Fix orientation from EXIF
            val rotation = context.contentResolver.openInputStream(uri)?.use { stream ->
                when (ExifInterface(stream).getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                    ExifInterface.ORIENTATION_ROTATE_90  -> 90f
                    ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                    ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                    else -> 0f
                }
            } ?: 0f

            bitmap.rotate(rotation)
        } catch (e: Exception) {
            null
        }
    }
}