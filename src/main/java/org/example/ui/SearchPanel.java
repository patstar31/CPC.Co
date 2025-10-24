package org.example.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.InputStream;
import java.io.BufferedInputStream;

public class SearchPanel extends JPanel {
    private final JPanel cardPanel;
    private Clip clip;
    private JPanel nowPlayingPanel;
    private JLabel songLabel;
    private JButton playBtn;

    public SearchPanel(JPanel cardPanel) {
        this.cardPanel = cardPanel;
        setLayout(new BorderLayout());
        setBackground(new Color(70, 50, 120)); // purple hue

        // Search bar
        JTextField searchField = new JTextField("Search songs, artists...");
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        searchField.setBackground(new Color(220, 200, 250));

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        searchPanel.add(searchField, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.NORTH);

        // Search results list
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setOpaque(false);

        Object[][] songs = {
            {"Decode", "/songs/Decode.wav", "/decode_album_art.jpg"},
            {"Televised", "/songs/Televised.wav", "/televised_album_art.jpg"},
            {"Complicated", "/songs/Complicated.wav", "/complicated_album_art.jpg"},
            {"Still Into You", "/songs/StillIntoYou.wav", "/still_into_you_album_art.jpg"}
        };

        for (Object[] song : songs) {
            JPanel songRow = createSongRow((String) song[0], (String) song[1], (String) song[2]);
            resultsPanel.add(songRow);
            resultsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createSongRow(String name, String audioPath, String artPath) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(new Color(90, 70, 150));
        row.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        ImageIcon icon = new ImageIcon(getClass().getResource(artPath));
        Image scaled = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel artLabel = new JLabel(new ImageIcon(scaled));
        row.add(artLabel, BorderLayout.WEST);

        JLabel title = new JLabel(name);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        row.add(title, BorderLayout.CENTER);

        row.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showNowPlaying(name, audioPath, artPath);
            }
        });

        return row;
    }

    private void showNowPlaying(String songName, String audioPath, String artPath) {
        stopMusic();

        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        Container contentPane = topFrame.getContentPane();

        if (nowPlayingPanel != null) {
            contentPane.remove(nowPlayingPanel);
        }

        nowPlayingPanel = new JPanel(new BorderLayout());
        nowPlayingPanel.setBackground(new Color(40, 30, 70));
        nowPlayingPanel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        ImageIcon icon = new ImageIcon(getClass().getResource(artPath));
        Image scaled = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel miniArt = new JLabel(new ImageIcon(scaled));
        nowPlayingPanel.add(miniArt, BorderLayout.WEST);

        songLabel = new JLabel(songName);
        songLabel.setForeground(Color.WHITE);
        songLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nowPlayingPanel.add(songLabel, BorderLayout.CENTER);

        playBtn = new JButton("⏸");
        JButton nextBtn = new JButton("⏭");
        playBtn.addActionListener(e -> togglePlayPause());
        nextBtn.addActionListener(e -> restartSong(audioPath));

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controls.setOpaque(false);
        controls.add(playBtn);
        controls.add(nextBtn);
        nowPlayingPanel.add(controls, BorderLayout.EAST);

        // Smooth slide-in animation
        nowPlayingPanel.setPreferredSize(new Dimension(topFrame.getWidth(), 0));
        contentPane.add(nowPlayingPanel, BorderLayout.SOUTH);
        topFrame.revalidate();
        topFrame.repaint();

        Timer timer = new Timer(5, null);
        timer.addActionListener(new ActionListener() {
            int height = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                height += 3;
                nowPlayingPanel.setPreferredSize(new Dimension(topFrame.getWidth(), height));
                contentPane.revalidate();
                if (height >= 60) {
                    timer.stop();
                }
            }
        });
        timer.start();

        playMusic(audioPath);
    }

    private void playMusic(String resourcePath) {
        try {
            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.close();
            }

            InputStream is = getClass().getResourceAsStream(resourcePath);
            if (is == null) {
                System.out.println("Cannot find: " + resourcePath);
                return;
            }

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void togglePlayPause() {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
                playBtn.setText("▶️");
            } else {
                clip.start();
                playBtn.setText("⏸");
            }
        }
    }

    private void restartSong(String path) {
        stopMusic();
        playMusic(path);
    }

    private void stopMusic() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
        }
    }

    public void updateQuery(String text) {
        System.out.println("Searching for: " + text);
    }
}
