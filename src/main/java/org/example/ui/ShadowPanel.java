package org.example.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * A panel that draws a drop shadow behind its child component.
 */
class ShadowPanel extends JPanel {
    private static final int SHADOW_SIZE = 4;

    public ShadowPanel(JComponent component) {
        super(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(SHADOW_SIZE, SHADOW_SIZE, SHADOW_SIZE, SHADOW_SIZE));
        add(component, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int cornerRadius = 35 + SHADOW_SIZE; // Match the child's corner radius

        for (int i = 0; i < SHADOW_SIZE; i++) {
            g2d.setColor(new Color(0, 0, 0, 20 - i * 2));
            g2d.fillRoundRect(i, i, width - i * 2, height - i * 2, cornerRadius, cornerRadius);
        }

        g2d.dispose();
    }
}
