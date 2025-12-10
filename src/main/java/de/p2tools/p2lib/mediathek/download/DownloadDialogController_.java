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

package de.p2tools.p2lib.mediathek.download;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.P2ProgIcons;
import de.p2tools.p2lib.alert.P2Alert;
import de.p2tools.p2lib.dialogs.P2DirFileChooser;
import de.p2tools.p2lib.dialogs.dialog.P2DialogExtra;
import de.p2tools.p2lib.guitools.grid.P2GridConstraints;
import de.p2tools.p2lib.tools.P2InfoFactory;
import de.p2tools.p2lib.tools.file.P2FileUtils;
import de.p2tools.p2lib.tools.net.PUrlTools;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DownloadDialogController_ extends P2DialogExtra {

    private final VBox vBoxCont;

    private final Button btnOk = new Button("_Ok");
    private final Button btnCancel = new Button("_Abbrechen");

    private final GridPane gridPane = new GridPane();
    private final TextField txtDestPath = new TextField();
    private final TextField txtName = new TextField();

    private final StringProperty filePath = new SimpleStringProperty();
    private final StringProperty fileName = new SimpleStringProperty();
    private final String url;
    private final String urlFile;
    private final String orgFileName;
    private final Stage stage;
    StringProperty path;
    private boolean nameChanged = false;
    private boolean ok = false;

    public DownloadDialogController_(final Stage stage, final String url, final StringProperty path, final String orgFileName) {
        super(stage, null, "Download", true, true, true, DECO.BORDER_SMALL);

        this.stage = stage;
        this.url = url;
        this.urlFile = PUrlTools.getFileName(url);
        this.orgFileName = orgFileName.isEmpty() ? urlFile : orgFileName;
        this.path = path;

        if (path == null || path.getValueSafe().isEmpty()) {
            this.filePath.set(P2InfoFactory.getHomePath());
        } else {
            this.filePath.set(path.getValue());
        }
        this.fileName.setValue(getFileName(filePath.getValueSafe(), this.orgFileName));

        vBoxCont = getVBoxCont();
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
        gridPane.getColumnConstraints().addAll(P2GridConstraints.getCcPrefSize(),
                P2GridConstraints.getCcComputedSizeAndHgrow());

        final Label lblDest = new Label("Speicherziel:");
        lblDest.setStyle("-fx-font-weight: bold;");
        final Label lblSrc = new Label("Download:");
        lblSrc.setStyle("-fx-font-weight: bold;");

        final Label lblDestPath = new Label("Pfad:");
        final Label lblName = new Label("Dateiname:");
        final Label lblUrlFile = new Label("Datei:");
        final Label lblUrl = new Label("URL:");

        txtDestPath.textProperty().bindBidirectional(filePath);
        txtName.textProperty().bindBidirectional(fileName);
        txtName.textProperty().addListener((observableValue, s, t1) -> {
            nameChanged = true;
        });
        final Button btnDest = new Button();
        btnDest.setGraphic(P2ProgIcons.IMAGE_FILE_OPEN.getImageView());
        btnDest.setTooltip(new Tooltip("Einen Ordner zum Speichern der Datei auswählen"));
        btnDest.setOnAction(event -> {
            P2DirFileChooser.DirChooser(stage, txtDestPath);
            final boolean nc = nameChanged;
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
        if (path != null) {
            path.setValue(txtDestPath.getText());
        }
        close();
    }

    private String getFileName(final String dir, final String name) {
        if (nameChanged) {
            return name;
        }

        String newName = orgFileName;
        String file = P2FileUtils.addsPath(dir, orgFileName);

        final String noSuff = P2FileUtils.removeFileNameSuffix(orgFileName);
        final String suff = P2FileUtils.getFileNameSuffix(orgFileName);
        if (noSuff.isEmpty() || suff.isEmpty()) {
            return name;
        }

        int i = 1;
        while (P2FileUtils.fileExist(file)) {
            newName = noSuff + "-" + i++ + "." + suff;
            file = P2FileUtils.addsPath(dir, newName);
        }

        return newName;
    }

    private boolean checkDest() {
        boolean ret = false;

        final String destDir = txtDestPath.getText();
        final String destName = txtName.getText();
        final String file = P2FileUtils.addsPath(destDir, destName);
        if (P2FileUtils.fileExist(file)) {
            ret = false;
            final P2Alert.BUTTON button = P2Alert.showAlert_yes_no("Hinweis", "Datei speichern",
                    "Die Zieldatei exisiert bereits:" + P2LibConst.LINE_SEPARATORx2 +
                            destName + P2LibConst.LINE_SEPARATORx2 +
                            "Soll die Datei überschrieben werden?");
            if (button.equals(P2Alert.BUTTON.YES)) {
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
