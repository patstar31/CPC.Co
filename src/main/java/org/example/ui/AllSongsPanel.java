package org.example.ui;

import javax.swing.*;
import java.awt.*;

public class AllSongsPanel extends JPanel {
    private JPanel cardPanel;

    public AllSongsPanel(JPanel cardPanel) {
        this.cardPanel = cardPanel;

        setLayout(new BorderLayout());
        setBackground(new Color(70, 130, 180)); // Steel Blue

        JLabel label = new JLabel("🎵 All Songs Page", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.WHITE);

        add(label, BorderLayout.CENTER);
        add(new NavBar(cardPanel, "AllSongs"), BorderLayout.SOUTH);
    }
}
