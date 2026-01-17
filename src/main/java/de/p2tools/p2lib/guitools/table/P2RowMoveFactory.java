package de.p2tools.p2lib.guitools.table;

import de.p2tools.p2lib.alert.P2Alert;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.util.List;

public class P2RowMoveFactory<T> implements Callback<TableView<T>, TableRow<T>> {

    private final Callback<TableView<T>, TableRow<T>> baseFactory;
    private final IntegerProperty startPos = new SimpleIntegerProperty();
    private final List<T> fromList;

    public P2RowMoveFactory() {
        this(null, null);
    }

    public P2RowMoveFactory(List<T> fromList) {
        this.baseFactory = null;
        this.fromList = fromList;
    }

    public P2RowMoveFactory(Callback<TableView<T>, TableRow<T>> baseFactory) {
        this.baseFactory = baseFactory;
        this.fromList = null;
    }

    public P2RowMoveFactory(Callback<TableView<T>, TableRow<T>> baseFactory, List<T> fromList) {
        this.baseFactory = baseFactory;
        this.fromList = fromList;
    }

    @Override
    public TableRow<T> call(TableView<T> tableView) {

        final TableRow<T> row;
        if (baseFactory == null) {
            row = new TableRow<>();
        } else {
            row = baseFactory.call(tableView);
        }


        row.setOnDragDetected(event -> {
//            System.out.println("====================");
//            System.out.println("setOnDragDetected");
//            System.out.println("Row: " + row.getIndex());

            if (!event.isPrimaryButtonDown()) {
                return;
            }

            if (event.isControlDown() && fromList != null) {
                if (!tableView.getSortOrder().isEmpty()) {
//                    System.out.println("getSortOrder");
                    String s = tableView.getSortOrder().get(0).getText();
                    P2Alert.showInfoAlert("Sortieren",
                            "Zeilen verschieben",
                            "Die Tabelle wird nach:\n"
                                    + " >> " + s + " <<\n" +
                                    "sortiert. So können keine " +
                                    "Zeilen verschoben werden. Die Sortierung wird zuerst gelöscht.");
                    startPos.set(-1);
                    tableView.getSortOrder().clear();
                    row.startFullDrag();

                    tableView.getSelectionModel().clearSelection();
                    tableView.getSelectionModel().select(row.getItem());
                    return;
                }
            }

//            System.out.println("startFullDrag");
            startPos.set(-1);
            row.startFullDrag();
            tableView.getSelectionModel().clearSelection();
            tableView.getSelectionModel().select(row.getItem());
        });

        row.setOnMouseDragEntered(event -> {
            if (!event.isPrimaryButtonDown()) {
                return;
            }

//            System.out.println("<- setOnMouseDragEntered");
//            System.out.println("Row: " + row.getIndex());
            if (!event.isControlDown() || fromList == null) {
                // dann nur markieren
                tableView.getSelectionModel().select(row.getItem());

            } else {
                if (startPos.get() == -1) {
                    return;
                }
                if (tableView.getItems().size() <= startPos.get()) {
                    // sind die Zeilen unterhalb der Tabelle
                    return;
                }

                int destPos = row.getIndex();
                if (tableView.getItems().size() <= destPos) {
                    // sind die Zeilen unterhalb der Tabelle
                    return;
                }

                T stationSrc = tableView.getItems().get(startPos.get());
                T stationDest = tableView.getItems().get(destPos);
                int src = fromList.indexOf(stationSrc);
                int dest = fromList.indexOf(stationDest);

                if (dest == src) {
                    return;
                }

                fromList.remove(stationSrc);
                fromList.remove(stationDest);
                if (dest > src) {
                    // nach unten
                    fromList.add(src, stationDest);
                    fromList.add(dest, stationSrc);

                } else if (dest < src) {
                    // nach oben
                    fromList.add(dest, stationSrc);
                    fromList.add(src, stationDest);
                }

                tableView.getSelectionModel().clearSelection();
                tableView.getSelectionModel().select(stationSrc);
            }
        });

        row.setOnMouseDragExited(event -> {
//                    System.out.println("-> setOnMouseDragExited");
//                    System.out.println("Row: " + row.getIndex());
                    if (event.isControlDown()) {
                        startPos.set(row.getIndex());
                    }
                }
        );


        return row;
    }
}
