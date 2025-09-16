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

public class AllSongsPanel extends JPanel implements ThemeListener {
    private final JPanel cardPanel;
    private GradientPanel gradientPanel;
    private RoundedPanel phonePanel;
    private JLabel backButton;
    private JLabel titleLabel;
    private JPanel navBar;

    public AllSongsPanel(JPanel cardPanel) {
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
                ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Home");
            }
        });

        titleLabel = new JLabel("All Songs");
        titleLabel.setFont(FONT_TITLE_LARGE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        topBarPanel.add(backButton, BorderLayout.WEST);
        topBarPanel.add(titleLabel, BorderLayout.CENTER);
        gradientPanel.add(topBarPanel, BorderLayout.NORTH);

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

        JPanel homeItem = createNavItem(ICON_HOME, "Home", activeColor, true); // Active since we came from Home
        homeItem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Home"); }
        });
        JPanel libraryItem = createNavItem(ICON_LIBRARY, "Library", inactiveColor, false);
        libraryItem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Library"); }
        });
        JPanel accountItem = createNavItem(ICON_ACCOUNT, "Account", inactiveColor, false);
        accountItem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Account"); }
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