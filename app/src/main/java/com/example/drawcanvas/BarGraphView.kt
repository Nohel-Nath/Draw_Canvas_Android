package com.example.drawcanvas

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class BarGraphView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val redPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL_AND_STROKE
    }

    private val bluePaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL_AND_STROKE
    }

    private val yellowPaint = Paint().apply {
        color = Color.YELLOW
        style = Paint.Style.FILL_AND_STROKE
    }

    private val cyanPaint = Paint().apply {
        color = Color.CYAN
        style = Paint.Style.FILL_AND_STROKE
    }

    private val textPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL_AND_STROKE
        textSize = 50f
        textAlign = Paint.Align.CENTER
    }

    private val axisPaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width.toFloat()
        val height = height.toFloat()

        val barWidth = width / 10
        val maxBarHeight = height * 0.8f
        val bottom = height * 0.9f
        // Draw Y-axis
        canvas.drawLine(barWidth, bottom, barWidth, bottom - maxBarHeight, axisPaint)

        // Draw X-axis
        canvas.drawLine(barWidth, bottom, width, bottom, axisPaint)
        // Draw percentage labels on Y-axis
        for (i in 1..5) {
            val y = bottom - maxBarHeight * i / 5
            canvas.drawLine(barWidth - 10, y, barWidth, y, axisPaint)
            canvas.drawText("${i * 20}%", barWidth - 50, y + 15, textPaint)
        }

        // Draw the red bar (60%)
        val redHeight = maxBarHeight * .6f
        canvas.drawRect(barWidth, bottom - redHeight, barWidth * 2, bottom, redPaint)
        canvas.drawText("60%", barWidth * 1.5f, bottom - redHeight - 20, textPaint)

        // Draw the blue bar (10%)
        val blueHeight = maxBarHeight * 0.10f
        canvas.drawRect(barWidth * 2.5f, bottom - blueHeight, barWidth * 3.5f, bottom, bluePaint)
        canvas.drawText("10%", barWidth * 3f, bottom - blueHeight - 20, textPaint)

        // Draw the yellow bar (25%)
        val yellowHeight = maxBarHeight * 0.25f
        canvas.drawRect(barWidth * 4f, bottom - yellowHeight, barWidth * 5f, bottom, yellowPaint)
        canvas.drawText("25%", barWidth * 4.5f, bottom - yellowHeight - 20, textPaint)

        // Draw the cyan bar (5%)
        val cyanHeight = maxBarHeight * 0.05f
        canvas.drawRect(barWidth * 5.5f, bottom - cyanHeight, barWidth * 6.5f, bottom, cyanPaint)
        canvas.drawText("5%", barWidth * 6f, bottom - cyanHeight - 20, textPaint)
    }
}