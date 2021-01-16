package com.example.astrodream.ui

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.doOnLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.astrodream.R
import com.example.astrodream.services.setImageAsWallpaper
import kotlinx.android.synthetic.main.activity_full_screen_img.*
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.OutputStream

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

    private var downloadAction: () -> Unit = {}
    private var useAsWallpaperAction: () -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_img)

        val imgURL = intent.extras?.getString("img")
        val hdimgURL = intent.extras?.getString("hdimg")

        useAsWallpaperAction = {
            Toast.makeText(baseContext, "A imagem em alta definição está sendo baixada!", Toast.LENGTH_LONG).show()
        }
        downloadAction = useAsWallpaperAction

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
                            updateDownloadAndWallpaperAction(resource)
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
                        updateDownloadAndWallpaperAction(resource)
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        }

        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
        gestureDetector = GestureDetector(this, GestureListener())

        ivCloseFullscreen.setOnClickListener { finish() }
    }

    private fun updateDownloadAndWallpaperAction(resource: Drawable) {
        downloadAction = {
            Toast.makeText(baseContext, "Imagem salva!", Toast.LENGTH_LONG).show()
            saveImage(resource.toBitmap(), baseContext, getString(R.string.app_name))
        }

        useAsWallpaperAction = {
            try {
                Toast.makeText(baseContext, "Atualizando wallpaper...", Toast.LENGTH_SHORT).show()
                setImageAsWallpaper(baseContext, resource)
                Toast.makeText(baseContext, "Wallpaper atualizado!", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Log.e("FullScreenImgActivity", "Erro ao atualizar wallpaper: ${e.message}")
                Toast.makeText(baseContext, "Erro ao usar imagem como wallpaper!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun saveImage(bitmap: Bitmap, context: Context, folderName: String) {
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            val values = contentValues()
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$folderName")
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            // RELATIVE_PATH and IS_PENDING are introduced in API 29.

            val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                context.contentResolver.update(uri, values, null, null)
            }
        } else {
            val directory = File(Environment.getExternalStorageDirectory().toString() + separator + folderName)
            // getExternalStorageDirectory is deprecated in API 29

            if (!directory.exists()) {
                directory.mkdirs()
            }
            val fileName = System.currentTimeMillis().toString() + ".png"
            val file = File(directory, fileName)
            saveImageToStream(bitmap, FileOutputStream(file))
            val values = contentValues()
            values.put(MediaStore.Images.Media.DATA, file.absolutePath)
            // .DATA is deprecated in API 29
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        }
    }

    private fun contentValues() : ContentValues {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
        return values
    }

    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
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

        override fun onLongPress(e: MotionEvent?) {
            PopupMenu(this@FullScreenImgActivity, ivCloseFullscreen, Gravity.TOP).apply {
                inflate(R.menu.menu_fullscreen_images)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.downloadImageItem -> downloadAction()
                        R.id.useAsWallpaperItem -> useAsWallpaperAction()
                    }

                    true
                }
            }.show()
        }

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