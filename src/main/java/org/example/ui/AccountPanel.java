package org.example.ui;

import javax.swing.*;
import java.awt.*;

public class AccountPanel extends JPanel {
    private JPanel cardPanel;

    public AccountPanel(JPanel cardPanel) {
        this.cardPanel = cardPanel;

        setLayout(new BorderLayout());
        setBackground(new Color(46, 139, 87)); // Sea Green

        JLabel label = new JLabel("👤 Account Page", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.WHITE);

        add(label, BorderLayout.CENTER);
        add(new NavBar(cardPanel, "Account"), BorderLayout.SOUTH);
    }
}
