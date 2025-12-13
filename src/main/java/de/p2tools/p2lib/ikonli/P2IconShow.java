package de.p2tools.p2lib.ikonli;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.dialogs.dialog.P2DialogExtra;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

public class P2IconShow extends P2DialogExtra {

    public P2IconShow() {
        super(P2LibConst.primaryStage, null, "P2Icons");
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

        int row = 0;
        int col = 0;
        for (P2IconFactory.P2ICON p2Icon : P2IconFactory.P2ICON.values()) {
            gridPane.add(new Label(p2Icon.getLiteral()), col++, row);
            gridPane.add(p2Icon.getFontIcon(), col++, row);
            if (col > 2) {
                col = 0;
                row++;
            }
        }

        getVBoxCont().getChildren().add(scrollPane);
    }
}