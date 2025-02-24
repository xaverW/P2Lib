package de.p2tools.p2lib.tools;

import de.p2tools.p2lib.tools.log.P2Log;

import java.util.ArrayList;

public class P2ToolsRaspberry {
    public static final String RASPBERRY_OS_NAME = "Linux";
    public static final String RASPBERRY_OS_ARCH = "aarch64";

    private P2ToolsRaspberry() {
    }

    public static boolean isRaspberry() {
        // os.name = Linux
        // os.arch = aarch64
        // os.version = 6.6.62+rpt-rpi-2712
        // sun.arch.data.model = 64

        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");

        P2Log.sysLog(getRaspberryInfo());
        if (osName.equals(RASPBERRY_OS_NAME) && osArch.equals(RASPBERRY_OS_ARCH)) {
            P2Log.sysLog(" -> läuft auf einem Raspberry");
            P2Log.sysLog("===================================");
            return true;
        } else {
            P2Log.sysLog(" -> kein Raspberry");
            P2Log.sysLog("===================================");
            return false;
        }
    }

    public static String[] getRaspberryInfo() {
        // os.name = Linux
        // os.arch = aarch64
        // os.version = 6.6.62+rpt-rpi-2712
        // sun.arch.data.model = 64

        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");
        String osVersion = System.getProperty("os.version");

        ArrayList<String> list = new ArrayList<>();
        list.add("===================================");
        list.add("osName:    " + osName);
        list.add("osArch:    " + osArch);
        list.add("osVersion: " + osVersion);
        list.add("===================================");
        return list.toArray(new String[0]);
    }
}
