/*
 * P2tools Copyright (C) 2020 W. Xaver W.Xaver[at]googlemail.com
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

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;

public class PTextAreaIgnoreTab extends TextArea {
    final TextArea textArea = this;
    final boolean enter, tab;

    public PTextAreaIgnoreTab(boolean enter, boolean tab) {
        this.enter = enter;
        this.tab = tab;

        addEventFilter(KeyEvent.KEY_PRESSED, new TabAndEnterHandler());
    }

    class TabAndEnterHandler implements EventHandler<KeyEvent> {
        private KeyEvent recodedEvent;

        @Override
        public void handle(KeyEvent event) {
            if (recodedEvent != null) {
                recodedEvent = null;
                return;
            }

            Parent parent = textArea.getParent();
            if (parent != null) {
//                System.out.println(event.getCharacter() + " - " + event.getCode());
                switch (event.getCode()) {
                    case ENTER:
                        if (!enter) {
                            break;
                        }

                        if (event.isControlDown()) {
                            recodedEvent = recodeWithoutControlDown(event);
                            textArea.fireEvent(recodedEvent);
                        } else {
                            Event parentEvent = event.copyFor(parent, parent);
                            textArea.getParent().fireEvent(parentEvent);
                        }
                        event.consume();
                        break;

                    case TAB:
                        if (!tab) {
                            break;
                        }

                        if (event.isControlDown()) {
                            recodedEvent = recodeWithoutControlDown(event);
                            textArea.fireEvent(recodedEvent);
                        } else if (event.isShiftDown()) {
                            ObservableList<Node> children = parent.getChildrenUnmodifiable();
                            int idx = children.indexOf(textArea);
                            if (idx > 0) {
                                for (int i = idx - 1; i >= 0; i--) {
                                    if (children.get(i).isFocusTraversable()) {
                                        children.get(i).requestFocus();
                                        break;
                                    }
                                }
                                for (int i = children.size() - 1; i > idx; i--) {
                                    if (children.get(i).isFocusTraversable()) {
                                        children.get(i).requestFocus();
                                        break;
                                    }
                                }
                            }
                        } else {
                            ObservableList<Node> children = parent.getChildrenUnmodifiable();
                            int idx = children.indexOf(textArea);
                            if (idx >= 0) {
                                for (int i = idx + 1; i < children.size(); i++) {
                                    if (children.get(i).isFocusTraversable()) {
                                        children.get(i).requestFocus();
                                        break;
                                    }
                                }
                                for (int i = 0; i < idx; i++) {
                                    if (children.get(i).isFocusTraversable()) {
                                        children.get(i).requestFocus();
                                        break;
                                    }
                                }
                            }
                        }
                        event.consume();
                        break;
                }
            }
        }

        private KeyEvent recodeWithoutControlDown(KeyEvent event) {
            return new KeyEvent(
                    event.getEventType(),
                    event.getCharacter(),
                    event.getText(),
                    event.getCode(),
                    event.isShiftDown(),
                    false,
                    event.isAltDown(),
                    event.isMetaDown()
            );
        }
    }

}
