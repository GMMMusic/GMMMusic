package com.gaochlei.mulmusic.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import static android.R.attr.centerX;
import static android.R.attr.centerY;

/**
 * Created by 高春雷 on 2017/4/9.
 */

public class PlayerView extends View {
    private Paint circlePaint;
    private int circleColor;
    private boolean turnOn = true;

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void setPlayerOn(boolean turnOn) {
        this.turnOn = turnOn;
    }

    /**
     * 开始
     */
    public void start(){
        this.turnOn = false;
        this.invalidate();
    }

    /**
     * 暂停
     */
    public void pause(){
        this.turnOn = true;
        this.invalidate();
    }
    public boolean getPlayerOn() {
        return this.turnOn;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        circlePaint = new Paint();
        circlePaint.setStrokeWidth(4f);
        circlePaint.setAntiAlias(true);
        circleColor = Color.parseColor("#ff0000");
        circlePaint.setColor(circleColor);
        circlePaint.setStyle(Paint.Style.STROKE);
        int centerX = getWidth() / 2;
        int centerY = centerX;
        int radius = getWidth() / 2 - 2;
        canvas.drawCircle(centerX, centerY, radius, circlePaint);

        if (turnOn) {
            // 画三角形
            int measureUnit = getWidth() / 6;
            int startX_line1 = centerX - measureUnit + 3;
            int stopX_line1 = startX_line1;
            int startY_line1 = centerY - measureUnit - 3;
            int stopY_line1 = centerY + measureUnit + 3;
            canvas.drawLine(startX_line1, startY_line1, stopX_line1, stopY_line1, circlePaint);
            Path path2 = new Path();
            path2.moveTo(90, 330);
            path2.lineTo(150, 330);
            path2.lineTo(120, 270);
            path2.close();
            canvas.drawPath(path2, circlePaint);
            Shader mShader = new LinearGradient(0, 0, 100, 100, new int[]{Color.RED, Color.GREEN,
                    Color.BLUE, Color.YELLOW}, null, Shader.TileMode.REPEAT);
            circlePaint.setShader(mShader);
            int startX_line2 = startX_line1;
            int startY_line2 = startY_line1;
            int stopX_line2 = centerX + measureUnit;
            int stopY_line2 = centerY;
            canvas.drawLine(startX_line2, startY_line2, stopX_line2, stopY_line2, circlePaint);

            int startX_line3 = startX_line1;
            int startY_line3 = stopY_line1;
            int stopX_line3 = stopX_line2;
            int stopY_line3 = stopY_line2;
            canvas.drawLine(startX_line3, startY_line3, stopX_line3, stopY_line3, circlePaint);
        } else {
            int measureUnit = getWidth() / 6;
            int startX_line1 = centerX - measureUnit + 3;
            int stopX_line1 = startX_line1;
            int startY_line1 = centerY - measureUnit - 3;
            int stopY_line1 = centerY + measureUnit + 3;
            canvas.drawLine(startX_line1, startY_line1, stopX_line1, stopY_line1, circlePaint);

            int startX_line2 = centerX + measureUnit + 3;
            int startY_line2 = centerY - measureUnit - 3;
            int stopX_line2 = startX_line2;
            int stopY_line2 = centerY + measureUnit + 3;
            canvas.drawLine(startX_line2, startY_line2, stopX_line2, stopY_line2, circlePaint);
        }
//        startPlay(canvas,circlePaint);
    }

    private void stopPlay(Canvas canvas, Paint circlePaint) {
        int measureUnit = getWidth() / 6;
        int startX_line1 = centerX - measureUnit + 3;
        int stopX_line1 = startX_line1;
        int startY_line1 = centerY - measureUnit - 3;
        int stopY_line1 = centerY + measureUnit + 3;
        canvas.drawLine(startX_line1, startY_line1, stopX_line1, stopY_line1, circlePaint);

        int startX_line2 = centerX + measureUnit + 3;
        int startY_line2 = centerY - measureUnit - 3;
        int stopX_line2 = startX_line2;
        int stopY_line2 = centerY + measureUnit + 3;
        canvas.drawLine(startX_line2, startY_line2, stopX_line2, stopY_line2, circlePaint);
    }


    private void startPlay(Canvas canvas, Paint circlePaint) {
        // 画三角形
        int measureUnit = getWidth() / 6;
        int startX_line1 = centerX - measureUnit + 3;
        int stopX_line1 = startX_line1;
        int startY_line1 = centerY - measureUnit - 3;
        int stopY_line1 = centerY + measureUnit + 3;
        canvas.drawLine(startX_line1, startY_line1, stopX_line1, stopY_line1, circlePaint);

        int startX_line2 = startX_line1;
        int startY_line2 = startY_line1;
        int stopX_line2 = centerX + measureUnit;
        int stopY_line2 = centerY;
        canvas.drawLine(startX_line2, startY_line2, stopX_line2, stopY_line2, circlePaint);

        int startX_line3 = startX_line1;
        int startY_line3 = stopY_line1;
        int stopX_line3 = stopX_line2;
        int stopY_line3 = stopY_line2;
        canvas.drawLine(startX_line3, startY_line3, stopX_line3, stopY_line3, circlePaint);
    }
}
