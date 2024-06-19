package com.example.drawcanvas

import android.database.DataSetObservable
import android.database.DataSetObserver
import android.graphics.RectF
import android.util.Log

abstract class ChartAdapter {
    abstract val count: Int
    abstract fun getItem(index: Int): Any
    abstract fun getY(index: Int): Float
    abstract fun getX(index: Int): Float
    private val observable = DataSetObservable()
    fun notifyDataSetChanged() {
        observable.notifyChanged()
    }

    fun notifyDataSetInvalidated() {
        observable.notifyInvalidated()
    }

    fun registerDataSetObserver(observer: DataSetObserver) {
        observable.registerObserver(observer)
    }

    fun unregisterDataSetObserver(observer: DataSetObserver) {
        observable.unregisterObserver(observer)
    }

    open val dataBounds: RectF
        get() {
            val count = count
            var minY = Float.MAX_VALUE
            var maxY = -Float.MAX_VALUE
            var minX = Float.MAX_VALUE
            var maxX = -Float.MAX_VALUE
            for (i in 0 until count) {
                val x = getX(i)
                Log.i("### LINE", "### INSTANCE OF ${x::class.java}")
//                val y= lineChart.getChildAt(i)
//                Log.i("### LINE", "### INSTANCE OF ${v::class.java}")
                minX = minX.coerceAtMost(x)
                maxX = maxX.coerceAtLeast(x)
                val y = getY(i)
                Log.i("### LINE", "### INSTANCE OF ${y::class.java}")
                minY = minY.coerceAtMost(y)
                maxY = maxY.coerceAtLeast(y)
            }
            return createRectF(minX, minY, maxX, maxY)
        }

    private fun createRectF(left: Float, top: Float, right: Float, bottom: Float): RectF =
        RectF(left, top, right, bottom)

    open fun hasBaseLine(): Boolean = false
}