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

package de.p2tools.p2Lib.dialogs;


import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class PDialogShowAgain extends PDialogExtra {

    private final Button btnOk = new Button("_Ok");
    private final BooleanProperty showAgain;
    private final String text;

    public PDialogShowAgain(Stage ownerForCenteringDialog, StringProperty conf,
                            String title, String text, BooleanProperty showAgain) {
        super(ownerForCenteringDialog, conf, title, true, false, DECO.NONE, true);
        this.showAgain = showAgain;
        this.text = text;

        if (!showAgain.getValue()) {
            //dann nicht mehr anzeigen
            return;
        }

        init(true);
    }

    @Override
    protected void make() {
        addOkButton(btnOk);
        btnOk.setOnAction(a -> close());

        CheckBox cbxShowAgain = new CheckBox();
        cbxShowAgain.selectedProperty().bindBidirectional(showAgain);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(new Label("Wieder anzeigen: "), cbxShowAgain);
        getHboxLeft().getChildren().add(hBox);

        final TextArea textArea = new TextArea(text);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        getVBoxCont().getChildren().add(textArea);
    }
}
