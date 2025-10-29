package org.GUI.Components;

import org.GUI.Functionalities.Songs;
import org.GUI.Theme.ThemeListener;
import org.GUI.Theme.ThemeManager;
import org.SongPlaying.MusicPlayerService;
import static org.GUI.utils.UIConstants.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The smaller panel at the bottom of the screen to display the current song
 * and playback controls (Prev, Play/Pause, Next).
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
        super(0, new Color(0, 0, 0, 0)); // Transparent background for the container
        setLayout(new BorderLayout());
        setOpaque(false);

        // The inner panel which gets the rounding and theme color
        RoundedPanel contentPanel = new RoundedPanel(8, ThemeManager.getInstance().getComponentBgColor());
        contentPanel.setLayout(new BorderLayout(5, 0));
        // Use a fixed height for the player bar
        contentPanel.setPreferredSize(new Dimension(PHONE_PANEL_DIMENSION.width - 20, 60));

        initializeComponents(contentPanel);

        // The ShadowPanel wraps the content panel for the floating effect
        shadowWrapper = new ShadowPanel(contentPanel);
        add(shadowWrapper, BorderLayout.CENTER);

        ThemeManager.getInstance().addListener(this);
        MusicPlayerService.getInstance().setListener(this);
        updateTheme();

        // The panel starts invisible until a song is played
        setVisible(false);
    }

    private void initializeComponents(RoundedPanel contentPanel) {
        // 1. Album Cover & Info Panel (LEFT)
        albumCoverLabel = new JLabel("💿");
        albumCoverLabel.setFont(FONT_ICON_BIG); // Assuming FONT_ICON_BIG exists in UIConstants
        albumCoverLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        songTitleLabel = new JLabel("No song playing");
        songArtistLabel = new JLabel("Tap a song to start");
        songTitleLabel.setFont(FONT_NORMAL_BOLD); // Assuming FONT_NORMAL_BOLD exists
        songArtistLabel.setFont(FONT_SMALL); // Assuming FONT_SMALL exists

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


        // 2. Control Buttons Panel (CENTER)
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


        // 3. Placeholder for other buttons/alignment (RIGHT)
        JPanel eastPanel = new JPanel();
        eastPanel.setOpaque(false);
        eastPanel.setPreferredSize(new Dimension(80, 50));
        contentPanel.add(eastPanel, BorderLayout.EAST);
    }

    /**
     * Helper method to create consistent control buttons.
     * This method was likely the source of your "cannot resolve method" error.
     */
    private JButton createControlButton(String icon, ActionListener listener) {
        JButton button = new JButton(icon);
        button.setFont(FONT_ICON_BIG); // Using a large font for the icons
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.addActionListener(listener);
        return button;
    }

    // --- ThemeListener Implementation ---
    @Override
    public void themeChanged() {
        updateTheme();
    }

    private void updateTheme() {
        ThemeManager theme = ThemeManager.getInstance();

        // Update the inner rounded panel background
        RoundedPanel innerPanel = (RoundedPanel)((ShadowPanel)getComponent(0)).getComponent(0);
        innerPanel.setBackground(theme.getComponentBgColor());

        songTitleLabel.setForeground(theme.getTextPrimary());
        songArtistLabel.setForeground(theme.getTextSecondary());

        Color btnColor = theme.getAccentColor();
        Color fgColor = theme.getTextPrimary();

        // Update all control buttons
        for (JButton btn : new JButton[] {playPauseButton, prevButton, nextButton}) {
            btn.setBackground(btnColor);
            btn.setForeground(fgColor);
        }

        shadowWrapper.themeChanged();
    }

    // --- MusicPlayerService.PlaybackListener Implementation (Key UI Update Logic) ---

    @Override
    public void onSongChanged(Songs song) {
        if (song != null) {
            songTitleLabel.setText(song.songName);
            songArtistLabel.setText(song.songArtist);
            // TODO: Logic to load and display the actual album cover image here
            setVisible(true); // Make the player panel visible
        } else {
            songTitleLabel.setText("No song playing");
            songArtistLabel.setText("Tap a song to start");
            setVisible(false);
        }
        revalidate();
        repaint();
    }

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