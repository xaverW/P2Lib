package de.p2tools.p2lib.tools;

import de.p2tools.p2lib.tools.log.P2Log;
import javafx.beans.property.BooleanProperty;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.function.Function;

public class P2ToolsFactory {
    private P2ToolsFactory() {
    }

    public static void copyToClipboard(String s) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(s), null);
    }

    public static void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            P2Log.errorLog(105897874, "P2ToolsFactory.pause: Interrupted");
        }
    }

    public static void waitWhile(int waitMax, BooleanProperty waitWhile) {
        final int WAIT = 250;
        final int COUNT_MAX = waitMax / WAIT;

        int count = 0;
        while (waitWhile.get()) {
            count += 1;
            if (count > COUNT_MAX) {
                // kann nicht ewig hier festhängen
                break;
            }
//            System.out.println("waitWhile");
            pause(WAIT);
        }
    }

    public static void waitWhile(int waitMax, Function<Boolean, Boolean> function) {
        final int WAIT = 250;
        final int COUNT_MAX = waitMax / WAIT;

        int count = 0;
        while (function.apply(true)) {
            count += 1;
            if (count > COUNT_MAX) {
                // kann nicht ewig hier festhängen
                break;
            }
//            System.out.println("waitWhile");
            pause(WAIT);
        }
    }
}
