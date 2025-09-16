package org.GUI.Page;

import org.GUI.Components.GradientPanel;
import org.GUI.Components.RoundedPanel;
import org.GUI.Components.ShadowPanel;
import org.GUI.Theme.ThemeListener;
import org.GUI.Theme.ThemeManager;
import static org.GUI.utils.UIConstants.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class LibraryPanel extends JPanel implements ThemeListener {
    private final JPanel cardPanel;
    private GradientPanel gradientPanel;
    private RoundedPanel phonePanel;
    private JLabel titleLabel;
    private RoundedPanel likedSongsPanel;
    private JLabel likedSongsLabel;
    private RoundedPanel createPlaylistPanel;
    private JLabel createPlaylistLabel;
    private JPanel playlistsContainer;
    private JPanel navBar;
    private final List<RoundedPanel> playlistPanels = new ArrayList<>();
    private final List<JLabel> playlistLabels = new ArrayList<>();

    public LibraryPanel(JPanel cardPanel) {
        this.cardPanel = cardPanel;
        ThemeManager theme = ThemeManager.getInstance();

        phonePanel = new RoundedPanel(PHONE_PANEL_CORNER_RADIUS, theme.getPhoneBackgroundColor());
        phonePanel.setLayout(new BorderLayout());
        phonePanel.setPreferredSize(PHONE_PANEL_DIMENSION);
        phonePanel.setMaximumSize(PHONE_PANEL_DIMENSION);
        phonePanel.setMinimumSize(PHONE_PANEL_DIMENSION);
        phonePanel.setOpaque(false);

        gradientPanel = new GradientPanel(theme.getGradientBg1(), theme.getGradientBg2());
        gradientPanel.setLayout(new BorderLayout());
        phonePanel.add(gradientPanel, BorderLayout.CENTER);

        JPanel topBarPanel = new JPanel(new BorderLayout());
        topBarPanel.setOpaque(false);
        topBarPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        titleLabel = new JLabel("Library");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topBarPanel.add(titleLabel, BorderLayout.CENTER);
        gradientPanel.add(topBarPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        likedSongsPanel = new RoundedPanel(CARD_CORNER_RADIUS, theme.getAccentColor());
        likedSongsPanel.setLayout(new GridBagLayout());
        likedSongsPanel.setPreferredSize(CARD_DIMENSION_MEDIUM);
        likedSongsPanel.setMaximumSize(CARD_DIMENSION_MEDIUM);
        likedSongsPanel.setMinimumSize(CARD_DIMENSION_MEDIUM);

        likedSongsLabel = new JLabel("Liked Songs");
        likedSongsLabel.setFont(FONT_SUBTITLE);
        likedSongsPanel.add(likedSongsLabel);
        likedSongsPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(LibraryPanel.this, "Liked Songs clicked!");
            }
        });

        contentPanel.add(likedSongsPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        createPlaylistPanel = new RoundedPanel(CARD_CORNER_RADIUS, theme.getComponentBgColor());
        createPlaylistPanel.setLayout(new GridBagLayout());
        createPlaylistPanel.setPreferredSize(CARD_DIMENSION_MEDIUM);
        createPlaylistPanel.setMaximumSize(CARD_DIMENSION_MEDIUM);
        createPlaylistPanel.setMinimumSize(CARD_DIMENSION_MEDIUM);

        createPlaylistLabel = new JLabel("Create Playlist");
        createPlaylistLabel.setFont(FONT_SUBTITLE);
        createPlaylistPanel.add(createPlaylistLabel);
        createPlaylistPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showCreatePlaylistFrame();
            }
        });

        contentPanel.add(createPlaylistPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 18)));

        playlistsContainer = new JPanel();
        playlistsContainer.setOpaque(false);
        playlistsContainer.setLayout(new BoxLayout(playlistsContainer, BoxLayout.Y_AXIS));
        playlistsContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(playlistsContainer);
        gradientPanel.add(contentPanel, BorderLayout.CENTER);

        navBar = createBottomNavBar();
        gradientPanel.add(navBar, BorderLayout.SOUTH);

        ShadowPanel shadowPanel = new ShadowPanel(phonePanel);
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);
        this.add(shadowPanel);

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
        titleLabel.setForeground(theme.getTextPrimary());
        likedSongsPanel.setBackground(theme.getAccentColor());
        likedSongsLabel.setForeground(theme.getTextPrimary());
        createPlaylistPanel.setBackground(theme.getComponentBgColor());
        createPlaylistLabel.setForeground(theme.getTextPrimary());

        for (RoundedPanel panel : playlistPanels) {
            panel.setBackground(theme.getComponentBgColor());
        }
        for (JLabel label : playlistLabels) {
            label.setForeground(theme.getTextPrimary());
        }

        gradientPanel.remove(navBar);
        navBar = createBottomNavBar();
        gradientPanel.add(navBar, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private void addPlaylist(String name) {
        ThemeManager theme = ThemeManager.getInstance();
        RoundedPanel playlistPanel = new RoundedPanel(CARD_CORNER_RADIUS, theme.getComponentBgColor());
        playlistPanel.setLayout(new GridBagLayout());
        playlistPanel.setPreferredSize(CARD_DIMENSION_MEDIUM);
        playlistPanel.setMaximumSize(CARD_DIMENSION_MEDIUM);
        playlistPanel.setMinimumSize(CARD_DIMENSION_MEDIUM);
        playlistPanels.add(playlistPanel);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setForeground(theme.getTextPrimary());
        nameLabel.setFont(FONT_SUBTITLE);
        playlistPanel.add(nameLabel);
        playlistLabels.add(nameLabel);

        playlistPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(LibraryPanel.this, "You clicked on playlist: " + name);
            }
        });

        playlistsContainer.add(playlistPanel);
        playlistsContainer.add(Box.createRigidArea(new Dimension(0, 15)));
        playlistsContainer.revalidate();
        playlistsContainer.repaint();
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
            public void mouseClicked(MouseEvent e) { ((CardLayout) (cardPanel.getLayout())).show(cardPanel, "Home"); }
        });
        JPanel libraryItem = createNavItem(ICON_LIBRARY, "Library", activeColor, true);
        JPanel accountItem = createNavItem(ICON_ACCOUNT, "Account", inactiveColor, false);
        accountItem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { ((CardLayout) (cardPanel.getLayout())).show(cardPanel, "Account"); }
        });

        newNavBar.add(homeItem);
        newNavBar.add(libraryItem);
        newNavBar.add(accountItem);
        return newNavBar;
    }
    
    private void showCreatePlaylistFrame() {
        JFrame createPlaylistFrame = new JFrame("Create Playlist");
        createPlaylistFrame.setSize(300, 150);
        createPlaylistFrame.setLocationRelativeTo(this);
        createPlaylistFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createPlaylistFrame.setResizable(false);
        
        ThemeManager theme = ThemeManager.getInstance();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(theme.getGradientBg1());
        
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.setOpaque(false);
        JTextField nameField = new JTextField(15);
        JLabel inputLabel = new JLabel("Playlist name:");
        inputLabel.setForeground(theme.getTextPrimary());
        namePanel.add(inputLabel);
        namePanel.add(nameField);
        
        JButton createButton = new JButton("Create");
        createButton.setBackground(theme.getAccentColor());
        createButton.setForeground(theme.getTextPrimary());
        createButton.setFocusPainted(false);
        createButton.addActionListener(e -> {
            String playlistName = nameField.getText().trim();
            if (!playlistName.isEmpty()) {
                addPlaylist(playlistName);
                createPlaylistFrame.dispose();
            }
        });
        
        panel.add(namePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createButton);
        createPlaylistFrame.add(panel);
        createPlaylistFrame.setVisible(true);
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