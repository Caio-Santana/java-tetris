package com.software.tetris.mino;

import com.software.tetris.GamePanel;
import com.software.tetris.KeyHandler;
import com.software.tetris.PlayManager;
import com.software.tetris.enums.Direction;

import java.awt.*;
import java.util.Arrays;

import static com.software.tetris.Sound.SoundType.*;
import static com.software.tetris.mino.Block.BLOCK_MARGIN;

public abstract class Mino {

    // - - +
    // - = +
    // - + +

    public Block[] b = new Block[4];
    public Block[] tempB = new Block[4];
    int autoDropCounter = 0;
    public Direction direction = Direction.ONE;  // There are 4 directions (ONE/TWO/THREE/FOUR)
    boolean leftCollision, rightCollision, bottomCollision;
    public boolean active = true;
    public boolean deactivating;
    int deactivateCounter = 0;

    public Block[] getB() {
        return b;
    }

    public void create(Color c) {

        b[0] = new Block(c);
        b[1] = new Block(c);
        b[2] = new Block(c);
        b[3] = new Block(c);

        tempB[0] = new Block(c);
        tempB[1] = new Block(c);
        tempB[2] = new Block(c);
        tempB[3] = new Block(c);

    }

    public abstract void setXY(int x, int y);

    public void updateXY(Direction direction) {

        checkRotationCollision();
        if (!leftCollision && !rightCollision && !bottomCollision) {
            this.direction = direction;
            for (int i = 0; i < b.length; i++) {
                b[i].x = tempB[i].x;
                b[i].y = tempB[i].y;
            }
        }

    }

    public abstract void getDirectionOne();

    public abstract void getDirectionTwo();

    public abstract void getDirectionThree();

    public abstract void getDirectionFour();

    public void checkMovementCollision() {

        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        checkStaticBlockCollision();

        // Check frame collision
        // left wall
        for (Block block : b) {
            if (block.x == PlayManager.left_x) {
                leftCollision = true;
                break;
            }
        }

        // right wall
        for (Block block : b) {
            if (block.x + Block.SIZE == PlayManager.right_x) {
                rightCollision = true;
                break;
            }
        }

        // bottom floor
        for (Block block : b) {
            if (block.y + Block.SIZE == PlayManager.bottom_y) {
                bottomCollision = true;
                break;
            }
        }

    }

    public void checkRotationCollision() {

        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        checkStaticBlockCollision();

        // Check frame collision
        // left wall
        for (Block block : tempB) {
            if (block.x < PlayManager.left_x) {
                leftCollision = true;
                break;
            }
        }

        // right wall
        for (Block block : tempB) {
            if (block.x + Block.SIZE > PlayManager.right_x) {
                rightCollision = true;
                break;
            }
        }

        // bottom floor
        for (Block block : tempB) {
            if (block.y + Block.SIZE > PlayManager.bottom_y) {
                bottomCollision = true;
                break;
            }
        }
    }

    private void checkStaticBlockCollision() {
        for (Block staticBlock : PlayManager.staticBlocks) {
            int targetX = staticBlock.x;
            int targetY = staticBlock.y;

            for (Block block : b) {
                // check down
                if (block.y + Block.SIZE == targetY && block.x == targetX) {
                    bottomCollision = true;
                    break;
                }
            }

            for (Block block : b) {
                // check left
                if (block.x - Block.SIZE == targetX && block.y == targetY) {
                    leftCollision = true;
                    break;
                }
            }

            for (Block block : b) {
                // check right
                if (block.x + Block.SIZE == targetX && block.y == targetY) {
                    rightCollision = true;
                    break;
                }
            }

        }
    }

    public void update() {

        if (deactivating) {
            deactivating();
        }

        minoControlInput();

        if (bottomCollision) {
            if (!deactivating) {
                GamePanel.se.play(TOUCH_FLOOR.getValue(), false);
            }
            deactivating = true;
        } else {
            autoDropCounter++; // the counter increases in every frame
            if (autoDropCounter == PlayManager.dropInterval) {
                // the mino goes down
                moveMinoDown();
                autoDropCounter = 0;
            }
        }
    }

    private void deactivating() {

        deactivateCounter++;

        // Wait 45 frames until deactivate
        if (deactivateCounter == 45) {

            deactivateCounter = 0;
            checkMovementCollision(); // check if the bottom is still hitting

            // if the bottom is still hitting after 45 frames, deactivate the mino
            if (bottomCollision) {
                active = false;
            }
        }

    }

    private void minoControlInput() {

        if (KeyHandler.upPressed) {
            switch (direction) {
                case ONE -> getDirectionTwo();
                case TWO -> getDirectionThree();
                case THREE -> getDirectionFour();
                case FOUR -> getDirectionOne();
            }
            KeyHandler.upPressed = false;
            GamePanel.se.play(ROTATION.getValue(), false);
        }

        checkMovementCollision();

        if (KeyHandler.downPressed) {

            // If the mino's bottom is not hitting, it can go down
            if (!bottomCollision) {
                moveMinoDown();
                // When moved down, reset the autoDropCounter
                autoDropCounter = 0;
            }

            KeyHandler.downPressed = false;
        }

        if (KeyHandler.leftPressed) {
            if (!leftCollision) {
                moveMinoLeft();
                GamePanel.se.play(SIDE_MOVEMENT.getValue(), false);
            }
            KeyHandler.leftPressed = false;
        }

        if (KeyHandler.rightPressed) {
            if (!rightCollision) {
                moveMinoRight();
                GamePanel.se.play(SIDE_MOVEMENT.getValue(), false);
            }
            KeyHandler.rightPressed = false;
        }

    }

    private void moveMinoDown() {
        Arrays.stream(b).forEach(Block::moveBlockDown);
    }

    private void moveMinoRight() {
        Arrays.stream(b).forEach(Block::moveBlockRight);
    }

    private void moveMinoLeft() {
        Arrays.stream(b).forEach(Block::moveBlockLeft);
    }

    public void draw(Graphics2D g2) {
        g2.setColor(b[0].c);
        Arrays.stream(b).forEach(
                block -> g2.fillRect(
                        block.x + BLOCK_MARGIN,
                        block.y + BLOCK_MARGIN,
                        Block.SIZE - (BLOCK_MARGIN * 2),
                        Block.SIZE - (BLOCK_MARGIN * 2)
                )
        );
    }

}
