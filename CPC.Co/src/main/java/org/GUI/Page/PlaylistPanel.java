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
import java.io.File;

public class PlaylistPanel extends JPanel implements ThemeListener {
    private final JPanel cardPanel;
    private final String playlistName;
    private GradientPanel gradientPanel;
    private RoundedPanel phonePanel;
    private JLabel backButton;
    private JLabel titleLabel;
    private JPanel navBar;
    private JScrollPane scrollPane;
    private JPanel songsListPanel;
    private final List<SongPanel> songPanels = new ArrayList<>();

    // --- NEW ---
    private JLabel addSongButton;
    private boolean isAddingMode = false;
    // --- END NEW ---

    public PlaylistPanel(JPanel cardPanel, String playlistName) {
        this.cardPanel = cardPanel;
        this.playlistName = playlistName;
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

        backButton = new JLabel(ICON_BACK); // Assuming ICON_BACK is your back arrow
        backButton.setFont(FONT_ICON_LARGE);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // --- MODIFIED: Back button now handles both states ---
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isAddingMode) {
                    exitAddingMode();
                } else {
                    ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Library");
                }
            }
        });

        // --- MODIFIED: Title is simpler ---
        titleLabel = new JLabel(playlistName); 
        titleLabel.setFont(FONT_TITLE_LARGE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // --- NEW: Add Song Button ---
        // Assuming ICON_ADD is a constant like "+". If not, use new JLabel("Add")
        // Inside PlaylistPanel.java
        addSongButton = new JLabel("+");
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
        topBarPanel.add(addSongButton, BorderLayout.EAST); // Add button to the right
        gradientPanel.add(topBarPanel, BorderLayout.NORTH);
        // --- END NEW/MODIFIED ---

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
        addSongButton.setForeground(theme.getTextPrimary()); // Style the add button

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
        // ... (This method is unchanged, copy it from your existing code)
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
        // ... (This method is unchanged, copy it from your existing code)
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
        
        // --- MODIFIED: Load playlist songs by default ---
        loadPlaylistSongs(); 
        
        scrollPane = new JScrollPane(songsListPanel);
        // ... (rest of scrollPane setup is unchanged)
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
    
    // --- NEW: Renamed from loadAndDisplaySongs ---
    private void loadPlaylistSongs() {
        songsListPanel.removeAll();
        songPanels.clear();

        // --- THIS IS THE CORRECTED CODE ---
        // It builds the full, external path to the file
        File playlistFile = new File(SongLoader.getPlaylistsDirectory(), playlistName + ".txt");

        // It calls the new method that reads external files
        List<Songs> songs = SongLoader.loadSongsFromExternalPath(playlistFile.getAbsolutePath());
        // --- END OF CORRECTION ---

        if (songs.isEmpty()) { 
            // This will now work correctly for your new "ad" playlist
            JLabel emptyLabel = new JLabel("This playlist is empty.");
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

    // --- NEW: This loads ALL songs from the main file ---
    private void loadAllSongsForAdding() {
        songsListPanel.removeAll();
        songPanels.clear();
        
        // Load ALL songs
        List<Songs> songs = SongLoader.loadSongs("/songs.txt");
        
        if (songs == null) {
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
                    // In adding mode, clicking a song *adds* it
                    addSongToPlaylist(song, playlistName);
                }
            });
            
            songPanels.add(songPanel);
            songsListPanel.add(songPanel);
            songsListPanel.add(Box.createRigidArea(new Dimension(0, 1))); 
        }
        
        songsListPanel.revalidate();
        songsListPanel.repaint();
    }

    // --- NEW: Logic for clicking a song in the playlist ---
    private void onPlaylistSongClicked(Songs song) {
        // TODO: Implement your "play song" logic
        JOptionPane.showMessageDialog(this, "Playing: " + song.songName);
    }
    
    // --- NEW: Methods to switch states ---
    private void enterAddingMode() {
        isAddingMode = true;
        titleLabel.setText("Select Songs to Add");
        addSongButton.setVisible(false); // Hide "Add" button
        loadAllSongsForAdding(); // Load all songs
    }
    
    private void exitAddingMode() {
        isAddingMode = false;
        titleLabel.setText(playlistName); // Set title back
        addSongButton.setVisible(true); // Show "Add" button
        loadPlaylistSongs(); // Reload playlist songs
    }
    // --- END NEW ---
    
    // This method is the same as before, but it's now
    // called when in "Adding Mode"
    private void addSongToPlaylist(Songs song, String playlistName) {
        System.out.println("Adding '" + song.songName + "' to playlist '" + playlistName + "'");
        
        // TODO: Implement your file-saving logic here
        SongLoader.addSongToPlaylistFile(playlistName, song);

        JOptionPane.showMessageDialog(this,
                "Added '" + song.songName + "'",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }
}