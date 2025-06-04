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


package de.p2tools.p2lib.guitools;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.tools.log.P2Log;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.util.Optional;

public class P2Hyperlink extends Hyperlink {
    private String url;
    private final StringProperty prog;
    private final Stage stage;
    private boolean changeAble = false;
    private final boolean size11;

    public P2Hyperlink(Stage stage, String url, StringProperty prog, boolean size11) {
        super(url);
        this.stage = stage;
        this.url = url;
        this.prog = prog;
        this.size11 = size11;
        init();
    }

    public P2Hyperlink(String url, StringProperty prog, boolean size11) {
        super(url);
        stage = P2LibConst.primaryStage;
        this.url = url;
        this.prog = prog;
        this.size11 = size11;
        init();
    }

    public P2Hyperlink(Stage stage, String url, StringProperty prog) {
        super(url);
        this.stage = stage;
        this.url = url;
        this.prog = prog;
        this.size11 = true;
        init();
    }

    public P2Hyperlink(String url, StringProperty prog) {
        super(url);
        stage = P2LibConst.primaryStage;
        this.url = url;
        this.prog = prog;
        this.size11 = true;
        init();
    }

    public P2Hyperlink(String url) {
        super(url);
        stage = P2LibConst.primaryStage;
        this.url = url;
        this.prog = null;
        this.size11 = true;
        init();
    }

    public void setUrl(String url) {
        this.url = url;
        super.setText(url);
    }

    public void setChangeable() {
        changeAble = true;
    }

    private void init() {
        setPadding(new Insets(0));
        if (size11) {
            setStyle("-fx-font-size: 1.1em;");
        }
        setOnAction(a -> {
            try {
                P2Open.openURL(stage, url, prog);
            } catch (Exception e) {
                P2Log.errorLog(974125469, e);
            }
        });
        setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                setContextMenu(getMenu());
            }
        });

    }

    private ContextMenu getMenu() {
        final ContextMenu contextMenu = new ContextMenu();

        MenuItem copyUrl = new MenuItem("URL kopieren");
        copyUrl.setOnAction(a -> {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(url);
            clipboard.setContent(content);
        });
        contextMenu.getItems().addAll(copyUrl);

        if (changeAble) {
            MenuItem changeUrl = new MenuItem("URL ändern");
            changeUrl.setOnAction(a -> {
                TextInputDialog dialog = new TextInputDialog(url);
                dialog.setResizable(true);
                dialog.setTitle("URL ändern");
                dialog.setHeaderText("Eine neue URL angeben");
                dialog.setContentText("URL:");
                dialog.initOwner(stage);

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(newUrl -> {
                    url = newUrl;
                    this.setText(url);
                });
            });
            contextMenu.getItems().addAll(changeUrl);
        }

        return contextMenu;
    }
}
