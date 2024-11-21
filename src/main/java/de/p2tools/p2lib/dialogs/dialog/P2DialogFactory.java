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


package de.p2tools.p2lib.dialogs.dialog;

import de.p2tools.p2lib.guitools.P2GuiSize;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class P2DialogFactory {
    private P2DialogFactory() {
    }

    static void addSizeListener(Stage stage, StringProperty sizeConfiguration) {
        // Größe der Stage überwachen und setzen
        if (sizeConfiguration != null) {
            stage.widthProperty().addListener((v, o, n) -> {
                if (stage.isShowing()) {
                    P2GuiSize.getSizeStage(sizeConfiguration, stage);
                }
            });
            stage.heightProperty().addListener((v, o, n) -> {
                if (stage.isShowing()) {
                    P2GuiSize.getSizeStage(sizeConfiguration, stage);
                }
            });
            stage.xProperty().addListener((v, o, n) -> {
                if (stage.isShowing()) {
                    P2GuiSize.getSizeStage(sizeConfiguration, stage);
                }
            });
            stage.yProperty().addListener((v, o, n) -> {
                if (stage.isShowing()) {
                    P2GuiSize.getSizeStage(sizeConfiguration, stage);
                }
            });
        }
    }

    static void addSizeListener(Stage stage, Scene scene, StringProperty sizeConfiguration) {
        // Größe der Scene überwachen (wenn stage zu sehen ist) und setzen
        if (sizeConfiguration != null) {
            scene.widthProperty().addListener((v, o, n) -> {
                if (stage.isShowing()) {
                    P2GuiSize.getSizeScene(sizeConfiguration, stage, scene);
                }
            });
            scene.heightProperty().addListener((v, o, n) -> {
                if (stage.isShowing()) {
                    P2GuiSize.getSizeScene(sizeConfiguration, stage, scene);
                }
            });
            scene.xProperty().addListener((v, o, n) -> {
                if (stage.isShowing()) {
                    P2GuiSize.getSizeScene(sizeConfiguration, stage, scene);
                }
            });
            scene.yProperty().addListener((v, o, n) -> {
                if (stage.isShowing()) {
                    P2GuiSize.getSizeScene(sizeConfiguration, stage, scene);
                }
            });
        }
    }
}
