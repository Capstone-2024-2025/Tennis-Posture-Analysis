package com.example.tennispostureanalysis;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class InferenceOverlayView extends View {
    private String[] anglesToDisplay;
    private List<PointF> wristPath = new ArrayList<>();

    private Paint paint = new Paint();

    public InferenceOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(Color.YELLOW);
        paint.setTextSize(50f);
        paint.setStrokeWidth(5f);
    }
    public void setWristPath(List<PointF> path) {
        this.wristPath = path;
    }


    public void setInferenceData(String[] angles) {
        this.anglesToDisplay = angles;
        invalidate(); // Force redraw
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (wristPath != null && wristPath.size() > 1) {
            Paint paint = new Paint();
            paint.setColor(Color.GREEN);
            paint.setStrokeWidth(8f);

            for (int i = 0; i < wristPath.size() - 1; i++) {
                PointF p1 = wristPath.get(i);
                PointF p2 = wristPath.get(i + 1);
                canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);
            }
        }
    }
}