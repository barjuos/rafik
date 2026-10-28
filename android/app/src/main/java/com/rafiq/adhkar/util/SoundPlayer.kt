package com.rafiq.adhkar.util

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.rafiq.adhkar.R

/**
 * Two short feedback sounds, mirroring the web app's single-completion and
 * completion MP3s.
 */
class SoundPlayer(context: Context) {
    private val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(2)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build(),
        )
        .build()

    private val tickId: Int = soundPool.load(context, R.raw.single_completion, 1)
    private val completionId: Int = soundPool.load(context, R.raw.completion_sound, 1)

    fun playTick() {
        soundPool.play(tickId, 1f, 1f, 1, 0, 1f)
    }

    fun playCompletion() {
        soundPool.play(completionId, 1f, 1f, 1, 0, 1f)
    }

    fun release() {
        soundPool.release()
    }
}
