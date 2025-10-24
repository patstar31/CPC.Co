package org.example.ui;

import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.File;

public class AllSongsPanel extends JPanel {
    private JPanel cardPanel;
    private Clip clip;

    public AllSongsPanel(JPanel cardPanel) {
        this.cardPanel = cardPanel;

        setLayout(new BorderLayout());
        setBackground(new Color(70, 130, 180)); // Steel Blue

        // Create panel to hold song buttons
        JPanel songsPanel = new JPanel();
        songsPanel.setLayout(new GridLayout(0, 1, 10, 10));
        songsPanel.setBackground(new Color(70, 130, 180));
        songsPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Create song buttons
        JButton decodeBtn = new JButton("Decode");
        JButton televisedBtn = new JButton("Televised");
        JButton complicatedBtn = new JButton("Complicated");
        JButton stillIntoYouBtn = new JButton("Still Into You");

        // Add listeners for each song (resource method)
        decodeBtn.addActionListener(e -> playMusicResource("/songs/Decode.wav"));
        televisedBtn.addActionListener(e -> playMusicResource("/songs/Televised.wav"));
        complicatedBtn.addActionListener(e -> playMusicResource("/songs/Complicated.wav"));
        stillIntoYouBtn.addActionListener(e -> playMusicResource("/songs/StillIntoYou.wav"));

        // Add buttons to panel
        songsPanel.add(decodeBtn);
        songsPanel.add(televisedBtn);
        songsPanel.add(complicatedBtn);
        songsPanel.add(stillIntoYouBtn);

        add(songsPanel, BorderLayout.CENTER);
        add(new NavBar(cardPanel, "AllSongs"), BorderLayout.SOUTH);
    }

    // Recommended: load from resources (put WAVs into src/main/resources/songs/)
    private void playMusicResource(String resourcePath) {
        try {
            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.close();
            }

            InputStream is = getClass().getResourceAsStream(resourcePath);
            if (is == null) {
                System.out.println("Resource not found: " + resourcePath);
                return;
            }

            try (BufferedInputStream bis = new BufferedInputStream(is)) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(bis);
                clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Simpler file-based method (use while testing)
    private void playMusicFile(String filePath) {
        try {
            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.close();
            }

            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("File not found: " + filePath);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Optional: stop currently playing music
    private void stopMusic() {
        if (clip != null) {
            if (clip.isRunning()) clip.stop();
            clip.close();
            clip = null;
        }
    }
}
