package com.rafiq.adhkar

import com.rafiq.adhkar.ui.tracker.TapOutcome
import com.rafiq.adhkar.ui.tracker.TrackerLogic
import com.rafiq.adhkar.ui.tracker.TrackerState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TrackerLogicTest {

    @Test
    fun `tap below target increments the counter`() {
        val outcome = TrackerLogic.tap(TrackerState(), total = 3, target = 3)
        assertTrue(outcome is TapOutcome.Counting)
        assertEquals(1, (outcome as TapOutcome.Counting).state.count)
        assertEquals(0, outcome.state.index)
    }

    @Test
    fun `reaching the target completes the current dhikr`() {
        val outcome = TrackerLogic.tap(TrackerState(count = 2), total = 3, target = 3)
        assertTrue(outcome is TapOutcome.DhikrDone)
        assertEquals(3, (outcome as TapOutcome.DhikrDone).state.count)
        assertEquals(0, outcome.state.index)
    }

    @Test
    fun `advance moves to the next dhikr and resets the counter`() {
        val advanced = TrackerLogic.advance(TrackerState(index = 0, count = 3), total = 3)
        assertEquals(1, advanced.index)
        assertEquals(0, advanced.count)
    }

    @Test
    fun `last dhikr finishes the session`() {
        val outcome = TrackerLogic.tap(TrackerState(index = 2, count = 0), total = 3, target = 1)
        assertTrue(outcome is TapOutcome.AllDone)
        assertTrue((outcome as TapOutcome.AllDone).state.finished)
        assertEquals(1, outcome.state.count)
    }

    @Test
    fun `advance does not move past the last dhikr`() {
        val advanced = TrackerLogic.advance(TrackerState(index = 2), total = 3)
        assertEquals(2, advanced.index)
    }

    @Test
    fun `taps are ignored after completion`() {
        val outcome = TrackerLogic.tap(TrackerState(finished = true), total = 3, target = 1)
        assertEquals(TapOutcome.Ignored, outcome)
    }

    @Test
    fun `a zero target is treated as one`() {
        val outcome = TrackerLogic.tap(TrackerState(), total = 3, target = 0)
        assertTrue(outcome is TapOutcome.DhikrDone)
        assertEquals(1, (outcome as TapOutcome.DhikrDone).state.count)
    }
}
