package com.imrohansoni.docleaf.utils

/**
 * Typed errors for the scanner, so callers know WHAT failed, not just that
 * something did. Extend as needed.
 */
sealed class AppError(val message: String, val cause: Throwable? = null) {
    class ModelLoad(val modelName: String, cause: Throwable?) :
        AppError("Failed to load model '$modelName'", cause)

    class Inference(val stage: String, cause: Throwable?) :
        AppError("Inference failed during '$stage'", cause)

    class ImageProcessing(val stage: String, cause: Throwable?) :
        AppError("Image processing failed during '$stage'", cause)

    class GalleryLoad(cause: Throwable?) :
        AppError("Could not load the selected image", cause)

    class Save(cause: Throwable?) :
        AppError("Could not save the document", cause)

    class Unknown(cause: Throwable?) :
        AppError("Unexpected error", cause)
}

/**
 * Central error handler. Every catch block calls this — it logs consistently
 * (with all context) and returns a user-facing message. One place to later add
 * crash reporting, analytics, or user-facing dialogs.
 */
object ErrorHandler {

    private const val TAG = "ErrorHandler"

    /**
     * Log an error with full detail and get a short message safe to show the user.
     * @param context extra key-values for debugging (device, image size, etc.)
     */
    fun handle(error: AppError, context: Map<String, Any?> = emptyMap()): String {
        val details = buildString {
            append(error.message)
            if (context.isNotEmpty()) {
                append(" | context: ")
                append(context.entries.joinToString(", ") { "${it.key}=${it.value}" })
            }
        }
        AppLogger.e(TAG, details, error.cause)
        return userMessage(error)
    }

    // What the user actually sees — never a stack trace.
    private fun userMessage(error: AppError): String = when (error) {
        is AppError.ModelLoad -> "Scanner failed to start. Please reinstall the app."
        is AppError.Inference -> "Couldn't detect the document. Try again or crop manually."
        is AppError.ImageProcessing -> "Couldn't process the image. Please try again."
        is AppError.GalleryLoad -> "Couldn't open that image. Try a different one."
        is AppError.Save -> "Couldn't save. Check storage space and try again."
        is AppError.Unknown -> "Something went wrong. Please try again."
    }
}