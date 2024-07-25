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
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class P2MenuButton extends MenuButton {
    private final StringProperty filterProperty;
    private final ObservableList<String> allButtonList;
    private final ArrayList<MenuItemClass> menuItemsList = new ArrayList<>();

    public P2MenuButton(StringProperty filterProperty, ObservableList<String> allButtonList) {
        this.filterProperty = filterProperty;
        this.allButtonList = allButtonList;

        initMenuButton();
    }

    public P2MenuButton(StringProperty filterProperty, ObservableList<String> allButtonList, boolean minWidth) {
        this.filterProperty = filterProperty;
        this.allButtonList = allButtonList;
        initMenuButton();
        if (minWidth) {
            setMaxWidth(-1);
            setMinWidth(200);
        }
    }

    private void initMenuButton() {
        getStyleClass().add("cbo-menu");
        setMaxWidth(Double.MAX_VALUE);
        updateMenuButton();
        filterProperty.addListener((observable, oldValue, newValue) -> {
            updateMenuButton();
        });
        allButtonList.addListener((ListChangeListener<String>) c -> updateMenuButton());
        textProperty().bindBidirectional(filterProperty);
    }

    private void updateMenuButton() {
        getItems().clear();
        menuItemsList.clear();

        List<String> filterList = new ArrayList<>();
        List<String> tmpList = new ArrayList<>();
        String channelFilter = filterProperty.get();
        if (channelFilter != null) {
            if (channelFilter.contains(",")) {
                tmpList.addAll(Arrays.asList(channelFilter/*.replace(" ", "")*/.toLowerCase().split(",")));
            } else {
                tmpList.add(channelFilter.toLowerCase());
            }
            tmpList.forEach(s -> filterList.add(s.trim()));
        }

        CheckBox miCheckAll = new CheckBox();
        miCheckAll.setVisible(false);

        Button btnClear = new Button("Auswahl löschen");
        btnClear.getStyleClass().add("cbo-menu-button");
        btnClear.setMaxWidth(Double.MAX_VALUE);
        btnClear.minWidthProperty().bind(widthProperty().add(-50));
        btnClear.setOnAction(e -> {
            clearChannelMenuText();
            hide();
        });

        HBox hBoxAll = new HBox(P2LibConst.DIST_BUTTON);
        hBoxAll.setAlignment(Pos.CENTER_LEFT);
        hBoxAll.getChildren().addAll(miCheckAll, btnClear);

        CustomMenuItem cmiAll = new CustomMenuItem(hBoxAll);
        cmiAll.getStyleClass().add("cbo-menu-item");
        getItems().add(cmiAll);

        for (String s : allButtonList) {
            if (s.trim().isEmpty()) {
                continue;
            }

            CheckBox miCheck = new CheckBox();
            if (filterList.contains(s.trim().toLowerCase())) {
                miCheck.setSelected(true);
            }
            miCheck.setOnAction(a -> setMenuText());

            MenuItemClass menuItemClass = new MenuItemClass(s, miCheck);
            menuItemsList.add(menuItemClass);

            Button btnChannel = new Button(s);
            btnChannel.getStyleClass().add("cbo-menu-button");
            btnChannel.setMaxWidth(Double.MAX_VALUE);
            btnChannel.minWidthProperty().bind(widthProperty().add(-50));
            btnChannel.setOnAction(e -> {
                setChannelCheckBoxAndMenuText(menuItemClass);
                hide();
            });

            HBox hBox = new HBox(5);
            hBox.prefWidthProperty().bind(hBoxAll.widthProperty());
            hBox.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(btnChannel, Priority.ALWAYS);
            hBox.getChildren().addAll(miCheck, btnChannel);

            CustomMenuItem cmi = new CustomMenuItem(hBox);
            cmi.getStyleClass().add("cbo-menu-item");
            getItems().add(cmi);

            cmi.setOnAction(e -> {
//                System.out.println("CustomMenuItem");
                setChannelCheckBoxAndMenuText(menuItemClass);
            });
        }
    }

    private void setChannelCheckBoxAndMenuText(MenuItemClass cmi) {
        for (MenuItemClass cm : menuItemsList) {
            cm.getCheckBox().setSelected(false);
        }
        cmi.getCheckBox().setSelected(true);
        setMenuText();
    }

    private void clearChannelMenuText() {
        for (MenuItemClass cmi : menuItemsList) {
            cmi.getCheckBox().setSelected(false);
        }
        setText("");
        doAfterSelction("");
    }

    private void setMenuText() {
        StringBuilder text = new StringBuilder();
        for (MenuItemClass cmi : menuItemsList) {
            if (cmi.getCheckBox().isSelected()) {
                text.append((text.isEmpty()) ? "" : ", ").append(cmi.getText());
            }
        }
        setText(text.toString());
        doAfterSelction(text.toString());
    }

    public void doAfterSelction(String text) {

    }

    private static class MenuItemClass {
        private final String text;
        private final CheckBox checkBox;

        MenuItemClass(String text, CheckBox checkbox) {
            this.text = text;
            this.checkBox = checkbox;
        }

        public String getText() {
            return text;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }
    }
}