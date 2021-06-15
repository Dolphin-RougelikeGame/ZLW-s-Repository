package magwer.dolphin.room;

import android.graphics.Canvas;

public abstract class Wall implements magwer.dolphin.room.Shape {

    public static final float DEFAULT_WIDTH = 30;
    public static final float DEFAULT_HEIGHT = 30;

    /**
     * 左上角
     */
    private final float x;
    private final float y;

    public Wall(float x, float y) {
        this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Wall(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Canvas canvas) {}

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    protected abstract void onDraw(Canvas canvas);
}
