package com.example.loginyokaiinfection;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class FullScreenVideoView extends VideoView {

    private int videoWidth;
    private int videoHeight;

    public FullScreenVideoView(Context context) {
        super(context);
    }

    public FullScreenVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullScreenVideoView(
            Context context,
            AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setVideoSize(int width, int height) {
        videoWidth = width;
        videoHeight = height;
        requestLayout();
    }

    @Override
    protected void onMeasure(
            int widthMeasureSpec,
            int heightMeasureSpec) {

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (videoWidth > 0 && videoHeight > 0) {
            float videoRatio =
                    (float) videoWidth / videoHeight;

            float screenRatio =
                    (float) width / height;

            if (videoRatio > screenRatio) {
                // Recorta los laterales.
                width = Math.round(height * videoRatio);
            } else {
                // Recorta la parte superior e inferior.
                height = Math.round(width / videoRatio);
            }
        }

        setMeasuredDimension(width, height);
    }
}