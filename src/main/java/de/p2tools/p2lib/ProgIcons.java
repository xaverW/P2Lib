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


import de.p2tools.p2lib.icons.GetIcon;
import javafx.scene.image.ImageView;

public class ProgIcons {
    public enum Icons {
        ICON_BUTTON_CLEAR_FILTER("button-clear-filter.png", 21, 21),
        IMAGE_HELP("button-help.png"),
        IMAGE_FILE_OPEN("button-file-open.png"),
        IMAGE_STOP("button-stop.png");

        private String path;
        private int w = 0;
        private int h = 0;

        Icons(String path, int w, int h) {
            this.path = path;
            this.w = w;
            this.h = h;
        }

        Icons(String path) {
            this.path = path;
        }

        public ImageView getImageView() {
            return GetIcon.getImageView(path, w, h);
        }
    }
}
