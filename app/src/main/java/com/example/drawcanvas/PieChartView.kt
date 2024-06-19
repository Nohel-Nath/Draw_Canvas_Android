package com.example.drawcanvas

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class PieChartView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

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

    private val cyanPaint=Paint().apply{
        color = Color.CYAN
        style = Paint.Style.FILL_AND_STROKE
    }

    private val textPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL_AND_STROKE
        textSize = 50f
        textAlign = Paint.Align.CENTER
    }

    private val rectF = RectF(0f, 0f, 1000f, 1000f)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
        val canvasBitmap = Canvas(bitmap)

        // Draw the red arc
//        canvasBitmap.drawArc(rectF, 0f, 60f, true, redPaint)
//        drawTextOnArc(canvasBitmap, 30.0, "60")
        val redSweepAngle = 216f
        canvasBitmap.drawArc(rectF, 0f, redSweepAngle, true, redPaint)
        drawTextOnArc(canvasBitmap, 0f, redSweepAngle, "60%")

        // Draw the blue arc
//        canvasBitmap.drawArc(rectF, 60f, 50f, true, bluePaint)
//        drawTextOnArc(canvasBitmap, 60 + 25.0, "50")
        val blueStartAngle = redSweepAngle
        val blueSweepAngle = 36f
        canvasBitmap.drawArc(rectF, blueStartAngle, blueSweepAngle, true, bluePaint)
        drawTextOnArc(canvasBitmap,blueStartAngle, blueSweepAngle, "10%")

        // Draw the yellow arc
//        canvasBitmap.drawArc(rectF, 110f, 70f, true, yellowPaint)
//        drawTextOnArc(canvasBitmap, 110 + 35.0, "70")
        val yellowStartAngle = blueStartAngle + blueSweepAngle
        val yellowSweepAngle = 90f
        canvasBitmap.drawArc(rectF, yellowStartAngle, yellowSweepAngle, true, yellowPaint)
        drawTextOnArc(canvasBitmap,yellowStartAngle, yellowSweepAngle, "25%")

        // Draw the cyan arc
//        canvasBitmap.drawArc(rectF, 110f, 70f, true, yellowPaint)
//        drawTextOnArc(canvasBitmap, 110 + 35.0, "70")
        val cyanStartAngle = yellowStartAngle + yellowSweepAngle
        val cyanSweepAngle = 18f
        canvasBitmap.drawArc(rectF, cyanStartAngle, cyanSweepAngle, true, cyanPaint)
        drawTextOnArc(canvasBitmap,cyanStartAngle, cyanSweepAngle, "5%")

        // Draw the bitmap on the canvas
        canvas.drawBitmap(bitmap, 0f, 0f, null)
    }

    private fun drawTextOnArc(canvas: Canvas, startAngle: Float, sweepAngle: Float, text: String) {
//        val angle = Math.toRadians(angleDegrees.toDouble())
//        val x = (rectF.centerX() + 300 * Math.cos(angle)).toFloat()
//        val y = (rectF.centerY() + 300 * Math.sin(angle)).toFloat()
//        canvas.drawText(text, x, y, textPaint)
        val angleDegrees = startAngle + sweepAngle / 2
        val angle = Math.toRadians(angleDegrees.toDouble())
        val x = (rectF.centerX() + 400 * Math.cos(angle)).toFloat()
        val y = (rectF.centerY() + 400 * Math.sin(angle)).toFloat()

        // Draw the text centered
        canvas.drawText(text, x, y, textPaint)
    }
}