package com.example.dolphin.bussiness;

import java.util.Random;

import static com.example.dolphin.bussiness.FightingDelegage.MapArray;

/**
 * 房间种类：宝箱房 boss房 普通房
 * 铺设类型：墙壁（不可击碎） 地板 障碍物（可击碎）敌人
 * 敌人类：发射逻辑 仇恨机制
 *
 */
public class RoomRemodel {
    // 房间种类: 宝箱 普通 boss 商店
    public static final int REWARD = 0;
    public static final int NORMAL = 1;
    public static final int BOSS   = 2;
    public static final int SHOP   = 3;

    // 房间中物品种类：墙壁 地板 障碍物 宝箱
    public static final int WALL   = 0;
    public static final int FLOOR  = 1;
    public static final int BAR    = 2;
    public static final int TREASURE = 3;

    // 房间大小类型
    public static final int LARGE   = 9;
    public static final int MIDDLE  = 7;
    public static final int LITTLE  = 5;

    /**
     * @description 边界检测
     * @param x 中心x坐标
     * @param y 中心y坐标
     * @param n 以（x,y）为中心的n*n范围
     * @return 是否出现墙体
     */
    public boolean boarderCheck(int x, int y, int n){
        int radius = n / 2;
        for(int i = x - radius; i <= x + radius; i++){
            for(int j = y - radius; j <= y + radius; j++){
                if(MapArray[i][j] != FLOOR){
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * @description 普通房间设置
     * @param roomX_init 本房间左上角X坐标
     * @param roomX_len  本房间长度
     * @param roomY_init 本房间左上角Y坐标
     * @param roomY_len  本房间宽度
     * @param init_x     本房间中心X坐标
     * @param init_y     本房间中心Y坐标
     */
    public void remodelNormal(int roomX_init, int roomY_init, int roomX_len, int roomY_len, int init_x, int init_y){
        if(boarderCheck(init_x, init_y, LARGE)){
            remodelLarge(init_x, init_y);
        }else if(boarderCheck(init_x, init_y, MIDDLE)){
            remodelMiddle(init_x, init_y);
        }else if (boarderCheck(init_x, init_y, LITTLE)) {
            remodelLittle(init_x, init_y);
        }
    }

    /**
     * @description 宝箱房间设置
     * @param roomX_init 本房间左上角X坐标
     * @param roomX_len  本房间长度
     * @param roomY_init 本房间左上角Y坐标
     * @param roomY_len  本房间宽度
     * @param init_x     本房间中心X坐标
     * @param init_y     本房间中心Y坐标
     */
    public void remodelTreasure(int roomX_init, int roomY_init, int roomX_len, int roomY_len, int init_x, int init_y){
            MapArray[init_x][init_y] = TREASURE;
    }

    /**
     * @description boss房间设置
     * @param roomX_init 本房间左上角X坐标
     * @param roomX_len  本房间长度
     * @param roomY_init 本房间左上角Y坐标
     * @param roomY_len  本房间宽度
     * @param init_x     本房间中心X坐标
     * @param init_y     本房间中心Y坐标
     */
    public void remodelBoss(int roomX_init, int roomY_init, int roomX_len, int roomY_len, int init_x, int init_y){
        MapArray[init_x - 5][init_y + 1] = BAR;
        MapArray[init_x - 5][init_y + 3] = BAR;
        MapArray[init_x - 4][init_y + 2] = BAR;
        MapArray[init_x - 6][init_y + 2] = BAR;

        MapArray[init_x - 5][init_y - 1] = BAR;
        MapArray[init_x - 5][init_y - 3] = BAR;
        MapArray[init_x - 4][init_y - 2] = BAR;
        MapArray[init_x - 6][init_y - 2] = BAR;

        MapArray[init_x + 5][init_y + 1] = BAR;
        MapArray[init_x + 5][init_y + 3] = BAR;
        MapArray[init_x + 4][init_y + 2] = BAR;
        MapArray[init_x + 6][init_y + 2] = BAR;

        MapArray[init_x + 5][init_y - 1] = BAR;
        MapArray[init_x + 5][init_y - 3] = BAR;
        MapArray[init_x + 4][init_y - 2] = BAR;
        MapArray[init_x + 6][init_y - 2] = BAR;
    }


    /**
     * @description 普通大房间设置
     * @param init_x     本房间中心X坐标
     * @param init_y     本房间中心Y坐标
     */
    private void remodelLarge(int init_x, int init_y){
        int barKind = new Random().nextInt(4);
        switch(barKind){
            case 0:
                for(int i = init_x - 3; i <= init_x + 3; i++) {
                    MapArray[i][init_y + 4] = BAR;
                    MapArray[i][init_y - 4] = BAR;
                }
                for(int i = init_y - 3; i <= init_y + 3; i++){
                    MapArray[init_x - 4][i] = BAR;
                    MapArray[init_x + 4][i] = BAR;
                }
                break;

            case 1:
                for(int i = init_x - 4; i <= init_x + 4; i++) {
                    MapArray[i][init_y] = BAR;
                }
                for(int i = init_y - 4; i <= init_y + 4; i++){
                    MapArray[init_x][i] = BAR;
                }

                MapArray[init_x - 1][init_y] = FLOOR;
                MapArray[init_x + 1][init_y] = FLOOR;
                MapArray[init_x][init_y + 1] = FLOOR;
                MapArray[init_x][init_y + 1] = FLOOR;
                break;

            case 2: break;
            case 3: remodelMiddle(init_x, init_y); break;
        }
    }

    /**
     * @description 普通中房间设置
     * @param init_x     本房间中心X坐标
     * @param init_y     本房间中心Y坐标
     */
    private void remodelMiddle(int init_x, int init_y){
        int barKind = new Random().nextInt(6);
        switch (barKind){
            case 0:
                MapArray[init_x - 3][init_y + 2] = BAR;
                MapArray[init_x - 2][init_y + 3] = BAR;
                MapArray[init_x - 1][init_y + 2] = BAR;

                MapArray[init_x + 3][init_y + 2] = BAR;
                MapArray[init_x + 2][init_y + 3] = BAR;
                MapArray[init_x + 1][init_y + 2] = BAR;

                MapArray[init_x - 3][init_y - 1] = BAR;
                MapArray[init_x + 3][init_y - 1] = BAR;
                MapArray[init_x - 2][init_y - 2] = BAR;
                MapArray[init_x + 2][init_y - 2] = BAR;
                MapArray[init_x - 1][init_y - 3] = BAR;
                MapArray[init_x + 1][init_y - 3] = BAR;
                MapArray[init_x][init_y - 3] = BAR;
                break;

            case 1:
                for(int x = init_x - 2; x <= init_x + 2; x = x + 2){
                    for(int y = init_y -2; y <= init_y + 2; y = y + 2){
                        MapArray[x][y] = BAR;
                    }
                }
                break;

            case 2:
                for(int y = init_y -2; y <= init_y + 2; y++){
                    MapArray[init_x-2][y] = BAR;
                    MapArray[init_x][y] = BAR;
                    MapArray[init_x+2][y] = BAR;
                }
                break;

            case 3:
                MapArray[init_x - 2][init_y + 2] = BAR;
                MapArray[init_x - 2][init_y + 1] = BAR;
                MapArray[init_x - 1][init_y + 2] = BAR;
                MapArray[init_x - 1][init_y + 1] = BAR;

                MapArray[init_x + 2][init_y + 2] = BAR;
                MapArray[init_x + 2][init_y + 1] = BAR;
                MapArray[init_x + 1][init_y + 2] = BAR;
                MapArray[init_x + 1][init_y + 1] = BAR;

                MapArray[init_x - 2][init_y - 2] = BAR;
                MapArray[init_x - 2][init_y - 1] = BAR;
                MapArray[init_x - 1][init_y - 2] = BAR;
                MapArray[init_x - 1][init_y - 1] = BAR;

                MapArray[init_x + 2][init_y - 2] = BAR;
                MapArray[init_x + 2][init_y - 1] = BAR;
                MapArray[init_x + 1][init_y - 2] = BAR;
                MapArray[init_x + 1][init_y - 1] = BAR;
                break;

            case 4: break;
            case 5: remodelLittle(init_x, init_y); break;
        }
    }

    /**
     * @description 普通小房间设置
     * @param init_x     本房间中心X坐标
     * @param init_y     本房间中心Y坐标
     */
    private void remodelLittle(int init_x, int init_y){
        int barKind = new Random().nextInt(6);
        switch (barKind) {
            case 0:
                MapArray[init_x - 2][init_y - 2] = BAR;
                MapArray[init_x + 2][init_y - 2] = BAR;
                MapArray[init_x][init_y] = BAR;
                MapArray[init_x - 2][init_y + 2] = BAR;
                MapArray[init_x + 2][init_y + 2] = BAR;
                break;

            case 1:
                MapArray[init_x - 2][init_y] = BAR;
                MapArray[init_x + 2][init_y] = BAR;
                MapArray[init_x][init_y] = BAR;
                MapArray[init_x][init_y - 2] = BAR;
                MapArray[init_x][init_y + 2] = BAR;
                break;

            case 2:
                MapArray[init_x - 2][init_y - 2] = BAR;
                MapArray[init_x - 2][init_y - 1] = BAR;
                MapArray[init_x - 2][init_y] = BAR;
                MapArray[init_x - 2][init_y + 1] = BAR;
                MapArray[init_x - 2][init_y + 2] = BAR;


                MapArray[init_x - 1][init_y + 2] = BAR;
                MapArray[init_x][init_y + 2] = BAR;
                MapArray[init_x + 1][init_y + 2] = BAR;

                MapArray[init_x + 2][init_y - 2] = BAR;
                MapArray[init_x + 2][init_y - 1] = BAR;
                MapArray[init_x + 2][init_y] = BAR;
                MapArray[init_x + 2][init_y + 1] = BAR;
                MapArray[init_x + 2][init_y + 2] = BAR;
                break;

            case 3:
                MapArray[init_x - 2][init_y + 1] = BAR;
                MapArray[init_x - 1][init_y + 1] = BAR;
                MapArray[init_x - 1][init_y + 2] = BAR;

                MapArray[init_x - 2][init_y - 1] = BAR;
                MapArray[init_x - 1][init_y - 1] = BAR;
                MapArray[init_x - 1][init_y - 2] = BAR;

                MapArray[init_x + 2][init_y + 1] = BAR;
                MapArray[init_x + 1][init_y + 1] = BAR;
                MapArray[init_x + 1][init_y + 2] = BAR;

                MapArray[init_x + 2][init_y - 1] = BAR;
                MapArray[init_x + 1][init_y - 1] = BAR;
                MapArray[init_x + 1][init_y - 2] = BAR;
                break;

            case 4:
                MapArray[init_x - 2][init_y + 1] = BAR;
                MapArray[init_x - 2][init_y + 2] = BAR;
                MapArray[init_x - 1][init_y + 2] = BAR;

                MapArray[init_x - 2][init_y - 1] = BAR;
                MapArray[init_x - 2][init_y - 2] = BAR;
                MapArray[init_x - 1][init_y - 2] = BAR;

                MapArray[init_x + 2][init_y + 1] = BAR;
                MapArray[init_x + 2][init_y + 2] = BAR;
                MapArray[init_x + 1][init_y + 2] = BAR;

                MapArray[init_x + 2][init_y - 1] = BAR;
                MapArray[init_x + 2][init_y - 2] = BAR;
                MapArray[init_x + 1][init_y - 2] = BAR;
                break;

            case 5:
                MapArray[init_x - 2][init_y - 2] = BAR;
                MapArray[init_x][init_y - 2] = BAR;
                MapArray[init_x + 2][init_y - 2] = BAR;
                MapArray[init_x][init_y - 2] = BAR;
                MapArray[init_x][init_y] = BAR;
                MapArray[init_x][init_y + 2] = BAR;
                MapArray[init_x - 2][init_y + 2] = BAR;
                MapArray[init_x][init_y + 2] = BAR;
                MapArray[init_x + 2][init_y + 2] = BAR;
                break;
        }
    }
}
