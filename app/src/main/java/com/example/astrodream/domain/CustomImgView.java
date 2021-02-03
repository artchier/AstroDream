package com.example.astrodream.domain;
import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;

public class CustomImgView extends androidx.appcompat.widget.AppCompatImageView
{

    public CustomImgView(Context context)
    {
        super(context);
        setScaleType(ScaleType.MATRIX);
    }

    public CustomImgView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setScaleType(ScaleType.MATRIX);
    }

    public CustomImgView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        setScaleType(ScaleType.MATRIX);
    }


    @Override
    // Override to crop the image in vertical direction and fit in horizontal direction
    protected boolean setFrame(int frameLeft, int frameTop, int frameRight, int frameBottom) {
        if (getDrawable() != null) {
            float frameWidth = frameRight - frameLeft;
            float originalImageWidth = (float) getDrawable().getIntrinsicWidth();

            // => Crop it, keep aspect ratio, align top to top of frame and center horizontally
            float fitHorizontallyScaleFactor = frameWidth / originalImageWidth;
            float usedScaleFactor = fitHorizontallyScaleFactor;

            Matrix matrix = getImageMatrix();
            matrix.setScale(usedScaleFactor, usedScaleFactor, 0, 0); // Replaces the old matrix completly
            setImageMatrix(matrix);
        }
        return super.setFrame(frameLeft, frameTop, frameRight, frameBottom);
    }
}