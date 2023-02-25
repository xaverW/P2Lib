/*
 * P2Tools Copyright (C) 2020 W. Xaver W.Xaver[at]googlemail.com
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

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;

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
                            //previous node
                            ObservableList<Node> children = parent.getChildrenUnmodifiable();
                            int idx = children.indexOf(textArea);
                            if (idx < 0) {
                                //dann gibts es nicht??
                                break;
                            }
                            selPreviousNode(parent, idx);

                        } else {
                            //next node
                            ObservableList<Node> children = parent.getChildrenUnmodifiable();
                            int idx = children.indexOf(textArea);
                            if (idx < 0) {
                                //dann gibts es nicht??
                                break;
                            }
                            selNextNode(parent, idx);
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


        private boolean selPreviousNode(Parent parent, int startNode) {
            ObservableList<Node> children = parent.getChildrenUnmodifiable();
            for (int i = startNode - 1; i >= 0; i--) {
                //vom startNode zurück
                Node n = children.get(i);
                if (!n.isFocusTraversable() && n instanceof Region) {
                    if (selPreviousNode(((Region) n), ((Region) n).getChildrenUnmodifiable().size())) {
                        return true;
                    }
                }
                if (n.isFocusTraversable()) {
                    n.requestFocus();
                    return true;
                }
            }

            for (int i = children.size() - 1; i > startNode; i--) {
                //vom ende zum startNode
                Node n = children.get(i);
                if (!n.isFocusTraversable() && n instanceof Region) {
                    if (selPreviousNode(((Region) n), ((Region) n).getChildrenUnmodifiable().size())) {
                        return true;
                    }
                }
                if (n.isFocusTraversable()) {
                    n.requestFocus();
                    return true;
                }
            }

            return false;
        }

        private boolean selNextNode(Parent parent, int startNode) {
            ObservableList<Node> children = parent.getChildrenUnmodifiable();
            for (int i = startNode + 1; i < children.size(); i++) {
                Node n = children.get(i);
                if (!n.isFocusTraversable() && n instanceof Region) {
                    if (selNextNode(((Region) n), 0)) {
                        return true;
                    }
                }
                if (n.isFocusTraversable()) {
                    n.requestFocus();
                    return true;
                }
            }

            for (int i = 0; i < startNode; i++) {
                Node n = children.get(i);
                if (!n.isFocusTraversable() && n instanceof Region) {
                    if (selNextNode(((Region) n), 0)) {
                        return true;
                    }
                }
                if (n.isFocusTraversable()) {
                    n.requestFocus();
                    return true;
                }
            }

            return false;
        }
    }
}
