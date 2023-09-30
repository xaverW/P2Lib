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

import de.p2tools.p2lib.tools.GermanStringSorter;
import de.p2tools.p2lib.tools.PRegEx;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.regex.Pattern;

public class P2ComboBoxString extends ComboBox<String> {

    public final int MAX_ELEMENTS = 15;
    private int maxElements = MAX_ELEMENTS;
    private StringProperty selValueStringProperty = null;
    private ObservableList<String> itemsList = null;
    Pattern pattern = null;

    public P2ComboBoxString() {
        super();
        this.setEditable(true);
    }

    public P2ComboBoxString(String regEx) {
        super();
        this.setEditable(true);
        pattern = PRegEx.makePattern(regEx);
    }

    public void init(ObservableList<String> dataList) {
        this.itemsList = dataList;

        setCombo("");
    }

    public void init(ObservableList<String> dataList, StringProperty selValueStringProperty) {
        this.itemsList = dataList;
        this.selValueStringProperty = selValueStringProperty;

        setCombo(selValueStringProperty.getValueSafe());
    }

    public void init(ObservableList<String> dataList, String init, StringProperty selValueStringProperty) {
        //init wird gesetzt, wenn selValue leer ist
        this.selValueStringProperty = selValueStringProperty;
        this.itemsList = dataList;

        setCombo(init);
    }

    public void setRegEx(String regEx) {
        pattern = PRegEx.makePattern(regEx);
    }

    public void bindSelValueProperty(StringProperty stringProperty) {
        unbind();
        selValueStringProperty = stringProperty;
        bind();
    }

    public void unbindSelValueProperty() {
        unbind();
        this.selValueStringProperty = null;
        this.selectElement("");
    }

    public String getSelValue() {
        String s = this.getSelectionModel().getSelectedItem();
        if (s == null) {
            s = "";
        }
        return s;
    }

    public ReadOnlyObjectProperty<String> getSelValueProperty() {
        ReadOnlyObjectProperty<String> s = this.getSelectionModel().selectedItemProperty();
        return s;
    }

    public void selectElement(String element) {
//       selValueStringProperty.setValue(element);
        setValue(element);
    }

    public int getMaxElements() {
        return maxElements;
    }

    public void setMaxElements(int maxElements) {
        this.maxElements = maxElements;
    }

    private void setCombo(String start) {
        if (itemsList == null) {
            return;
        }

        if (!start.isEmpty() && !itemsList.contains(start)) {
            itemsList.add(start);
        }
        reduceList();
        Collections.sort(itemsList, new GermanStringSorter());

        this.setItems(itemsList);

        getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !itemsList.contains(newValue)) {
                //itemsList.add(newValue);
                //oder
                itemsList.add(1, newValue);
                setValue(newValue);
            }
        });

        getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
//           if (newValue != null && !itemsList.contains(newValue)) {
//               itemsList.add(0, newValue); todo da landen sonst alle Tippser in der Liste ??
//               setValue(newValue);
//           }
            check();
        });

        getEditor().setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                getEditor().setContextMenu(getMenu());
            }
        });

        itemsList.addListener((ListChangeListener<String>) c -> {
                    if (itemsList.size() == 1) {
                        getSelectionModel().selectFirst(); //ist: ""
                    }
                }
        );

        bind();

        if (!start.isEmpty() && getSelValue().isEmpty()) {
            selectElement(start);
        }
    }


    private void bind() {
        if (selValueStringProperty == null) {
            return;
        }

        valueProperty().bindBidirectional(selValueStringProperty);
    }

    private void unbind() {
        if (selValueStringProperty == null) {
            setValue("");
            return;
        }

        valueProperty().unbindBidirectional(selValueStringProperty);
    }

    private void delList() {
        String akt = getSelValue();
        int select = 0;
        ArrayList<String> list = new ArrayList<>();
        list.add("");


        if (!akt.isEmpty()) {
            list.add(akt);
            select = 1;
        }

        itemsList.setAll(list);
        getSelectionModel().select(select);
    }

    private void delAll() {
        ArrayList<String> list = new ArrayList<>();
        list.add("");
        itemsList.setAll(list);
        getSelectionModel().selectFirst();
    }

    private void reduceList() {
        ArrayList<String> list = new ArrayList<>();

        if (itemsList.isEmpty() || itemsList.get(0) != "") {
            itemsList.add(0, "");
        }

        Iterator<String> it = itemsList.listIterator();
        while (it.hasNext()) {
            String s = it.next();
            if (!list.contains(s)) {
                list.add(s);
            } else {
                it.remove();
            }
        }


        //macht Probleme wenn die Liste für 2 Combos benutzt wird
//       list.add("");
//       itemsList.stream().forEach(d -> {
//           if (!list.contains(d) && list.size() < maxElements) {
//               list.add(d);
//           }
//       });
//        itemsList.setAll(list);
    }

    private ContextMenu getMenu() {
        final ContextMenu contextMenu = new ContextMenu();

        MenuItem delEntries = new MenuItem("andere Einträge löschen");
        delEntries.setOnAction(a -> delList());
        contextMenu.getItems().addAll(delEntries);

        MenuItem delAll = new MenuItem("alles löschen");
        delAll.setOnAction(a -> delAll());
        contextMenu.getItems().addAll(delAll);
        return contextMenu;
    }

    private void check() {
        if (pattern == null) {
            return;
        }

        if (/*!PRegEx.check(valueProperty().getValue(), pattern) ||*/
                PRegEx.check(getEditor().textProperty().getValue(), pattern)) {
            this.getEditor().setStyle("");
//            this.setStyle("");
        } else {
            this.getEditor().setStyle(P2Styles.PCOMBO_ERROR);
//            this.setStyle(PStyles.PCOMBO_ERROR);
        }

    }

}
