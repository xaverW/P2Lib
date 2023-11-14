/*
 * P2tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
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

import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class P2ClipBoardContext {
    private P2ClipBoardContext() {
    }

    public static void addMenu(String text, StringProperty txtClipBoard, Node node) {
        // Menü
        node.setOnContextMenuRequested(event -> {
            ContextMenu contextMenu = new ContextMenu();
            final MenuItem miCopyName = new MenuItem(text);
            miCopyName.setOnAction(a -> {
                final Clipboard clipboard = Clipboard.getSystemClipboard();
                final ClipboardContent content = new ClipboardContent();
                content.putString(txtClipBoard.getValueSafe());
                clipboard.setContent(content);
            });
            contextMenu.getItems().add(miCopyName);
            contextMenu.show(node, event.getScreenX(), event.getScreenY());
        });
    }

    public static void addMenu(String text, String txtClipBoard, Node node) {
        // Menü
        node.setOnContextMenuRequested(event -> {
            ContextMenu contextMenu = new ContextMenu();
            final MenuItem miCopyName = new MenuItem(text);
            miCopyName.setOnAction(a -> {
                final Clipboard clipboard = Clipboard.getSystemClipboard();
                final ClipboardContent content = new ClipboardContent();
                content.putString(txtClipBoard);
                clipboard.setContent(content);
            });
            contextMenu.getItems().add(miCopyName);
            contextMenu.show(node, event.getScreenX(), event.getScreenY());
        });
    }
}
