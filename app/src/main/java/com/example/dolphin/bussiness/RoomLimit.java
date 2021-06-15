package com.example.dolphin.bussiness;

import android.graphics.Canvas;

import com.example.dolphin.bussiness.parts.Shape;

import java.util.Random;

public class RoomLimit implements Shape {
    //房间生成的参数种子，包括房间大小、房间形状（拐点控制）、房间间距
    private float RoomSizeParam;
    private float RoomShapeParam;
    private float RoomRejectParam;
    private int RoomSize;
    private int RoomShape;
    private int RoomReject;
    private Random random = new Random();

    //FMY体验之后设定的最佳超参数
    private static final int ffSizeSeed = 8;
    private static final int ffSizeParam = 12;
    private static final int ffShapeSeed = 10;
    private static final int ffShapeParam = 5;
    private static final int ffRejectSeed = 4;
    private static final int ffRejectParam = 2;

    public RoomLimit(float RoomSizeParam, float RoomShapeParam, float RoomRejectParam) {
        this.RoomSizeParam = RoomSizeParam;
        this.RoomShapeParam = RoomShapeParam;
        this.RoomRejectParam = RoomRejectParam;
    }

    //使用参数种子（伪）随机生成房间参数
    public int RoomSizeFeature() {
        int random1 = random.nextInt(ffSizeSeed);
        this.RoomSize = (int) (RoomSizeParam * random1) + ffSizeParam;
        return RoomSize;
    }

    public boolean RoomShapeFeature() {
        int random2 = random.nextInt(ffShapeSeed);
        return random2 - ffShapeParam >= 0;
    }

    public int RoomRejectFeature() {
        int random3 = random.nextInt(ffRejectSeed);
        this.RoomReject = (int) (RoomRejectParam * (float) random3) + ffRejectParam;
        return RoomReject;
    }

    @Override
    public void draw(Canvas canvas) {
    }
}
