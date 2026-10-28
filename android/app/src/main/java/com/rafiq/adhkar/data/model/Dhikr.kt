package com.rafiq.adhkar.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

/**
 * A single dhikr. `id` is only unique inside its category (the source data has
 * two entries with id 83, one in after-prayer and one in relief), so [key] is
 * the stable identity used by the UI.
 */
@Keep
@Serializable
data class Dhikr(
    val id: Int,
    val category: String,
    val content: String,
    val count: Int,
    val description: String = "",
    val reference: String = "",
) {
    val key: String get() = "$category-$id"
}
