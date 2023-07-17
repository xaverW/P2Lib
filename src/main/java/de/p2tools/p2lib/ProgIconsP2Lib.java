package de.p2tools.p2lib;/*
 * P2Tools Copyright (C) 2022 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de/
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


import de.p2tools.p2lib.icons.P2Icon;

import java.util.ArrayList;
import java.util.List;

public class ProgIconsP2Lib {

    public static String ICON_PATH = "icons/";
    public static String ICON_PATH_LONG = "de/p2tools/p2lib/icons/";

    private static final List<P2IconP2Lib> iconList = new ArrayList<>();
    public static P2IconP2Lib ICON_BUTTON_CLEAR_FILTER = new P2IconP2Lib(ICON_PATH_LONG, ICON_PATH, "button-clear-filter.png", 21, 21);
    public static P2IconP2Lib IMAGE_HELP = new P2IconP2Lib(ICON_PATH_LONG, ICON_PATH, "button-help.png");
    public static P2IconP2Lib IMAGE_FILE_OPEN = new P2IconP2Lib(ICON_PATH_LONG, ICON_PATH, "button-file-open.png");
    public static P2IconP2Lib IMAGE_STOP = new P2IconP2Lib(ICON_PATH_LONG, ICON_PATH, "button-stop.png");
    public static P2IconP2Lib P2_ICON_16 = new P2IconP2Lib(ICON_PATH_LONG, ICON_PATH, "P2_16.png");
    public static P2IconP2Lib P2_ICON_24 = new P2IconP2Lib(ICON_PATH_LONG, ICON_PATH, "P2_24.png");
    public static P2IconP2Lib P2_ICON_32 = new P2IconP2Lib(ICON_PATH_LONG, ICON_PATH, "P2_32.png");
    public static P2IconP2Lib P2_WINDOW_ICON = new P2IconP2Lib(ICON_PATH_LONG, ICON_PATH, "P2_32.png", 58, 58);


    public static final P2IconP2Lib ICON_BUTTON_NEXT = new P2IconP2Lib(ICON_PATH_LONG, ICON_PATH, "button-next.png");
    public static final P2IconP2Lib ICON_BUTTON_PREV = new P2IconP2Lib(ICON_PATH_LONG, ICON_PATH, "button-prev.png");


    public static String ICON_PATH_NOTIFICATION = "guitools/pnotification/";
    public static String ICON_PATH_NOTIFICATION_LONG = "de/p2tools/p2lib/guitools/pnotification/";

    public static final P2IconP2Lib INFO_ICON = new P2IconP2Lib(ICON_PATH_NOTIFICATION, ICON_PATH_NOTIFICATION_LONG, "info.png", 25, 24);
    public static final P2IconP2Lib WARNING_ICON = new P2IconP2Lib(ICON_PATH_NOTIFICATION, ICON_PATH_NOTIFICATION_LONG, "warning.png", 25, 24);
    public static final P2IconP2Lib SUCCESS_ICON = new P2IconP2Lib(ICON_PATH_NOTIFICATION, ICON_PATH_NOTIFICATION_LONG, "success.png", 25, 24);
    public static final P2IconP2Lib ERROR_ICON = new P2IconP2Lib(ICON_PATH_NOTIFICATION, ICON_PATH_NOTIFICATION_LONG, "error.png", 25, 24);


    public static void initIcons() {
        iconList.forEach(p -> {
            String url = p.genUrl(P2LibInit.class, P2LibConst.class, ProgIconsP2Lib.class);
            if (url.isEmpty()) {
                // dann wurde keine gefunden
                System.out.println("ProgIconsP2Lib: keine URL, icon: " + p.getPathFileNameDark() + " - " + p.getFileName());
            }
        });
    }

    public static class P2IconP2Lib extends P2Icon {
        public P2IconP2Lib(String longPath, String path, String fileName) {
            super(longPath, path, fileName, 0, 0);
            iconList.add(this);
        }

        public P2IconP2Lib(String longPath, String path, String fileName, int w, int h) {
            super(longPath, path, fileName, w, h);
            iconList.add(this);
        }
    }
}