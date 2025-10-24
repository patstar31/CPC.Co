package org.example;

import org.example.ui.Home;
import org.example.ui.SearchPanel;
import org.example.ui.AccountPanel;
import org.example.ui.PlaylistPanel;
import org.example.ui.AllSongsPanel;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Prevent headless issues
        System.setProperty("java.awt.headless", "false");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Music Player");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                frame.setSize(420, 880);
                frame.setMinimumSize(new Dimension(420, 880));
                frame.setLocationRelativeTo(null);
                
                JPanel cardPanel = new JPanel(new CardLayout());
                
                Home homePanel = new Home(cardPanel);
                AllSongsPanel allSongsPanel = new AllSongsPanel(cardPanel);
                PlaylistPanel playlistPanel = new PlaylistPanel(cardPanel);
                SearchPanel searchPanel = new SearchPanel(cardPanel);
                AccountPanel accountPanel = new AccountPanel(cardPanel);
                
                cardPanel.add(homePanel, "Home");
                cardPanel.add(allSongsPanel, "AllSongs");
                cardPanel.add(playlistPanel, "Playlists");
                cardPanel.add(searchPanel, "Search");
                cardPanel.add(accountPanel, "Account");
                
                frame.setContentPane(cardPanel);
                frame.setVisible(true);
            }
        });
    }
}
