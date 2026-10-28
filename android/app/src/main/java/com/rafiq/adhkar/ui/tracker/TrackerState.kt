package com.rafiq.adhkar.ui.tracker

/**
 * Pure state machine for the tap counter. Kept separate from Compose so it can
 * be unit tested.
 */
data class TrackerState(
    val index: Int = 0,
    val count: Int = 0,
    val finished: Boolean = false,
)

sealed interface TapOutcome {
    data object Ignored : TapOutcome
    data class Counting(val state: TrackerState) : TapOutcome
    data class DhikrDone(val state: TrackerState) : TapOutcome
    data class AllDone(val state: TrackerState) : TapOutcome
}

object TrackerLogic {
    fun tap(state: TrackerState, total: Int, target: Int): TapOutcome {
        if (state.finished || total <= 0) return TapOutcome.Ignored

        val safeTarget = target.coerceAtLeast(1)
        return when {
            state.count + 1 < safeTarget -> TapOutcome.Counting(state.copy(count = state.count + 1))
            state.index >= total - 1 -> TapOutcome.AllDone(
                state.copy(count = safeTarget, finished = true),
            )
            else -> TapOutcome.DhikrDone(state.copy(count = safeTarget))
        }
    }

    fun advance(state: TrackerState, total: Int): TrackerState {
        if (state.finished || state.index >= total - 1) return state
        return state.copy(index = state.index + 1, count = 0)
    }
}
