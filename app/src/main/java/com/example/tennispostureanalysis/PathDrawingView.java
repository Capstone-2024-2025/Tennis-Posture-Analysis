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

public class PathDrawingView extends View {
    private List<PointF> wristPath = new ArrayList<>();
    private Paint paint;

    public PathDrawingView(Context context) {
        super(context);
        init();
    }

    public PathDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED); // Red is more visible
        paint.setStrokeWidth(10f); // Thicker line
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        setWillNotDraw(false);
    }

    public void setWristPath(List<PointF> path) {
        if (path != null) {
            this.wristPath = new ArrayList<>(path); // Create a copy to avoid reference issues
            invalidate();
        }
    }

    public void clearPath() {
        wristPath.clear();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (wristPath != null && wristPath.size() > 1) {
            for (int i = 0; i < wristPath.size() - 1; i++) {
                PointF p1 = wristPath.get(i);
                PointF p2 = wristPath.get(i + 1);
                canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);
            }
        }
    }
}