package com.example.astrodream.ui

import android.graphics.Matrix
import android.graphics.PointF
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.doOnLayout
import com.example.astrodream.R
import com.example.astrodream.domain.util.AstroDreamUtil
import com.example.astrodream.domain.util.useGlide
import com.example.astrodream.services.buildDownloadSetWallpaperMenu
import com.example.astrodream.services.shareImageFromBitmap
import com.example.astrodream.services.shareImageFromUrl
import kotlinx.android.synthetic.main.activity_full_screen_img.*

class FullScreenImgActivity : AppCompatActivity() {

    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var gestureDetector: GestureDetector
    private lateinit var imgURL: String
    private lateinit var hdimgURL: String
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

        val imageTitle: String = intent.getStringExtra("title") ?: ""
        val imageDescription: String = intent.getStringExtra("description") ?: ""
        imgURL = intent.getStringExtra("img") ?: ""
        hdimgURL = intent.getStringExtra("hdimg") ?: ""

        ivFull.doOnLayout {
            viewWidth = ivFull.width
            viewHeight = ivFull.height

            AstroDreamUtil.useGlide(this, imgURL) { resource ->
                imageWidth = resource.intrinsicWidth
                imageHeight = resource.intrinsicHeight
                origWidth = imageWidth.toFloat()
                origHeight = imageHeight.toFloat()
                ivFull.setImageDrawable(resource)
                ivFull.requestLayout()
                ivFull.imageMatrix = mMatrix
                ivFull.scaleType = ImageView.ScaleType.MATRIX
                fitToScreen()
            }
        }

        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
        gestureDetector = GestureDetector(this, GestureListener())

        ivCloseFullscreen.setOnClickListener { finish() }

        ivDownloadWallpaper.setOnClickListener {
            buildDownloadSetWallpaperMenu(this, ivDownloadWallpaper, getBestUrl())
        }

        ivShare.setOnClickListener {
            if (hdimgURL != "") {
                shareImageFromUrl(hdimgURL, imageTitle, imageDescription, this)
            } else {
                shareImageFromBitmap(ivFull.drawable.toBitmap(), imageTitle, imageDescription, this)
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

    private fun getBestUrl() = if (hdimgURL != "") hdimgURL else imgURL

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

        override fun onLongPress(e: MotionEvent?) {
            buildDownloadSetWallpaperMenu(this@FullScreenImgActivity, ivDownloadWallpaper, getBestUrl())
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean { return false }

        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            val newVisibility = if (ivCloseFullscreen.visibility == View.VISIBLE) {
                View.GONE
            } else {
                View.VISIBLE
            }

            ivCloseFullscreen.visibility = newVisibility
            ivDownloadWallpaper.visibility = newVisibility
            ivShare.visibility = newVisibility

            return false
        }

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