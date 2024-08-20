package de.p2tools.p2lib;/*
 * P2Tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
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


import de.p2tools.p2lib.icons.P2Image;

public class P2ProgIcons {

    public static String ICON_PATH_LONG = "de/p2tools/p2lib/p2icons/";

    public final static P2Image ICON_BUTTON_CLEAR_FILTER = new P2Image(ICON_PATH_LONG, "button-clear-filter.png", 21, 21);
    public final static P2Image IMAGE_HELP = new P2Image(ICON_PATH_LONG, "button-help.png");
    public final static P2Image IMAGE_FILE_OPEN = new P2Image(ICON_PATH_LONG, "button-file-open.png");
    public final static P2Image IMAGE_STOP = new P2Image(ICON_PATH_LONG, "button-stop.png");
    public final static P2Image ICON_BUTTON_NEXT = new P2Image(ICON_PATH_LONG, "button-next.png");
    public final static P2Image ICON_BUTTON_PREV = new P2Image(ICON_PATH_LONG, "button-prev.png");
    public final static P2Image ICON_BUTTON_CLOSE = new P2Image(ICON_PATH_LONG, "button-close.png");
    public final static P2Image ICON_BUTTON_RIP = new P2Image(ICON_PATH_LONG, "button-rip.png");

    public static P2Image P2_STAGE_ICON = new P2Image(ICON_PATH_LONG, "P2_Stage_Icon.png");
    public static P2Image P2_ABOUT_ICON = new P2Image(ICON_PATH_LONG, "P2_About_Icon.png");

    public static String ICON_PATH_NOTIFICATION_LONG = "de/p2tools/p2lib/guitools/pnotification/";
    public static final P2Image INFO_ICON = new P2Image(ICON_PATH_NOTIFICATION_LONG, "info.png", 25, 24);
    public static final P2Image WARNING_ICON = new P2Image(ICON_PATH_NOTIFICATION_LONG, "warning.png", 25, 24);
    public static final P2Image SUCCESS_ICON = new P2Image(ICON_PATH_NOTIFICATION_LONG, "success.png", 25, 24);
    public static final P2Image ERROR_ICON = new P2Image(ICON_PATH_NOTIFICATION_LONG, "error.png", 25, 24);
}
