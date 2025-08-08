package de.p2tools.p2lib.guitools;

import de.p2tools.p2lib.P2LibConst;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class P2Text {
    private P2Text() {
    }

    public static Text getTextBold(String t) {
        Text text = new Text(t);
        text.setFont(Font.font(null, FontWeight.BOLD, -1));
        if (P2LibConst.fontSize.get() > 0) {
            text.setStyle("-fx-font-size: " + P2LibConst.fontSize.get() + " ;");
        }
        return text;
    }
}
