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

//        @Override
//        public String toString();
    }

    public enum P2ICON implements P2Icon {
        BTN_P2DIALOG_CLOSE("gmi-close", 14), // Infopane: Schließen
        BTN_P2DIALOG_RIP("gmi-arrow-right", 22), // Infopane: Abkoppeln
        BTN_P2NOTIFY_CLOSE("gmi-close", 8),
        BTN_P2CBO_DEL("gmi-close", 12),

        BTN_CLEAR_FILTER("mdi2f-filter-remove-outline", 18),
        BTN_CLEAR_FILTER_15("mdi2f-filter-remove-outline", 15),
        BTN_CLEAR("gmi-clear", 18),
        BTN_HELP("mdi-help"),
        BTN_OPEN_DIR("mdi2f-folder-open-outline", 18),

        BTN_STOP("mdmz-stop", 18),
        BTN_SAVE("mdomz-save", 18),
        BTN_FORWARD("gmi-arrow-forward-ios", 18),
        BTN_BACKWARD("gmi-arrow-back-ios", 18),
        BTN_FORWARD_15("gmi-arrow-forward-ios", 15),
        BTN_BACKWARD_15("gmi-arrow-back-ios", 15),
        BTN_NEXT("mdi-chevron-double-right", 18),
        BTN_PREV("mdi-chevron-double-left", 18),
        BTN_FIRST("gmi-first-page", 18),
        BTN_LAST("gmi-last-page", 18),
        BTN_SEARCH("gmi-search", 20),
        BTN_SEARCH_15("gmi-search", 15),
        BTN_EDIT("mdomz-settings", 18),
        BTN_EDIT_15("mdomz-settings", 15),
        BTN_RESET("gmi-rotate-right", 18),
        BTN_PLUS("mdal-add", 20),
        BTN_MINUS("mdmz-minus", 20),
        BTN_PLUS_15("mdal-add", 15),
        BTN_MINUS_15("mdmz-minus", 15),
        BTN_PLUS_OUTLINE("mdi-plus-circle-outline", 18),
        BTN_MINUS_OUTLINE("mdi-minus-circle-outline", 18),
        BTN_TOP("gmi-vertical-align-top", 18),
        BTN_UP("mdoal-arrow_upward", 18),
        BTN_DOWN("mdoal-arrow_downward", 18),
        BTN_BOTTOM("gmi-vertical-align-bottom", 18),
        BTN_QUIT("gmi-power-settings-new", 18),

        PROG_MENU("gmi-menu", 25),
        TAB_MENU("gmi-menu", 20),

        SMALL_GUI_ICON_30("mdmz-sports_basketball", 30),
        SMALL_GUI_ICON_25("mdmz-sports_basketball", 25),

        ATTENTION_80("mdomz-report", 80),
        ATTENTION_65("mdomz-report_problem", 65);

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
