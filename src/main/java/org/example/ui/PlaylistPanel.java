package org.example.ui;

import javax.swing.*;
import java.awt.*;

public class PlaylistPanel extends JPanel {
    private JPanel cardPanel;

    public PlaylistPanel(JPanel cardPanel) {
        this.cardPanel = cardPanel;

        setLayout(new BorderLayout());
        setBackground(new Color(255, 160, 122)); // Light Salmon

        JLabel label = new JLabel("📂 Playlist Page", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.WHITE);

        add(label, BorderLayout.CENTER);
        add(new NavBar(cardPanel, "Playlists"), BorderLayout.SOUTH);
    }
}
