package com.example.drawcanvas

open class LineChartAdapter(var yData: FloatArray = floatArrayOf()) : ChartAdapter() {

    override val count: Int
        get() = yData.size

    open fun setData(yData: FloatArray) {
        this.yData = yData
        notifyDataSetChanged()
    }

    fun setDataWithoutNotify(yData: FloatArray) {
        this.yData = yData
    }

    override fun hasBaseLine(): Boolean =
        containsNegativeValue()

    private fun containsNegativeValue(): Boolean {
        for (value in yData) {
            if (value < 0)
                return true
        }
        return false
    }

    override fun getItem(index: Int): Any =
        yData[index]

    override fun getY(index: Int): Float =
        yData[index]

    override fun getX(index: Int): Float =
        index.toFloat() // Return the index as the x-coordinate, or implement your logic here

    fun clearData(){
        setData(floatArrayOf())
    }
}
