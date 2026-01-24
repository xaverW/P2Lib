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

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.guitools.P2SmallGuiFactory;
import de.p2tools.p2lib.guitools.pmask.P2MaskerPane;
import de.p2tools.p2lib.icons.P2Image;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class P2DialogOnly extends P2Dialog {
    private final VBox vBoxDialog = new VBox(); // ist der gesamte Dialog
    private final VBox vBoxCompleteDialog = new VBox(); // ist der nutzbare Dialog
    private P2MaskerPane maskerPane = null;
    private boolean masker = false;

    public P2DialogOnly() {
        super(P2LibConst.primaryStage, null, "", true);
        initDialog();
    }

    public P2DialogOnly(StringProperty conf, String title) {
        // ist nur ein einfacher Dialog, zentral über dem Hauptfenster
        super(P2LibConst.primaryStage, conf, title, true);
        initDialog();
    }

    public P2DialogOnly(Stage ownerForCenteringDialog, StringProperty conf, String title) {
        super(ownerForCenteringDialog, conf, title, true);
        initDialog();
    }

    public P2DialogOnly(Stage ownerForCenteringDialog, StringProperty conf,
                        String title, boolean modal, boolean setSize, boolean setPos) {
        super(ownerForCenteringDialog, conf, title, modal, setSize, setPos, "");
        initDialog();
    }

    public P2DialogOnly(Stage ownerForCenteringDialog, StringProperty conf,
                        String title, boolean modal, boolean setSize, boolean setPos, boolean masker) {
        super(ownerForCenteringDialog, conf, title, modal, setSize, setPos, "");
        this.masker = masker;
        maskerPane = new P2MaskerPane();
        initDialog();
    }

    public void init(boolean start) {
        super.init(false);

        P2SmallGuiFactory.addBorderListener(getStage());
        getStage().initStyle(StageStyle.TRANSPARENT);
        vBoxDialog.getStyleClass().add("smallGui");

        if (start) {
            showDialog();
        }
    }

    public VBox getVBoxCompleteDialog() {
        return vBoxCompleteDialog;
    }

    public void setMaskerVisible(boolean visible) {
        if (masker) {
            //ist nur dann, enthalten
            maskerPane.setMaskerVisible(visible, false, false);
        }
    }

    public P2MaskerPane getMaskerPane() {
        return maskerPane;
    }

    private void initDialog() {
        if (masker) {
            StackPane.setAlignment(maskerPane, Pos.CENTER);
            maskerPane.setPadding(new Insets(4, 1, 1, 1));

            final StackPane stackPane = new StackPane(); // ist der gesamte Dialog
            super.setPane(stackPane);
            stackPane.getChildren().addAll(vBoxDialog, maskerPane);

        } else {
            super.setPane(vBoxDialog);
        }

        vBoxDialog.setPadding(new Insets(20));
        vBoxDialog.getChildren().add(vBoxCompleteDialog);
        vBoxCompleteDialog.setSpacing(P2LibConst.SPACING_VBOX);
        VBox.setVgrow(vBoxCompleteDialog, Priority.ALWAYS);

        P2LibConst.themeChanged.addListener((u, o, n) -> {
//            super.updateCss();
            P2Image.getAllNodes(getStage().getScene().getRoot());
        });
    }
}
