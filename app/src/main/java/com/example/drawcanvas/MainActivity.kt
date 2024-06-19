package com.example.drawcanvas

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), OnValueTextListener {
    @SuppressLint("MissingInflatedId")
    private lateinit var valueText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val lineChartView = findViewById<LineChartView>(R.id.lineChartView)
        valueText = findViewById<TextView>(R.id.tv_chart_value)
        val yData = floatArrayOf(
            20.5f, 18.2f, 22.7f, 24.3f, 23.8f, 21.6f, 19.8f, 17.4f, 15.9f, 18.2f, 20.1f, 21.9f,
            20.3f, 19.1f, 16.8f, 14.7f, 12.5f, 15.0f, 17.2f, 15.8f, 18.3f, 20.7f, 23.4f, 25.6f,
            24.2f, 22.6f, 20.5f, 18.7f, 16.9f, 14.4f, 16.2f, 18.1f, 16.8f, 14.6f, 12.8f, 13.7f,
            10.9f, 13.2f, 15.4f, 13.9f, 11.8f, 10.5f, 11.7f, 13.5f, 15.6f, 17.3f, 18.9f, 20.2f,
            21.4f, 22.8f, 24.1f, 25.3f, 24.6f, 23.2f, 21.8f, 20.4f, 19.0f, 17.6f, 16.2f, 15.4f,
            12.3f, 14.1f, 16.8f, 14.7f, 12.5f, 15.0f, 17.2f, 15.8f, 18.3f, 20.7f, 23.4f, 23.4f,
            23.4f, 23.4f, 23.4f, 23.4f, 10.9f, 13.2f, 15.4f, 13.9f, 11.8f, 10.5f, 11.7f, 13.5f,
            15.6f, 17.3f, 18.9f, 20.2f,
        )
        val adapter = LineChartAdapter(yData)
        lineChartView.adapter = adapter
        valueText.text = yData.last().toString()
        lineChartView.setOnValueTouchListener(this)
    }

    override fun onValueTouch(value: Float) {
        valueText.text = value.toString()
    }
}


//        val customView = findViewById<TestRec>(R.id.custom_view)
//        val toggleButton = findViewById<Button>(R.id.toggleButton)
//        toggleButton.setOnClickListener {
//            customView.toggleColors()
//        }
//        val circleView = findViewById<TestCircle>(R.id.circleView)
//        circleView.viewTreeObserver.addOnGlobalLayoutListener {
//            val location = IntArray(2)
//            circleView.getLocationOnScreen(location)
//            val x = location[0]
//            val y = location[1]
//            Log.d(TAG,"CircleView coordinates on screen: x=$x, y=$y")
//        }

