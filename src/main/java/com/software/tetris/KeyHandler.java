package com.software.tetris;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static com.software.tetris.Sound.SoundType.MUSIC;

public class KeyHandler implements KeyListener {

    public static boolean
            upPressed,
            downPressed,
            leftPressed,
            rightPressed,
            pausedPressed;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) upPressed = true;
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) leftPressed = true;
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) downPressed = true;
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) rightPressed = true;
        if (code == KeyEvent.VK_P || code == KeyEvent.VK_SPACE) pausedPressed();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void pausedPressed() {
        if (pausedPressed) {
            GamePanel.music.play(MUSIC.getValue(), true);
            GamePanel.music.loop();
        } else {
            GamePanel.music.stop();
        }

        pausedPressed = !pausedPressed;
    }
}
