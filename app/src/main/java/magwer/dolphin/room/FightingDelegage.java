package magwer.dolphin.room;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import magwer.dolphin.game.room.RoomModel;
import magwer.dolphin.game.room.ChapterModel;

public class FightingDelegage{
    Context context;
    /**
     * 缓冲
     */
    private final Canvas mBufferCanvas;
    private final Bitmap mBufferBitmap;

    Paint clearPaint = new Paint();

    /**
     * 刷新互斥锁, 刷新界面时缓冲区不能刷新，刷新缓冲区时界面不能刷新
     */
    private final Mutex mMutex = new Mutex();
    private boolean mIsRunning;

    /**
     * 绘图列表
     */
    private final List<Floor> floors;
    private final List<Bar> bars;
    private final List<Treasure> treasures;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public FightingDelegage(int width, int height, Context context) {
        this.context = context;
        mBufferBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mBufferCanvas = new Canvas(mBufferBitmap);

        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        this.floors = new LinkedList<>();
        this.bars = new LinkedList<>();
        this.treasures = new LinkedList<>();

        test();

        mIsRunning = true;
        executorService.submit(new BufferDrawer());
    }

    /**
     * 结束时停止线程池
     */
    public void gameOver() {
        try {
            executorService.shutdown();
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
            Log.i("zhoukai", "gameOver: executorService end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @description 显示到界面, 将缓冲区中画好的图形绘制到surfaceview的canvas中
     * @param canvas 画布
     */
    public void draw(Canvas canvas) {
        if (null != canvas) {
            if (null != mBufferBitmap) {
                try {
                    mMutex.acquire(Thread.currentThread());

                    clearCanvas(canvas);
                    canvas.drawBitmap(mBufferBitmap, 0, 0, null);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mMutex.release(Thread.currentThread());
                }
            }
        }
    }

    private void clearCanvas(Canvas canvas) {
        canvas.drawPaint(clearPaint);
    }

    public void setmIsRunning(boolean mIsRunning) {
        this.mIsRunning = mIsRunning;
        gameOver();
    }

    /**
     * 绘制缓冲区的线程
     */

    class BufferDrawer implements Runnable {
        @Override
        public void run() {
            while (mIsRunning) {

                try {
                    mMutex.acquire(Thread.currentThread());

                    //绘制前先清除
                    clearCanvas(mBufferCanvas);

                    //绘制开始
                    for (int i = 0; i < floors.size(); i++) {
                        floors.get(i).draw(mBufferCanvas);
                    }
                    for (int i = 0; i < bars.size(); i++) {
                        bars.get(i).draw(mBufferCanvas);
                    }
                    for (int i = 0; i < treasures.size(); i++) {
                        treasures.get(i).draw(mBufferCanvas);
                    }
                    //绘制结束

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    mMutex.release(Thread.currentThread());
                }

                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Log.i("zhoukai", "BufferDrawer: end");
        }
    }

    /**
     * 互斥锁
     *
     */
    static class Mutex {
        private Thread owner;

        public Mutex() {
        }

        /**
         * 进入锁
         * @param thread
         * @throws InterruptedException
         */
        public synchronized void acquire(Thread thread) throws InterruptedException {
            if (owner != null) {
                wait();
            }

            owner = thread;
        }

        /**
         * 释放锁
         * @param thread
         * @throws InterruptedException
         */
        public synchronized void release(Thread thread) {
            if (owner == thread) {
                owner = null;
                notify();
            }
        }
    }

    // 房间宽高、地图宽高
    private final int room_width = 17;
    private final int room_height = 9;
    private final int map_width = 27;
    private final int map_height = 15;


    // 房间到边界的距离
    int border_x = 5;
    int border_y = 3;

    // 地图数组
    static RoomRemodel.SlotType[][] MapArray = new RoomRemodel.SlotType[27][15];

    /**
     * @description 生成房间
     */
    public void makeRoom() {
        int limit_x = room_width + border_x;
        int limit_y = room_height + border_y;
        for (int i = border_x; i < limit_x; i++) {
            for (int j = border_y; j < limit_y; j++) {
                MapArray[i][j] = RoomRemodel.SlotType.FLOOR;
            }
        }
    }

    /**
     * @description 装饰房间
     * @param init_x     本房间中心X坐标
     * @param init_y     本房间中心Y坐标
     * @param kind       房间类型
     */
    private void roomRemodel(int init_x, int init_y, int kind){
        // 初始化RoomRemodel方法对象
        RoomRemodel remodel = new RoomRemodel();
        switch (kind){
            case RoomRemodel.REWARD:
                remodel.remodelTreasure(init_x, init_y);
                break;
            case RoomRemodel.NORMAL:
                remodel.remodelNormal(init_x, init_y);
                break;
            case RoomRemodel.BOSS:
                remodel.remodelBoss(init_x, init_y);
                break;
        }
    }

    public void test() {
        //设置房间中心位置
        int init_x = border_x + room_width / 2;
        int init_y = border_y + room_height / 2;

        // 调用makeRoom制作地图并加入障碍物
        makeRoom();
        roomRemodel(init_x, init_y, RoomRemodel.NORMAL);

        // 房间2的开始
        makeRoom();
        roomRemodel(init_x, init_y, RoomRemodel.BOSS);
        // 房间2结束

        // 房间3的开始
        makeRoom();
        roomRemodel(init_x, init_y, RoomRemodel.REWARD);
        // 房间3的结束

        // 将存在MapArray中的地图画出来
        for (int i = 0; i < map_width; i++){
            for (int j = 0; j < map_height; j++){
                int floorSize = 40;
                if (MapArray[i][j] == RoomRemodel.SlotType.FLOOR)
                    floors.add(new Floor(floorSize * i, floorSize * j, context));
                if (MapArray[i][j] == RoomRemodel.SlotType.BAR)
                    bars.add(new Bar(floorSize * i, floorSize * j, context));
                if (MapArray[i][j] == RoomRemodel.SlotType.TREASURE)
                    treasures.add(new Treasure(floorSize * i, floorSize * j, context));
            }
        }

    }
}
