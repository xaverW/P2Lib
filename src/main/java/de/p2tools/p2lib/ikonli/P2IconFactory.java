package de.p2tools.p2lib.ikonli;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.guitools.P2Button;
import javafx.scene.control.Button;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

public class P2IconFactory {

    public interface P2Icon {
        public String getLiteral();

        public int getSize();

        public void setSize(int size);

        public FontIcon getFontIcon();

        public FontIcon getFontIcon(int size);

        @Override
        public String toString();
    }

    public enum P2ICON implements P2Icon {
        BTN_HELP("mdi-help"),
        BTN_CLEAR_FILTER("mdi-filter-remove-outline"),
        BTN_DIR_OPEN("mdi2f-folder-open-outline", 18),
        BTN_STOP("gmi-close", 18),
        BTN_DEL("gmi-close", 12),
        BTN_CLOSE("gmi-close", 8),
        BTN_DIALOG_CLOSE("gmi-close", 14),
        BTN_DIALOG_RIP("gmi-arrow-right", 22),
        BTN_NEXT("mdoal-fast_forward", 18),
        BTN_PREV("mdoal-fast_rewind", 18),
        ATTENTION("mdomz-report_problem", 65);

        private final String literal;
        private int size = 18;

        P2ICON(String literal) {
            this.literal = literal;
        }

        P2ICON(String literal, int size) {
            this.literal = literal;
            this.size = size;
        }

        public String getLiteral() {
            return literal;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public FontIcon getFontIcon() {
            return getIcon(literal, size);
        }

        public FontIcon getFontIcon(int size) {
            this.size = size;
            return getIcon(literal, size);
        }

        @Override
        public String toString() {
            return literal;
        }
    }

    private P2IconFactory() {
    }

    static int count = 0;

    public static FontIcon getIcon(String literal, int size) {
        FontIcon fontIcon = new FontIcon();
        fontIcon.setIconSize(size);
        fontIcon.setIconColor(Paint.valueOf(P2LibConst.iconColor.getValueSafe()));
        fontIcon.setIconLiteral(literal);
        return fontIcon;
    }

    public static Button getHelpButton(String header, String helpText) {
        return P2Button.helpButton(P2ICON.BTN_HELP.getFontIcon(), header, helpText);
    }

    public static Button getHelpButton(Stage stage, String header, String helpText) {
        return P2Button.helpButton(stage, P2ICON.BTN_HELP.getFontIcon(), header, helpText);
    }
}
