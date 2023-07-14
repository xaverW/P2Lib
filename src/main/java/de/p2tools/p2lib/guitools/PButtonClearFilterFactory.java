package de.p2tools.p2lib.guitools;/*
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


import de.p2tools.p2lib.ProgIconsP2Lib;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

public class PButtonClearFilterFactory {
    private PButtonClearFilterFactory() {
    }

    public static Button getPButtonClearFilter() {
        Button button = new Button();
        button.setGraphic(ProgIconsP2Lib.ICON_BUTTON_CLEAR_FILTER.getImageView());
        button.getStyleClass().add("btnClearFilter");
        button.setTooltip(new Tooltip("Textfilter löschen, ein zweiter Klick löscht alle Filter"));
        return button;
    }

    public static Button getPButtonClear() {
        Button button = new Button();
        button.setGraphic(ProgIconsP2Lib.ICON_BUTTON_CLEAR_FILTER.getImageView());
        button.getStyleClass().add("btnClearFilter");
        button.setTooltip(new Tooltip("Filter löschen, ein Klick löscht alle Filter"));
        return button;
    }

    public static Button getPButtonClearSmall() {
        Button button = new Button();
        button.setGraphic(ProgIconsP2Lib.ICON_BUTTON_CLEAR_FILTER.getImageView());
        button.getStyleClass().add("btnClearFilterSmall");
        button.setTooltip(new Tooltip("Filter löschen, ein Klick löscht alle Filter"));
        return button;
    }
}
