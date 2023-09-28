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

import de.p2tools.p2lib.guitools.PGuiSize;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;

public class PDialogFactory {
    private PDialogFactory() {
    }

    static void addSizeListener(Stage stage, StringProperty sizeConfiguration) {
        if (sizeConfiguration != null) {
            stage.widthProperty().addListener((v, o, n) -> {
                if (stage.isShowing()) {
                    PGuiSize.getSizeStage(sizeConfiguration, stage);
                }
            });
            stage.heightProperty().addListener((v, o, n) -> {
                if (stage.isShowing()) {
                    PGuiSize.getSizeStage(sizeConfiguration, stage);
                }
            });
            stage.xProperty().addListener((v, o, n) -> {
                if (stage.isShowing()) {
                    PGuiSize.getSizeStage(sizeConfiguration, stage);
                }
            });
            stage.yProperty().addListener((v, o, n) -> {
                if (stage.isShowing()) {
                    PGuiSize.getSizeStage(sizeConfiguration, stage);
                }
            });
        }
    }

//    public static void showDialog(Stage stage, StringProperty sizeConfiguration) {
//        showDialog(stage, sizeConfiguration, /*0, 0,*/
//                null, false, false);
//    }

//    public static void showDialog(Stage stage, StringProperty sizeConfiguration, /*double stageHeight, double stageWidth,*/
//                                  Stage ownerForCenteringDialog, boolean modal, boolean onlySize) {
//
//        //Größe setzen
//      /*  if (stageHeight > 0 && stageWidth > 0) {
//            //die gemerkte Größe wieder setzen
//            stage.setHeight(stageHeight);
//            stage.setWidth(stageWidth);
//
//        } else*/
//
////        if (sizeConfiguration != null && !sizeConfiguration.getValueSafe().isEmpty()) {
////            PGuiSize.setSize(sizeConfiguration, stage);
////
////        } else {
////            //dann einpassen
////            stage.sizeToScene();
////        }
////
////        //Pos setzen
////        if (onlySize || sizeConfiguration == null || !PGuiSize.setPos(sizeConfiguration, stage, ownerForCenteringDialog)) {
////            if (ownerForCenteringDialog == null) {
////                stage.centerOnScreen();
////            } else {
////                setInFrontOfPrimaryStage(stage, ownerForCenteringDialog);
////            }
////        }
//
//        stage.requestFocus();
//        stage.toFront();
//        if (!stage.isShowing()) {
//            if (modal) {
//                stage.showAndWait();
//            } else {
//                stage.show();
//            }
//        }
//    }


}
