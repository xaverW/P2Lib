package de.p2tools.p2lib.ikonli;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.tools.log.P2Log;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;

public class IkonlyFactory {

    private IkonlyFactory() {
    }

    public static void getAllNodes(Parent root) {
        Paint paint;
        try {
            // Paint.valueOf(ProgConfig.SYSTEM_ICON_COLOR.getValueSafe())
            paint = Color.web(P2LibConst.iconColor.getValueSafe());
        } catch (Exception e) {
            P2Log.errorLog(656564541, e.getMessage());
            return;
        }
        ArrayList<Node> nodes = new ArrayList<>();
        add(root, nodes);
        int i = 0;
        for (Node node : nodes) {
            if (node.getClass().equals(FontIcon.class)) {
                ((FontIcon) node).setIconColor(paint);
                ++i;
            }
        }
        P2Log.sysLog("FontIcon von --> " + i + " <-- Nodes geändert");
    }

    private static void add(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent)
                add((Parent) node, nodes);
        }
    }
}


