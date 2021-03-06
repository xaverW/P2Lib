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


package de.p2tools.p2Lib.guiTools;

import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.util.Optional;

public class PHyperlink extends Hyperlink {
    private String url;
    private final StringProperty prog;
    private ImageView imageView = null;
    private final Stage stage;
    private boolean changeAble = false;

    public PHyperlink(Stage stage, String url, StringProperty prog, ImageView imageView) {
        super(url);
        this.stage = stage;
        this.url = url;
        this.prog = prog;
        this.imageView = imageView;
        init();
    }

    public PHyperlink(String url, StringProperty prog, ImageView imageView) {
        super(url);
        stage = P2LibConst.primaryStage;
        this.url = url;
        this.prog = prog;
        this.imageView = imageView;
        init();
    }

    public PHyperlink(String url, ImageView imageView) {
        super(url);
        stage = P2LibConst.primaryStage;
        this.url = url;
        this.prog = null;
        this.imageView = imageView;
        init();
    }

    public PHyperlink(String url) {
        super(url);
        stage = P2LibConst.primaryStage;
        this.url = url;
        this.prog = null;
        init();
    }

    public void setUrl(String url) {
        this.url = url;
        super.setText(url);
//        init();
    }

    public void setChangeable() {
        changeAble = true;
    }

    private void init() {
        setStyle("-fx-font-size: 15px;");
        setOnAction(a -> {
            try {
                if (prog != null) {
                    POpen.openURL(stage, url, prog, imageView);
                } else {
                    POpen.openURL(stage, url);
                }
            } catch (Exception e) {
                PLog.errorLog(974125469, e);
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
