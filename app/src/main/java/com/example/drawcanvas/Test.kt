package com.example.drawcanvas

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class Test(context: Context,attributeSet: AttributeSet):View(context,attributeSet) {

    private val paint = Paint().apply {
        color = Color.RED
        strokeWidth = 10f
        //strokeCap = Paint.Cap.SQUARE
        style=Paint.Style.FILL_AND_STROKE
    }
    //private var animatedValue = 0f

//    init {
//        // Set up the animation
//        val animator = ValueAnimator.ofFloat(0f, 1f).apply {
//            duration = 2000 // Duration in milliseconds
//            interpolator = LinearInterpolator()
//            addUpdateListener { animation ->
//                this@Test.animatedValue = animation.animatedValue as Float
//                invalidate()
//            }
//        }
//        animator.start()
//    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val x = 0f
        val y = height.toFloat()/2
        val x1 = width.toFloat()/2
        val y1 = height.toFloat()
        val x2=width.toFloat()/2
        // Calculate the current end point based on the animated value
//        val currentX = x + animatedValue * (x1 - x)
//        val currentY = y + animatedValue * (y1 - y)
        canvas.drawLine(x,y,x1,y1,paint)
        //canvas.drawLine(width.toFloat(),y,x2,y1,paint)
    }


}