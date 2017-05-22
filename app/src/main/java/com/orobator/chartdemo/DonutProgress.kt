package com.orobator.chartdemo

import android.content.Context
import android.content.res.TypedArray
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.SweepGradient
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View

/**
 * Created by bruce on 14-10-30.
 */
class DonutProgress @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
  private var finishedPaint: Paint? = null
  private var unfinishedPaint: Paint? = null
  private var innerCirclePaint: Paint? = null

  private val finishedOuterRect = RectF()
  private val unfinishedOuterRect = RectF()

  var attributeResourceId = 0
  var isShowText: Boolean = false
  private var textSize: Float = 0.toFloat()
  private var textColor: Int = 0
  private var innerBottomTextColor: Int = 0
  var progress = 0f
    set(progress) {
      field = progress
      if (this.progress > max) {
        field %= max.toFloat()
      }
      invalidate()
    }
  var max: Int = 0
    set(max) {
      if (max > 0) {
        field = max
        invalidate()
      }
    }
  private var finishedStrokeColor: Int = 0
  private var unfinishedStrokeColor: Int = 0
  private var startingDegree: Int = 0
  private var finishedStrokeWidth: Float = 0.toFloat()
  private var unfinishedStrokeWidth: Float = 0.toFloat()
  private var innerBackgroundColor: Int = 0
  private var prefixText = ""
  private var suffixText = "%"
  private var text: String? = null
  private var innerBottomTextSize: Float = 0.toFloat()
  private var innerBottomText: String? = null
  private var innerBottomTextHeight: Float = 0.toFloat()

  private val default_stroke_width: Float
  private val default_finished_color = Color.rgb(66, 145, 241)
  private val default_unfinished_color = Color.rgb(204, 204, 204)
  private val default_inner_bottom_text_color = Color.rgb(66, 145, 241)
  private val default_inner_background_color = Color.TRANSPARENT
  private val default_max = 100
  private val default_startingDegree = 0
  private val default_text_size: Float
  private val default_inner_bottom_text_size: Float
  private val min_size: Int

  init {

    default_text_size = Utils.sp2px(resources, 18f)
    min_size = Utils.dp2px(resources, 100f).toInt()
    default_stroke_width = Utils.dp2px(resources, 10f)
    default_inner_bottom_text_size = Utils.sp2px(resources, 18f)

    val attributes = context.theme
        .obtainStyledAttributes(attrs, R.styleable.DonutProgress, defStyleAttr, 0)
    initByAttributes(attributes)
    attributes.recycle()

    initPainters()
  }

  private fun initPainters() {
    finishedPaint = Paint()
    finishedPaint!!.strokeWidth = finishedStrokeWidth
    finishedPaint!!.style = Paint.Style.STROKE

    finishedPaint!!.isAntiAlias = true
    finishedPaint!!.shader = SweepGradient(
        (width / 2).toFloat(),
        (height / 2).toFloat(),
        intArrayOf(
            Color.argb(0xff, 0x30, 0xe8, 0xe4),
            Color.argb(0xff, 0x8f, 0xe5, 0xff),
            Color.argb(0xff, 0x94, 0x8f, 0xff),
            Color.argb(0xff, 0xff, 0x9c, 0xf6),
            Color.argb(0xff, 0xff, 0x43, 0x43)),
        null)

    unfinishedPaint = Paint()
    unfinishedPaint!!.color = unfinishedStrokeColor
    unfinishedPaint!!.style = Paint.Style.STROKE
    unfinishedPaint!!.isAntiAlias = true
    unfinishedPaint!!.strokeWidth = unfinishedStrokeWidth

    innerCirclePaint = Paint()
    innerCirclePaint!!.color = innerBackgroundColor
    innerCirclePaint!!.isAntiAlias = true
  }

  protected fun initByAttributes(attributes: TypedArray) {
    finishedStrokeColor = attributes.getColor(R.styleable.DonutProgress_donut_finished_color,
        default_finished_color)
    unfinishedStrokeColor = attributes.getColor(R.styleable.DonutProgress_donut_unfinished_color,
        default_unfinished_color)
    isShowText = attributes.getBoolean(R.styleable.DonutProgress_donut_show_text, true)
    attributeResourceId = attributes.getResourceId(R.styleable.DonutProgress_donut_inner_drawable,
        0)

    max = attributes.getInt(R.styleable.DonutProgress_donut_max, default_max)
    progress = attributes.getFloat(R.styleable.DonutProgress_donut_progress, 0f)
    finishedStrokeWidth = attributes.getDimension(
        R.styleable.DonutProgress_donut_finished_stroke_width,
        default_stroke_width)
    unfinishedStrokeWidth = attributes.getDimension(
        R.styleable.DonutProgress_donut_unfinished_stroke_width,
        default_stroke_width)

    innerBottomTextSize = attributes.getDimension(
        R.styleable.DonutProgress_donut_inner_bottom_text_size,
        default_inner_bottom_text_size)
    innerBottomTextColor = attributes.getColor(
        R.styleable.DonutProgress_donut_inner_bottom_text_color,
        default_inner_bottom_text_color)
    innerBottomText = attributes.getString(R.styleable.DonutProgress_donut_inner_bottom_text)

    startingDegree = attributes.getInt(R.styleable.DonutProgress_donut_circle_starting_degree,
        default_startingDegree)
    innerBackgroundColor = attributes.getColor(R.styleable.DonutProgress_donut_background_color,
        default_inner_background_color)
  }

  override fun invalidate() {
    initPainters()
    super.invalidate()
  }

  fun getFinishedStrokeWidth(): Float {
    return finishedStrokeWidth
  }

  fun setFinishedStrokeWidth(finishedStrokeWidth: Float) {
    this.finishedStrokeWidth = finishedStrokeWidth
    this.invalidate()
  }

  fun getUnfinishedStrokeWidth(): Float {
    return unfinishedStrokeWidth
  }

  fun setUnfinishedStrokeWidth(unfinishedStrokeWidth: Float) {
    this.unfinishedStrokeWidth = unfinishedStrokeWidth
    this.invalidate()
  }

  private val progressAngle: Float
    get() = progress / this.max.toFloat() * 360f

  fun getTextSize(): Float {
    return textSize
  }

  fun setTextSize(textSize: Float) {
    this.textSize = textSize
    this.invalidate()
  }

  fun getTextColor(): Int {
    return textColor
  }

  fun setTextColor(textColor: Int) {
    this.textColor = textColor
    this.invalidate()
  }

  fun getFinishedStrokeColor(): Int {
    return finishedStrokeColor
  }

  fun setFinishedStrokeColor(finishedStrokeColor: Int) {
    this.finishedStrokeColor = finishedStrokeColor
    this.invalidate()
  }

  fun getUnfinishedStrokeColor(): Int {
    return unfinishedStrokeColor
  }

  fun setUnfinishedStrokeColor(unfinishedStrokeColor: Int) {
    this.unfinishedStrokeColor = unfinishedStrokeColor
    this.invalidate()
  }

  fun getText(): String? {
    return text
  }

  fun setText(text: String) {
    this.text = text
    this.invalidate()
  }

  fun getSuffixText(): String {
    return suffixText
  }

  fun setSuffixText(suffixText: String) {
    this.suffixText = suffixText
    this.invalidate()
  }

  fun getPrefixText(): String {
    return prefixText
  }

  fun setPrefixText(prefixText: String) {
    this.prefixText = prefixText
    this.invalidate()
  }

  fun getInnerBackgroundColor(): Int {
    return innerBackgroundColor
  }

  fun setInnerBackgroundColor(innerBackgroundColor: Int) {
    this.innerBackgroundColor = innerBackgroundColor
    this.invalidate()
  }

  fun getInnerBottomText(): String? {
    return innerBottomText
  }

  fun setInnerBottomText(innerBottomText: String) {
    this.innerBottomText = innerBottomText
    this.invalidate()
  }

  fun getInnerBottomTextSize(): Float {
    return innerBottomTextSize
  }

  fun setInnerBottomTextSize(innerBottomTextSize: Float) {
    this.innerBottomTextSize = innerBottomTextSize
    this.invalidate()
  }

  fun getInnerBottomTextColor(): Int {
    return innerBottomTextColor
  }

  fun setInnerBottomTextColor(innerBottomTextColor: Int) {
    this.innerBottomTextColor = innerBottomTextColor
    this.invalidate()
  }

  fun getStartingDegree(): Int {
    return startingDegree
  }

  fun setStartingDegree(startingDegree: Int) {
    this.startingDegree = startingDegree
    this.invalidate()
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec))

    //TODO calculate inner circle height and then position bottom text at the bottom (3/4)
    innerBottomTextHeight = (height - height * 3 / 4).toFloat()
  }

  private fun measure(measureSpec: Int): Int {
    var result: Int
    val mode = View.MeasureSpec.getMode(measureSpec)
    val size = View.MeasureSpec.getSize(measureSpec)
    if (mode == View.MeasureSpec.EXACTLY) {
      result = size
    } else {
      result = min_size
      if (mode == View.MeasureSpec.AT_MOST) {
        result = Math.min(result, size)
      }
    }
    return result
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    val delta = Math.max(finishedStrokeWidth, unfinishedStrokeWidth)
    finishedOuterRect.set(delta, delta, width - delta, height - delta)
    unfinishedOuterRect.set(delta, delta, width - delta, height - delta)

    val innerCircleRadius = (width - Math.min(finishedStrokeWidth,
        unfinishedStrokeWidth) + Math.abs(
        finishedStrokeWidth - unfinishedStrokeWidth)) / 2f
    canvas.drawCircle(width / 2.0f, height / 2.0f, innerCircleRadius, innerCirclePaint!!)

    canvas.save()
    canvas.rotate(-90f, width / 2f, height / 2f)
    canvas.drawArc(finishedOuterRect, getStartingDegree().toFloat() - 270, progressAngle,
        false,
        finishedPaint!!)
    canvas.restore()


    canvas.drawArc(unfinishedOuterRect, getStartingDegree() + progressAngle,
        360 - progressAngle, false, unfinishedPaint!!)

    if (attributeResourceId != 0) {
      val bitmap = BitmapFactory.decodeResource(resources, attributeResourceId)
      canvas.drawBitmap(bitmap, (width - bitmap.width) / 2.0f,
          (height - bitmap.height) / 2.0f, null)
    }
  }

  override fun onSaveInstanceState(): Parcelable {
    val bundle = Bundle()
    bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState())
    bundle.putInt(INSTANCE_TEXT_COLOR, getTextColor())
    bundle.putFloat(INSTANCE_TEXT_SIZE, getTextSize())
    bundle.putFloat(INSTANCE_INNER_BOTTOM_TEXT_SIZE, getInnerBottomTextSize())
    bundle.putFloat(INSTANCE_INNER_BOTTOM_TEXT_COLOR, getInnerBottomTextColor().toFloat())
    bundle.putString(INSTANCE_INNER_BOTTOM_TEXT, getInnerBottomText())
    bundle.putInt(INSTANCE_INNER_BOTTOM_TEXT_COLOR, getInnerBottomTextColor())
    bundle.putInt(INSTANCE_FINISHED_STROKE_COLOR, getFinishedStrokeColor())
    bundle.putInt(INSTANCE_UNFINISHED_STROKE_COLOR, getUnfinishedStrokeColor())
    bundle.putInt(INSTANCE_MAX, max)
    bundle.putInt(INSTANCE_STARTING_DEGREE, getStartingDegree())
    bundle.putFloat(INSTANCE_PROGRESS, progress)
    bundle.putString(INSTANCE_SUFFIX, getSuffixText())
    bundle.putString(INSTANCE_PREFIX, getPrefixText())
    bundle.putString(INSTANCE_TEXT, getText())
    bundle.putFloat(INSTANCE_FINISHED_STROKE_WIDTH, getFinishedStrokeWidth())
    bundle.putFloat(INSTANCE_UNFINISHED_STROKE_WIDTH, getUnfinishedStrokeWidth())
    bundle.putInt(INSTANCE_BACKGROUND_COLOR, getInnerBackgroundColor())
    bundle.putInt(INSTANCE_INNER_DRAWABLE, attributeResourceId)
    return bundle
  }

  override fun onRestoreInstanceState(state: Parcelable) {
    if (state is Bundle) {
      val bundle = state
      textColor = bundle.getInt(INSTANCE_TEXT_COLOR)
      textSize = bundle.getFloat(INSTANCE_TEXT_SIZE)
      innerBottomTextSize = bundle.getFloat(INSTANCE_INNER_BOTTOM_TEXT_SIZE)
      innerBottomText = bundle.getString(INSTANCE_INNER_BOTTOM_TEXT)
      innerBottomTextColor = bundle.getInt(INSTANCE_INNER_BOTTOM_TEXT_COLOR)
      finishedStrokeColor = bundle.getInt(INSTANCE_FINISHED_STROKE_COLOR)
      unfinishedStrokeColor = bundle.getInt(INSTANCE_UNFINISHED_STROKE_COLOR)
      finishedStrokeWidth = bundle.getFloat(INSTANCE_FINISHED_STROKE_WIDTH)
      unfinishedStrokeWidth = bundle.getFloat(INSTANCE_UNFINISHED_STROKE_WIDTH)
      innerBackgroundColor = bundle.getInt(INSTANCE_BACKGROUND_COLOR)
      attributeResourceId = bundle.getInt(INSTANCE_INNER_DRAWABLE)
      initPainters()
      max = bundle.getInt(INSTANCE_MAX)
      setStartingDegree(bundle.getInt(INSTANCE_STARTING_DEGREE))
      progress = bundle.getFloat(INSTANCE_PROGRESS)
      prefixText = bundle.getString(INSTANCE_PREFIX)
      suffixText = bundle.getString(INSTANCE_SUFFIX)
      text = bundle.getString(INSTANCE_TEXT)
      super.onRestoreInstanceState(bundle.getParcelable<Parcelable>(INSTANCE_STATE))
      return
    }
    super.onRestoreInstanceState(state)
  }

  fun setDonut_progress(percent: String) {
    if (!TextUtils.isEmpty(percent)) {
      progress = Integer.parseInt(percent) * 1f
    }
  }

  companion object {

    private val INSTANCE_STATE = "saved_instance"
    private val INSTANCE_TEXT_COLOR = "text_color"
    private val INSTANCE_TEXT_SIZE = "text_size"
    private val INSTANCE_TEXT = "text"
    private val INSTANCE_INNER_BOTTOM_TEXT_SIZE = "inner_bottom_text_size"
    private val INSTANCE_INNER_BOTTOM_TEXT = "inner_bottom_text"
    private val INSTANCE_INNER_BOTTOM_TEXT_COLOR = "inner_bottom_text_color"
    private val INSTANCE_FINISHED_STROKE_COLOR = "finished_stroke_color"
    private val INSTANCE_UNFINISHED_STROKE_COLOR = "unfinished_stroke_color"
    private val INSTANCE_MAX = "max"
    private val INSTANCE_PROGRESS = "progress"
    private val INSTANCE_SUFFIX = "suffix"
    private val INSTANCE_PREFIX = "prefix"
    private val INSTANCE_FINISHED_STROKE_WIDTH = "finished_stroke_width"
    private val INSTANCE_UNFINISHED_STROKE_WIDTH = "unfinished_stroke_width"
    private val INSTANCE_BACKGROUND_COLOR = "inner_background_color"
    private val INSTANCE_STARTING_DEGREE = "starting_degree"
    private val INSTANCE_INNER_DRAWABLE = "inner_drawable"
  }
}