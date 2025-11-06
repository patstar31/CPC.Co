package org.GUI.Components;

import org.GUI.Functionalities.Song; // CHANGED: Use Song, not Songs
import org.GUI.Theme.ThemeListener;
import org.GUI.Theme.ThemeManager;
import org.SongPlaying.MusicPlayerService;

import javax.swing.*;
import java.awt.*;

import static org.GUI.utils.UIConstants.*;

/**
 * Mini-player panel that appears at bottom when song is playing
 * Implements OBSERVER PATTERN to listen to MusicPlayerService
 */
public class CurrentlyPlayingPanel extends RoundedPanel
        implements ThemeListener, MusicPlayerService.PlaybackListener {

    private JLabel albumCoverLabel;
    private JLabel songTitleLabel;
    private JLabel songArtistLabel;
    private JButton playPauseButton;
    private JButton nextButton;
    private JButton prevButton;
    private ShadowPanel shadowWrapper;

    public CurrentlyPlayingPanel() {
        super(0, new Color(0, 0, 0, 0));
        setLayout(new BorderLayout());
        setOpaque(false);

        RoundedPanel contentPanel = new RoundedPanel(8, ThemeManager.getInstance().getComponentBgColor());
        contentPanel.setLayout(new BorderLayout(5, 0));
        contentPanel.setPreferredSize(new Dimension(PHONE_PANEL_DIMENSION.width - 20, 60));

        initializeComponents(contentPanel);

        shadowWrapper = new ShadowPanel(contentPanel);
        add(shadowWrapper, BorderLayout.CENTER);

        // Register as observer
        ThemeManager.getInstance().addListener(this);
        MusicPlayerService.getInstance().setListener(this);
        
        updateTheme();
        setVisible(false); // Hidden until song plays
    }

    private void initializeComponents(RoundedPanel contentPanel) {
        // Album cover
        albumCoverLabel = new JLabel("💿");
        albumCoverLabel.setFont(FONT_ICON_BIG);
        albumCoverLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        // Song info
        songTitleLabel = new JLabel("No song playing");
        songArtistLabel = new JLabel("Tap a song to start");
        songTitleLabel.setFont(FONT_NORMAL_BOLD);
        songArtistLabel.setFont(FONT_SMALL);

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 0));
        textPanel.add(songTitleLabel);
        textPanel.add(songArtistLabel);

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);
        infoPanel.add(albumCoverLabel, BorderLayout.WEST);
        infoPanel.add(textPanel, BorderLayout.CENTER);

        contentPanel.add(infoPanel, BorderLayout.WEST);

        // Playback controls
        prevButton = createControlButton("⏮", e -> MusicPlayerService.getInstance().previousSong());
        playPauseButton = createControlButton("▶", e -> MusicPlayerService.getInstance().togglePlayback());
        nextButton = createControlButton("⏭", e -> MusicPlayerService.getInstance().nextSong());

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        controlPanel.setOpaque(false);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        controlPanel.add(prevButton);
        controlPanel.add(playPauseButton);
        controlPanel.add(nextButton);

        contentPanel.add(controlPanel, BorderLayout.CENTER);

        JPanel eastPanel = new JPanel();
        eastPanel.setOpaque(false);
        eastPanel.setPreferredSize(new Dimension(80, 50));
        contentPanel.add(eastPanel, BorderLayout.EAST);
    }

    private JButton createControlButton(String icon, java.awt.event.ActionListener listener) {
        JButton button = new JButton(icon);
        button.setFont(FONT_ICON_BIG);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(listener);
        return button;
    }

    @Override
    public void themeChanged() {
        updateTheme();
    }

    private void updateTheme() {
        ThemeManager theme = ThemeManager.getInstance();

        RoundedPanel innerPanel = (RoundedPanel)((ShadowPanel)getComponent(0)).getComponent(0);
        innerPanel.setBackground(theme.getComponentBgColor());

        songTitleLabel.setForeground(theme.getTextPrimary());
        songArtistLabel.setForeground(theme.getTextSecondary());

        Color btnColor = theme.getAccentColor();
        Color fgColor = theme.getTextPrimary();

        for (JButton btn : new JButton[] {playPauseButton, prevButton, nextButton}) {
            btn.setBackground(btnColor);
            btn.setForeground(fgColor);
        }

        shadowWrapper.themeChanged();
    }

    /**
     * OBSERVER PATTERN: Called when MusicPlayerService changes song
     */
    @Override
    public void onSongChanged(Song song) {
        if (song != null) {
            songTitleLabel.setText(song.title);    // Use public field
            songArtistLabel.setText(song.artist);  // Use public field
            
            // TODO: Load actual album cover image
            // For now, keep emoji
            
            setVisible(true); // Show the panel
            System.out.println("🎵 UI updated: " + song.title);
        } else {
            songTitleLabel.setText("No song playing");
            songArtistLabel.setText("Tap a song to start");
            setVisible(false);
        }
        revalidate();
        repaint();
    }

    /**
     * OBSERVER PATTERN: Called when play/pause state changes
     */
    @Override
    public void onPlaybackStateChanged(boolean isPlaying) {
        if (isPlaying) {
            playPauseButton.setText("⏸");
            playPauseButton.setToolTipText("Pause");
        } else {
            playPauseButton.setText("▶");
            playPauseButton.setToolTipText("Play");
        }
    }
}