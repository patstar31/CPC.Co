package org.GUI.Components;

import org.GUI.Theme.ThemeManager;
import static org.GUI.utils.UIConstants.*;
import javax.swing.*;
import java.awt.*;

public class DarkModeToggle extends JPanel {
    private final JToggleButton toggleButton;

    public DarkModeToggle() {
        setLayout(new FlowLayout(FlowLayout.RIGHT));
        setOpaque(false);

        toggleButton = new JToggleButton();
        
        // --- THE FIX: Calculate and set a fixed size ---

        // 1. Temporarily set the text to each state to measure its preferred size.
        toggleButton.setText("☀️");
        Dimension sunSize = toggleButton.getPreferredSize();
        toggleButton.setText("🌙");
        Dimension moonSize = toggleButton.getPreferredSize();

        // 2. Determine the maximum width and height needed to fit either icon.
        int maxWidth = Math.max(sunSize.width, moonSize.width);
        int maxHeight = Math.max(sunSize.height, moonSize.height);
        Dimension fixedSize = new Dimension(maxWidth, maxHeight);

        // 3. Set the button's preferred size permanently.
        toggleButton.setPreferredSize(fixedSize);
        
        // --- End of Fix ---

        updateButtonAppearance(); // Now set the initial appearance correctly

        toggleButton.addActionListener(e -> {
            ThemeManager.getInstance().toggleDarkMode();
            // The listener below will handle the update, so no need to call it here.
        });

        ThemeManager.getInstance().addListener(this::updateButtonAppearance);
        add(toggleButton);
    }

    private void updateButtonAppearance() {
        boolean isDarkMode = ThemeManager.getInstance().isDarkMode();

        if (isDarkMode) {
            toggleButton.setText("☀️");
            toggleButton.setToolTipText("Switch to Light Mode");
        } else {
            toggleButton.setText("🌙");
            toggleButton.setToolTipText("Switch to Dark Mode");
        }

        toggleButton.setBackground(ThemeManager.getInstance().getComponentBgColor());
        toggleButton.setForeground(ThemeManager.getInstance().getTextPrimary());
        toggleButton.setFont(FONT_BUTTON);
        toggleButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        toggleButton.setFocusPainted(false);
    }
}