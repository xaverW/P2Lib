package de.p2tools.p2lib.guitools;

import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class P2RowFactory<T> implements Callback<TableView<T>, TableRow<T>> {

    private final Callback<TableView<T>, TableRow<T>> baseFactory;

    public P2RowFactory(Callback<TableView<T>, TableRow<T>> baseFactory) {
        this.baseFactory = baseFactory;
    }

    public P2RowFactory() {
        this(null);
    }

    @Override
    public TableRow<T> call(TableView<T> tv) {

        final TableRow<T> row;
        if (baseFactory == null) {
            row = new TableRow<>();
        } else {
            row = baseFactory.call(tv);
        }
        row.setOnDragDetected(event -> {
            row.startFullDrag();
            tv.getSelectionModel().clearSelection();
            tv.getSelectionModel().select(row.getItem());
        });
        row.setOnMouseDragEntered(event -> {
            tv.getSelectionModel().select(row.getItem());
        });

        return row;
    }
}
