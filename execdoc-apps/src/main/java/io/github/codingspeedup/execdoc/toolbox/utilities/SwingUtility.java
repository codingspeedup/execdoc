package io.github.codingspeedup.execdoc.toolbox.utilities;

import jiconfont.IconFont;
import jiconfont.bundle.Bundle;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;

public class SwingUtility {

    public static void setNimbus() {
        for (IconFont iconFont : Bundle.getIconFonts()) {
            IconFontSwing.register(iconFont);
        }
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, fall back to cross-platform
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                // Not worth my time
            }
        }
        ToolTipManager.sharedInstance().setDismissDelay(60_000);
    }

}
