/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2Lib.tools.download;

import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.dialogs.PDirFileChooser;
import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.tools.file.PFileUtils;
import de.p2tools.p2Lib.tools.net.PUrlTools;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DownloadDialogController extends PDialogExtra {

    private VBox vBoxCont;

    private Button btnOk = new Button("_Ok");
    private Button btnCancel = new Button("_Abbrechen");

    private GridPane gridPane = new GridPane();
    private TextField txtDestPath = new TextField();
    private TextField txtName = new TextField();

    private StringProperty filePath = new SimpleStringProperty();
    private StringProperty fileName = new SimpleStringProperty();
    private String url;
    private String urlFile;
    private String orgFileName;
    private boolean nameChanged = false;

    private boolean ok = false;
    private final Stage stage;

    DownloadDialogController(Stage stage, String url, String orgFileName) {
        super(stage, null, "Download", true, false, DECO.SMALL);

        this.stage = stage;
        this.url = url;
        this.urlFile = PUrlTools.getFileName(url);
        this.orgFileName = orgFileName.isEmpty() ? urlFile : orgFileName;

        this.filePath.set(PFileUtils.getHomePath());
        this.fileName.setValue(getFileName(filePath.getValueSafe(), this.orgFileName));

        vBoxCont = getvBoxCont();
        init(true);
    }

    @Override
    public void make() {
        vBoxCont.setPadding(new Insets(5));
        vBoxCont.setSpacing(10);
        vBoxCont.getChildren().addAll(gridPane);
        addOkCancelButtons(btnOk, btnCancel);

        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        Label lblDest = new Label("Speicherziel:");
        lblDest.setStyle("-fx-font-weight: bold;");
        Label lblSrc = new Label("Download:");
        lblSrc.setStyle("-fx-font-weight: bold;");

        Label lblDestPath = new Label("Pfad:");
        Label lblName = new Label("Dateiname:");
        Label lblUrlFile = new Label("Datei:");
        Label lblUrl = new Label("URL:");

        txtDestPath.textProperty().bindBidirectional(filePath);
        txtName.textProperty().bindBidirectional(fileName);
        txtName.textProperty().addListener((observableValue, s, t1) -> {
            nameChanged = true;
        });
        final Button btnDest = new Button();
        btnDest.setGraphic(new ImageView(P2LibConst.IMAGE_FILE_OPEN));
        btnDest.setTooltip(new Tooltip("Einen Ordner zum Speichern der Datei auswählen."));
        btnDest.setOnAction(event -> {
            PDirFileChooser.DirChooser(stage, txtDestPath);
            boolean nc = nameChanged;
            this.fileName.setValue(getFileName(filePath.getValueSafe(), fileName.getValueSafe()));
            nameChanged = nc;
        });

        int row = 0;
        gridPane.add(lblSrc, 0, row, 2, 1);
        gridPane.add(lblUrl, 0, ++row);
        gridPane.add(new Label(url), 1, row);
        gridPane.add(lblUrlFile, 0, ++row);
        gridPane.add(new Label(urlFile), 1, row);

        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(lblDest, 0, ++row, 2, 1);
        gridPane.add(lblDestPath, 0, ++row);
        gridPane.add(txtDestPath, 1, row);
        gridPane.add(btnDest, 2, row);
        gridPane.add(lblName, 0, ++row);
        gridPane.add(txtName, 1, row);

        btnOk.setOnAction(event -> {
            ok = checkDest();
            if (ok) {
                quit();
            }
        });

        btnCancel.setOnAction(event -> quit());
    }

    private void quit() {
        close();
    }

    private String getFileName(String dir, String name) {
        if (nameChanged) {
            return name;
        }

        String newName = orgFileName;
        String file = PFileUtils.addsPath(dir, orgFileName);

        String noSuff = PFileUtils.removeFileNameSuffix(orgFileName);
        String suff = PFileUtils.getFileNameSuffix(orgFileName);
        if (noSuff.isEmpty() || suff.isEmpty()) {
            return name;
        }

        int i = 1;
        while (PFileUtils.fileExist(file)) {
            newName = noSuff + "-" + i++ + "." + suff;
            file = PFileUtils.addsPath(dir, newName);
        }

        return newName;
    }

    private boolean checkDest() {
        boolean ret = false;

        String destDir = txtDestPath.getText();
        String destName = txtName.getText();
        String file = PFileUtils.addsPath(destDir, destName);
        if (PFileUtils.fileExist(file)) {
            ret = false;
            PAlert.BUTTON button = PAlert.showAlert_yes_no("Hinweis", "Datei speichern",
                    "Die Zieldatei exisiert bereits:" + P2LibConst.LINE_SEPARATORx2 +
                            destName + P2LibConst.LINE_SEPARATORx2 +
                            "Soll die Datei überschrieben werden?");
            if (button.equals(PAlert.BUTTON.YES)) {
                ret = true;
            }
        } else {
            ret = true;
        }

        return ret;
    }

    public boolean getOk() {
        return ok;
    }

    public String getDestPath() {
        return txtDestPath.getText();
    }

    public String getDestName() {
        return txtName.getText();
    }
}
