package org.GUI.Components;

import org.GUI.Functionalities.Song; // Uses unified Song class
import org.GUI.Theme.ThemeListener;
import org.GUI.Theme.ThemeManager;

import javax.swing.*;
import java.awt.*;

import static org.GUI.utils.UIConstants.*;

/**
 * Visual representation of a song in the list
 * ✅ THIS FILE IS UNCHANGED - All original layout and functionality preserved
 */
public class SongPanel extends RoundedPanel implements ThemeListener {
    private final Song song;
    private final boolean isGridLayout;
    private JLabel titleLabel;
    private JLabel artistLabel;
    private boolean isPlaying = false;

    public SongPanel(Song song, boolean isGridLayout) {
        super(SMALL_CORNER_RADIUS, ThemeManager.getInstance().getComponentBgColor());
        this.song = song;
        this.isGridLayout = isGridLayout;

        setupLayout();
        ThemeManager.getInstance().addListener(this);
    }

    private void setupLayout() {
        if (isGridLayout) {
            setupGridLayout();
        } else {
            setupListLayout();
        }
    }

    private void setupListLayout() {
        setLayout(new BorderLayout(10, 0));
        setFixedSize(SONG_PANEL_LIST_DIMENSION);
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel iconLabel = new JLabel("🎵");
        iconLabel.setFont(FONT_ICON_BIG);

        titleLabel = new JLabel(song.title);
        artistLabel = new JLabel(song.artist);
        titleLabel.setFont(FONT_NORMAL_BOLD);
        artistLabel.setFont(FONT_SMALL);

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        textPanel.add(artistLabel);

        add(iconLabel, BorderLayout.WEST);
        add(textPanel, BorderLayout.CENTER);

        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void setupGridLayout() {
        setLayout(new BorderLayout());
        setFixedSize(SONG_PANEL_GRID_DIMENSION);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        titleLabel = new JLabel(song.title, SwingConstants.CENTER);
        artistLabel = new JLabel(song.artist, SwingConstants.CENTER);

        titleLabel.setFont(FONT_NORMAL_BOLD);
        artistLabel.setFont(FONT_TINY);

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(Box.createVerticalGlue());
        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        textPanel.add(artistLabel);

        add(textPanel, BorderLayout.SOUTH);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void setPlaying(boolean playing) {
        this.isPlaying = playing;
        updateColors();
    }

    public Song getSong() {
        return song;
    }

    @Override
    public void themeChanged() {
        updateColors();
    }

    private void updateColors() {
        ThemeManager theme = ThemeManager.getInstance();

        if (isPlaying) {
            setBackground(theme.getAccentColor());
            titleLabel.setForeground(Color.WHITE);
            artistLabel.setForeground(Color.WHITE);
        } else {
            setBackground(theme.getComponentBgColor());
            titleLabel.setForeground(theme.getTextPrimary());
            artistLabel.setForeground(theme.getTextSecondary());
        }
    }
}
