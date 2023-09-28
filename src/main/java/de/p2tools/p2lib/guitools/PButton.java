/*
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


package de.p2tools.p2lib.guitools;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.ProgIconsP2Lib;
import de.p2tools.p2lib.alert.PAlert;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class PButton {

    private static Image hlpImage = null;

    private PButton() {
    }

    public static Image getHlpImage() {
        return hlpImage;
    }

    public static void setHlpImage(Image hlpImage) {
        PButton.hlpImage = hlpImage;
    }

    public static Button helpButton(ImageView imageView, String header, String helpText) {
        return helpButton(P2LibConst.primaryStage, imageView, header, helpText);
    }

    public static Button helpButton(Stage stage, ImageView imageView, String header, String helpText) {
        final Button btnHelp = new Button("");
        btnHelp.setTooltip(new Tooltip("Hilfe anzeigen"));
        btnHelp.setGraphic(imageView);
        btnHelp.setOnAction(a -> PAlert.showHelpAlert(stage, header, helpText));
        return btnHelp;
    }

    public static Button helpButton(String header, String helpText) {
        return helpButton(P2LibConst.primaryStage, header, helpText);
    }

    public static Button helpButton(Stage stage, String header, String helpText) {
        final Button btnHelp = new Button("");
        btnHelp.setTooltip(new Tooltip("Hilfe anzeigen"));
        if (hlpImage == null) {
            btnHelp.setGraphic(ProgIconsP2Lib.IMAGE_HELP.getImageView()); //neues ImageView!
        } else {
            btnHelp.setGraphic(new ImageView(hlpImage)); //neues ImageView!
        }


        btnHelp.setOnAction(a -> PAlert.showHelpAlert(stage, header, helpText));
        return btnHelp;
    }

    public static Button helpButton(ObjectProperty<Stage> stageProp, String header, String helpText) {
        final Button btnHelp = new Button("");
        btnHelp.setTooltip(new Tooltip("Hilfe anzeigen"));
        if (hlpImage == null) {
            btnHelp.setGraphic(ProgIconsP2Lib.IMAGE_HELP.getImageView()); //neues ImageView!
        } else {
            btnHelp.setGraphic(new ImageView(hlpImage)); //neues ImageView!
        }

        btnHelp.setOnAction(a -> PAlert.showHelpAlert(stageProp.getValue(), header, helpText));
        return btnHelp;
    }

    public static Button getButton(ImageView imageView, String helpText) {
        final Button button = new Button("");
        button.setTooltip(new Tooltip(helpText));
        button.setGraphic(imageView);
        return button;
    }

}
