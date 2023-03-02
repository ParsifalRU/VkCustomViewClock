package com.example.vkcustomviewclock

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import java.util.*
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class ClockView : View {
    private var height = 0
    private var width = 0
    private var padding = 0
    private var fontSize = 0
    private val numeralSpacing = 0
    private var handTruncation = 0
    private var hourHandTruncation = 0
    private var radius = 0
    private var paint: Paint? = null
    private var isInit = false
    private val numbers = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    private val rect = Rect()
    private var minSize = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    //Init Value
    private fun initClock() {
        height = getHeight()
        width = getWidth()
        minSize = min(height, width)
        padding = numeralSpacing + minSize / 8
        fontSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            (minSize / 30).toFloat(),
            resources.displayMetrics
        ).toInt()
        radius = minSize / 2 - padding
        handTruncation = minSize / 20
        hourHandTruncation = minSize / 7
        paint = Paint()
        isInit = true
    }

    //Draw all elements
    override fun onDraw(canvas: Canvas) {
        if (!isInit) {
            initClock()
        }
        canvas.drawColor(Color.WHITE)
        drawCenter(canvas)
        drawNumeral(canvas)
        drawDoterCircle(canvas)
        drawHands(canvas)
        postInvalidateDelayed(500)
        drawCircle(canvas)
    }

    //pattern to draw hand
    private fun drawHand(
        canvas: Canvas,
        loc: Double,
        isHour: Boolean,
        isMinute: Boolean,
        isSecond: Boolean
    ) {
        val angle = Math.PI * loc / 30 - Math.PI / 2
        var handRadius = 0
        var strokeWidth = 0f
        when {
            isHour -> {
                handRadius = radius - handTruncation - hourHandTruncation
                strokeWidth = (minSize/ 40).toFloat()
            }
            isMinute -> {
                handRadius = radius - handTruncation - hourHandTruncation / 2
                strokeWidth = (minSize/90).toFloat()
            }
            isSecond -> {
                handRadius = radius - handTruncation
                strokeWidth = (minSize/200).toFloat()
            }
        }
        paint!!.strokeWidth = strokeWidth
        canvas.drawLine(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            (width / 2 + cos(angle) * handRadius).toFloat(),
            (height / 2 + sin(angle) * handRadius).toFloat(),
            paint!!
        )
    }

    //Logic to draw all hand
    private fun drawHands(canvas: Canvas) {
        val calendar = Calendar.getInstance()
        var hour = calendar[Calendar.HOUR_OF_DAY].toFloat()
        hour = if (hour > 12) hour - 12 else hour
        drawHand(canvas, ((hour + calendar[Calendar.MINUTE] / 60) * 5f).toDouble(),
            isHour = true,
            isMinute = false,
            isSecond = false
        )
        drawHand(canvas, calendar[Calendar.MINUTE].toDouble(),
            isHour = false,
            isMinute = true,
            isSecond = false
        )
        drawHand(canvas, calendar[Calendar.SECOND].toDouble(),
            isHour = false,
            isMinute = false,
            isSecond = true
        )
    }

    //Draw numbers on clock face
    private fun drawNumeral(canvas: Canvas) {
        paint!!.textSize = fontSize.toFloat()
        for (number in numbers) {
            val tmp = number.toString()
            paint!!.getTextBounds(tmp, 0, tmp.length, rect)
            val angle = Math.PI / 6 * (number - 3)
            val x = (width / 2 + cos(angle) * (radius - minSize/10)  - rect.width() / 2).toInt()
            val y = (height / 2 + sin(angle) * (radius - minSize/10)  + rect.height() / 2).toInt()
            canvas.drawText(tmp, x.toFloat(), y.toFloat(), paint!!)
        }
    }

    //Draw Central dot
    private fun drawCenter(canvas: Canvas) {
        paint!!.style = Paint.Style.FILL
        canvas.drawCircle(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            (minSize/ 90).toFloat(),
            paint!!)
    }

    //Draw a circle of dots
    private fun drawDoterCircle(canvas: Canvas){
        var doterRadius: Float
        for (i in 1 .. 60) {
            val angle = Math.PI / 30 * (i - 1)
            val x = (width / 2 + cos(angle) * radius).toInt()
            val y = (height / 2 + sin(angle) * radius).toInt()
            doterRadius = if ((i-1) % 5 == 0){
                (minSize / 90).toFloat()
            }else{
                (minSize / 125).toFloat()
            }
            canvas.drawCircle(x.toFloat(), y.toFloat(), doterRadius,  paint!!)
        }
    }

    //Draw main (external) circle solid
    private fun drawCircle(canvas: Canvas) {
        paint!!.reset()
        paint!!.color = ContextCompat.getColor(context, R.color.black)
        paint!!.strokeWidth = (minSize/25).toFloat()
        paint!!.style = Paint.Style.STROKE
        paint!!.isAntiAlias = true
        canvas.drawCircle(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            (radius + padding - (minSize/25).toFloat()),
            paint!!
        )
        paint!!.strokeWidth = 5f
    }
}