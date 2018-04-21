/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2Lib.guiTools;

import de.p2tools.p2Lib.dialog.PAlert;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

public class PButton {

    private static ImageView imageView = null;

    public static ImageView getImageView() {
        return imageView;
    }

    public static void setImageView(ImageView imageView) {
        PButton.imageView = imageView;
    }

    public Button helpButton(ImageView imageView, String header, String helpText) {
        final Button btnHelp = new Button("");
        btnHelp.setTooltip(new Tooltip("Hilfe anzeigen."));
        btnHelp.setGraphic(imageView);
        btnHelp.setOnAction(a -> PAlert.showHelpAlert(header, helpText));
        return btnHelp;
    }

    public Button helpButton(String header, String helpText) {
        final Button btnHelp = new Button("");
        btnHelp.setTooltip(new Tooltip("Hilfe anzeigen."));
        if (imageView == null) {
            btnHelp.setText("Hilfe");
        } else {
            btnHelp.setGraphic(imageView);
        }
        btnHelp.setOnAction(a -> PAlert.showHelpAlert(header, helpText));
        return btnHelp;
    }


}
