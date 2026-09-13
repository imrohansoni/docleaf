package com.imrohansoni.docleaf

import android.app.Application
import android.util.Log
import org.opencv.android.OpenCVLoader

class DocLeafApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (!OpenCVLoader.initLocal()) {
            Log.e("OpenCV", "OpenCV failed to load")
        }
    }
}