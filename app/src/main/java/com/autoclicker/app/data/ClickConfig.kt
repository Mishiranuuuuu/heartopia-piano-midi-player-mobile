package com.autoclicker.app.data

/**
 * Configuration for the auto clicker.
 *
 * @param x The x-coordinate of the click target (in screen pixels).
 * @param y The y-coordinate of the click target (in screen pixels).
 * @param intervalMs The interval between clicks in milliseconds.
 * @param repeatCount Number of times to repeat. -1 means infinite.
 */
data class ClickConfig(
    val x: Float = 540f,
    val y: Float = 960f,
    val intervalMs: Long = 1000L,
    val repeatCount: Int = -1,
    val layoutType: LayoutType = LayoutType.LAYOUT_5COL,
    val gridScaleX: Float = 1.0f,
    val gridScaleY: Float = 1.0f,
    val gridOffsetX: Float = 0f,
    val gridOffsetY: Float = 400f,
    val midiFileUri: String? = null,
    val midiFileName: String? = null,
    val midiSpeedMultiplier: Float = 1.0f
) {
    companion object {
        const val INFINITE = -1
        const val MIN_INTERVAL_MS = 50L
        const val MAX_INTERVAL_MS = 10000L
        const val MIN_GRID_SCALE = 0.3f
        const val MAX_GRID_SCALE = 3.0f
        const val MIN_MIDI_SPEED = 0.25f
        const val MAX_MIDI_SPEED = 3.0f
    }
}
