package de.p2tools.p2lib.ikonli;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.dialogs.dialog.P2DialogExtra;
import de.p2tools.p2lib.guitools.P2Text;
import de.p2tools.p2lib.guitools.grid.P2GridConstraints;
import de.p2tools.p2lib.tools.P2ToolsFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class P2IconShow extends P2DialogExtra {

    private static int SIZE = 25;
    private final List<P2IconFactory.P2Icon> p2Icon;

    public P2IconShow(List<P2IconFactory.P2Icon> p2Icon) {
        super(P2LibConst.primaryStage, new SimpleStringProperty("1000:800"), "Icons");
        this.p2Icon = p2Icon;
        init(true);
    }

    @Override
    public void make() {
        Button btnOk = new Button("OK");
        btnOk.setOnAction(a -> close());
        addOkButton(btnOk);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(10);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(gridPane);

        final int maxCol = 4;
        int row = 0;
        int col = 0;


        Label t1 = P2Text.getLblTextBoldUnderline("P2ICON");
        t1.setPadding(new Insets(5));
        gridPane.add(t1, 0, row++);
        for (P2IconFactory.P2ICON p2Icon : P2IconFactory.P2ICON.values()) {
            gridPane.add(getContent(p2Icon), col++, row);
            if (col > maxCol) {
                col = 0;
                row++;
            }
        }
        gridPane.add(new Label(""), 0, ++row);
        Label t2 = P2Text.getLblTextBoldUnderline("PICON");
        t2.setPadding(new Insets(5));
        gridPane.add(t2, 0, ++row);

        if (p2Icon != null) {
            ++row;
            col = 0;
            for (P2IconFactory.P2Icon pIcon : p2Icon) {
                gridPane.add(getContent(pIcon), col++, row);
                if (col > maxCol) {
                    col = 0;
                    row++;
                }
            }
        }

        getVBoxCont().getChildren().add(scrollPane);
    }

    private VBox getContent(P2IconFactory.P2Icon pIcon) {
        VBox vBox = new VBox(3);
        vBox.setStyle("-fx-border-color: black;");

        GridPane gridPane = new GridPane(5, 2);
        gridPane.getColumnConstraints().addAll(P2GridConstraints.getCcPrefSize(), P2GridConstraints.getCcComputedSizeAndHgrowLeft());

        Label lbl = P2Text.getLblTextBold(pIcon.toString());
        lbl.setOnMouseClicked(event -> P2ToolsFactory.copyToClipboard(pIcon.toString()));

        int row = 0;
        gridPane.add(pIcon.getFontIcon(), 0, row);
        gridPane.add(lbl, 1, row);
        gridPane.add(new Label("Größe:"), 0, ++row);
        gridPane.add(new Label(pIcon.getSize() + ""), 1, row);
        gridPane.add(new Label("Literal:"), 0, ++row);
        gridPane.add(new Label(pIcon.getLiteral()), 1, row);
        vBox.getChildren().add(gridPane);

        return vBox;
    }
}