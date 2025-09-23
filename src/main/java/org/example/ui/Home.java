package org.example.ui;

import javax.swing.*;
import java.awt.*;

public class Home extends JPanel {
    private final JPanel cardPanel;

    public Home(JPanel cardPanel) {
        this.cardPanel = cardPanel;

        setLayout(new BorderLayout());

        // Gradient Background
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

        // 🔍 Search bar with icon (white color)
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel searchIcon = new JLabel("\uD83D\uDD0D"); // magnifying glass
        searchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        searchIcon.setForeground(Color.WHITE); // make icon visible
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));

        JTextField searchBar = new JTextField(" Search songs, artists...");
        searchBar.setFont(new Font("Arial", Font.PLAIN, 14));
        searchBar.setPreferredSize(new Dimension(300, 40));
        searchBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchBar.setBackground(new Color(255, 255, 255, 180));

        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchBar, BorderLayout.CENTER);

        // Switch to Search Panel when user presses Enter
        searchBar.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "Search");

            for (Component c : cardPanel.getComponents()) {
                if (c instanceof SearchPanel) {
                    ((SearchPanel) c).updateQuery(searchBar.getText());
                }
            }
        });

        // Buttons
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

        // Recently Played label
        JLabel recentLabel = new JLabel("Recently Played");
        recentLabel.setForeground(Color.WHITE);
        recentLabel.setFont(new Font("Arial", Font.BOLD, 18));
        recentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Put label inside a left-aligned panel
    JPanel labelWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    labelWrapper.setOpaque(false);
    labelWrapper.add(recentLabel);
        

        // Recently Played grid
        JPanel recentPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        recentPanel.setOpaque(false);
        recentPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 20, 0));

        recentPanel.add(createSongCard("Decode", "Paramore", "decode_album_art.jpg"));
        recentPanel.add(createSongCard("Televised", "HUNNY", "televised_album_art.jpg"));
        recentPanel.add(createSongCard("Complicated", "Avril Lavigne", "complicated_album_art.jpg"));
        recentPanel.add(createSongCard("Still Into You", "Paramore", "still_into_you_album_art.jpg"));

        JPanel recentWrapper = new JPanel();
        recentWrapper.setLayout(new BoxLayout(recentWrapper, BoxLayout.Y_AXIS));
        recentWrapper.setOpaque(false);
        recentWrapper.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        recentWrapper.add(recentLabel);
        recentWrapper.add(Box.createRigidArea(new Dimension(0, 5))); // tight gap
        recentWrapper.add(recentPanel);

        // Assemble center
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(buttonPanel);
        centerPanel.add(recentWrapper);

        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(new NavBar(cardPanel, "Home"), BorderLayout.SOUTH);

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

    // Rectangle card with rounded edges
    private JPanel createSongCard(String title, String artist, String imagePath) {
        JPanel panel = new JPanel(new BorderLayout(6, 6)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 10); // rectangle w/ rounded edge
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        panel.setPreferredSize(new Dimension(130, 50)); // rectangle instead of square

        // Load image
        ImageIcon icon;
        try {
            icon = new ImageIcon(getClass().getResource("/" + imagePath));
        } catch (Exception e) {
            System.out.println("Could not load: " + imagePath);
            icon = new ImageIcon();
        }

        // Resize
        if (icon.getIconWidth() > 0) {
            Image img = icon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
        }

        JLabel picLabel = new JLabel(icon);
        picLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel songLabel = new JLabel("<html><b>" + title + "</b><br/>" + artist + "</html>");
        songLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        songLabel.setForeground(Color.WHITE);

        panel.add(picLabel, BorderLayout.WEST);
        panel.add(songLabel, BorderLayout.CENTER);

        return panel;
    }

    private void switchPanel(String panelName) {
        CardLayout cl = (CardLayout) cardPanel.getLayout();
        cl.show(cardPanel, panelName);
    }
}
