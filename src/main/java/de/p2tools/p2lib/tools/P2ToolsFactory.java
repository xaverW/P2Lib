package de.p2tools.p2lib.tools;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class P2ToolsFactory {
    private P2ToolsFactory() {
    }

    public static void copyToClipboard(String s) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(s), null);
    }
}
