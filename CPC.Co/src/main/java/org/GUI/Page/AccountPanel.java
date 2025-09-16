package org.GUI.Page;

import org.GUI.Components.DarkModeToggle;
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

public class AccountPanel extends JPanel implements ThemeListener {
    private final JPanel cardPanel;
    private GradientPanel gradientPanel;
    private RoundedPanel phonePanel;
    private JLabel titleLabel;
    private RoundedPanel signUpPanel;
    private JLabel signUpLabel;
    private RoundedPanel logInPanel;
    private JLabel logInLabel;
    private JPanel navBar;

    public AccountPanel(JPanel cardPanel) {
        this.cardPanel = cardPanel;
        ThemeManager theme = ThemeManager.getInstance(); // Get theme instance

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

        titleLabel = new JLabel("Account");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        topBarPanel.add(titleLabel, BorderLayout.CENTER);
        topBarPanel.add(new DarkModeToggle(), BorderLayout.EAST);
        gradientPanel.add(topBarPanel, BorderLayout.NORTH);

        JPanel accountOptionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        accountOptionsPanel.setOpaque(false);

        signUpPanel = new RoundedPanel(CARD_CORNER_RADIUS, theme.getComponentBgColor());
        signUpPanel.setPreferredSize(CARD_DIMENSION_MEDIUM);
        signUpPanel.setLayout(new GridBagLayout());
        signUpLabel = new JLabel("Sign Up");
        signUpLabel.setFont(FONT_BUTTON);
        signUpPanel.add(signUpLabel);

        logInPanel = new RoundedPanel(CARD_CORNER_RADIUS, theme.getComponentBgColor());
        logInPanel.setPreferredSize(CARD_DIMENSION_MEDIUM);
        logInPanel.setLayout(new GridBagLayout());
        logInLabel = new JLabel("Log In");
        logInLabel.setFont(FONT_BUTTON);
        logInPanel.add(logInLabel);

        accountOptionsPanel.add(signUpPanel);
        accountOptionsPanel.add(logInPanel);
        gradientPanel.add(accountOptionsPanel, BorderLayout.CENTER);

        navBar = createBottomNavBar();
        gradientPanel.add(navBar, BorderLayout.SOUTH);

        ShadowPanel shadowPanel = new ShadowPanel(phonePanel);
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);
        this.add(shadowPanel);

        ThemeManager.getInstance().addListener(this);
        updateTheme(); // Initial theme setup
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
        signUpPanel.setBackground(theme.getComponentBgColor());
        signUpLabel.setForeground(theme.getTextPrimary());
        logInPanel.setBackground(theme.getComponentBgColor());
        logInLabel.setForeground(theme.getTextPrimary());
        
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
        JPanel libraryItem = createNavItem(ICON_LIBRARY, "Library", inactiveColor, false);
        libraryItem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Library"); }
        });
        JPanel accountItem = createNavItem(ICON_ACCOUNT, "Account", activeColor, true);

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