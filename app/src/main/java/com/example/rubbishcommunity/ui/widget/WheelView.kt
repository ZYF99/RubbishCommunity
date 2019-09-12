package com.example.rubbishcommunity.ui.widget

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.util.*
import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.graphics.Canvas
import android.view.MotionEvent
import android.graphics.Paint.Align


/**
 * from website https://blog.csdn.net/weixin_41454168/article/details/79541354
 * */
class WheelView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    /**
     * text之间间距和minTextSize之比
     */
    val MARGIN_ALPHA = 2.8f
    /**
     * 自动回滚到中间的速度
     */
    val SPEED = 2f

    private var mDataList: MutableList<String>? = null
    /**
     * 选中的位置，这个位置是mDataList的中心位置，一直不变
     */
    private var mCurrentSelected: Int = 0

    private var mPaint: Paint? = null

    private var mMaxTextSize = 80f
    private var mMinTextSize = 40f

    private val mMaxTextAlpha = 255f
    private val mMinTextAlpha = 120f

    private val mColorText = 0x333333

    private var mViewHeight: Int = 0
    private var mViewWidth: Int = 0

    private var mLastDownY: Float = 0.toFloat()
    /**
     * 滑动的距离
     */
    private var mMoveLen = 0f
    private var isInit = false
    private var mSelectListener: OnSelectListener? = null
    private var timer: Timer? = null
    private var mTask: MyTimerTask? = null


    private var updateHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            if (Math.abs(mMoveLen) < SPEED) {
                mMoveLen = 0f
                if (mTask != null) {
                    mTask!!.cancel()
                    mTask = null
                    performSelect()
                }
            } else
            // 这里mMoveLen / Math.abs(mMoveLen)是为了保有mMoveLen的正负号，以实现上滚或下滚
                mMoveLen -= mMoveLen / Math.abs(mMoveLen) * SPEED
            invalidate()
        }
    }

    init {
        initView()
    }


    fun setOnSelectListener(listener: OnSelectListener) {
        mSelectListener = listener
    }

    private fun performSelect() {
        mSelectListener?.onSelect(mDataList!![mCurrentSelected])
    }

    fun setData(data: MutableList<String>) {
        mDataList = data
        mCurrentSelected = data.size / 2
        invalidate()
    }

    fun setSelected(str: String) {
        mDataList?.indexOf(str)?.let { setSelected(it) }
    }

    private fun setSelected(selected: Int) {
        mCurrentSelected = selected
    }

    private fun moveHeadToTail() {
        val head = mDataList?.get(0)
        mDataList?.removeAt(0)
        head?.let { mDataList?.add(it) }
    }

    private fun moveTailToHead() {
        val tail = mDataList!![mDataList!!.size - 1]
        mDataList!!.removeAt(mDataList!!.size - 1)
        mDataList!!.add(0, tail)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mViewHeight = measuredHeight
        mViewWidth = measuredWidth
        // 按照View的高度计算字体大小
        mMaxTextSize = mViewHeight / 4.0f
        mMinTextSize = mMaxTextSize / 2f
        isInit = true
        invalidate()
    }

    private fun initView() {
        timer = Timer()
        mDataList = ArrayList()
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.textAlign = Align.CENTER
        mPaint!!.color = mColorText
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 根据index绘制view
        if (isInit)
            drawData(canvas)
    }

    private fun drawData(canvas: Canvas) {
        // 先绘制选中的text再往上往下绘制其余的text
        val scale = parabola(mViewHeight / 4.0f, mMoveLen)
        val size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize
        mPaint?.textSize = size
        mPaint?.alpha = ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha).toInt()
        // text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
        val x = (mViewWidth / 2.0).toFloat()
        val y = (mViewHeight / 2.0 + mMoveLen).toFloat()
        val fmi = mPaint?.fontMetricsInt
        val baseline = (y - (fmi!!.bottom / 2.0 + fmi.top / 2.0)).run { toFloat() }

        mPaint?.let { canvas.drawText(mDataList!![mCurrentSelected], x, baseline, it) }
        // 绘制上方data
        run {
            var i = 1
            while (mCurrentSelected - i >= 0) {
                drawOtherText(canvas, i, -1)
                i++
            }
        }
        // 绘制下方data
        var i = 1
        while (mCurrentSelected + i < mDataList!!.size) {
            drawOtherText(canvas, i, 1)
            i++
        }

    }

    /**
     * @param canvas
     * @param position
     * 距离mCurrentSelected的差值
     * @param type
     * 1表示向下绘制，-1表示向上绘制
     */
    private fun drawOtherText(canvas: Canvas, position: Int, type: Int) {
        val d = MARGIN_ALPHA * mMinTextSize * position + type * mMoveLen
        val scale = parabola(mViewHeight / 4.0f, d)
        val size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize
        mPaint?.textSize = size
        mPaint?.alpha = ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha).toInt()
        val y = (mViewHeight / 2.0 + type * d).toFloat()
        val fmi = mPaint!!.fontMetricsInt
        val baseline = (y - (fmi.bottom / 2.0 + fmi.top / 2.0)).toFloat()
        canvas.drawText(
            mDataList!![mCurrentSelected + type * position],
            (mViewWidth / 2.0).toFloat(), baseline, mPaint!!
        )
    }

    /**
     * 抛物线
     *
     * @param zero
     * 零点坐标
     * @param x
     * 偏移量
     * @return scale
     */
    private fun parabola(zero: Float, x: Float): Float {
        val f = (1 - Math.pow((x / zero).toDouble(), 2.0)).toFloat()
        return if (f < 0) 0f else f
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> doDown(event)
            MotionEvent.ACTION_MOVE -> doMove(event)
            MotionEvent.ACTION_UP -> doUp()
        }
        return true
    }

    private fun doDown(event: MotionEvent) {
        if (mTask != null) {
            mTask!!.cancel()
            mTask = null
        }
        mLastDownY = event.y
    }

    private fun doMove(event: MotionEvent) {

        mMoveLen += event.y - mLastDownY

        if (mMoveLen > MARGIN_ALPHA * mMinTextSize / 2) {
            // 往下滑超过离开距离
            moveTailToHead()
            mMoveLen -= MARGIN_ALPHA * mMinTextSize
        } else if (mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2) {
            // 往上滑超过离开距离
            moveHeadToTail()
            mMoveLen += MARGIN_ALPHA * mMinTextSize
        }
        mLastDownY = event.y
        invalidate()
    }

    private fun doUp() {
        // 抬起手后mCurrentSelected的位置由当前位置move到中间选中位置
        if (Math.abs(mMoveLen) < 0.0001) {
            mMoveLen = 0f
            return
        }
        if (mTask != null) {
            mTask!!.cancel()
            mTask = null
        }
        mTask = MyTimerTask(updateHandler)
        timer!!.schedule(mTask, 0, 10)
    }

    internal inner class MyTimerTask(private var handler: Handler) : TimerTask() {

        override fun run() {
            handler.sendMessage(handler.obtainMessage())
        }

    }

    interface OnSelectListener {
        fun onSelect(text: String)
    }


}