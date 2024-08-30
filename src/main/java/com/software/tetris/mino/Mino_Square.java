package com.software.tetris.mino;

import com.software.tetris.enums.Direction;

import java.awt.*;

public class Mino_Square extends Mino {
    public Mino_Square() {
        create(Color.YELLOW);
    }

    @Override
    public void setXY(int x, int y) {

        //  0 1
        //  2 3
        //

        b[0].x = x;
        b[0].y = y;
        b[1].x = b[0].x;
        b[1].y = b[0].y + Block.SIZE;
        b[2].x = b[0].x + Block.SIZE;
        b[2].y = b[0].y;
        b[3].x = b[0].x + Block.SIZE;
        b[3].y = b[0].y + Block.SIZE;

    }

    @Override
    public void getDirectionOne() {
    }

    @Override
    public void getDirectionTwo() {
    }

    @Override
    public void getDirectionThree() {
    }

    @Override
    public void getDirectionFour() {
    }
}
