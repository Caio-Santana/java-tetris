package com.software.tetris.mino;

import java.awt.*;

public class Block extends Rectangle {
    public final static int BLOCK_MARGIN = 1;
    public int x, y;
    public static final int SIZE = 30; // 30x30 block
    public Color c;

    public Block(Color c) {
        this.c = c;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(c);
        g2.fillRect(
                x + BLOCK_MARGIN,
                y + BLOCK_MARGIN,
                SIZE - (BLOCK_MARGIN * 2),
                SIZE - (BLOCK_MARGIN * 2)
        );
    }

    public static void moveBlockDown(Block b) {
        b.y += Block.SIZE;
    }

    public static void moveBlockRight(Block b) {
        b.x += Block.SIZE;
    }

    public static void moveBlockLeft(Block b) {
        b.x -= Block.SIZE;
    }
}
