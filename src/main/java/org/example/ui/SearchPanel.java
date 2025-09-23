package org.example.ui;

import javax.swing.*;
import java.awt.*;

public class SearchPanel extends JPanel {
    private JPanel cardPanel;
    private JLabel resultLabel;

    public SearchPanel(JPanel cardPanel) {
        this.cardPanel = cardPanel;

        setLayout(new BorderLayout());
        setBackground(new Color(123, 104, 238)); // Medium Slate Blue

        resultLabel = new JLabel("🔍 Search Page", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 24));
        resultLabel.setForeground(Color.WHITE);

        add(resultLabel, BorderLayout.CENTER);
        add(new NavBar(cardPanel, "Search"), BorderLayout.SOUTH);
    }

    // Method to update the search query
    public void updateQuery(String query) {
        resultLabel.setText("🔍 Results for: " + query);
    }
}
