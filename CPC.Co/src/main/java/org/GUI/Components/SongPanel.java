package org.GUI.Components;

import org.GUI.Functionalities.Songs;
import org.GUI.Theme.ThemeListener;
import org.GUI.Theme.ThemeManager;
import static org.GUI.utils.UIConstants.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A reusable component for displaying individual songs in a Spotify-like format.
 * Supports both grid layout (Home panel) and list layout (AllSongs panel).
 */
public class SongPanel extends RoundedPanel implements ThemeListener {
    private Songs song;
    private JLabel albumCoverLabel;
    private JLabel songTitleLabel;
    private JLabel songArtistLabel;
    private JLabel trackNumberLabel;
    private boolean isListLayout;
    private boolean isPlaying;
    
    public SongPanel(Songs song, boolean isListLayout) {
        super(8, ThemeManager.getInstance().getComponentBgColor()); // Small rounded corners, grey background
        this.song = song;
        this.isListLayout = isListLayout;
        this.isPlaying = false;
        
        initializeComponents();
        setupLayout();
        ThemeManager.getInstance().addListener(this);
        updateTheme();
    }
    
    private void initializeComponents() {
        // Album cover - smaller for list layout
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource(song.songPhotoCover));
            int imageSize = isListLayout ? 30 : 55; // Tiny album cover for very small grey boxes
            Image scaledImage = originalIcon.getImage().getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
            albumCoverLabel = new JLabel(new ImageIcon(scaledImage));
            albumCoverLabel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 0));
        } catch (Exception e) {
            System.err.println("Could not load image: " + song.songPhotoCover);
            albumCoverLabel = new JLabel("🎵");
            albumCoverLabel.setFont(new Font(FONT_SECONDARY, Font.BOLD, 12));
            albumCoverLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 0));
        }
        
        // Song title - smaller font for small panels
        songTitleLabel = new JLabel(song.songName);
        songTitleLabel.setFont(new Font(FONT_PRIMARY, Font.PLAIN, isListLayout ? 10 : 12));
        
        // Artist name - much smaller font below title
        songArtistLabel = new JLabel(song.songArtist);
        songArtistLabel.setFont(new Font(FONT_PRIMARY, Font.PLAIN, 9));
        
        // Track number (for list layout) - smaller
        if (isListLayout) {
            trackNumberLabel = new JLabel("1"); // Will be set by parent
            trackNumberLabel.setFont(new Font(FONT_PRIMARY, Font.PLAIN, 10));
            trackNumberLabel.setPreferredSize(new Dimension(30, 15));
        }
    }
    
    private void setupLayout() {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        Dimension panelSize = isListLayout ? 
            SONG_PANEL_LIST_DIMENSION : 
            SONG_PANEL_GRID_DIMENSION;
        
        if (isListLayout) {
            // For list layout, allow flexible width but fixed height
            setPreferredSize(new Dimension(panelSize.width, panelSize.height));
            setMinimumSize(new Dimension(0, panelSize.height));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, panelSize.height));
            setupListLayout();
        } else {
            // For grid layout, use fixed size
            setFixedSize(panelSize);
            setupGridLayout();
        }
        
        // Add click listener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onSongClicked();
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(ThemeManager.getInstance().getComponentBgColor().brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(ThemeManager.getInstance().getComponentBgColor());
            }
        });
    }
    
    private void setupListLayout() {
        setLayout(new BorderLayout(8, 0));
        setAlignmentX(Component.LEFT_ALIGNMENT); // Prevent stretching in BoxLayout
        
        // Left side: Track number + Album cover
        JPanel leftPanel = new JPanel(new BorderLayout(3, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(trackNumberLabel, BorderLayout.WEST);
        leftPanel.add(albumCoverLabel, BorderLayout.CENTER);
        
        // Center: Song info - VERTICAL layout (song name on top, artist below)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 1));
        
        // Song title on top
        songTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        // Artist name below
        songArtistLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        centerPanel.add(songTitleLabel);
        centerPanel.add(songArtistLabel);
        
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private void setupGridLayout() {
        setLayout(new BorderLayout(5, 0));
        
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
        
        songTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        songArtistLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        textPanel.add(songTitleLabel);
        textPanel.add(songArtistLabel);
        
        add(albumCoverLabel, BorderLayout.WEST);
        add(textPanel, BorderLayout.CENTER);
    }
    
    private void onSongClicked() {
        // This will be handled by the parent panel
        System.out.println("Playing: " + song.songName + " by " + song.songArtist);
        // TODO: Integrate with actual audio playback
    }
    
    public void setTrackNumber(int trackNumber) {
        if (trackNumberLabel != null) {
            trackNumberLabel.setText(String.valueOf(trackNumber));
        }
    }
    
    public void setPlaying(boolean playing) {
        this.isPlaying = playing;
        updateTheme();
    }
    
    public Songs getSong() {
        return song;
    }
    
    @Override
    public void themeChanged() {
        updateTheme();
    }
    
    private void updateTheme() {
        ThemeManager theme = ThemeManager.getInstance();
        setBackground(isPlaying ? theme.getAccentColor() : theme.getComponentBgColor());
        songTitleLabel.setForeground(theme.getTextPrimary());
        songArtistLabel.setForeground(theme.getTextSecondary());
        if (trackNumberLabel != null) {
            trackNumberLabel.setForeground(isPlaying ? theme.getTextPrimary() : theme.getTextSecondary());
        }
    }
}
