package magwer.dolphin.room;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import magwer.dolphin.api.FileUtilsKt;

public class Treasure extends magwer.dolphin.room.Wall {
    private Bitmap bitmap;
    private Bitmap bitmap_open;

    private final boolean isClosed;

    public Treasure(float x, float y, Context context) {
        super(x, y);
        bitmap = FileUtilsKt.loadBitmapAsset(context,"texture/treasure.png").copy(Bitmap.Config.ARGB_8888, true);
        bitmap_open = FileUtilsKt.loadBitmapAsset(context,"texture/treasure_open.png").copy(Bitmap.Config.ARGB_8888, true);
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
