package org.GUI.Page;

import org.GUI.Components.GradientPanel;
import org.GUI.Components.RoundedPanel;
import org.GUI.Components.ShadowPanel;
import org.GUI.Functionalities.Song;
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

public class Home extends JPanel implements ThemeListener {
    private final JPanel cardPanel;
    private GradientPanel gradientPanel;
    private RoundedPanel phonePanel;
    private RoundedPanel searchBar;
    private JTextField searchField;
    private RoundedPanel allSongsPanel;
    private JLabel allSongsLabel;
    private JLabel recentlyPlayedLabel;
    private JPanel navBar;
    private final List<RoundedPanel> songBoxes = new ArrayList<>();
    private final List<JLabel> songTitleLabels = new ArrayList<>();
    private final List<JLabel> songArtistLabels = new ArrayList<>();
    private final JPanel gridPanel;

    public Home(JPanel cardPanel) {
        this.cardPanel = cardPanel;
        ThemeManager theme = ThemeManager.getInstance();

        phonePanel = new RoundedPanel(PHONE_PANEL_CORNER_RADIUS, theme.getPhoneBackgroundColor());
        phonePanel.setLayout(new BorderLayout());
        phonePanel.setPreferredSize(PHONE_PANEL_DIMENSION);
        phonePanel.setMaximumSize(PHONE_PANEL_DIMENSION);
        phonePanel.setMinimumSize(PHONE_PANEL_DIMENSION);

        gradientPanel = new GradientPanel(theme.getGradientBg1(), theme.getGradientBg2());
        gradientPanel.setLayout(new BorderLayout());
        phonePanel.add(gradientPanel, BorderLayout.CENTER);

        JPanel topContentPanel = new JPanel();
        topContentPanel.setOpaque(false);
        topContentPanel.setLayout(new BoxLayout(topContentPanel, BoxLayout.Y_AXIS));
        gradientPanel.add(topContentPanel, BorderLayout.NORTH);

        JPanel topSearchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topSearchPanel.setOpaque(false);
        topSearchPanel.setBorder(BorderFactory.createEmptyBorder(18, 0, 6, 0));
        searchBar = new RoundedPanel(CARD_CORNER_RADIUS, theme.getComponentBgColor());
        searchBar.setLayout(new BorderLayout(10, 0));
        searchBar.setPreferredSize(SEARCH_BAR_DIMENSION);
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(FONT_SUBTITLE);
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        searchBar.add(searchIcon, BorderLayout.WEST);
        searchField = new JTextField("Search for songs, artists, and more...");
        searchField.setBorder(BorderFactory.createEmptyBorder());
        searchField.setFont(FONT_SEARCH_FIELD);
        searchBar.add(searchField, BorderLayout.CENTER);
        topSearchPanel.add(searchBar);
        topContentPanel.add(topSearchPanel);

        JPanel allSongsContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        allSongsContainer.setOpaque(false);
        allSongsContainer.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        allSongsPanel = new RoundedPanel(CARD_CORNER_RADIUS, theme.getAccentColor());
        allSongsPanel.setLayout(new GridBagLayout());
        allSongsPanel.setPreferredSize(CARD_DIMENSION_LARGE);
        allSongsLabel = new JLabel("All Songs");
        allSongsLabel.setFont(FONT_TITLE);
        allSongsPanel.add(allSongsLabel);
        allSongsPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ((CardLayout) (cardPanel.getLayout())).show(cardPanel, "AllSongs");
            }
        });
        allSongsContainer.add(allSongsPanel);
        topContentPanel.add(allSongsContainer);

        JPanel recentlyPlayedLabelContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        recentlyPlayedLabelContainer.setOpaque(false);
        recentlyPlayedLabelContainer.setBorder(BorderFactory.createEmptyBorder(2, 35, 10, 0));
        recentlyPlayedLabel = new JLabel("Recently Played");
        recentlyPlayedLabel.setFont(FONT_SUBTITLE);
        recentlyPlayedLabelContainer.add(recentlyPlayedLabel);
        topContentPanel.add(recentlyPlayedLabelContainer);
        
        JPanel gridContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        gridContainer.setOpaque(false);
        
        gridPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        gridPanel.setOpaque(false);
        
        populateRecentlyPlayed();
        
        gridContainer.add(gridPanel);
        topContentPanel.add(gridContainer);
        
        navBar = createBottomNavBar();
        gradientPanel.add(navBar, BorderLayout.SOUTH);

        ShadowPanel shadowPanel = new ShadowPanel(phonePanel);
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);
        this.add(shadowPanel);

        ThemeManager.getInstance().addListener(this);
        updateTheme();
    }

    private void populateRecentlyPlayed() {
        gridPanel.removeAll();
        songBoxes.clear();
        songTitleLabels.clear();
        songArtistLabels.clear();
        
        List<Song> songs = SongLoader.loadSongs("/songs.txt");
        
        for (Song song : songs) {
            gridPanel.add(createSongBox(song));
        }

        this.revalidate();
        this.repaint();
    }
    
    @Override
    public void themeChanged() {
        updateTheme();
    }

    private void updateTheme() {
        ThemeManager theme = ThemeManager.getInstance();
        phonePanel.setBackground(theme.getPhoneBackgroundColor());
        gradientPanel.setColors(theme.getGradientBg1(), theme.getGradientBg2());
        searchBar.setBackground(theme.getComponentBgColor());
        searchField.setBackground(theme.getComponentBgColor());
        searchField.setForeground(theme.getTextSecondary());
        searchField.setCaretColor(theme.getTextPrimary());
        allSongsPanel.setBackground(theme.getAccentColor());
        allSongsLabel.setForeground(theme.getTextPrimary());
        recentlyPlayedLabel.setForeground(theme.getTextPrimary());

        for (RoundedPanel box : songBoxes) {
            box.setBackground(theme.getComponentBgColor());
        }
        for (JLabel title : songTitleLabels) {
            title.setForeground(theme.getTextPrimary());
        }
        for (JLabel artist : songArtistLabels) {
            artist.setForeground(theme.getTextSecondary());
        }

        gradientPanel.remove(navBar);
        navBar = createBottomNavBar();
        gradientPanel.add(navBar, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private JPanel createSongBox(Song song) {
        RoundedPanel songPanel = new RoundedPanel(SMALL_CORNER_RADIUS, ThemeManager.getInstance().getComponentBgColor());
        songPanel.setLayout(new BorderLayout(5, 0));
        songPanel.setPreferredSize(new Dimension(170, 85));
        songBoxes.add(songPanel);
        songPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        songPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showNowPlayingFrame(song);
            }
        });

        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource(song.coverImage));
            Image scaledImage = originalIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
            songPanel.add(imageLabel, BorderLayout.WEST);
        } catch (Exception e) {
            System.err.println("Could not load image: " + song.coverImage);
        }

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));

        JLabel titleLabel = new JLabel(song.title);
        titleLabel.setFont(new Font(FONT_PRIMARY, Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        songTitleLabels.add(titleLabel);

        JLabel artistLabel = new JLabel(song.artist);
        artistLabel.setFont(FONT_ARTIST);
        artistLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        songArtistLabels.add(artistLabel);

        textPanel.add(titleLabel);
        textPanel.add(artistLabel);
        songPanel.add(textPanel, BorderLayout.CENTER);

        return songPanel;
    }
    
    private void showNowPlayingFrame(Song song) {
        JFrame nowPlayingFrame = new JFrame("Now Playing");
        nowPlayingFrame.setSize(300, 150);
        nowPlayingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        nowPlayingFrame.setLocationRelativeTo(this);
        nowPlayingFrame.setResizable(false);

        ThemeManager theme = ThemeManager.getInstance();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(theme.getGradientBg1());

        JLabel label = new JLabel("<html><center>Playing music...<br><b>" + song.title + "</b> by " + song.artist + "</center></html>");
        label.setForeground(theme.getTextPrimary());
        label.setFont(FONT_BUTTON);
        panel.add(label);

        nowPlayingFrame.add(panel);
        nowPlayingFrame.setVisible(true);
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
        JPanel libraryItem = createNavItem(ICON_LIBRARY, "Library", inactiveColor, false);
        libraryItem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { ((CardLayout) (cardPanel.getLayout())).show(cardPanel, "Library"); }
        });
        JPanel accountItem = createNavItem(ICON_ACCOUNT, "Account", inactiveColor, false);
        accountItem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { ((CardLayout) (cardPanel.getLayout())).show(cardPanel, "Account"); }
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