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
import de.p2tools.p2lib.guitools.pmask.P2MaskerPane;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class P2DialogOnly extends P2Dialog {
    private static final ArrayList<P2Dialog> dialogList = new ArrayList<>();

    private final VBox vBoxCompleteDialog = new VBox(); // ist der gesamte Dialog
    private P2MaskerPane maskerPane = null;
    private boolean masker = false;

    public P2DialogOnly() {
        super(P2LibConst.actStage, null, "", true, true);
        initDialog();
    }

    public P2DialogOnly(StringProperty conf, String title) {
        // ist nur ein einfacher Dialog, zentral über dem Hauptfenster
        super(P2LibConst.actStage, conf, title, true, true);
        initDialog();
    }

    public P2DialogOnly(Stage ownerForCenteringDialog, StringProperty conf, String title) {
        super(ownerForCenteringDialog, conf, title, true, true);
        initDialog();
    }

    public P2DialogOnly(Stage ownerForCenteringDialog, StringProperty conf,
                        String title, boolean modal, boolean setOnlySize) {
        super(ownerForCenteringDialog, conf, title, modal, setOnlySize);
        initDialog();
    }

    public P2DialogOnly(Stage ownerForCenteringDialog, StringProperty conf,
                        String title, boolean modal, boolean setOnlySize, boolean masker) {
        super(ownerForCenteringDialog, conf, title, modal, setOnlySize);
        this.masker = masker;
        maskerPane = new P2MaskerPane();
        initDialog();
    }

    private static synchronized void addDialog(P2Dialog p2Dialog) {
        boolean found = false;
        for (P2Dialog dialog : dialogList) {
            if (dialog.equals(p2Dialog)) {
                found = true;
            }
        }
        if (!found) {
            dialogList.add(p2Dialog);
        }
    }

    private static synchronized void removeDialog(P2Dialog p2Dialog) {
        dialogList.remove(p2Dialog);
    }

    public static void closeAllDialog() {
        dialogList.stream().forEach(p2Dialog -> {
            Platform.runLater(() -> {
                p2Dialog.hide();
            });
        });
    }

    public static void showAllDialog() {
        dialogList.stream().forEach(p2Dialog -> {
            Platform.runLater(() -> {
                p2Dialog.showDialog();
            });
        });
    }

    @Override
    public void hide() {
        // close/hide are the same
        super.close();
    }

    @Override
    public void close() {
        //bei wiederkehrenden Dialogen: die pos/size merken
        removeDialog(this);
        super.close();
    }

    @Override
    public void init(boolean show) {
        if (show) {
            addDialog(this);
        }
        super.init(show);
    }

    @Override
    public void showDialog() {
        addDialog(this);
        super.showDialog();
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
        initBefore();
    }

    private void initBefore() {
        if (masker) {
            StackPane.setAlignment(maskerPane, Pos.CENTER);
            maskerPane.setPadding(new Insets(4, 1, 1, 1));

            final StackPane stackPane = new StackPane(); // ist der gesamte Dialog
            super.setPane(stackPane);
            stackPane.getChildren().addAll(vBoxCompleteDialog, maskerPane);

        } else {
            super.setPane(vBoxCompleteDialog);
        }

        vBoxCompleteDialog.setSpacing(10);
        vBoxCompleteDialog.setPadding(new Insets(10));
    }
}
