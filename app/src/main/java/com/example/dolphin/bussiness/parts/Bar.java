package com.example.dolphin.bussiness.parts;

/**
 * @anthor: zlw
 * @date: 2021/6/15
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.dolphin.R;

public class Bar extends Wall {
    private Paint BarPaint;

    private Bitmap bitmap;

    public Bar(float x, float y, Context context) {
        super(x, y);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.stone).copy(Bitmap.Config.ARGB_8888, true);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scaleWidth = ((float) 40) / width;
        float scaleHeight = ((float) 40) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public Bar(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, getX(), getY(), new Paint());
    }

    @Override
    protected void onDraw(Canvas canvas) {
    }
}

