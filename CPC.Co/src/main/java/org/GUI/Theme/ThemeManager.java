package org.GUI.Theme;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ThemeManager {
    private static ThemeManager instance;
    private boolean isDarkMode = true;
    private final List<ThemeListener> listeners = new ArrayList<>();

    // --- Dark Mode Colors ---
    private final Color DARK_BG_1 = Color.decode("#355C7D");
    private final Color DARK_BG_2 = Color.decode("#725A7A");
    private final Color DARK_ACCENT = Color.decode("#C56C86");
    private final Color DARK_COMPONENT_BG = new Color(220, 220, 220, 100);
    private final Color DARK_TEXT_PRIMARY = Color.WHITE;
    private final Color DARK_TEXT_SECONDARY = Color.LIGHT_GRAY;
    private final Color DARK_SHADOW = new Color(0, 0, 0);
    private final Color DARK_PHONE_BG = Color.BLACK; // Color for the main phone body

    // --- Light Mode Colors ---
    //private final Color LIGHT_BG_1 = Color.decode("#E0EAFC");//lighter White
    private final Color LIGHT_BG_1 = Color.decode("#A8C0E0");//lighter White
    //private final Color LIGHT_BG_2 = Color.decode("#CFDEF3");//light greyish blue
    private final Color LIGHT_BG_2 = Color.decode("#D1B7CC");
    //private final Color LIGHT_ACCENT = Color.decode("#007BFF");
    private final Color LIGHT_ACCENT = Color.decode("#F1A8BB");//blue
    private final Color LIGHT_COMPONENT_BG = new Color(0, 0, 0, 20);
    private final Color LIGHT_TEXT_PRIMARY = Color.BLACK;
    private final Color LIGHT_TEXT_SECONDARY = Color.DARK_GRAY;
    private final Color LIGHT_SHADOW = new Color(150, 150, 150);
    private final Color LIGHT_PHONE_BG = new Color(245, 245, 245); // Off-white for light mode phone body

    private ThemeManager() {}

    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    //
    public void toggleDarkMode() {
        isDarkMode = !isDarkMode;
        notifyListeners();
    }

    public boolean isDarkMode() {
        return isDarkMode;
    }

    // --- Color Getters ---
    public Color getGradientBg1() { return isDarkMode ? DARK_BG_1 : LIGHT_BG_1; }
    public Color getGradientBg2() { return isDarkMode ? DARK_BG_2 : LIGHT_BG_2; }
    public Color getAccentColor() { return isDarkMode ? DARK_ACCENT : LIGHT_ACCENT; }
    public Color getComponentBgColor() { return isDarkMode ? DARK_COMPONENT_BG : LIGHT_COMPONENT_BG; }
    public Color getTextPrimary() { return isDarkMode ? DARK_TEXT_PRIMARY : LIGHT_TEXT_PRIMARY; }
    public Color getTextSecondary() { return isDarkMode ? DARK_TEXT_SECONDARY : LIGHT_TEXT_SECONDARY; }
    public Color getShadowColor() { return isDarkMode ? DARK_SHADOW : LIGHT_SHADOW; }
    public Color getPhoneBackgroundColor() { return isDarkMode ? DARK_PHONE_BG : LIGHT_PHONE_BG; } // New getter

    // --- Listener Management ---
    public void addListener(ThemeListener listener) {
        listeners.add(listener);
    }
    public void removeListener(ThemeListener listener) {
        listeners.remove(listener);
    }
    private void notifyListeners() {
        for (ThemeListener listener : listeners) {
            listener.themeChanged();
        }
    }
}