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


package de.p2tools.p2Lib.guiTools.pCheckComboBox;

import de.p2tools.p2Lib.tools.PException;
import de.p2tools.p2Lib.tools.PStringUtils;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.ArrayList;
import java.util.Optional;

public class PCheckComboBox extends HBox {
    private final SplitMenuButton menuButton = new SplitMenuButton();
    private final ObservableList<String> items = FXCollections.observableArrayList();
    private final ArrayList<CheckBox> arrayList = new ArrayList<>();

    public PCheckComboBox() {
        init();
    }

    public void addItem(String item, BooleanProperty property) {
        Optional<String> otp = items.stream().filter(p -> p.equals(item)).findAny();
        if (otp.isPresent()) {
            PException.throwPException(912032014, "Item exists already");
            return;
        }

        items.add(item);
        add(item, property);
        setTitle();
    }

    public final void setTooltip(Tooltip value) {
        menuButton.setTooltip(value);
    }

    private void add(String item, BooleanProperty property) {
        CheckBox cb = new CheckBox("Item ");
        cb.selectedProperty().bindBidirectional(property);
        cb.setText(item);
//        cb.prefWidthProperty().bind(menuButton.widthProperty());
        addListener(cb);

        CustomMenuItem cmi = new CustomMenuItem(cb);
        cmi.setHideOnClick(false);
        menuButton.getItems().add(cmi);
    }

    private void addListener(CheckBox checkBox) {
        arrayList.add(checkBox);
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            setTitle();
        });
    }

    ArrayList<String> list = new ArrayList<>();

    private void setTitle() {
        list.clear();
        arrayList.stream().filter(ch -> ch.isSelected()).forEach(ch -> list.add(ch.getText()));
        menuButton.setText(PStringUtils.appendList(list, ", "));
    }

    private void init() {
        this.setAlignment(Pos.CENTER_LEFT);
        this.setSpacing(10);
        this.setPadding(new Insets(2));

        HBox.setHgrow(menuButton, Priority.ALWAYS);
        menuButton.setMaxWidth(Double.MAX_VALUE);
        this.getChildren().addAll(menuButton);

        menuButton.setOnMouseClicked(m -> {
            if (m.getButton().equals(MouseButton.PRIMARY) && m.getClickCount() == 2) {
                arrayList.stream().forEach(ch -> ch.setSelected(false));
            }
        });

        menuButton.setOnMousePressed(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
                if (menuButton.isShowing()) {
                    menuButton.hide();
                } else {
                    menuButton.show();
                }
            }
        });

        getStyleClass().add("pCheckComboBox");
        final String CSS_FILE = "de/p2tools/p2Lib/guiTools/pCheckComboBox/pCheckComboBox.css";
        getStylesheets().add(CSS_FILE);
    }

}
