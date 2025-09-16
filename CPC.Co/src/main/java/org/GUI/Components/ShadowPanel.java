package org.GUI.Components;

import org.GUI.Theme.ThemeListener;
import org.GUI.Theme.ThemeManager;

import javax.swing.*;
import java.awt.*;

public class ShadowPanel extends JPanel implements ThemeListener {
    private static final int SHADOW_SIZE = 4;

    public ShadowPanel(JComponent component) {
        super(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(SHADOW_SIZE, SHADOW_SIZE, SHADOW_SIZE, SHADOW_SIZE));
        add(component, BorderLayout.CENTER);

        // Listen for theme changes to update the shadow color
        ThemeManager.getInstance().addListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int cornerRadius = 35 + SHADOW_SIZE; 
        
        // Get the base shadow color from the ThemeManager
        Color shadowBaseColor = ThemeManager.getInstance().getShadowColor();

        for (int i = 0; i < SHADOW_SIZE; i++) {
            // Create a transparent version of the theme's shadow color
            g2d.setColor(new Color(shadowBaseColor.getRed(), shadowBaseColor.getGreen(), shadowBaseColor.getBlue(), 20 - i * 2));
            g2d.fillRoundRect(i, i, width - i * 2, height - i * 2, cornerRadius, cornerRadius);
        }

        g2d.dispose();
    }

    @Override
    public void themeChanged() {
        // When the theme changes, just repaint to redraw the shadow with the new color
        repaint();
    }
}