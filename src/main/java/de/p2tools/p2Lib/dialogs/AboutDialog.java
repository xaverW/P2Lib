/*
 * Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2Lib.dialogs;

import com.sun.javafx.runtime.VersionInfo;
import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PHyperlink;
import de.p2tools.p2Lib.tools.ProgramTools;
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

public abstract class AboutDialog extends PDialogExtra {

    private Button btnOk = new Button("_Ok");
    private Button btnCheck = new Button("_Programmupdate prüfen");
    private final Color GRAY;
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
        super(stage, null, "Über das Programm", true, false, DECO.SMALL, masker);

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
            this.GRAY = Color.LAVENDER;
        } else {
            this.GRAY = Color.DARKBLUE;
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

    public void runCheckButtonk() {
    }

    private void makeGrid() {
        btnOk.setOnAction(a -> close());
        btnCheck.setOnAction(a -> runCheckButtonk());

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(0, 10, 0, 10));

        HBox hBox = new HBox();
        hBox.getChildren().add(gridPane);
        hBox.setAlignment(Pos.CENTER);
        hBox.getStyleClass().add("dialog-about");
        getvBoxCont().getChildren().add(hBox);

        int row = 0;

        ImageView iv = new ImageView();
        Image im = getImage();
        iv.setSmooth(true);
        iv.setCache(true);
        iv.setImage(im);
        gridPane.add(iv, 0, row, 1, 3);

        // top
        Text text1 = new Text(progName);
        text1.setFont(Font.font(null, FontWeight.BOLD, 40));
        gridPane.add(text1, 1, row);
        GridPane.setValignment(text1, VPos.TOP);
        GridPane.setHalignment(text1, HPos.CENTER);

        Text text2 = new Text(P2LibConst.LINE_SEPARATOR + "Version: " + ProgramTools.getProgVersion());
        text2.setFont(new Font(18));
        gridPane.add(text2, 1, ++row);
        GridPane.setHalignment(text2, HPos.CENTER);

        Text text3 = new Text("[ Build: " + ProgramTools.getBuild() + " vom " + ProgramTools.getCompileDate() + " ]");
        text3.setFont(new Font(15));
        text3.setFill(GRAY);
        gridPane.add(text3, 1, ++row);
        GridPane.setValignment(text3, VPos.BOTTOM);
        GridPane.setHalignment(text3, HPos.CENTER);

        HBox.setHgrow(gridPane, Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());


        //=======================
        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(3);
        gridPane.setPadding(new Insets(0, 10, 10, 10));
        getvBoxCont().getChildren().add(gridPane);

        row = 0;
        int c = 0;

        Text text = new Text(P2LibConst.LINE_SEPARATOR + "Autor");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));
        gridPane.add(text, c, ++row, 2, 1);

        text = new Text("Xaver W. (xaverW)");
        text.setFont(new Font(15));
        gridPane.add(text, c, ++row, 2, 1);

        // Pfade
        text = new Text(P2LibConst.LINE_SEPARATOR + "Programm Informationen");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));
        gridPane.add(text, c, ++row, 2, 1);

        PHyperlink hyperlinkWeb = new PHyperlink(URL_WEBSITE,
                urlOpenProg, imageView);
        PHyperlink hyperlinkHelp = new PHyperlink(URL_WEBSITE_HELP,
                urlOpenProg, imageView);
        PHyperlink hyperlinkDonate = new PHyperlink(P2LibConst.URL_WEBSITE_DONATE,
                urlOpenProg, imageView);

        text = new Text("Website:");
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, c, ++row);
        gridPane.add(hyperlinkWeb, c + 1, row);

        text = new Text("Anleitung:");
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, c, ++row);
        gridPane.add(hyperlinkHelp, c + 1, row);

        text = new Text("Spende:");
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, c, ++row);
        gridPane.add(hyperlinkDonate, c + 1, row);

        gridPane.add(new Label(""), c, ++row);

        for (int i = 0; i < listName.length && i < listValue.length; ++i) {
            text = new Text(listName[i]);
            text.setFont(new Font(15));
            text.setFill(GRAY);
            gridPane.add(text, c, ++row);

            text = new Text(listValue[i]);
            text.setFont(new Font(15));
            text.setFill(GRAY);
            gridPane.add(text, c + 1, row);
        }

        // Java
        text = new Text(P2LibConst.LINE_SEPARATOR + "Java Informationen");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));
        gridPane.add(text, c, ++row, 2, 1);

        text = new Text("Version:");
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, c, ++row);

        text = new Text(System.getProperty("java.version"));
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, c + 1, row);

        text = new Text("Type:");
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, c, ++row);

        String strVmType = System.getProperty("java.vm.name");
        strVmType += " (" + System.getProperty("java.vendor") + ")";
        text = new Text(strVmType);
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, c + 1, row);

        text = new Text("JavaFX:");
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, c, ++row);

        String fxVmType = VersionInfo.getVersion();
        fxVmType += " (" + VersionInfo.getRuntimeVersion() + ")";
        text = new Text(fxVmType);
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, c + 1, row);

        text = new Text(P2LibConst.LINE_SEPARATORx2 + "Ein Dankeschön an alle," + P2LibConst.LINE_SEPARATOR +
                "die mit Vorschlägen oder Quelltext" + P2LibConst.LINE_SEPARATOR +
                "zu diesem Programm beigetragen haben.");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));
        gridPane.add(text, c, ++row, 2, 1);
    }

    private Image getImage() {
        final String path = "/de/p2tools/p2Lib/icons/P2.png";
        return new Image(path, 128, 128, false, true);
    }
}
