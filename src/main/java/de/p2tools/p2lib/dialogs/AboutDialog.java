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
import de.p2tools.p2lib.dialogs.dialog.P2DialogExtra;
import de.p2tools.p2lib.guitools.P2ClipBoardContext;
import de.p2tools.p2lib.guitools.P2ColumnConstraints;
import de.p2tools.p2lib.guitools.P2Hyperlink;
import de.p2tools.p2lib.tools.P2ToolsFactory;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public abstract class AboutDialog extends P2DialogExtra {

    private final Button btnOk = new Button("_Ok");
    private final Button btnCheck = new Button("_Programmupdate prüfen");

    private final Color PROG_COLOR_MARK;
    private final Color PROG_COLOR;
    private final String progName;
    private final String URL_WEBSITE;
    private final String URL_WEBSITE_HELP;
    private final ImageView imageView; //ist nur für Fehlermeldungen
    private final StringProperty urlOpenProg;
    private final String[] listName;
    private final String[] listValue;


    public AboutDialog(Stage stage, String progName, String URL_WEBSITE, String URL_WEBSITE_HELP,
                       String imgPath, StringProperty urlOpenProg,
                       boolean dark, String[] listName, String[] listValue, boolean masker) {
        super(stage, null, "Über das Programm", true, false, DECO.BORDER_SMALL, masker);

        this.progName = progName;
        this.URL_WEBSITE = URL_WEBSITE;
        this.URL_WEBSITE_HELP = URL_WEBSITE_HELP;
        this.urlOpenProg = urlOpenProg;

        ImageView imageView = new ImageView();
        imageView.setSmooth(true);
        imageView.setCache(true);
        imageView.setImage(new Image(imgPath, 128, 128, false, true));
        this.imageView = imageView;

        if (dark) {
            this.PROG_COLOR_MARK = Color.rgb(244, 244, 255);
            this.PROG_COLOR = Color.rgb(200, 200, 209);
        } else {
            this.PROG_COLOR_MARK = Color.rgb(0, 0, 75);
            this.PROG_COLOR = Color.rgb(0, 0, 75);
        }

        this.listName = listName;
        this.listValue = listValue;

        addOkButton(btnOk);
        getHboxLeft().getChildren().add(btnCheck);
        init(false);
    }


    @Override
    public void make() {
        makeGrid();
    }

    public void runCheckButton() {
    }

    private void makeGrid() {
        getVBoxCont().setSpacing(5);

        btnOk.setOnAction(a -> close());
        btnCheck.setOnAction(a -> runCheckButton());

        GridPane gridPane = getGridPane();
        int row = 0;

        HBox hBox = new HBox();
        hBox.getChildren().add(gridPane);
        hBox.setAlignment(Pos.CENTER);
        hBox.getStyleClass().add("dialog-about");
        getVBoxCont().getChildren().add(hBox);

        ImageView iv = new ImageView();
        Image im = getImage();
        iv.setSmooth(true);
        iv.setCache(true);
        iv.setImage(im);
        gridPane.add(iv, 0, row, 1, 3);

        // top
        Text text1 = new Text(progName);
        text1.setFont(Font.font(null, FontWeight.BOLD, 40));
        text1.setFill(PROG_COLOR_MARK);
        gridPane.add(text1, 1, row);
        GridPane.setValignment(text1, VPos.TOP);
        GridPane.setHalignment(text1, HPos.CENTER);

        Text text2 = new Text(P2LibConst.LINE_SEPARATOR + "Version: " + P2ToolsFactory.getProgVersion());
        text2.setFont(new Font(18));
        text2.setFill(PROG_COLOR);
        gridPane.add(text2, 1, ++row);
        GridPane.setHalignment(text2, HPos.CENTER);

        Text text3 = new Text("[ Build: " + P2ToolsFactory.getBuild() + " vom " +
                P2ToolsFactory.getCompileDate() + " ]");
        text3.setFont(new Font(15));
        text3.setFill(PROG_COLOR);
        gridPane.add(text3, 1, ++row);
        GridPane.setValignment(text3, VPos.BOTTOM);
        GridPane.setHalignment(text3, HPos.CENTER);

        HBox.setHgrow(gridPane, Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcComputedSizeAndHgrow());
        // Menü
        P2ClipBoardContext.addMenu("Infos kopieren",
                text1.getText() + "\n" + text2.getText() + "\n" + text3.getText(), gridPane);

        //=======================
        gridPane = getGridPane();
        row = 0;
        int c = 0;

        Text text = new Text("Autor");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));
        text.setFill(PROG_COLOR_MARK);
        gridPane.add(text, c, row, 2, 1);

        text = new Text("Xaver W. (xaverW)");
        text.setFont(new Font(15));
        text.setFill(PROG_COLOR);
        gridPane.add(text, c, ++row, 2, 1);

        final Text textM = new Text(P2LibConst.MAIL_XAVER);
        textM.setFont(new Font(14));
        textM.setFill(PROG_COLOR);
        gridPane.add(textM, c, ++row, 2, 1);
        P2ClipBoardContext.addMenu("Mail kopieren", P2LibConst.MAIL_XAVER, gridPane);

        //====================
        // Pfade
        StringBuilder txtContext = new StringBuilder();
        gridPane = getGridPane();
        row = 0;

        text = new Text("Programm Informationen");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));
        text.setFill(PROG_COLOR_MARK);
        txtContext.append(text.getText());

        gridPane.add(text, c, row, 2, 1);
        P2Hyperlink hyperlinkWeb = new P2Hyperlink(URL_WEBSITE,
                urlOpenProg, imageView);
        P2Hyperlink hyperlinkHelp = new P2Hyperlink(URL_WEBSITE_HELP,
                urlOpenProg, imageView);
        P2Hyperlink hyperlinkForum = new P2Hyperlink(P2LibConst.URL_WEBSITE_FORUM,
                urlOpenProg, imageView);
        P2Hyperlink hyperlinkDonate = new P2Hyperlink(P2LibConst.URL_WEBSITE_DONATE,
                urlOpenProg, imageView);

        txtContext.append("\n").append("Website: ").append(hyperlinkWeb.getText()).append("\n")
                .append("Anleitung: ").append(hyperlinkHelp.getText()).append("\n")
                .append("Spende: ").append(hyperlinkDonate.getText()).append("\n")
                .append("Forum: ").append(hyperlinkForum.getText());

        text = new Text("Website:");
        text.setFont(new Font(15));
        text.setFill(PROG_COLOR);
        gridPane.add(text, c, ++row);
        gridPane.add(hyperlinkWeb, c + 1, row);

        text = new Text("Anleitung:");
        text.setFont(new Font(15));
        text.setFill(PROG_COLOR);
        gridPane.add(text, c, ++row);
        gridPane.add(hyperlinkHelp, c + 1, row);

        text = new Text("Spende:");
        text.setFont(new Font(15));
        text.setFill(PROG_COLOR);
        gridPane.add(text, c, ++row);
        gridPane.add(hyperlinkDonate, c + 1, row);

        text = new Text("Forum:");
        text.setFont(new Font(15));
        text.setFill(PROG_COLOR);
        gridPane.add(text, c, ++row);
        gridPane.add(hyperlinkForum, c + 1, row);

        gridPane.add(new Label(""), c, ++row);
        for (int i = 0; i < listName.length && i < listValue.length; ++i) {
            text = new Text(listName[i]);
            text.setFont(new Font(15));
            text.setFill(PROG_COLOR);
            gridPane.add(text, c, ++row);

            text = new Text(listValue[i]);
            text.setFont(new Font(15));
            text.setFill(PROG_COLOR);
            gridPane.add(text, c + 1, row);
            txtContext.append("\n").append(listName[i]).append(": ").append(listValue[i]);
        }

        // Menü
        P2ClipBoardContext.addMenu("Programm-Infos kopieren", txtContext.toString(), gridPane);

        //====================
        // Java
        txtContext = new StringBuilder();
        gridPane = getGridPane();
        row = 0;

        text = new Text("Java Informationen");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));
        text.setFill(PROG_COLOR_MARK);
        gridPane.add(text, c, row, 2, 1);
        txtContext.append("Java Informationen");

        text = new Text("Version:");
        text.setFont(new Font(15));
        text.setFill(PROG_COLOR);
        gridPane.add(text, c, ++row);

        text = new Text(System.getProperty("java.version"));
        text.setFont(new Font(15));
        text.setFill(PROG_COLOR);
        gridPane.add(text, c + 1, row);
        txtContext.append("\n").append("Version: ").append(text.getText());

        text = new Text("Type:");
        text.setFont(new Font(15));
        text.setFill(PROG_COLOR);
        gridPane.add(text, c, ++row);

        String strVmType = System.getProperty("java.vm.name");
        strVmType += " (" + System.getProperty("java.vendor") + ")";
        text = new Text(strVmType);
        text.setFont(new Font(15));
        text.setFill(PROG_COLOR);
        gridPane.add(text, c + 1, row);
        txtContext.append("\n").append("Type: ").append(text.getText());

        text = new Text("JavaFX:");
        text.setFont(new Font(15));
        text.setFill(PROG_COLOR);
        gridPane.add(text, c, ++row);

        //https://edencoding.com/how-to-check-your-javafx-version/
        //String fxVmType = VersionInfo.getVersion();
        //fxVmType += " (" + VersionInfo.getRuntimeVersion() + ")";
        String fxVmType = System.getProperty("javafx.runtime.version");
        text = new Text(fxVmType);
        text.setFont(new Font(15));
        text.setFill(PROG_COLOR);
        gridPane.add(text, c + 1, row);
        txtContext.append("\n").append("JavaFX: ").append(text.getText());

        long totalMem = Runtime.getRuntime().totalMemory();
        String totalMemStr = totalMem / 1000000L + "";
        long maxMem = Runtime.getRuntime().maxMemory();
        String maxMemStr = maxMem / 1000000L + "";
        long freeMem = Runtime.getRuntime().freeMemory();
        String freeMemStr = freeMem / 1000000L + "";

        text = new Text("Speicher [MB]:");
        text.setFont(new Font(15));
        text.setFill(PROG_COLOR);
        gridPane.add(text, c, ++row);

        text = new Text(totalMemStr + ",  max: " + maxMemStr + ",  frei: " + freeMemStr);
        text.setFont(new Font(15));
        text.setFill(PROG_COLOR);
        gridPane.add(text, c + 1, row);
        txtContext.append("\n").append("Speicher (max / frei): ").append(text.getText());

        P2ClipBoardContext.addMenu("Java-Infos kopieren", txtContext.toString(), gridPane);

        //====================
        // Danke
        gridPane = getGridPane();
        row = 0;
        text = new Text("Ein Dankeschön an alle," + P2LibConst.LINE_SEPARATOR +
                "die mit Vorschlägen oder Quelltext" + P2LibConst.LINE_SEPARATOR +
                "zu diesem Programm beigetragen haben.");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));
        text.setFill(PROG_COLOR_MARK);
        gridPane.add(text, c, row, 2, 1);
    }

    private Image getImage() {
        final String path = "/de/p2tools/p2lib/icons/P2.png";
        return new Image(path, 128, 128, false, true);
    }

    private GridPane getGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(3);
        gridPane.setPadding(new Insets(0, 10, 10, 10));
        getVBoxCont().getChildren().add(gridPane);
        return gridPane;
    }
}
