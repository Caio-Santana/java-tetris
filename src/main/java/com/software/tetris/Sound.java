package com.software.tetris;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.net.URL;

public class Sound {

    Clip musicClip;
    URL[] url = new URL[10];

    public enum SoundType {
        MUSIC(0),
        DELETE_LINE(1),
        GAME_OVER(2),
        ROTATION(3),
        TOUCH_FLOOR(4),
        SIDE_MOVEMENT(5);

        private final int value;

        SoundType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public Sound() {
        url[SoundType.MUSIC.value] = getClass().getResource("");
        url[SoundType.DELETE_LINE.value] = getClass().getResource("/delete line.wav");
        url[SoundType.GAME_OVER.value] = getClass().getResource("/gameover.wav");
        url[SoundType.ROTATION.value] = getClass().getResource("/rotation.wav");
        url[SoundType.SIDE_MOVEMENT.value] = getClass().getResource("/rotation.wav");
        url[SoundType.TOUCH_FLOOR.value] = getClass().getResource("/touch floor.wav");
    }

    public void play(int i, boolean music) {
        if (i == 0) return; // no music atm

        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(url[i]);
            Clip clip = AudioSystem.getClip();
            if (music) {
                musicClip = clip;
            }

            clip.open(ais);
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
            ais.close();
            clip.start();

        } catch (Exception e) {
            System.out.println("Sound problem. " + e.getMessage());
        }
    }

    public void loop() {
        if (musicClip == null) return;

        musicClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        if (musicClip == null) return;

        musicClip.stop();
        musicClip.close();
    }
}
