package magwer.dolphin.room;

import magwer.dolphin.api.FileUtilsKt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class Bar extends magwer.dolphin.room.Wall {
    private Bitmap bitmap;

    public Bar(float x, float y, Context context) {
        super(x, y);
        bitmap = FileUtilsKt.loadBitmapAsset(context, "texture/stone.png").copy(Bitmap.Config.ARGB_8888, true);

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

