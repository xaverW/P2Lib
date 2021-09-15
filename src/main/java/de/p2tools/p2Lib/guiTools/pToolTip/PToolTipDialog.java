/*
 * P2tools Copyright (C) 2021 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2Lib.guiTools.pToolTip;

import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2Lib.guiTools.PButton;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class PToolTipDialog extends PDialogExtra {

    public static final Image ICON_BUTTON_NEXT = new Image(PToolTipDialog.class.getResourceAsStream("button-next.png"));
    public static final Image ICON_BUTTON_PREV = new Image(PToolTipDialog.class.getResourceAsStream("button-prev.png"));

    private final List<PToolTip> pToolTipList;
    private Button btnOk, btnPrev, btnNext;
    private CheckBox chkDontShow = new CheckBox("keine ToolTips anzeigen");
    private int actToolTip = 0;
    ImageView iv = new ImageView();
    Label lblText = new Label();
    HBox hBoxHyper = new HBox();
    StringProperty shownProp;
    BooleanProperty dontShow;

    public PToolTipDialog(Stage stage, List<PToolTip> pToolTipList, StringProperty shownProp, BooleanProperty dontShow) {
        super(stage, null, "Tool-Tip", true, true, DECO.NONE);
        this.pToolTipList = pToolTipList;
        this.shownProp = shownProp;
        this.dontShow = dontShow;

        init(true);
    }

    @Override
    public void make() {
        PToolTipFactory.setToolTipsFromShownString(pToolTipList, shownProp.getValueSafe());
        initTop();
        initButton();
    }

    private void initTop() {
        iv.setSmooth(true);
        setFirstToolTip();

        VBox vBoxL = new VBox(0);
        VBox vBoxR = new VBox(0);
        vBoxL.getChildren().add(iv);
        vBoxR.getChildren().add(lblText);

        hBoxHyper.setPadding(new Insets(0));
        vBoxR.getChildren().add(hBoxHyper);

        HBox hBox = new HBox(20);
        HBox.setHgrow(vBoxR, Priority.ALWAYS);
        hBox.getChildren().addAll(vBoxL, vBoxR);

        VBox.setVgrow(hBox, Priority.ALWAYS);
        this.getvBoxCont().getChildren().add(hBox);
    }

    private void initButton() {
        chkDontShow.selectedProperty().bindBidirectional(dontShow);

        btnOk = new Button("_Ok");
        btnOk.setOnAction(a -> {
            super.close();
        });

        btnNext = PButton.getButton(new ImageView(ICON_BUTTON_NEXT), "nächste Seite");
        btnNext.setOnAction(event -> {
            selectActToolTip(true);
        });
        btnPrev = PButton.getButton(new ImageView(ICON_BUTTON_PREV), "vorherige Seite");
        btnPrev.setOnAction(event -> {
            selectActToolTip(false);
        });

        btnOk.getStyleClass().add("btnStartDialog");
        btnNext.getStyleClass().add("btnStartDialog");
        btnPrev.getStyleClass().add("btnStartDialog");

        addOkButton(btnOk);
        ButtonBar.setButtonData(btnPrev, ButtonBar.ButtonData.BACK_PREVIOUS);
        ButtonBar.setButtonData(btnNext, ButtonBar.ButtonData.NEXT_FORWARD);
        addAnyButton(btnNext);
        addAnyButton(btnPrev);
        getButtonBar().setButtonOrder("BX+CO");

        getHBoxOverButtons().getChildren().add(chkDontShow);
    }

    private void selectActToolTip(boolean next) {
        if (next) {
            //next
            if (actToolTip < pToolTipList.size() - 1) {
                ++actToolTip;
            } else {
                actToolTip = 0;
            }

        } else {
            //!next
            if ((actToolTip > 0)) {
                --actToolTip;
            } else {
                actToolTip = pToolTipList.size() - 1;
            }
        }
        setToolTip();
    }

    private void setToolTip() {
        pToolTipList.get(actToolTip).setWasShown(true);
        Image im = new Image(pToolTipList.get(actToolTip).getImage(), 400, 400, true, true);
        iv.setImage(im);
        lblText.setText(pToolTipList.get(actToolTip).getText());
        hBoxHyper.getChildren().clear();
        if (pToolTipList.get(actToolTip).getHyperlinkWeb() != null) {
            hBoxHyper.getChildren().add(pToolTipList.get(actToolTip).getHyperlinkWeb());
        }

        super.getStage().setTitle("ToolTip: " + (actToolTip + 1));
        shownProp.setValue(PToolTipFactory.getToolTipShownString(pToolTipList));
    }

    private void setFirstToolTip() {
        //ersten noch nicht angezeigten suchen, wenn alle schon gesehen, wieder am Anfang beginnen
        for (int i = 0; i < pToolTipList.size(); ++i) {
            if (!pToolTipList.get(i).isWasShown()) {
                actToolTip = i;
                break;
            }
        }
        setToolTip();
    }
}
