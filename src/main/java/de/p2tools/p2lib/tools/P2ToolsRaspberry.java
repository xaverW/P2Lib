package de.p2tools.p2lib.tools;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.tools.log.P2Log;

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

    public static String getRaspberryInfo() {
        // os.name = Linux
        // os.arch = aarch64
        // os.version = 6.6.62+rpt-rpi-2712
        // sun.arch.data.model = 64

        StringBuilder stringBuilder = new StringBuilder();

        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");
        String osVersion = System.getProperty("os.version");

        stringBuilder.append("===================================\n");
        stringBuilder.append("osName:    ").append(osName).append(P2LibConst.LINE_SEPARATOR);
        stringBuilder.append("osArch:    ").append(osArch).append(P2LibConst.LINE_SEPARATOR);
        stringBuilder.append("osVersion: ").append(osVersion).append(P2LibConst.LINE_SEPARATOR);
        stringBuilder.append("===================================\n");
        return stringBuilder.toString();
    }
}
