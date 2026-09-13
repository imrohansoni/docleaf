package com.imrohansoni.docleaf.features.scanner.model

sealed interface ShutterState {
    data object Idle : ShutterState
    /** [secondsLeft] shows 3, 2, 1. [progress] 0f..1f fills the ring. */
    data class Countdown(val secondsLeft: Int, val progress: Float) : ShutterState
    data object Capturing : ShutterState
}