package com.rafiq.adhkar.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * The custom Kaaba mark used in the web app's header, ported from
 * src/components/icons.tsx.
 */
val KaabaIcon: ImageVector by lazy {
    ImageVector.Builder(
        name = "Kaaba",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.6f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(4f, 4f)
            lineTo(20f, 4f)
            lineTo(20f, 20f)
            lineTo(4f, 20f)
            close()

            moveTo(18f, 4f)
            lineTo(12f, 10f)
            lineTo(6f, 4f)

            moveTo(6f, 20f)
            lineTo(12f, 14f)
            lineTo(18f, 20f)

            moveTo(4f, 12f)
            lineTo(20f, 12f)
        }
    }.build()
}
