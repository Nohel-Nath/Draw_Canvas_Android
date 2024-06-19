package com.example.drawcanvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver

class TestCircle(context:Context, attributeSet: AttributeSet?): View(context,attributeSet) {
    companion object {
        private const val COLOR_HEX = "#E74300"
    }

    private val drawPaint: Paint = Paint().apply {
        color = Color.parseColor(COLOR_HEX)
        isAntiAlias = true
    }

    private var size: Float = 0f

    init {
        setOnMeasureCallback()
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(size, size, size, drawPaint)
    }
    private fun setOnMeasureCallback() {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                removeOnGlobalLayoutListener(this)
                size = measuredWidth / 2f
            }
        })
    }

    private fun removeOnGlobalLayoutListener(listener: ViewTreeObserver.OnGlobalLayoutListener) {
        viewTreeObserver.removeOnGlobalLayoutListener(listener)
    }
}