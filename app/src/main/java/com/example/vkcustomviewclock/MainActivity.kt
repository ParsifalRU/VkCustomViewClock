package com.example.vkcustomviewclock

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Title TextView and CustomClockView
        val textView = TextView(this)
        textView.text = getString(R.string.textView)
        val clockView = ClockView(this)

        //Value in dp
        val width = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            333f,
            resources.displayMetrics
        )
        val height = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            222f,
            resources.displayMetrics
        )

        //Add Views in LinearLayout
        val linerLayout = findViewById<LinearLayout>(R.id.linearlayout)
        linerLayout.addView(textView)
        linerLayout.addView(clockView, width.toInt(), height.toInt())
    }
}