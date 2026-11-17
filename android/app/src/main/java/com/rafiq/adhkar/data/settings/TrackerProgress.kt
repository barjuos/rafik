package com.rafiq.adhkar.data.settings

/**
 * Saved progress of a dhikr list (tracker).
 *
 * @param index zero-based index of the dhikr the user is currently on
 * @param count how many taps have already been counted for that dhikr
 */
data class TrackerProgress(
    val index: Int = 0,
    val count: Int = 0,
)