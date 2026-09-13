package com.imrohansoni.docleaf.utils

import android.util.Log

/**
 * Central logger. One place to control log format, filtering, and (later)
 * routing to a file or crash-reporting service.
 *
 * Usage: AppLogger.d(TAG, "message") / .e(TAG, "message", throwable)
 */
object AppLogger {

    // Flip to false for release builds to silence debug/info logs.
    // (Errors always log.)
    var debugEnabled: Boolean = true

    // Prefix so all app logs are filterable by one string in Logcat.
    private const val PREFIX = "PDFScanner"

    fun d(tag: String, message: String) {
        if (debugEnabled) Log.d("$PREFIX/$tag", message)
    }

    fun i(tag: String, message: String) {
        if (debugEnabled) Log.i("$PREFIX/$tag", message)
    }

    fun w(tag: String, message: String, throwable: Throwable? = null) {
        Log.w("$PREFIX/$tag", message, throwable)
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        Log.e("$PREFIX/$tag", message, throwable)
        // Later: forward to a file or Crashlytics here — single choke point.
    }
}