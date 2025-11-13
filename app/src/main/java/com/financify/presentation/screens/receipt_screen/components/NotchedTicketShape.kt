package com.financify.presentation.screens.receipt_screen.components

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class NotchedTicketShape(
    private val cornerRadius: Float = 0.dp.value, // Standard corner radius
    private val notchHeight: Float = 22.dp.value,    // Height of the triangular notch
    private val notchWidth: Float = 27.dp.value,    // Width of the triangular notch base
    private val notchSpacing: Float = 0.dp.value   // Space between notches
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = Path().apply {
                // Convert Dp values to Pixel values based on density
                val cornerRadiusPx = with(density) { cornerRadius.dp.toPx() }
                val notchHeightPx = with(density) { notchHeight.dp.toPx() }
                val notchWidthPx = with(density) { notchWidth.dp.toPx() }
                val notchSpacingPx = with(density) { notchSpacing.dp.toPx() }

                // --- 4. Draw Bottom Edge (Line with Notches) ---
                var currentXBottom = cornerRadiusPx

                while (currentXBottom < size.width - cornerRadiusPx) {
                    val nextNotchStart = currentXBottom + notchSpacingPx
                    val notchPeakX = nextNotchStart + notchWidthPx / 2
                    val notchEndX = nextNotchStart + notchWidthPx

                    if (notchEndX < size.width - cornerRadiusPx) {
                        // Draw line segment before triangle
                        lineTo(nextNotchStart, size.height)

                        // Draw the triangle pointing upwards (cutting into the shape from the bottom)
                        lineTo(notchPeakX, size.height + notchHeightPx) // Peak is below the edge
                        lineTo(notchEndX, size.height) // Back to the edge

                        currentXBottom = notchEndX
                    } else {
                        // Line to the end of the straight part before the corner
                        lineTo(size.width - cornerRadiusPx, size.height)
                        break
                    }
                }

                // Draw Top Right Corner (Arc)
                arcTo(
                    rect = Rect(
                        size.width - 2 * cornerRadiusPx,
                        0f,
                        size.width,
                        2 * cornerRadiusPx
                    ),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                close()
            }
        )
    }
}