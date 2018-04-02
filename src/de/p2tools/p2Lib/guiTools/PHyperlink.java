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

import de.p2tools.p2Lib.tools.Log;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;

public class PHyperlink extends Hyperlink {
    private final String url;
    private final StringProperty prog;

    public PHyperlink(String url, StringProperty prog) {
        super(url);
        this.url = url;
        this.prog = prog;
        init();
    }

    public PHyperlink(String url) {
        super(url);
        this.url = url;
        this.prog = null;
        init();
    }

    private void init() {
        setStyle("-fx-font-size: 15px;");
        setOnAction(a -> {
            try {
                if (prog != null) {
                    POpen.openURL(url, prog);
                } else {
                    POpen.openURL(url);
                }
            } catch (Exception e) {
                Log.errorLog(974125469, e);
            }
        });
        setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                setContextMenu(getMenu(url));
            }
        });

    }

    private ContextMenu getMenu(String url) {
        final ContextMenu contextMenu = new ContextMenu();

        MenuItem resetTable = new MenuItem("URL kopieren");
        resetTable.setOnAction(a -> {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(url);
            clipboard.setContent(content);
        });
        contextMenu.getItems().addAll(resetTable);
        return contextMenu;
    }

}
