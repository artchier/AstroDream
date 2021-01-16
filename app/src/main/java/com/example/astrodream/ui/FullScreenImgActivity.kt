package com.example.astrodream.ui

import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.astrodream.R
import com.example.astrodream.services.setImageAsWallpaper
import kotlinx.android.synthetic.main.activity_full_screen_img.*

class FullScreenImgActivity : AppCompatActivity() {

    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var gestureDetector: GestureDetector
    private var scaleFactor = 1.0f

    var mMatrix = Matrix()
    private var mMatrixValues = FloatArray(9)
    var mode = NONE

    // Scales
    var mMinScale = 1f
    var mMaxScale = 10f

    // view dimensions
    var origWidth = 0f
    var origHeight = 0f
    var viewWidth = 0
    var viewHeight = 0
    private var mLast = PointF()
    private var mStart = PointF()

    private var imageWidth: Int = 0
    private var imageHeight: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_img)

        val imgURL = intent.extras?.getString("img")
        val hdimgURL = intent.extras?.getString("hdimg")

        btnUsarWallpaper.setOnClickListener {
            Toast.makeText(baseContext, "A imagem em alta definição está sendo baixada!", Toast.LENGTH_LONG).show()
        }

        ivFull.doOnLayout {
            viewWidth = ivFull.width
            viewHeight = ivFull.height

            Glide.with(this)
                .load(imgURL)
                .into(object : CustomTarget<Drawable?>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable?>?
                    ) {
                        imageWidth = resource.intrinsicWidth
                        imageHeight = resource.intrinsicHeight
                        origWidth = imageWidth.toFloat()
                        origHeight = imageHeight.toFloat()
                        ivFull.setImageDrawable(resource)
                        ivFull.requestLayout()
                        ivFull.imageMatrix = mMatrix
                        ivFull.scaleType = ImageView.ScaleType.MATRIX
                        fitToScreen()

                        if (hdimgURL == null) {
                            updateOnClickWallpaper(resource)
                        }
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {}
                })

            if (hdimgURL == null) return@doOnLayout

            Glide.with(this)
                .load(hdimgURL)
                .into(object : CustomTarget<Drawable?>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable?>?
                    ) {
                        updateOnClickWallpaper(resource)
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        }

        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
        gestureDetector = GestureDetector(this, GestureListener())

        ivCloseFullscreen.setOnClickListener { finish() }
    }

    private fun updateOnClickWallpaper(resource: Drawable) {
        findViewById<Button>(R.id.btnUsarWallpaper).setOnClickListener {
            try {
                setImageAsWallpaper(baseContext, resource)
                Toast.makeText(baseContext, "Wallpaper atualizado!", Toast.LENGTH_LONG).show()
            } catch (ignored: Exception) {
                Toast.makeText(baseContext, "Erro ao usar imagem como wallpaper!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {

        scaleGestureDetector.onTouchEvent(motionEvent)
        gestureDetector.onTouchEvent(motionEvent)

        val currentPoint = PointF(motionEvent.x, motionEvent.y)

        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                mLast.set(currentPoint)
                mStart.set(mLast)
                mode = DRAG
            }
            MotionEvent.ACTION_MOVE -> if (mode == DRAG) {
                val dx = currentPoint.x - mLast.x
                val dy = currentPoint.y - mLast.y
                val fixTransX = getFixDragTrans(dx, viewWidth.toFloat(), origWidth * scaleFactor)
                val fixTransY = getFixDragTrans(dy, viewHeight.toFloat(), origHeight * scaleFactor)
                mMatrix.postTranslate(fixTransX, fixTransY)
                fixTranslation()
                mLast[currentPoint.x] = currentPoint.y
            }
            MotionEvent.ACTION_POINTER_UP -> mode = NONE
        }
        ivFull.imageMatrix = mMatrix
        return false
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean { return false }
        override fun onShowPress(e: MotionEvent?) {}
        override fun onSingleTapUp(e: MotionEvent?): Boolean { return false }
        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean { return false }
        override fun onLongPress(e: MotionEvent?) {}
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean { return false }
        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean { return false }
        override fun onDoubleTap(e: MotionEvent?): Boolean {
            fitToScreen()
            return false
        }
        override fun onDoubleTapEvent(e: MotionEvent?): Boolean { return false }
    }

    companion object {
        // Image States
        const val NONE = 0
        const val DRAG = 1
        const val ZOOM = 2
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            mode = ZOOM
            return true
        }

        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            var mScaleFactor = scaleGestureDetector.scaleFactor
            val prevScale = scaleFactor
            scaleFactor *= mScaleFactor
            if (scaleFactor > mMaxScale) {
                scaleFactor = mMaxScale
                mScaleFactor = mMaxScale / prevScale
            } else if (scaleFactor < mMinScale) {
                scaleFactor = mMinScale
                mScaleFactor = mMinScale / prevScale
            }
            if (origWidth * scaleFactor <= viewWidth
                || origHeight * scaleFactor <= viewHeight) {
                mMatrix.postScale(mScaleFactor, mScaleFactor, viewWidth / 2.toFloat(),
                    viewHeight / 2.toFloat())
            } else {
                mMatrix.postScale(mScaleFactor, mScaleFactor,
                    scaleGestureDetector.focusX, scaleGestureDetector.focusY)
            }
            fixTranslation()
            return true
        }
    }

    private fun fitToScreen() {
        scaleFactor = 1f
        val scale: Float
        val drawable = ivFull.drawable
        if (drawable == null || drawable.intrinsicWidth == 0 || drawable.intrinsicHeight == 0) {
            return
        }
        val imageWidth = drawable.intrinsicWidth
        val imageHeight = drawable.intrinsicHeight
        val scaleX = viewWidth.toFloat() / imageWidth.toFloat()
        val scaleY = viewHeight.toFloat() / imageHeight.toFloat()
        scale = scaleX.coerceAtMost(scaleY)
        mMatrix.setScale(scale, scale)

        // Center the image
        centerImage(scale, imageWidth, imageHeight)
    }

    private fun centerImage(scale: Float, imageWidth: Int, imageHeight: Int) {
        val redundantYSpace = (viewHeight.toFloat() - scale * imageHeight.toFloat()) / 2
        val redundantXSpace = (viewWidth.toFloat() - scale * imageWidth.toFloat()) / 2
        mMatrix.postTranslate(redundantXSpace, redundantYSpace)
        origWidth = viewWidth - redundantXSpace * 2
        origHeight = viewHeight - redundantYSpace * 2
        ivFull.imageMatrix = mMatrix
    }

    private fun fixTranslation() {
        mMatrix.getValues(mMatrixValues) //put matrix values into a float array so we can analyze
        val transX = mMatrixValues[Matrix.MTRANS_X] //get the most recent translation in x direction
        val transY = mMatrixValues[Matrix.MTRANS_Y] //get the most recent translation in y direction
        val fixTransX = getFixTranslation(transX, viewWidth.toFloat(), origWidth * scaleFactor)
        val fixTransY = getFixTranslation(transY, viewHeight.toFloat(), origHeight * scaleFactor)
        if (fixTransX != 0f || fixTransY != 0f) mMatrix.postTranslate(fixTransX, fixTransY)
    }

    private fun getFixTranslation(trans: Float, viewSize: Float, contentSize: Float): Float {
        val minTrans: Float
        val maxTrans: Float
        if (contentSize <= viewSize) { // case: NOT ZOOMED
            minTrans = 0f
            maxTrans = viewSize - contentSize
        } else { //CASE: ZOOMED
            minTrans = viewSize - contentSize
            maxTrans = 0f
        }
        if (trans < minTrans) { // negative x or y translation (down or to the right)
            return -trans + minTrans
        }
        if (trans > maxTrans) { // positive x or y translation (up or to the left)
            return -trans + maxTrans
        }
        return 0F
    }

    private fun getFixDragTrans(delta: Float, viewSize: Float, contentSize: Float): Float {
        return if (contentSize <= viewSize) {
            0F
        } else delta
    }

}