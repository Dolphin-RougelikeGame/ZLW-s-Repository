package magwer.dolphin.room;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import magwer.dolphin.api.FileUtilsKt;

public class Floor extends magwer.dolphin.room.Wall {
    private Bitmap bitmap;

    public Floor(float x, float y, Context context) {
        super(x, y);
        bitmap = FileUtilsKt.loadBitmapAsset(context,"texture/aaa.jpg").copy(Bitmap.Config.ARGB_8888, true);

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

    public Floor(float x, float y, float width, float height) {
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
