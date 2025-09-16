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

public class PlaylistPanel extends JPanel implements ThemeListener {
    private final JPanel cardPanel;
    private GradientPanel gradientPanel;
    private RoundedPanel phonePanel;
    private JLabel backButton;
    private JLabel titleLabel;
    private RoundedPanel createPlaylistPanel;
    private JLabel createPlaylistLabel;
    private JPanel playlistListPanel;
    private JPanel navBar;

    // Keep track of dynamically created playlist components
    private final List<RoundedPanel> createdPlaylistPanels = new ArrayList<>();
    private final List<JLabel> createdPlaylistLabels = new ArrayList<>();

    public PlaylistPanel(JPanel cardPanel) {
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

        JPanel topBarPanel = new JPanel(new BorderLayout());
        topBarPanel.setOpaque(false);
        topBarPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        backButton = new JLabel(ICON_BACK);
        backButton.setFont(FONT_ICON_LARGE);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Library"); // Or wherever you came from
            }
        });

        titleLabel = new JLabel("Your Playlists");
        titleLabel.setFont(FONT_TITLE_LARGE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        topBarPanel.add(backButton, BorderLayout.WEST);
        topBarPanel.add(titleLabel, BorderLayout.CENTER);
        gradientPanel.add(topBarPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        createPlaylistPanel = new RoundedPanel(CARD_CORNER_RADIUS, theme.getComponentBgColor());
        createPlaylistPanel.setLayout(new GridBagLayout());
        createPlaylistPanel.setPreferredSize(CARD_DIMENSION_MEDIUM);
        createPlaylistPanel.setMaximumSize(CARD_DIMENSION_MEDIUM);
        createPlaylistPanel.setMinimumSize(CARD_DIMENSION_MEDIUM);
        createPlaylistPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showCreatePlaylistFrame();
            }
        });

        createPlaylistLabel = new JLabel("Create a new Playlist");
        createPlaylistLabel.setFont(FONT_SUBTITLE);
        createPlaylistPanel.add(createPlaylistLabel);

        contentPanel.add(createPlaylistPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        playlistListPanel = new JPanel();
        playlistListPanel.setOpaque(false);
        playlistListPanel.setLayout(new BoxLayout(playlistListPanel, BoxLayout.Y_AXIS));
        contentPanel.add(playlistListPanel);

        gradientPanel.add(new JScrollPane(contentPanel), BorderLayout.CENTER);

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
        backButton.setForeground(theme.getTextPrimary());
        titleLabel.setForeground(theme.getTextPrimary());
        createPlaylistPanel.setBackground(theme.getComponentBgColor());
        createPlaylistLabel.setForeground(theme.getTextPrimary());

        for (RoundedPanel panel : createdPlaylistPanels) {
            panel.setBackground(theme.getComponentBgColor());
        }
        for (JLabel label : createdPlaylistLabels) {
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
        createdPlaylistPanels.add(playlistPanel);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setForeground(theme.getTextPrimary());
        nameLabel.setFont(FONT_SUBTITLE);
        playlistPanel.add(nameLabel);
        createdPlaylistLabels.add(nameLabel);

        playlistListPanel.add(playlistPanel);
        playlistListPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        playlistListPanel.revalidate();
        playlistListPanel.repaint();
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
    
    private JPanel createBottomNavBar() {
        JPanel navBarPanel = new JPanel(new GridLayout(1, 3));
        navBarPanel.setOpaque(false);
        navBarPanel.setPreferredSize(new Dimension(0, 70));
        navBarPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        
        ThemeManager theme = ThemeManager.getInstance();
        Color activeColor = theme.getAccentColor();
        Color inactiveColor = theme.getTextPrimary();

        // This panel preserves the original nav bar with "Search"
        JPanel homeItem = createNavItem(ICON_HOME, "Home", inactiveColor, false);
        homeItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ((CardLayout) (cardPanel.getLayout())).show(cardPanel, "Home");
            }
        });

        JPanel searchItem = createNavItem("🔍", "Search", inactiveColor, false);
        searchItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Assuming you have a "Search" panel to switch to
                // ((CardLayout) (cardPanel.getLayout())).show(cardPanel, "Search");
                 JOptionPane.showMessageDialog(PlaylistPanel.this, "Search clicked!");
            }
        });

        JPanel accountItem = createNavItem(ICON_ACCOUNT, "Account", activeColor, true);

        navBarPanel.add(homeItem);
        navBarPanel.add(searchItem);
        navBarPanel.add(accountItem);

        return navBarPanel;
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