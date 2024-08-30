package com.software.tetris.mino;

import com.software.tetris.enums.Direction;

import java.awt.*;

public class Mino_Bar extends Mino {

    public Mino_Bar() {
        create(Color.CYAN);
    }

    @Override
    public void setXY(int x, int y) {

        //
        //3 1 0 2
        //

        b[0].x = x;
        b[0].y = y;
        b[1].x = b[0].x - Block.SIZE;
        b[1].y = b[0].y;
        b[2].x = b[0].x + Block.SIZE;
        b[2].y = b[0].y;
        b[3].x = b[0].x + Block.SIZE * 2;
        b[3].y = b[0].y;

    }

    @Override
    public void getDirectionOne() {

        //
        //3 1 0 2
        //

        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x - Block.SIZE;
        tempB[1].y = b[0].y;
        tempB[2].x = b[0].x + Block.SIZE;
        tempB[2].y = b[0].y;
        tempB[3].x = b[0].x + Block.SIZE * 2;
        tempB[3].y = b[0].y;

        updateXY(Direction.ONE);
    }

    @Override
    public void getDirectionTwo() {

        //    1
        //    0
        //    2
        //    3

        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x;
        tempB[1].y = b[0].y - Block.SIZE;
        tempB[2].x = b[0].x;
        tempB[2].y = b[0].y + Block.SIZE;
        tempB[3].x = b[0].x;
        tempB[3].y = b[0].y + Block.SIZE * 2;

        updateXY(Direction.TWO);
    }

    @Override
    public void getDirectionThree() {
        getDirectionOne();
    }

    @Override
    public void getDirectionFour() {
        getDirectionTwo();
    }
}
