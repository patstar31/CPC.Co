package org.GUI.utils;

import java.awt.Dimension;
import java.awt.Font;


 //A central class to hold all UI constants.

public final class UIConstants {
    // --- FONT FAMILIES ---
    public static final String FONT_PRIMARY = "Montserrat";
    public static final String FONT_SECONDARY = "SansSerif";

    // --- DIMENSIONS ---
    public static final Dimension PHONE_PANEL_DIMENSION = new Dimension(400, 800);
    public static final Dimension CARD_DIMENSION_LARGE = new Dimension(350, 140);
    public static final Dimension CARD_DIMENSION_MEDIUM = new Dimension(350, 120);
    public static final Dimension SEARCH_BAR_DIMENSION = new Dimension(350, 48);
    public static final Dimension SONG_PANEL_LIST_DIMENSION = new Dimension(375, 75);
    public static final Dimension SONG_PANEL_GRID_DIMENSION = new Dimension(170, 85);
    public static final int PHONE_PANEL_CORNER_RADIUS = 35;
    public static final int CARD_CORNER_RADIUS = 25;
    public static final int SMALL_CORNER_RADIUS = 15;

    // --- FONTS ---
    // Primary Fonts (General UI Text)
    public static final Font FONT_TITLE_LARGE = new Font(FONT_PRIMARY, Font.BOLD, 24);
    public static final Font FONT_TITLE = new Font(FONT_PRIMARY, Font.BOLD, 20);
    public static final Font FONT_SUBTITLE = new Font(FONT_PRIMARY, Font.BOLD, 18);
    public static final Font FONT_BUTTON = new Font(FONT_PRIMARY, Font.BOLD, 16);
    public static final Font FONT_SEARCH_FIELD = new Font(FONT_PRIMARY, Font.PLAIN, 14);

    // Song Info Fonts (New)
    public static final Font FONT_NORMAL_BOLD = new Font(FONT_PRIMARY, Font.BOLD, 14); // For Song Title in Player
    public static final Font FONT_SMALL = new Font(FONT_PRIMARY, Font.PLAIN, 12);     // For Song Artist in Player
    public static final Font FONT_ARTIST = new Font(FONT_PRIMARY, Font.PLAIN, 10);    // For Artist in Grid View

    // Navigation and Icon Fonts
    public static final Font FONT_NAV_BAR = new Font(FONT_PRIMARY, Font.PLAIN, 12);
    public static final Font FONT_NAV_BAR_BOLD = new Font(FONT_PRIMARY, Font.BOLD, 12);
    public static final Font FONT_ICON_BIG = new Font(FONT_SECONDARY, Font.BOLD, 28);  // Used for Player Controls
    public static final Font FONT_ICON_LARGE = new Font(FONT_SECONDARY, Font.BOLD, 28); // Used for general large icons
    public static final Font FONT_ICON_NAV = new Font(FONT_SECONDARY, Font.PLAIN, 28);  // Used for NavBar icons

    // --- ICONS ---
    public static final String ICON_HOME = "⌂";
    public static final String ICON_LIBRARY = "🎵";
    public static final String ICON_ACCOUNT = "👤";
    public static final String ICON_BACK = "←";
}