package com.example.dolphin.bussiness;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;

import com.example.dolphin.bussiness.parts.EnemyTank;
import com.example.dolphin.bussiness.parts.Explode;
import com.example.dolphin.bussiness.parts.Missile;
import com.example.dolphin.bussiness.parts.OursTank;
import com.example.dolphin.bussiness.parts.Floor;
import com.example.dolphin.bussiness.parts.Treasure;
import com.example.dolphin.bussiness.parts.Wall;
import com.example.dolphin.bussiness.parts.Bar;
import com.example.dolphin.bussiness.RoomRemodel;
import com.example.dolphin.view.FireButton;
import com.example.dolphin.view.SteeringWheelView;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FightingDelegage implements ITankHitHandler, IMissileHitHandler,
        SteeringWheelView.IDirectionListener, FireButton.IFireListener{
    private static final String TAG = "FightingDelegage";

    Context context;

    /**
     * canvas宽度和高度
     */
    private int mWidth;
    private int mHeight;

    /**
     * Room类随机生成房间的
     */
    private RoomLimit roomLimit;
    /**
     * 缓冲
     */
    private Canvas mBufferCanvas;
    private Bitmap mBufferBitmap;

    Paint clearPaint = new Paint();

    /**
     * 刷新互斥锁, 刷新界面时缓冲区不能刷新，刷新缓冲区时界面不能刷新
     */
    private Mutex mMutex = new Mutex();
    private boolean mIsRunning;

    /**
     * 敌方子弹
     */
    private List<Missile> enemyMissiles;
    private List<Missile> oursMissiles;
    private List<EnemyTank> enemyTanks;
    private List<Explode> explodes;
    private List<Wall> walls;
    private List<Floor> floors;
    private List<Bar> bars;
    private List<Treasure> treasures;
    private OursTank oursTank;
    /**
     * 控制消息
     */
    private Queue<Message> mq = new ArrayDeque<>(10);

    /**
     * 同样的方向只放一次
     */
    private Message currentMessage;
    private Object driveLock = new Object();
    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    public FightingDelegage(int width, int height, Context context) {
        this.context = context;
        this.mWidth = width;
        this.mHeight = height;
        mBufferBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mBufferCanvas = new Canvas(mBufferBitmap);

        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        this.enemyMissiles = new LinkedList<>();
        this.oursMissiles = new LinkedList<>();
        this.enemyTanks = new LinkedList<>();
        this.walls = new LinkedList<>();
        this.floors = new LinkedList<>();
        this.explodes = new LinkedList<>();
        this.bars = new LinkedList<>();
        this.treasures = new LinkedList<>();

        test();

        mIsRunning = true;
        executorService.submit(new BufferDrawer());
        executorService.submit(new MissileDriver());
        executorService.submit(new TankDriver());
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
    public void draw(Canvas canvas, Context context) {
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

                    //敌方坦克
                    for (int i = 0; i < enemyTanks.size(); i++) {
                        enemyTanks.get(i).draw(mBufferCanvas);
                    }

                    //我方坦克
                    oursTank.draw(mBufferCanvas);

                    //我方子弹
                    for (int i = 0; i < oursMissiles.size(); i++) {
                        oursMissiles.get(i).draw(mBufferCanvas);
                    }

                    //子弹
                    for (int i = 0; i < enemyMissiles.size(); i++) {
                        enemyMissiles.get(i).draw(mBufferCanvas);
                    }

                    //爆炸效果
                    for (int i = 0; i < explodes.size(); i++) {
                        explodes.get(i).draw(mBufferCanvas);
                    }
                    explodes.clear();
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
     * 控制敌方坦克移动, 子弹移动, 碰撞检测
     */
    class MissileDriver implements Runnable {
        @Override
        public void run() {
            while (mIsRunning) {
                try {
                    mMutex.acquire(Thread.currentThread());

                    //控制敌方坦克移动, 子弹移动,碰撞检测
                    for (int i = 0; i < enemyTanks.size(); i++) {
                        enemyTanks.get(i).fireAtWill();
                        enemyTanks.get(i).moveAtWill(enemyTanks, oursTank, walls);
                    }

                    for (int i = 0; i < enemyMissiles.size(); i++) {
                        enemyMissiles.get(i).move();
                    }
                    for (int i = 0; i < oursMissiles.size(); i++) {
                        oursMissiles.get(i).move();
                    }

                    //已检查是否出界, 还需要加入是否打到我方坦克和打到墙
                    for (int i = 0; i < enemyMissiles.size(); i++) {
                        if (!enemyMissiles.get(i).isOut()) {
                            enemyMissiles.get(i).hitTank(oursTank);
                        }
                    }

                    for (int i = 0; i < oursMissiles.size(); i++) {
                        if (!oursMissiles.get(i).isOut()) {
                            oursMissiles.get(i).hitTanks(enemyTanks);
                        }
                    }
                    //控制敌方坦克移动, 子弹移动,碰撞检测


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mMutex.release(Thread.currentThread());
                }


                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private float translateX;
    private float translateY;

    /**
     * 控制我方坦克移动和开火
     */
    class TankDriver implements Runnable {
        private boolean isDriving;
        private Direction currentDirection;

        @Override
        public void run() {
            while (mIsRunning) {
                Message message = mq.poll();
                if (null != message || isDriving) {
                    if (null != message) {
                        switch (message) {
                            case UP:
                                isDriving = true;
                                currentDirection = Direction.UP;
                                break;
                            case DOWN:
                                isDriving = true;
                                currentDirection = Direction.DOWN;
                                break;
                            case LEFT:
                                isDriving = true;
                                currentDirection = Direction.LEFT;
                                break;
                            case RIGHT:
                                isDriving = true;
                                currentDirection = Direction.RIGHT;
                                break;
                            case STOP:
                                isDriving = false;
                                currentDirection = null;
                                break;
                            default:
                                break;
                        }
                    }

                    if (null != currentDirection && isDriving) {
                        oursTank.moveWithDirection(currentDirection, enemyTanks, walls);
                    }
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        synchronized (driveLock) {
                            driveLock.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
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

    enum  Message {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        STOP
    }

    @Override
    public void onEnemyTankDestroy(EnemyTank destroy) {
        enemyTanks.remove(destroy);
    }

    @Override
    public void onOursTankDestroy(OursTank oursTank) {

    }

    @Override
    public void onEnemyFire(Missile missile) {
        missile.setMissileHitHandler(this);
        enemyMissiles.add(missile);
    }

    @Override
    public void onOursFire(Missile missile) {
        missile.setMissileHitHandler(this);
        oursMissiles.add(missile);
    }

    @Override
    public void onHit(Missile missile) {
        enemyMissiles.remove(missile);
        oursMissiles.remove(missile);
        explodes.add(new Explode(missile.getX(), missile.getY()));
    }

    @Override
    public void onUp() {
        Log.w(TAG, "onUp: ");
        if (currentMessage == Message.UP) {
            return;
        }

        mq.offer(Message.UP);
        synchronized (driveLock) {
            driveLock.notifyAll();
        }
    }

    @Override
    public void onDown() {
        Log.i(TAG, "onDown: ");
        if (currentMessage == Message.DOWN) {
            return;
        }

        mq.offer(Message.DOWN);
        synchronized (driveLock) {
            driveLock.notifyAll();
        }
    }

    @Override
    public void onLeft() {
        Log.i(TAG, "onLeft: ");
        if (currentMessage == Message.LEFT) {
            return;
        }

        mq.offer(Message.LEFT);
        synchronized (driveLock) {
            driveLock.notifyAll();
        }
    }

    @Override
    public void onRight() {
        Log.i(TAG, "onRight: ");
        if (currentMessage == Message.RIGHT) {
            return;
        }

        mq.offer(Message.RIGHT);
        synchronized (driveLock) {
            driveLock.notifyAll();
        }
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop: ");
        mq.offer(Message.STOP);

        currentMessage = null;
        synchronized (driveLock) {
            driveLock.notifyAll();
        }
    }

    @Override
    public void onFire() {
        Log.i(TAG, "onFire: ");
        oursTank.fire();
    }

// 定义地图数组
    private static final int mapWidth  = 108;
    private static final int mapHeight = 60;
    static int[][] MapArray = new int[mapWidth][mapHeight];
    private Random random = new Random();
    private int turn = random.nextInt(4);

    /**
     * @description 生成房间
     * @param roomX_init 本房间左上角X坐标
     * @param roomX_len  本房间长度
     * @param roomY_init 本房间左上角Y坐标
     * @param roomY_len  本房间宽度
     * @param init_x     本房间中心X坐标
     * @param init_y     本房间中心Y坐标
     */
    private void makeRoom(int roomX_init, int roomY_init, int roomX_len, int roomY_len, int init_x, int init_y) {
        // 如果到了边界就停止扩展
        if (init_x >= roomX_len - 1 + roomX_init || init_y >= roomY_len - 1 + roomY_init || init_x <= roomX_init || init_y <= roomY_init)
            return;

        if (roomLimit.RoomShapeFeature()) {
            turn = (turn + 1) % 4;
        }

        MapArray[init_x + 1][init_y] = RoomRemodel.FLOOR;
        MapArray[init_x][init_y + 1] = RoomRemodel.FLOOR;
        MapArray[init_x - 1][init_y] = RoomRemodel.FLOOR;
        MapArray[init_x][init_y - 1] = RoomRemodel.FLOOR;

        switch (turn) {
            case 0:
                makeRoom(roomX_init, roomY_init, roomX_len, roomY_len, init_x + 1, init_y);
                break;
            case 1:
                makeRoom(roomX_init, roomY_init, roomX_len, roomY_len, init_x - 1, init_y);
                break;
            case 2:
                makeRoom(roomX_init, roomY_init, roomX_len, roomY_len, init_x, init_y + 1);
                break;
            case 3:
                makeRoom(roomX_init, roomY_init, roomX_len, roomY_len, init_x, init_y - 1);
                break;
            default:
                break;
        }
    }

    /**
     * @description 生成通道
     * @param roomX_init 上一房间左上角X坐标
     * @param roomX_len  上一房间长度
     * @param roomY_init 上一房间左上角Y坐标
     * @param roomY_len  上一房间宽度
     * @param init_x     上一房间中心X坐标
     * @param init_y     上一房间中心Y坐标
     * @param kind       横向或纵向走廊
     */
    private void makePassWay(int roomX_init, int roomY_init, int roomX_len, int roomY_len, int init_x, int init_y, int kind){
        int passWay_X ;
        int passWay_Y ;
        int passWayLen = 10; //走廊长度

        // 横向走廊
        if(kind == 0){
            passWay_X = roomX_init + roomX_len;
            passWay_Y = init_y;

            while (MapArray[passWay_X][passWay_Y] == RoomRemodel.WALL) {
                passWay_X--;
                passWayLen++;
            }

            for (int i = 0; i < passWayLen; i++) {
                MapArray[passWay_X + i][passWay_Y] = RoomRemodel.FLOOR;
            }
        }

        //纵向走廊
        if(kind == 1){
            passWay_X = init_x;
            passWay_Y = roomY_init + roomY_len;
            while (MapArray[passWay_X][passWay_Y] == RoomRemodel.WALL) {
                passWay_Y--;
                passWayLen++;
            }
            for (int i = 0; i < passWayLen; i++) {
                MapArray[passWay_X][passWay_Y + i] = RoomRemodel.FLOOR;
            }
        }
    }

    /**
     * @description 生成房间
     * @param roomX_init 本房间左上角X坐标
     * @param roomX_len  本房间长度
     * @param roomY_init 本房间左上角Y坐标
     * @param roomY_len  本房间宽度
     * @param init_x     本房间中心X坐标
     * @param init_y     本房间中心Y坐标
     * @param kind       房间类型
     */
    private void roomRemodel(int roomX_init, int roomY_init, int roomX_len, int roomY_len, int init_x, int init_y, int kind){
        // 初始化RoomRemodel方法对象
        RoomRemodel remodel = new RoomRemodel();
        switch (kind){
            case RoomRemodel.REWARD:
                remodel.remodelTreasure(roomX_init, roomY_init, roomX_len, roomY_len, init_x, init_y);
                break;
            case RoomRemodel.NORMAL:
                remodel.remodelNormal(roomX_init, roomY_init, roomX_len, roomY_len, init_x, init_y);
                break;
            case RoomRemodel.BOSS:
                remodel.remodelBoss(roomX_init, roomY_init, roomX_len, roomY_len, init_x, init_y);
                break;
        }
    }



    public void test() {
        // 初始化roomLimit对象
        roomLimit = new RoomLimit(0.5f, 0.5f, 0.5f);

        // 初始化地图数组
        for (int i = 0; i < mapWidth; i++)
            for (int j = 0; j < mapHeight; j++)
                MapArray[i][j] = RoomRemodel.WALL;

        // 设置房间一
        int floorSize = 40;
        int temp = roomLimit.RoomSizeFeature();

        int roomX_1_init = 2;
        int roomY_1_init = 2;
        int roomY_1_len = temp;
        int roomX_1_len = (int) (roomY_1_len * 1.4);

        int init_x = roomX_1_init + (int) (roomX_1_len / 2);
        int init_y = roomY_1_init + (int) (roomY_1_len / 2);
        MapArray[init_x][init_y] = RoomRemodel.FLOOR;

        // 调用makeRoom制作地图并加入障碍物
        makeRoom(roomX_1_init, roomY_1_init, roomX_1_len, roomY_1_len, init_x, init_y);
        roomRemodel(roomX_1_init, roomY_1_init, roomX_1_len, roomY_1_len, init_x, init_y, RoomRemodel.NORMAL);

        // 制作房间1和2之间的通道
        makePassWay(roomX_1_init, roomY_1_init, roomX_1_len, roomY_1_len, init_x, init_y, 0);

        // 房间2的开始
        temp = roomLimit.RoomSizeFeature();
        int roomY_2_len = temp;
        int roomX_2_len = (int) (temp * 1.4);
        int roomX_2_init = roomX_1_init + roomX_1_len + roomLimit.RoomRejectFeature();
        int roomY_2_init = roomY_1_init;
        init_x = roomX_2_init + roomX_2_len / 2;
        init_y = roomY_2_init + roomY_2_len / 2;
        MapArray[init_x][init_y] = RoomRemodel.FLOOR;
        makeRoom(roomX_2_init, roomY_2_init, roomX_2_len, roomY_2_len, init_x, init_y);
        roomRemodel(roomX_2_init, roomY_2_init, roomX_2_len, roomY_2_len, init_x, init_y, RoomRemodel.BOSS);
        // 房间2结束

        // 制作房间2和3之间的通道
        makePassWay(roomX_2_init, roomY_2_init, roomX_2_len, roomY_2_len, init_x, init_y, 1);

        // 房间3的开始
        temp = roomLimit.RoomSizeFeature();
        int roomY_3_len = temp;
        int roomX_3_len = (int) (temp * 1.4);
        int roomX_3_init = roomX_2_init;
        int roomY_3_init = roomY_2_init + roomY_2_len + roomLimit.RoomRejectFeature();
        init_x = roomX_3_init + roomX_3_len / 2;
        init_y = roomY_3_init + roomY_3_len / 2;
        MapArray[init_x][init_y] = RoomRemodel.FLOOR;
        makeRoom(roomX_3_init, roomY_3_init, roomX_3_len, roomY_3_len, init_x, init_y);
        roomRemodel(roomX_3_init, roomY_3_init, roomX_3_len, roomY_3_len, init_x, init_y, RoomRemodel.REWARD);
        // 房间3的结束

        // 将存在MapArray中的地图画出来
        for (int i = 0; i < mapWidth; i++){
            for (int j = 0; j < mapHeight; j++){
                if (MapArray[i][j] == RoomRemodel.FLOOR)
                    floors.add(new Floor(floorSize * i, floorSize * j, context));
                if (MapArray[i][j] == RoomRemodel.BAR)
                    bars.add(new Bar(floorSize * i, floorSize * j, context));
                if (MapArray[i][j] == RoomRemodel.TREASURE)
                    treasures.add(new Treasure(floorSize * i, floorSize * j, context));
            }
        }


//        enemyTanks.add(new EnemyTank(1000, 1000, mWidth, mHeight, Direction.LEFT));
//        enemyTanks.add(new EnemyTank(1100, 1100, mWidth, mHeight, Direction.LEFT));
//        enemyTanks.add(new EnemyTank(mWidth - 100, 100, mWidth, mHeight, Direction.UP));
//        enemyTanks.add(new EnemyTank(mWidth - 1000, 1000, mWidth, mHeight, Direction.LEFT));
//
//        for (EnemyTank enemyTank : enemyTanks) {
//            enemyTank.setHitHandler(this);
//        }
//
//        oursTank = new OursTank(mWidth / 2, mHeight / 2, mWidth, mHeight, Direction.LEFT);
//        oursTank.setHitHandler(this);

        translateX = 0;
        translateY = 0;
    }
}
