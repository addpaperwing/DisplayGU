package com.apw.ql

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.min

class CircularImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val paint: Paint = Paint()

    private var diameter: Int = 0

    private var bitmap: Bitmap? = null

    //prevent from loading same bitmap twice
    private var mDrawable: Drawable? = null

    override fun getScaleType(): ScaleType = ScaleType.CENTER_CROP

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        bitmap?.let { setupShader(it) }

        val widthWithPadding = width - (paddingLeft + paddingRight)
        val heightWithPadding = height - (paddingTop + paddingBottom)

        diameter = min(widthWithPadding, heightWithPadding)
    }

    override fun onDraw(canvas: Canvas) {
        getBitmap()
        if (bitmap == null) return

        val radius = diameter / 2f

        canvas.drawCircle(
            radius, radius, radius, paint
        )
    }

    private fun getBitmap() {
        if (drawable == null) return

        if (mDrawable == drawable) return
        mDrawable = drawable
        bitmap = if (mDrawable is BitmapDrawable) {
            getBitmapFromBitmapDrawable(mDrawable as BitmapDrawable)
        } else {
            getBitmap(mDrawable!!)
        }
        setupShader(bitmap!!)
    }

    private fun getBitmapFromBitmapDrawable(bitmapDrawable: BitmapDrawable): Bitmap = bitmapDrawable.run {
        Bitmap.createScaledBitmap(
            bitmap, this.intrinsicWidth, this.intrinsicHeight, false
        )
    }

    private fun getBitmap(drawable: Drawable): Bitmap {
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }


    private fun setupShader(bitmap: Bitmap) {
        val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        shader.setLocalMatrix(
            Matrix().apply {
                val ratio: Float
                var dx = 0f
                var dy = 0f
                if (bitmap.width > bitmap.height) {
                    ratio = diameter / bitmap.height.toFloat()
                    dx = (diameter - bitmap.width * ratio) / 2f
                } else {
                    ratio = diameter / bitmap.width.toFloat()
                    dy = (diameter - bitmap.height * ratio) / 2f
                }
                setScale(ratio, ratio)
                postTranslate(dx, dy)
            }
        )
        paint.isAntiAlias = true
        paint.shader = shader
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthWithoutPadding = measure(widthMeasureSpec) - (paddingLeft + paddingRight)
        val heightWithoutPadding = measure(heightMeasureSpec) - (paddingTop + paddingBottom)
        diameter = min(widthWithoutPadding, heightWithoutPadding)
        setMeasuredDimension(diameter, diameter)
    }

    private fun measure(measureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        return if (specMode == MeasureSpec.EXACTLY || specMode == MeasureSpec.AT_MOST) specSize else diameter
    }
}
