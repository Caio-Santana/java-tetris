package com.software.tetris;

import com.software.tetris.mino.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.software.tetris.Sound.SoundType.DELETE_LINE;
import static com.software.tetris.Sound.SoundType.GAME_OVER;

public class PlayManager {

    // Main Play Area
    final static int WIDTH = 360;
    final static int HEIGHT = 600;
    final static int MAX_BLOCKS_PER_LINE = 12;
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    // Mino
    Mino currentMino;
    final int MINO_START_X;
    final int MINO_START_Y;
    Mino nextMino;
    final int NEXT_MINO_X;
    final int NEXT_MINO_Y;
    public static List<Block> staticBlocks = new ArrayList<>();


    // Others
    public static int dropInterval = 60; // mino drops in every 60 frames
    boolean gameOver;

    // Effect
    boolean effectCounterOn;
    int effectCounter;
    final static int EFFECT_COUNTER_FRAME = 10;
    List<Integer> effectY = new ArrayList<>();

    // Score
    int currentLevel = 1;
    final static int SCORE_POINTS = 10;
    int lines;
    int score;

    {
        // Defines Main Play Area Frame
        left_x = (GamePanel.WIDTH / 2) - (WIDTH / 2);
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;

        // Set Mino Start Position
        MINO_START_X = left_x + (WIDTH / 2) - Block.SIZE;
        MINO_START_Y = top_y + Block.SIZE;

        // Set next Mino Position
        NEXT_MINO_X = right_x + 175;
        NEXT_MINO_Y = top_y + 500;
    }

    public PlayManager() {

        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);

        nextMino = pickMino();
        nextMino.setXY(NEXT_MINO_X, NEXT_MINO_Y);

    }

    private Mino pickMino() {
        Mino mino = null;
        int i = new Random().nextInt(7);
        switch (i) {
            case 0 -> mino = new Mino_L1();
            case 1 -> mino = new Mino_L2();
            case 2 -> mino = new Mino_Square();
            case 3 -> mino = new Mino_Bar();
            case 4 -> mino = new Mino_T();
            case 5 -> mino = new Mino_Z1();
            case 6 -> mino = new Mino_Z2();
        }
        return mino;
    }

    public void update() {

        // Check if the current Mino is active
        if (!currentMino.active) {
            // if the mino is not active, put it into the staticBlocks
            staticBlocks.addAll(Arrays.asList(currentMino.getB()));

            checkGameOver();

            currentMino.deactivating = false;

            // replace the currentMino with the nextMino
            currentMino = nextMino;
            currentMino.setXY(MINO_START_X, MINO_START_Y);
            nextMino = pickMino();
            nextMino.setXY(NEXT_MINO_X, NEXT_MINO_Y);

            // when a mino becomes inactive, check if line(s) can be deleted
            checkDelete();

        } else {
            currentMino.update();
        }
    }

    private void checkGameOver() {
        if (currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y) {
            // this means the currentMino immediately collided a block and couldn't move at all,
            // so it's xy are the same with the nextMino's
            gameOver = true;
            GamePanel.music.stop();
            GamePanel.se.play(GAME_OVER.getValue(), false);
        }
    }

    private void checkDelete() {

        int x = left_x;
        int y = top_y;
        int blockCount = 0;
        int lineCount = 0;

        while (x < right_x && y < bottom_y) {

            for (Block staticBlock : staticBlocks) {
                if (staticBlock.x == x && staticBlock.y == y) {
                    // increase the count if there is a static block
                    blockCount++;
                }
            }

            x += Block.SIZE;

            if (x == right_x) {

                // if the blockCount hits MAX_BLOCKS_PER_LINE, that means the current y line is all filled with blocks,
                // so we can delete them
                if (blockCount == MAX_BLOCKS_PER_LINE) {

                    effectCounterOn = true;
                    effectY.add(y);

                    for (int i = staticBlocks.size() - 1; i > -1; i--) {
                        // remove all the blocks in the current y line
                        if (staticBlocks.get(i).y == y) {
                            staticBlocks.remove(i);
                        }
                    }

                    lineCount++;
                    lines++;

                    // Drop Speed
                    // if the line score hits a certain number, increase the drop speed
                    // 1 is the fastest
                    if (lines % 10 == 0 && dropInterval > 1) {
                        currentLevel++;
                        if (dropInterval > 10) {
                            dropInterval -= 10;
                        } else {
                            dropInterval -= 1;
                        }
                    }


                    // a line has been deleted so to slide down blocks that are above it
                    for (Block staticBlock : staticBlocks) {
                        // if a block is above the current y, move it down by the block size
                        if (staticBlock.y < y) {
                            staticBlock.y += Block.SIZE;
                        }
                    }
                }

                blockCount = 0;
                x = left_x;
                y += Block.SIZE;
            }
        }

        // Add Score
        if (lineCount > 0) {
            GamePanel.se.play(DELETE_LINE.getValue(), false);
            int singleLineScore = SCORE_POINTS * currentLevel;
            score += singleLineScore * lineCount;
        }
    }

    public void draw(Graphics2D g2) {

        drawPlayAreaFrame(g2);
        drawNextMinoFrame(g2);
        drawScoreFrame(g2);

        drawCurrentMino(g2);

        drawNextMino(g2);

        drawStaticBlocks(g2);

        drawEffect(g2);

        // Draw Game Over
        drawGameOver(g2);

        // Draw Pause
        if (KeyHandler.pausedPressed && !gameOver) {
            drawPause(g2);
        }

        // draw the Game Title
        drawGameTitle(g2);
    }

    private void drawPlayAreaFrame(Graphics2D g2) {

        final int offsetSize = 4;
        final int rightOffset = offsetSize + offsetSize;
        final int bottomOffset = offsetSize + offsetSize;
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(
                left_x - offsetSize,
                top_y - offsetSize,
                WIDTH + rightOffset,
                HEIGHT + bottomOffset
        );

    }

    private void drawNextMinoFrame(Graphics2D g2) {

        int x_offsetSize = 100;
        int y_offsetSize = 200;
        int x = right_x + x_offsetSize;
        int y = bottom_y - y_offsetSize;
        g2.drawRect(x, y, 200, 200);

        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x + 60, y + 60);
    }

    private void drawScoreFrame(Graphics2D g2) {

        int x_offsetSize = 100;
        int x = right_x + x_offsetSize;
        int y = top_y;
        g2.drawRect(x, y, 250, 300);

        int positionX_text = x + 40;
        int positionY_text = top_y + 90;

        g2.drawString("LEVEL: " + currentLevel, positionX_text, positionY_text);
        positionY_text += 70;
        g2.drawString("LINES: " + lines, positionX_text, positionY_text);
        positionY_text += 70;
        g2.drawString("SCORE: " + score, positionX_text, positionY_text);
    }

    private void drawCurrentMino(Graphics2D g2) {
        if (currentMino != null) {
            currentMino.draw(g2);
        }
    }

    private void drawNextMino(Graphics2D g2) {
        nextMino.draw(g2);
    }

    private static void drawStaticBlocks(Graphics2D g2) {
        staticBlocks.forEach(block -> block.draw(g2));
    }

    private void drawEffect(Graphics2D g2) {
        if (effectCounterOn) {
            effectCounter++;

            g2.setColor(Color.RED);
            for (Integer y : effectY) {
                g2.fillRect(left_x, y, WIDTH, Block.SIZE);
            }

            if (effectCounter == EFFECT_COUNTER_FRAME) {
                effectCounterOn = false;
                effectCounter = 0;
                effectY.clear();
            }
        }
    }

    private void drawPause(Graphics2D g2) {
        g2.setColor(Color.YELLOW);
        g2.setFont(g2.getFont().deriveFont(50f));
        int x_offsetSize = 70;
        int y_offsetSize = 320;
        int x = left_x + x_offsetSize;
        int y = top_y + y_offsetSize;
        g2.drawString("PAUSED", x, y);
    }

    private void drawGameOver(Graphics2D g2) {
        if (!gameOver) return;

        g2.setColor(Color.YELLOW);
        g2.setFont(g2.getFont().deriveFont(50f));
        int x_offsetSize = 25;
        int y_offsetSize = 320;
        int x = left_x + x_offsetSize;
        int y = top_y + y_offsetSize;
        g2.drawString("GAME OVER", x, y);
    }

    private void drawGameTitle(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Times New Roman", Font.ITALIC, 60));
        int x_offsetSize = 20;
        int y_offsetSize = 320;
        int x = 35 + x_offsetSize;
        int y = top_y + y_offsetSize;
        g2.drawString("Simple Tetris", x, y);
    }
}
