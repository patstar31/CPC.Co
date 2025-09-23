package org.example.ui;

import javax.swing.*;
import java.awt.*;

public class NavBar extends JPanel {
    private final JPanel cardPanel;

    public NavBar(JPanel cardPanel, String activePage) {
        this.cardPanel = cardPanel;
        setLayout(new GridLayout(1, 3));
        setBackground(new Color(30, 30, 50));

        JButton homeBtn = createNavButton("🏠", activePage.equals("Home"));
        JButton libraryBtn = createNavButton("🎵", activePage.equals("AllSongs"));
        JButton accountBtn = createNavButton("👤", activePage.equals("Account"));

        homeBtn.addActionListener(e -> switchPanel("Home"));
        libraryBtn.addActionListener(e -> switchPanel("AllSongs"));
        accountBtn.addActionListener(e -> switchPanel("Account"));

        add(homeBtn);
        add(libraryBtn);
        add(accountBtn);
    }

    private JButton createNavButton(String text, boolean active) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(false);

        if (active) {
            button.setBackground(new Color(60, 60, 90));
            button.setOpaque(true);
            button.setForeground(Color.YELLOW);
        } else {
            button.setForeground(Color.WHITE);
        }

        return button;
    }

    private void switchPanel(String panelName) {
        CardLayout cl = (CardLayout) cardPanel.getLayout();
        cl.show(cardPanel, panelName);
    }
}
