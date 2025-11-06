package org.GUI.Page;

import org.GUI.Components.*;
import org.GUI.Functionalities.Song; // CHANGED: Use unified Song class
import org.GUI.Theme.*;
import org.GUI.utils.SongLoader;
import org.SongPlaying.MusicPlayerService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import static org.GUI.utils.UIConstants.*;

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
    
    // NEW: Mini-player at bottom
    private CurrentlyPlayingPanel currentlyPlayingPanel;
    private JPanel southContainer;

    public AllSongsPanel(JPanel cardPanel) {
        this.cardPanel = cardPanel;
        ThemeManager theme = ThemeManager.getInstance();

        // Create phone panel (main container)
        phonePanel = new RoundedPanel(PHONE_PANEL_CORNER_RADIUS, theme.getPhoneBackgroundColor());
        phonePanel.setLayout(new BorderLayout());
        phonePanel.setPreferredSize(PHONE_PANEL_DIMENSION);
        phonePanel.setMinimumSize(PHONE_PANEL_DIMENSION);
        phonePanel.setMaximumSize(PHONE_PANEL_DIMENSION);

        // Create gradient background
        gradientPanel = new GradientPanel(theme.getGradientBg1(), theme.getGradientBg2());
        gradientPanel.setLayout(new BorderLayout());
        phonePanel.add(gradientPanel, BorderLayout.CENTER);

        // Create top bar with back button
        createTopBar();

        // Create scrollable songs list
        setupSongsList();
        gradientPanel.add(scrollPane, BorderLayout.CENTER);

        // Create bottom section: mini-player + nav bar
        currentlyPlayingPanel = new CurrentlyPlayingPanel();
        navBar = createBottomNavBar();

        southContainer = new JPanel(new BorderLayout());
        southContainer.setOpaque(false);
        southContainer.add(currentlyPlayingPanel, BorderLayout.NORTH); // Mini-player on top
        southContainer.add(navBar, BorderLayout.SOUTH);                // Nav bar below
        gradientPanel.add(southContainer, BorderLayout.SOUTH);

        // Wrap in shadow and add to main panel
        ShadowPanel shadowPanel = new ShadowPanel(phonePanel);
        setLayout(new GridBagLayout());
        setOpaque(false);
        add(shadowPanel);

        ThemeManager.getInstance().addListener(this);
        updateTheme();
    }

    private void createTopBar() {
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
    }

    private void setupSongsList() {
        songsListPanel = new JPanel();
        songsListPanel.setLayout(new BoxLayout(songsListPanel, BoxLayout.Y_AXIS));
        songsListPanel.setOpaque(false);
        songsListPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        scrollPane = new JScrollPane(songsListPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        verticalBar.setUnitIncrement(16);

        loadAndDisplaySongs();
    }

    private void loadAndDisplaySongs() {
        // Load songs using SongLoader
        List<Song> songs = SongLoader.loadSongs(); // FIXED: Use default path from SongLoader

        for (Song song : songs) {
            SongPanel songPanel = new SongPanel(song, false); // FIXED: false = list layout (not grid)
            songPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onSongClicked(song);
                }
            });

            songPanels.add(songPanel);
            songsListPanel.add(songPanel);
            songsListPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        songsListPanel.revalidate();
        songsListPanel.repaint();
    }

    /**
     * Called when user clicks a song
     * This triggers the entire playback flow:
     * 1. Tells MusicPlayerService to play the song
     * 2. Service finds audio file and starts SongPlayer
     * 3. Service notifies CurrentlyPlayingPanel (observer)
     * 4. Mini-player appears with song info
     */
    private void onSongClicked(Song song) {
        System.out.println("\n🖱️  User clicked: " + song.title);
        
        // Tell the service to play this song
        MusicPlayerService.getInstance().playSong(song);
        
        // Update visual state of all song panels
        for (SongPanel sp : songPanels) {
            sp.setPlaying(sp.getSong().equals(song));
        }
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

        for (SongPanel songPanel : songPanels) {
            songPanel.themeChanged();
        }

        // Rebuild south container for theme changes
        if (southContainer != null) {
            gradientPanel.remove(southContainer);
        }
        navBar = createBottomNavBar();
        southContainer = new JPanel(new BorderLayout());
        southContainer.setOpaque(false);
        southContainer.add(currentlyPlayingPanel, BorderLayout.NORTH);
        southContainer.add(navBar, BorderLayout.SOUTH);
        gradientPanel.add(southContainer, BorderLayout.SOUTH);

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

        JPanel homeItem = createNavItem(ICON_HOME, "Home", activeColor, true);
        homeItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Home");
            }
        });

        JPanel libraryItem = createNavItem(ICON_LIBRARY, "Library", inactiveColor, false);
        libraryItem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { 
                ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Library"); 
            }
        });
        
        JPanel accountItem = createNavItem(ICON_ACCOUNT, "Account", inactiveColor, false);
        accountItem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { 
                ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Account"); 
            }
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
