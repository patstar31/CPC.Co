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
import java.io.File; // <-- Make sure to add this
import java.util.ArrayList;
import java.util.List;

// This is a new class, simplified from PlaylistPanel
public class LikedSongsPanel extends JPanel implements ThemeListener {
    private final JPanel cardPanel;
    private GradientPanel gradientPanel;
    private RoundedPanel phonePanel;
    private JLabel backButton;
    private JLabel titleLabel;
    private JPanel navBar;
    private JScrollPane scrollPane;
    private JPanel songsListPanel;
    private final List<SongPanel> songPanels = new ArrayList<>();

    // This is a special, permanent playlist
    private static final String LIKED_SONGS_FILE_NAME = "LikedSongs";

    private JLabel addSongButton;
    private boolean isAddingMode = false;

    // The constructor is simpler, it only needs the cardPanel
    public LikedSongsPanel(JPanel cardPanel) {
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
                if (isAddingMode) {
                    exitAddingMode();
                } else {
                    // Always goes back to Library
                    ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Library");
                }
            }
        });

        // Title is hard-coded
        titleLabel = new JLabel("Liked Songs"); 
        titleLabel.setFont(FONT_TITLE_LARGE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        addSongButton = new JLabel("+"); // Using "+"
        addSongButton.setFont(FONT_ICON_LARGE);
        addSongButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addSongButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                enterAddingMode();
            }
        });
        
        topBarPanel.add(backButton, BorderLayout.WEST);
        topBarPanel.add(titleLabel, BorderLayout.CENTER);
        topBarPanel.add(addSongButton, BorderLayout.EAST);
        gradientPanel.add(topBarPanel, BorderLayout.NORTH);

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
        addSongButton.setForeground(theme.getTextPrimary());

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

        JPanel homeItem = createNavItem(ICON_HOME, "Home", inactiveColor, false); 
        homeItem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Home"); }
        });
        // Library is the active tab
        JPanel libraryItem = createNavItem(ICON_LIBRARY, "Library", activeColor, true);
        libraryItem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Library"); }
        });
        JPanel accountItem = createNavItem(ICON_ACCOUNT, "Account", inactiveColor, false);
        accountItem.addMouseListener(new MouseAdapter() {
            @Override
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
    
    private void setupSongsList() {
        songsListPanel = new JPanel();
        songsListPanel.setLayout(new BoxLayout(songsListPanel, BoxLayout.Y_AXIS));
        songsListPanel.setOpaque(false);
        songsListPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        loadPlaylistSongs(); 
        
        scrollPane = new JScrollPane(songsListPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
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
    
    // Loads songs from the "LikedSongs.txt" file
    private void loadPlaylistSongs() {
        songsListPanel.removeAll();
        songPanels.clear();
        
        File playlistFile = new File(SongLoader.getPlaylistsDirectory(), LIKED_SONGS_FILE_NAME + ".txt");
        List<Songs> songs = SongLoader.loadSongsFromExternalPath(playlistFile.getAbsolutePath());
        
        if (songs.isEmpty()) {
            JLabel emptyLabel = new JLabel("No liked songs yet.");
            emptyLabel.setFont(FONT_SUBTITLE);
            emptyLabel.setForeground(ThemeManager.getInstance().getTextPrimary());
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            songsListPanel.add(emptyLabel);
        } else {
            for (int i = 0; i < songs.size(); i++) {
                Songs song = songs.get(i);
                SongPanel songPanel = new SongPanel(song, true); 
                songPanel.setTrackNumber(i + 1);
                songPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        onPlaylistSongClicked(song);
                    }
                });
                
                songPanels.add(songPanel);
                songsListPanel.add(songPanel);
                songsListPanel.add(Box.createRigidArea(new Dimension(0, 1))); 
            }
        }
        
        songsListPanel.revalidate();
        songsListPanel.repaint();
    }

    // Loads ALL songs from "songs.txt"
    private void loadAllSongsForAdding() {
        songsListPanel.removeAll();
        songPanels.clear();
        
        List<Songs> songs = SongLoader.loadSongs("/songs.txt");
        
        if (songs == null || songs.isEmpty()) {
            System.err.println("Failed to load songs from /songs.txt");
            return;
        }
        
        for (int i = 0; i < songs.size(); i++) {
            Songs song = songs.get(i);
            SongPanel songPanel = new SongPanel(song, true); 
            songPanel.setTrackNumber(i + 1);
            songPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    addSongToPlaylist(song);
                }
            });
            
            songPanels.add(songPanel);
            songsListPanel.add(songPanel);
            songsListPanel.add(Box.createRigidArea(new Dimension(0, 1))); 
        }
        
        songsListPanel.revalidate();
        songsListPanel.repaint();
    }

    // Called when clicking a song in the main list (not adding mode)
    private void onPlaylistSongClicked(Songs song) {
        // TODO: Implement your "play song" logic
        JOptionPane.showMessageDialog(this, "Playing: " + song.songName);
    }
    
    private void enterAddingMode() {
        isAddingMode = true;
        titleLabel.setText("Select Songs to Like");
        addSongButton.setVisible(false);
        loadAllSongsForAdding();
    }
    
    private void exitAddingMode() {
        isAddingMode = false;
        titleLabel.setText("Liked Songs");
        addSongButton.setVisible(true);
        loadPlaylistSongs();
    }
    
    // Adds a song to the "LikedSongs.txt" file
    private void addSongToPlaylist(Songs song) {
        System.out.println("Adding '" + song.songName + "' to Liked Songs");
        
        SongLoader.addSongToPlaylistFile(LIKED_SONGS_FILE_NAME, song);

        JOptionPane.showMessageDialog(this,
                "Added '" + song.songName + "' to Liked Songs",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }
}