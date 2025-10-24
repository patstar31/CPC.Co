package org.example.ui;

import javax.sound.sampled.*;
import java.io.File;

public class MusicPlayer {
    private static Clip audioClip;
    private static String currentSong = "";

    public static void play(String filePath, String title) {
        stop(); // stop any currently playing song
        try {
            File audioFile = new File("src/main/resources/songs/" + filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            audioClip = AudioSystem.getClip();
            audioClip.open(audioStream);
            audioClip.start();
            currentSong = title;
            System.out.println("🎵 Playing: " + title);
        } catch (Exception e) {
            System.out.println("Error playing " + filePath + ": " + e.getMessage());
        }
    }

    public static void togglePlayPause() {
        if (audioClip == null) return;
        if (audioClip.isRunning()) audioClip.stop();
        else audioClip.start();
    }

    public static void stop() {
        if (audioClip != null) {
            audioClip.stop();
            audioClip.close();
            audioClip = null;
        }
    }

    public static boolean isPlaying() {
        return audioClip != null && audioClip.isRunning();
    }

    public static String getCurrentSong() {
        return currentSong;
    }
}
