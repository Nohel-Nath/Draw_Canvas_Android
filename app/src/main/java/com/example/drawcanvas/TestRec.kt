package com.example.drawcanvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class TestRec(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {
    private val paintRed = Paint().apply {
        color = Color.RED
        strokeWidth = 10f
        style = Paint.Style.FILL_AND_STROKE
    }

    private val paintBlue = Paint().apply {
        color = Color.BLUE
        strokeWidth = 10f
        style = Paint.Style.FILL_AND_STROKE
    }

    private var isRed = true

    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)
        val x1=0f
        val y1=0f
        val x2=width.toFloat()
        val y2=height.toFloat()
        if (isRed) {
            // Draw the upper half rectangle in red
            canvas.drawRect(x1, y1, x2, y2 / 2, paintRed)

            // Draw the lower half rectangle in blue
            canvas.drawRect(x1, y2 / 2, x2, y2, paintBlue)
        } else {
            // Draw the upper half rectangle in blue
            canvas.drawRect(x1, y1, x2, y2 / 2, paintBlue)

            // Draw the lower half rectangle in red
            canvas.drawRect(x1, y2 / 2, x2, y2, paintRed)
        }
    }

    fun toggleColors() {
        isRed = !isRed
        invalidate() // Request a redraw
    }
}