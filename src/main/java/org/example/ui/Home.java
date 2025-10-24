package org.example.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.sound.sampled.*;

public class Home extends JPanel {
    private final JPanel cardPanel;
    private Clip audioClip;
    private String currentSongTitle = "";
    private final JPanel nowPlayingPanel;
    private final JLabel nowPlayingLabel;
    private final JButton playPauseBtn;
    private final JButton stopBtn;
    private final JButton nextBtn;

    private final String[][] recentSongs = {
            {"Decode", "Paramore", "decode_album_art.jpg", "Decode.wav"},
            {"Televised", "HUNNY", "televised_album_art.jpg", "Televised.wav"},
            {"Complicated", "Avril Lavigne", "complicated_album_art.jpg", "Complicated.wav"},
            {"Still Into You", "Paramore", "still_into_you_album_art.jpg", "StillIntoYou.wav"}
    };

    public Home(JPanel cardPanel) {
        this.cardPanel = cardPanel;
        setLayout(new BorderLayout());

        // === Gradient background main panel ===
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(40, 40, 70),
                        0, getHeight(), new Color(90, 60, 120)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // === Search bar ===
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel searchIcon = new JLabel("\uD83D\uDD0D");
        searchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        searchIcon.setForeground(Color.WHITE);
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));

        JTextField searchBar = new JTextField(" Search songs, artists...");
        searchBar.setFont(new Font("Arial", Font.PLAIN, 14));
        searchBar.setPreferredSize(new Dimension(300, 40));
        searchBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchBar.setBackground(new Color(255, 255, 255, 180));

        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchBar, BorderLayout.CENTER);

        searchBar.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "Search");

            for (Component c : cardPanel.getComponents()) {
                if (c instanceof SearchPanel) {
                    ((SearchPanel) c).updateQuery(searchBar.getText());
                }
            }
        });

        // === Buttons ===
        JButton allSongsBtn = createStyledButton("All Songs", new Color(150, 100, 150));
        JButton playlistsBtn = createStyledButton("Playlists", new Color(255, 120, 120));

        allSongsBtn.addActionListener(e -> switchPanel("AllSongs"));
        playlistsBtn.addActionListener(e -> switchPanel("Playlists"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        buttonPanel.add(allSongsBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(playlistsBtn);

        // === Recently Played ===
        JLabel recentLabel = new JLabel("Recently Played");
        recentLabel.setForeground(Color.WHITE);
        recentLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel recentPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        recentPanel.setOpaque(false);
        recentPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 20, 0));

        for (String[] song : recentSongs) {
            JPanel card = createSongCard(song[0], song[1], song[2], song[3]);
            recentPanel.add(card);
        }

        JPanel recentWrapper = new JPanel();
        recentWrapper.setLayout(new BoxLayout(recentWrapper, BoxLayout.Y_AXIS));
        recentWrapper.setOpaque(false);
        recentWrapper.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        recentWrapper.add(recentLabel);
        recentWrapper.add(Box.createRigidArea(new Dimension(0, 5)));
        recentWrapper.add(recentPanel);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(buttonPanel);
        centerPanel.add(recentWrapper);

        // === Now Playing Bar (Bottom, above NavBar) ===
        nowPlayingPanel = new JPanel(new BorderLayout());
        nowPlayingPanel.setBackground(new Color(60, 60, 100));
        nowPlayingPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        nowPlayingLabel = new JLabel(" ");
        nowPlayingLabel.setForeground(Color.WHITE);
        nowPlayingLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel controlPanel = new JPanel();
        controlPanel.setOpaque(false);

        playPauseBtn = new JButton("⏯");
        stopBtn = new JButton("⏹");
        nextBtn = new JButton("⏭");

        for (JButton btn : new JButton[]{playPauseBtn, stopBtn, nextBtn}) {
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            btn.setBackground(new Color(80, 80, 130));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 18));
        }

        playPauseBtn.addActionListener(e -> togglePlayPause());
        stopBtn.addActionListener(e -> stopAudio());
        nextBtn.addActionListener(e -> playNextSong());

        controlPanel.add(playPauseBtn);
        controlPanel.add(stopBtn);
        controlPanel.add(nextBtn);

        nowPlayingPanel.add(nowPlayingLabel, BorderLayout.WEST);
        nowPlayingPanel.add(controlPanel, BorderLayout.EAST);
        nowPlayingPanel.setVisible(false);

        // === Layout Assembly ===
        JPanel bottomWrapper = new JPanel(new BorderLayout());
        bottomWrapper.setOpaque(false);
        bottomWrapper.add(nowPlayingPanel, BorderLayout.NORTH);
        bottomWrapper.add(new NavBar(cardPanel, "Home"), BorderLayout.SOUTH);

        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomWrapper, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(300, 60));
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return button;
    }

    // === Clickable Song Card ===
    private JPanel createSongCard(String title, String artist, String imagePath, String audioFile) {
        JPanel panel = new JPanel(new BorderLayout(6, 6)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 10);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        panel.setPreferredSize(new Dimension(130, 50));

        ImageIcon icon;
        try {
            icon = new ImageIcon(getClass().getResource("/" + imagePath));
            if (icon.getIconWidth() > 0) {
                Image img = icon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
                icon = new ImageIcon(img);
            }
        } catch (Exception e) {
            icon = new ImageIcon();
        }

        JLabel picLabel = new JLabel(icon);
        JLabel songLabel = new JLabel("<html><b>" + title + "</b><br/>" + artist + "</html>");
        songLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        songLabel.setForeground(Color.WHITE);

        panel.add(picLabel, BorderLayout.WEST);
        panel.add(songLabel, BorderLayout.CENTER);

        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playAudio(audioFile, title);
            }
        });

        return panel;
    }

    private void playAudio(String filename, String title) {
        stopAudio();
        try {
            File audioFile = new File("src/main/resources/songs/" + filename);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            audioClip = AudioSystem.getClip();
            audioClip.open(audioStream);
            audioClip.start();
            nowPlayingPanel.setVisible(true);
            nowPlayingLabel.setText("🎵 Now Playing: " + title);
            currentSongTitle = title;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error playing: " + e.getMessage());
        }
    }

    private void togglePlayPause() {
        if (audioClip == null) return;
        if (audioClip.isRunning()) audioClip.stop();
        else audioClip.start();
    }

    private void stopAudio() {
        if (audioClip != null) {
            audioClip.stop();
            audioClip.close();
        }
        nowPlayingLabel.setText(" ");
    }

    private void playNextSong() {
        for (int i = 0; i < recentSongs.length; i++) {
            if (recentSongs[i][0].equals(currentSongTitle)) {
                int nextIndex = (i + 1) % recentSongs.length;
                playAudio(recentSongs[nextIndex][3], recentSongs[nextIndex][0]);
                break;
            }
        }
    }

    private void switchPanel(String panelName) {
        CardLayout cl = (CardLayout) cardPanel.getLayout();
        cl.show(cardPanel, panelName);
    }
}
