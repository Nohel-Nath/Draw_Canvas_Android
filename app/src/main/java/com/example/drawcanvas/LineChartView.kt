package com.example.drawcanvas

import android.annotation.SuppressLint
import android.content.Context
import android.database.DataSetObserver
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.roundToInt

@SuppressLint("ClickableViewAccessibility")
interface OnValueTextListener {
    fun onValueTouch(value: Float)
}

open class LineChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.LineChartStyle,
    defStyleRes: Int = R.style.LineChartView
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val contentRect = RectF()
    private var scaleHelper: ScaleHelper? = null
    private val renderPath = android.graphics.Path()
    private val renderPaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.GREEN
        strokeWidth = 5f
    }
    private var yData: FloatArray = floatArrayOf()
    private val marginStart = 35f // Adjust this value as needed
    private val marginEnd = 35f
    private var showTouchPoint: Boolean = false
    private var touchPoints = mutableListOf<Pair<Float, Float>>()
    private var activePointers = mutableMapOf<Int, Pair<Float, Float>>()

    @Suppress("DEPRECATION")
    private val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    private var onValueTouchListener: OnValueTextListener? = null
    fun setOnValueTouchListener(listener: OnValueTextListener) {
        onValueTouchListener = listener
    }

    private val vibrationHandler = Handler(Looper.getMainLooper())
    private val stopVibrationRunnable = Runnable {
        vibrator.cancel()  // Stop the vibration
    }
    var adapter: LineChartAdapter? = null
        set(value) {
            field = value
            value?.let {
                yData = it.yData // Store reference to yData
                it.registerDataSetObserver(object : DataSetObserver() {
                    override fun onChanged() {
                        populatePath()
                    }

                    override fun onInvalidated() {
                        populatePath()
                    }
                })
            }
            populatePath()
        }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                if (activePointers.size < 2) {  // Allow only if less than 2 fingers are already active
                    val pointerIndex = event.actionIndex
                    val pointerId = event.getPointerId(pointerIndex)
                    if (isWithinBoundary(event.getX(pointerIndex))) {
                        activePointers[pointerId] =
                            Pair(event.getX(pointerIndex), event.getY(pointerIndex))
                        showTouchPoints()
                        startVibration()
                        invalidate()
                        return true
                    }
                }
            }

            MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_UP -> {
                val pointerIndex = event.actionIndex
                val pointerId = event.getPointerId(pointerIndex)
                activePointers.remove(pointerId)
                if (activePointers.isEmpty()) {
                    hideTouchPoints()
                    invalidate()
                } else {
                    showTouchPoints()
                    invalidate()
                }
                vibrationHandler.removeCallbacks(stopVibrationRunnable)
                onValueTouchListener?.onValueTouch(yData.last())
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                for (i in 0 until event.pointerCount) {
                    val pointerId = event.getPointerId(i)
                    if (activePointers.containsKey(pointerId) && isWithinBoundary(event.getX(i))) {
                        activePointers[pointerId] = Pair(event.getX(i), event.getY(i))
                    }
                }
                showTouchPoints()
                invalidate()
                startVibration()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun showTouchPoints() {
        showTouchPoint = true
        touchPoints.clear()
        activePointers.values.forEach { (x, y) ->
            if (isWithinBoundary(x)) {
                touchPoints.add(Pair(x, y))
            }
        }
    }

    private fun hideTouchPoints() {
        showTouchPoint = false
        touchPoints.clear()
    }

    private fun isWithinBoundary(x: Float): Boolean {
        val leftBoundary = paddingStart.toFloat()
        val rightBoundary = (width - paddingEnd).toFloat()
        return x in leftBoundary..rightBoundary
    }

    private fun startVibration() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    100,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(100)
        }
        vibrationHandler.removeCallbacks(stopVibrationRunnable)
        vibrationHandler.postDelayed(
            stopVibrationRunnable,
            1
        ) // Stop vibration after 3 milliseconds
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)
        updateContentRect()
        populatePath()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        updateContentRect()
        populatePath()
    }

    private fun updateContentRect() {
        contentRect.set(
            paddingStart.toFloat(),
            paddingTop.toFloat(),
            (width - paddingEnd).toFloat(),
            (height - paddingBottom).toFloat()
        )
    }

    private fun populatePath() {
        adapter?.let {
            scaleHelper = ScaleHelper(it, contentRect, renderPaint.strokeWidth, false)
            renderPath.reset()
            for (i in 0 until it.count) {
                val x = scaleHelper!!.getX(it.getX(i))
                val y = scaleHelper!!.getY(it.getY(i))
                if (i == 0) renderPath.moveTo(x, y)
                else renderPath.lineTo(x, y)
            }
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(renderPath, renderPaint)
        if (showTouchPoint) {
            renderTouchPoint(canvas)
        }
    }

    private fun renderTouchPoint(canvas: Canvas) {
        val startX = contentRect.left
        val endX = contentRect.right
        Log.d("LineChartView", "startX: $startX, endX: $endX")
        renderPaint.color = Color.RED
        renderPaint.style = Paint.Style.FILL_AND_STROKE
        renderPaint.strokeWidth = 2f
        // Draw lines for each active pointer
        activePointers.values.forEach { (x, y) ->
            val (minY, maxY) = getMinMaxYAtTouchPoint(x)
            Log.d("X", "X: $x")
            // Draw the vertical line from the highest to the lowest y-value
            canvas.drawLine(x, minY, x, maxY, renderPaint)
            //Log.d("relativePosition","relative position: $relativePosition")
        }
        activePointers.values.forEach { (x, y) ->
            val (minY, maxY) = getMinMaxYAtTouchPoint(x)
            val middleY = (minY + maxY) / 2
            val value = getValueAtTouchPoint(x)
            onValueTouchListener?.onValueTouch(value)
            val text = "$value"
            renderPaint.textSize = 30f
            renderPaint.color = Color.BLACK
            if (x >= startX && x <= startX + marginStart) {
                // Draw text adjusted by marginStart
                canvas.drawText(
                    text,
                    (startX + marginStart) - renderPaint.measureText(text) / 2,
                    middleY,
                    renderPaint
                )
            } else if (x <= endX && x >= endX - marginEnd) {
                canvas.drawText(
                    text,
                    endX - marginEnd - renderPaint.measureText(text) / 2,
                    middleY,
                    renderPaint
                )
            } else {
                canvas.drawText(text, x - renderPaint.measureText(text) / 2, middleY, renderPaint)
            }
        }
        renderPaint.color = Color.GREEN
        renderPaint.style = Paint.Style.STROKE
        renderPaint.strokeWidth = 5f
        renderPaint.textSize = 20f
    }

    private fun getMinMaxYAtTouchPoint(touchX: Float): Pair<Float, Float> {
        var minY = Float.MAX_VALUE
        var maxY = Float.MIN_VALUE
        Log.d("X", "touchX: $touchX")
        scaleHelper?.let {
            for (i in 0 until adapter!!.count) {
                val x = it.getX(adapter!!.getX(i))

                val y = it.getY(adapter!!.getY(i))
                if (x == touchX) {
                    if (y < minY) minY = y
                    if (y > maxY) maxY = y
                }
            }
        }
        // If no exact match for touchX, use closest points
        if (minY == Float.MAX_VALUE || maxY == Float.MIN_VALUE) {
            minY = paddingTop.toFloat()
            maxY = (height - paddingBottom).toFloat()
        }
        return Pair(minY, maxY)
    }

    private fun getValueAtTouchPoint(touchX: Float): Float {
        // Convert touchX to raw X coordinate in adapter space
        val rawX = (touchX - scaleHelper!!.xTranslation) / scaleHelper!!.xScale
        // Find the nearest index in the adapter data
        val index = rawX.roundToInt().coerceIn(0, adapter!!.count - 1)
        // Get the value at the index
        return adapter!!.getY(index)
    }
}


//private fun isTouchOnDataPoint(x: Float, y: Float): Boolean {
//    val radius = 20f // Adjust the radius as needed
//    adapter?.let {
//        for (i in 0 until it.count) {
//            val dataPointX = scaleHelper!!.getX(it.getX(i))
//            val dataPointY = scaleHelper!!.getY(it.getY(i))
//            val distance = calculateDistance(x, y, dataPointX, dataPointY)
//            if (distance <= radius) {
//                return true
//            }
//        }
//    }
//    return false
//}
//
//private fun calculateDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
//    val dx = x2 - x1
//    val dy = y2 - y1
//    return sqrt(dx * dx + dy * dy)
//}

// Reset the paint properties to their original state