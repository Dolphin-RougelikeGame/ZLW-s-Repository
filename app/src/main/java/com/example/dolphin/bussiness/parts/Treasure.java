package com.example.dolphin.bussiness.parts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.dolphin.R;

/**
 * @anthor: zlw
 * @date: 2021/6/13
 */

public class Treasure extends Wall{
    private Paint TreasurePaint;

    private Bitmap bitmap;
    private Bitmap bitmap_open;

    private boolean isClosed;

    public Treasure(float x, float y, Context context) {
        super(x, y);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.treasure).copy(Bitmap.Config.ARGB_8888, true);
        bitmap_open = BitmapFactory.decodeResource(context.getResources(), R.drawable.treasure_open).copy(Bitmap.Config.ARGB_8888, true);
        isClosed = true;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scaleWidth = ((float) 80) / width;
        float scaleHeight = ((float) 80) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        bitmap_open = Bitmap.createBitmap(bitmap_open, 0, 0, width, height, matrix, true);
    }

    public Treasure(float x, float y, float width, float height) {
        super(x, y, width, height);
        isClosed = true;
    }

    @Override
    public void draw(Canvas canvas) {
        if(isClosed)canvas.drawBitmap(bitmap, getX(), getY(), new Paint());
        else canvas.drawBitmap(bitmap_open, getX(), getY(), new Paint());
    }

    @Override
    protected void onDraw(Canvas canvas) {
    }
}
