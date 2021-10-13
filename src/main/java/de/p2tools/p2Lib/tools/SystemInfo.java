/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */


package de.p2tools.p2Lib.tools;

import java.awt.*;
import java.lang.reflect.Field;
import java.security.AccessControlException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class SystemInfo {
    private static boolean _isWindows = false;
    private static boolean _isWindowsNTor2000 = false;
    private static boolean _isWindowsXP = false;
    private static boolean _isWindowsVista = false;
    private static boolean _isWindows7 = false;
    private static boolean _isWindows8 = false;
    private static boolean _isWindows2003 = false;
    private static boolean _isClassicWindows = false;
    private static boolean _isWindows95 = false;
    private static boolean _isWindows98 = false;
    private static boolean _supportsTray = false;
    private static boolean _isMacClassic = false;
    private static boolean _isMacOSX = false;
    private static boolean _isLinux = false;
    private static boolean _isSolaris = false;
    private static boolean _isBSD = false;
    private static JavaVersion _currentVersion;

    private SystemInfo() {
    }

    public static String getJavaVersion() {
        return getProperty("java.version", "1.4.2");
    }

    public static String getJavaVendor() {
        return getProperty("java.vendor", "");
    }

    public static String getJavaClassVersion() {
        return getProperty("java.class.version", "");
    }

    public static String getOS() {
        return getProperty("os.name", "Windows XP");
    }

    public static String getOSVersion() {
        return getProperty("os.version", "");
    }

    public static String getOSArchitecture() {
        return getProperty("os.arch", "");
    }

    public static String getCurrentDirectory() {
        return getProperty("user.dir", "");
    }

    public static boolean supportsTray() {
        return _supportsTray;
    }

    public static void setSupportsTray(boolean support) {
        _supportsTray = support;
    }

    public static boolean isWindows() {
        return _isWindows;
    }

    public static boolean isClassicWindows() {
        return _isClassicWindows;
    }

    public static boolean isWindowsNTor2000() {
        return _isWindowsNTor2000;
    }

    public static boolean isWindowsXP() {
        return _isWindowsXP;
    }

    public static boolean isWindowsVista() {
        return _isWindowsVista;
    }

    public static boolean isWindows7() {
        return _isWindows7;
    }

    public static boolean isWindows8() {
        return _isWindows8;
    }

    public static boolean isWindowsVistaAbove() {
        return _isWindowsVista || _isWindows7 || _isWindows8;
    }

    public static boolean isWindows95() {
        return _isWindows95;
    }

    public static boolean isWindows98() {
        return _isWindows98;
    }

    public static boolean isWindows2003() {
        return _isWindows2003;
    }

    public static boolean isMacClassic() {
        return _isMacClassic;
    }

    public static boolean isMacOSX() {
        return _isMacOSX;
    }

    public static boolean isAnyMac() {
        return _isMacClassic || _isMacOSX;
    }

    public static boolean isSolaris() {
        return _isSolaris;
    }

    public static boolean isLinux() {
        return _isLinux;
    }

    public static boolean isBSD() {
        return _isBSD;
    }

    public static boolean isUnix() {
        return _isLinux || _isSolaris || _isBSD;
    }

    private static void checkJdkVersion() {
        if (_currentVersion == null) {
            _currentVersion = new JavaVersion(getJavaVersion());
        }

    }

    public static boolean isJdk13Above() {
        checkJdkVersion();
        return _currentVersion.compareVersion(1.3D, 0, 0) >= 0;
    }

    public static boolean isJdk142Above() {
        checkJdkVersion();
        return _currentVersion.compareVersion(1.4D, 2, 0) >= 0;
    }

    public static boolean isJdk14Above() {
        checkJdkVersion();
        return _currentVersion.compareVersion(1.4D, 0, 0) >= 0;
    }

    public static boolean isJdk15Above() {
        checkJdkVersion();
        return _currentVersion.compareVersion(1.5D, 0, 0) >= 0;
    }

    public static boolean isJdk6Above() {
        checkJdkVersion();
        return _currentVersion.compareVersion(1.6D, 0, 0) >= 0;
    }

    public static boolean isJdk6u10Above() {
        checkJdkVersion();
        return _currentVersion.compareVersion(1.6D, 0, 10) >= 0;
    }

    public static boolean isJdk6u14Above() {
        checkJdkVersion();
        return _currentVersion.compareVersion(1.6D, 0, 14) >= 0;
    }

    public static boolean isJdk6u25Above() {
        checkJdkVersion();
        return _currentVersion.compareVersion(1.6D, 0, 25) >= 0;
    }

    public static boolean isJdk7Above() {
        checkJdkVersion();
        return _currentVersion.compareVersion(1.7D, 0, 0) >= 0;
    }

    public static boolean isJdk7u40Above() {
        checkJdkVersion();
        return _currentVersion.compareVersion(1.7D, 0, 40) >= 0;
    }

    public static boolean isJdk8Above() {
        checkJdkVersion();
        return _currentVersion.compareVersion(1.8D, 0, 0) >= 0;
    }

    public static boolean isJdkVersion(double majorVersion, int minorVersion, int build) {
        checkJdkVersion();
        return _currentVersion.compareVersion(majorVersion, minorVersion, build) == 0;
    }

    public static boolean isJdkVersionAbove(double majorVersion, int minorVersion, int build) {
        checkJdkVersion();
        return _currentVersion.compareVersion(majorVersion, minorVersion, build) >= 0;
    }

    public static boolean isJdkVersionBelow(double majorVersion, int minorVersion, int build) {
        checkJdkVersion();
        return _currentVersion.compareVersion(majorVersion, minorVersion, build) <= 0;
    }

    public static boolean isCJKLocale() {
        return isCJKLocale(Locale.getDefault());
    }

    public static boolean isCJKLocale(Locale locale) {
        return locale.equals(Locale.CHINA) || locale.equals(Locale.CHINESE) || locale.equals(new Locale("zh", "HK")) || locale.equals(Locale.TAIWAN) || locale.equals(Locale.JAPAN) || locale.equals(Locale.JAPANESE) || locale.equals(Locale.KOREA) || locale.equals(Locale.KOREAN);
    }

    public static int getDisplayScale() {
        if (GraphicsEnvironment.isHeadless()) {
            return 1;
        } else {
            if (isJdk7u40Above()) {
                GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice device = graphicsEnvironment.getDefaultScreenDevice();

                try {
                    Field field = device.getClass().getDeclaredField("scale");
                    if (field != null) {
                        field.setAccessible(true);
                        Object scale = field.get(device);
                        if (scale instanceof Integer) {
                            return ((Integer) scale).intValue();
                        }
                    }
                } catch (Exception var4) {
                    ;
                }
            }

            return 1;
        }
    }

    static {
        String os = getProperty("os.name", "Windows XP");
        _isWindows = os.indexOf("Windows") != -1;

        try {
            String osVersion = getProperty("os.version", "5.0");
            Float version = Float.valueOf(osVersion);
            _isClassicWindows = (double) version.floatValue() <= 4.0D;
        } catch (NumberFormatException var3) {
            _isClassicWindows = false;
        }

        if (os.indexOf("Windows XP") != -1 || os.indexOf("Windows NT") != -1 || os.indexOf("Windows 2000") != -1) {
            _isWindowsNTor2000 = true;
        }

        if (os.indexOf("Windows XP") != -1) {
            _isWindowsXP = true;
        }

        if (os.indexOf("Windows Vista") != -1) {
            _isWindowsVista = true;
        }

        if (os.indexOf("Windows 7") != -1) {
            _isWindows7 = true;
        }

        if (os.indexOf("Windows 8") != -1) {
            _isWindows8 = true;
        }

        if (os.indexOf("Windows 2003") != -1) {
            _isWindows2003 = true;
            _isWindowsXP = true;
        }

        if (os.indexOf("Windows 95") != -1) {
            _isWindows95 = true;
        }

        if (os.indexOf("Windows 98") != -1) {
            _isWindows98 = true;
        }

        if (_isWindows) {
            _supportsTray = true;
        }

        _isSolaris = os.indexOf("Solaris") != -1 || os.indexOf("SunOS") != -1;
        _isBSD = os.endsWith("BSD");
        _isLinux = os.indexOf("Linux") != -1;
        if (os.startsWith("Mac OS")) {
            if (os.endsWith("X")) {
                _isMacOSX = true;
            } else {
                _isMacClassic = true;
            }
        }

    }

    public static class JavaVersion {
        private static Pattern SUN_JAVA_VERSION = Pattern.compile("(\\d+\\.\\d+)(\\.(\\d+))?(_([^-]+))?(.*)");
        private static Pattern SUN_JAVA_VERSION_SIMPLE = Pattern.compile("(\\d+\\.\\d+)(\\.(\\d+))?(.*)");
        private double _majorVersion;
        private int _minorVersion;
        private int _buildNumber;
        private String _patch;

        public JavaVersion(String version) {
            this._majorVersion = 1.4D;
            this._minorVersion = 0;
            this._buildNumber = 0;

            try {
                Matcher matcher = SUN_JAVA_VERSION.matcher(version);
                if (matcher.matches()) {
                    int groups = matcher.groupCount();
                    this._majorVersion = Double.parseDouble(matcher.group(1));
                    if (groups >= 3 && matcher.group(3) != null) {
                        this._minorVersion = Integer.parseInt(matcher.group(3));
                    }

                    if (groups >= 5 && matcher.group(5) != null) {
                        try {
                            this._buildNumber = Integer.parseInt(matcher.group(5));
                        } catch (NumberFormatException var6) {
                            this._patch = matcher.group(5);
                        }
                    }

                    if (groups >= 6 && matcher.group(6) != null) {
                        String s = matcher.group(6);
                        if (s != null && s.trim().length() > 0) {
                            this._patch = s;
                        }
                    }
                }
            } catch (NumberFormatException var7) {
                try {
                    Matcher matcher = SUN_JAVA_VERSION_SIMPLE.matcher(version);
                    if (matcher.matches()) {
                        int groups = matcher.groupCount();
                        this._majorVersion = Double.parseDouble(matcher.group(1));
                        if (groups >= 3 && matcher.group(3) != null) {
                            this._minorVersion = Integer.parseInt(matcher.group(3));
                        }
                    }
                } catch (NumberFormatException var5) {
                    System.err.println("Please check the installation of your JDK. The version number " + version + " is not right.");
                }
            }

        }

        public JavaVersion(double major, int minor, int build) {
            this._majorVersion = major;
            this._minorVersion = minor;
            this._buildNumber = build;
        }

        public int compareVersion(double major, int minor, int build) {
            double majorResult = this._majorVersion - major;
            if (majorResult != 0.0D) {
                return majorResult < 0.0D ? -1 : 1;
            } else {
                int result = this._minorVersion - minor;
                return result != 0 ? result : this._buildNumber - build;
            }
        }

        public double getMajorVersion() {
            return this._majorVersion;
        }

        public int getMinorVersion() {
            return this._minorVersion;
        }

        public int getBuildNumber() {
            return this._buildNumber;
        }

        public String getPatch() {
            return this._patch;
        }
    }

    public static String getProperty(String key, String defaultValue) {
        try {
            return System.getProperty(key, defaultValue);
        } catch (AccessControlException var3) {
            return defaultValue;
        }
    }
}



