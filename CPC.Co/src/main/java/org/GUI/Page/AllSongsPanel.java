package org.GUI.Page;

import org.GUI.Components.GradientPanel;
import org.GUI.Components.RoundedPanel;
import org.GUI.Components.ShadowPanel;
import org.GUI.Components.SongPanel;
import org.GUI.Functionalities.Songs;
import org.GUI.Theme.ThemeListener;
import org.GUI.Theme.ThemeManager;
import org.GUI.utils.SongLoader;
import static org.GUI.utils.UIConstants.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class AllSongsPanel extends JPanel implements ThemeListener {
    private final JPanel cardPanel;
    private GradientPanel gradientPanel;
    private RoundedPanel phonePanel;
    private JLabel backButton;
    private JLabel titleLabel;
    private JPanel navBar;
    private JScrollPane scrollPane;
    private JPanel songsListPanel;
    private final List<SongPanel> songPanels = new ArrayList<>();

    public AllSongsPanel(JPanel cardPanel) {
        this.cardPanel = cardPanel;
        ThemeManager theme = ThemeManager.getInstance();
        
        phonePanel = new RoundedPanel(PHONE_PANEL_CORNER_RADIUS, theme.getPhoneBackgroundColor());
        phonePanel.setLayout(new BorderLayout());
        phonePanel.setPreferredSize(PHONE_PANEL_DIMENSION);
        phonePanel.setMinimumSize(PHONE_PANEL_DIMENSION);
        phonePanel.setMaximumSize(PHONE_PANEL_DIMENSION);

        gradientPanel = new GradientPanel(theme.getGradientBg1(), theme.getGradientBg2());
        gradientPanel.setLayout(new BorderLayout());
        phonePanel.add(gradientPanel, BorderLayout.CENTER);

        JPanel topBarPanel = new JPanel(new BorderLayout());
        topBarPanel.setOpaque(false);
        topBarPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        backButton = new JLabel(ICON_BACK);
        backButton.setFont(FONT_ICON_LARGE);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Home");
            }
        });

        titleLabel = new JLabel("All Songs");
        titleLabel.setFont(FONT_TITLE_LARGE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        topBarPanel.add(backButton, BorderLayout.WEST);
        topBarPanel.add(titleLabel, BorderLayout.CENTER);
        gradientPanel.add(topBarPanel, BorderLayout.NORTH);

        // Create songs list
        setupSongsList();
        gradientPanel.add(scrollPane, BorderLayout.CENTER);

        navBar = createBottomNavBar();
        gradientPanel.add(navBar, BorderLayout.SOUTH);

        ShadowPanel shadowPanel = new ShadowPanel(phonePanel);
        setLayout(new GridBagLayout());
        setOpaque(false);
        add(shadowPanel);

        ThemeManager.getInstance().addListener(this);
        updateTheme();
    }
    
    private void setupSongsList() {
        // Create the songs list panel
        songsListPanel = new JPanel();
        songsListPanel.setLayout(new BoxLayout(songsListPanel, BoxLayout.Y_AXIS));
        songsListPanel.setOpaque(false);
        songsListPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Load and display songs
        loadAndDisplaySongs();
        
        // Create scroll pane
        scrollPane = new JScrollPane(songsListPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Style the scroll bar
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setPreferredSize(new Dimension(8, 0));
        verticalScrollBar.setOpaque(false);
        verticalScrollBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = ThemeManager.getInstance().getAccentColor();
                this.trackColor = ThemeManager.getInstance().getComponentBgColor();
            }
        });
    }
    
    private void loadAndDisplaySongs() {
        // Clear existing songs
        songsListPanel.removeAll();
        songPanels.clear();
        
        // Load songs from file
        List<Songs> songs = SongLoader.loadSongs("/songs.txt");
        
        // Create song panels
        for (int i = 0; i < songs.size(); i++) {
            Songs song = songs.get(i);
            SongPanel songPanel = new SongPanel(song, true); // true for list layout
            songPanel.setTrackNumber(i + 1);
            songPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onSongClicked(song);
                }
            });
            
            songPanels.add(songPanel);
            songsListPanel.add(songPanel);
            songsListPanel.add(Box.createRigidArea(new Dimension(0, 1))); // Tiny spacing between very small grey boxes
        }
        
        songsListPanel.revalidate();
        songsListPanel.repaint();
    }
    
    private void onSongClicked(Songs song) {
        // Handle song click - for now just show a message
        JOptionPane.showMessageDialog(this, 
            "Playing: " + song.songName + "\nby " + song.songArtist, 
            "Now Playing", 
            JOptionPane.INFORMATION_MESSAGE);
        
        // TODO: Integrate with actual audio playback system
        // TODO: Update UI to show currently playing song
    }

    @Override
    public void themeChanged() {
        updateTheme();
    }

    private void updateTheme() {
        ThemeManager theme = ThemeManager.getInstance();
        phonePanel.setBackground(theme.getPhoneBackgroundColor());
        gradientPanel.setColors(theme.getGradientBg1(), theme.getGradientBg2());
        backButton.setForeground(theme.getTextPrimary());
        titleLabel.setForeground(theme.getTextPrimary());

        // Update song panels theme
        for (SongPanel songPanel : songPanels) {
            songPanel.themeChanged();
        }

        gradientPanel.remove(navBar);
        navBar = createBottomNavBar();
        gradientPanel.add(navBar, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }
    
    private JPanel createBottomNavBar() {
        JPanel newNavBar = new JPanel(new GridLayout(1, 3));
        newNavBar.setOpaque(false);
        newNavBar.setPreferredSize(new Dimension(0, 70));
        newNavBar.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        ThemeManager theme = ThemeManager.getInstance();
        Color activeColor = theme.getAccentColor();
        Color inactiveColor = theme.getTextPrimary();

        JPanel homeItem = createNavItem(ICON_HOME, "Home", activeColor, true); // Active since we came from Home
        homeItem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Home"); }
        });
        JPanel libraryItem = createNavItem(ICON_LIBRARY, "Library", inactiveColor, false);
        libraryItem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Library"); }
        });
        JPanel accountItem = createNavItem(ICON_ACCOUNT, "Account", inactiveColor, false);
        accountItem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Account"); }
        });

        newNavBar.add(homeItem);
        newNavBar.add(libraryItem);
        newNavBar.add(accountItem);
        return newNavBar;
    }

    private JPanel createNavItem(String iconText, String labelText, Color color, boolean isActive) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
        itemPanel.setOpaque(false);
        JLabel iconLabel = new JLabel(iconText);
        iconLabel.setFont(FONT_ICON_NAV);
        iconLabel.setForeground(color);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel textLabel = new JLabel(labelText);
        textLabel.setFont(isActive ? FONT_NAV_BAR_BOLD : FONT_NAV_BAR);
        textLabel.setForeground(color);
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        itemPanel.add(iconLabel);
        itemPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        itemPanel.add(textLabel);
        return itemPanel;
    }
}