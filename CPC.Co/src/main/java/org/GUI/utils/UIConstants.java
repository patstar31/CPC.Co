package org.GUI.utils;

import java.awt.Dimension;
import java.awt.Font;

/**
 * A central class to hold all UI constants, removing "magic values" from the code.
 */
public final class UIConstants {
    // --- FONT FAMILIES ---
    public static final String FONT_PRIMARY = "Montserrat";
    public static final String FONT_SECONDARY = "SansSerif";

    // --- DIMENSIONS ---
    public static final Dimension PHONE_PANEL_DIMENSION = new Dimension(400, 800);
    public static final Dimension CARD_DIMENSION_LARGE = new Dimension(350, 140);
    public static final Dimension CARD_DIMENSION_MEDIUM = new Dimension(350, 120);
    public static final Dimension SEARCH_BAR_DIMENSION = new Dimension(350, 48);
    public static final int PHONE_PANEL_CORNER_RADIUS = 35;
    public static final int CARD_CORNER_RADIUS = 25;
    public static final int SMALL_CORNER_RADIUS = 15;

    // --- FONTS ---
    public static final Font FONT_TITLE_LARGE = new Font(FONT_PRIMARY, Font.BOLD, 24);
    public static final Font FONT_TITLE = new Font(FONT_PRIMARY, Font.BOLD, 20);
    public static final Font FONT_SUBTITLE = new Font(FONT_PRIMARY, Font.BOLD, 18);
    public static final Font FONT_BUTTON = new Font(FONT_PRIMARY, Font.BOLD, 16);
    public static final Font FONT_SEARCH_FIELD = new Font(FONT_PRIMARY, Font.PLAIN, 14);
    public static final Font FONT_ARTIST = new Font(FONT_PRIMARY, Font.PLAIN, 10);
    public static final Font FONT_NAV_BAR = new Font(FONT_PRIMARY, Font.PLAIN, 12);
    public static final Font FONT_NAV_BAR_BOLD = new Font(FONT_PRIMARY, Font.BOLD, 12);
    public static final Font FONT_ICON_LARGE = new Font(FONT_SECONDARY, Font.BOLD, 28);
    public static final Font FONT_ICON_NAV = new Font(FONT_SECONDARY, Font.PLAIN, 28);

    // --- ICONS ---
    public static final String ICON_HOME = "⌂";
    public static final String ICON_LIBRARY = "🎵";
    public static final String ICON_ACCOUNT = "👤";
    public static final String ICON_BACK = "←";
}