/*
 * P2Tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de
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

package de.p2tools.p2lib.dialogs;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2lib.guitools.P2ClipBoardContext;
import de.p2tools.p2lib.guitools.P2ColumnConstraints;
import de.p2tools.p2lib.guitools.P2Hyperlink;
import de.p2tools.p2lib.tools.ProgramToolsFactory;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

public class WhatsNewDialog extends PDialogExtra {

    private final Button btnOk = new Button("_Ok");

    private final String URL_WEBSITE;
    private final StringProperty urlOpenProg;
    private final String progName;
    private final Color PROG_COLOR_MARK;
    private final Color PROG_COLOR;
    private final ArrayList<WhatsNewInfo> list;

    public WhatsNewDialog(Stage stage, String PROGRAM_NAME, String URL_WEBSITE, StringProperty SYSTEM_PROG_OPEN_URL,
                          boolean darkMode, ArrayList<WhatsNewInfo> list) {
        super(stage, null, "Was gibt's neues", true, false, DECO.NO_BORDER, true);

        this.progName = PROGRAM_NAME;
        this.URL_WEBSITE = URL_WEBSITE;
        this.urlOpenProg = SYSTEM_PROG_OPEN_URL;
        this.list = list;

        if (darkMode) {
            this.PROG_COLOR_MARK = Color.rgb(244, 244, 255);
            this.PROG_COLOR = Color.rgb(200, 200, 209);
        } else {
            this.PROG_COLOR_MARK = Color.rgb(0, 0, 75);
            this.PROG_COLOR = Color.rgb(0, 0, 75);
        }

        addOkButton(btnOk);
        init(true);
    }

    @Override
    public void make() {
        getVBoxCont().setSpacing(10);
        getVBoxCont().setPadding(new Insets(0));
        btnOk.setOnAction(a -> close());
        makeGridProgram();
        makeGridWhatNew();
        makeGridBottom();
    }

    private void makeGridProgram() {
        GridPane gridPane = getGridPane();
        getVBoxCont().getChildren().add(gridPane);
        gridPane.getStyleClass().add("dialog-whats-new");

        int row = 0;
        Text text1 = new Text(progName);
        text1.setFont(Font.font(null, FontWeight.BOLD, -1));
        text1.setFill(PROG_COLOR_MARK);
        gridPane.add(text1, 0, row);

        Text text2 = new Text("Version: " + ProgramToolsFactory.getProgVersion());
        text2.setFill(PROG_COLOR);
        gridPane.add(text2, 1, row);

        Text text3 = new Text("[ Build: " + ProgramToolsFactory.getBuild() + " vom " +
                ProgramToolsFactory.getCompileDate() + " ]");
        text3.setFill(PROG_COLOR);
        gridPane.add(text3, 0, ++row, 2, 1);

        HBox.setHgrow(gridPane, Priority.ALWAYS);

        // Menü
        P2ClipBoardContext.addMenu("Infos kopieren",
                text1.getText() + "\n" /*+ text2.getText()*/ + "\n" + text3.getText(), gridPane);
    }

    private void makeGridWhatNew() {
        GridPane gridPane = getGridPane();
        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcComputedSizeAndHgrow());

        final ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setContent(gridPane);
        scrollPane.setPrefHeight(400);
        scrollPane.getStyleClass().add("dialog-whats-new-list");

        getVBoxCont().getChildren().add(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        int row = -1;
        for (WhatsNewInfo whatsNewInfo : list) {
            Text text = new Text(whatsNewInfo.getHeader());
            text.setFont(Font.font(null, FontWeight.BOLD, -1));
            text.setFill(PROG_COLOR_MARK);
            gridPane.add(text, 0, ++row);

            if (!whatsNewInfo.getImage().isEmpty()) {
                Image im = new Image(whatsNewInfo.getImage(), 350, 350, true, true);
                ImageView iv = new ImageView(im);
                gridPane.add(iv, 0, ++row);
                GridPane.setHalignment(iv, HPos.CENTER);
            }

            TextArea ta = new TextArea(whatsNewInfo.getText());
            ta.setEditable(false);
            ta.setWrapText(true);
            ta.setMinHeight(150);
            ta.setMaxHeight(150);
            gridPane.add(ta, 0, ++row);

            gridPane.add(new Label(""), 0, ++row);
            gridPane.add(new Label(""), 0, ++row);
        }
    }

    private void makeGridBottom() {
        GridPane gridPane = getGridPane();
        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcComputedSizeAndHgrow());
        getVBoxCont().getChildren().add(gridPane);
        gridPane.getStyleClass().add("dialog-whats-new");

        int row = 0;
        P2Hyperlink hyperlinkWeb = new P2Hyperlink(URL_WEBSITE, urlOpenProg);
        P2Hyperlink hyperlinkDonate = new P2Hyperlink(P2LibConst.URL_WEBSITE_DONATE, urlOpenProg);

        final Text text = new Text("Autor:");
        text.setFont(Font.font(null, FontWeight.BOLD, -1));
        text.setFill(PROG_COLOR_MARK);

        final Text textM = new Text(P2LibConst.MAIL_XAVER);
        textM.setFont(new Font(14));
        textM.setFill(PROG_COLOR);
        P2ClipBoardContext.addMenu("Mail kopieren", P2LibConst.MAIL_XAVER, gridPane);


        gridPane.add(text, 0, row);
        gridPane.add(new Label("Xaver W.   "), 1, row);
        gridPane.add(textM, 2, row);

        Label ta = new Label("Ideen oder Vorschläge zum Programm immer gerne per Mail.");
        gridPane.add(ta, 1, ++row, 2, 1);

        gridPane.add(new Label("Website:"), 0, ++row);
        gridPane.add(hyperlinkWeb, 1, row, 2, 1);

        gridPane.add(new Label("Spende:"), 0, ++row);
        gridPane.add(hyperlinkDonate, 1, row, 2, 1);
    }

    private GridPane getGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(3);
        gridPane.setPadding(new Insets(5));
        return gridPane;
    }
}
