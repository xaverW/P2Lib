package de.p2tools.p2lib.ikonli;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.dialogs.dialog.P2DialogExtra;
import de.p2tools.p2lib.guitools.P2Button;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

public class P2IconFactory {

    public interface P2Icon {
        public String getLiteral();

        public int getSize();

        public FontIcon getFontIcon();

        public FontIcon getFontIcon(int size);

        @Override
        public String toString();
    }

    public enum P2ICON implements P2Icon {
        // ========
        // P2
        P2__P2DIALOG_CLOSE("gmi-close", 14), // Infopane: Schließen
        P2__P2DIALOG_RIP("gmi-arrow-right", 22), // Infopane: Abkoppeln
        P2__P2NOTIFY_CLOSE("gmi-close", 8),
        P2__P2CBO_DEL("gmi-close", 12),
        P2__DIALOG_RESET("mdoal-forward", 30),
        P2__PROG_MENU("gmi-menu", 25),
        P2__TAB_MENU("gmi-menu", 20),

        P2__TIP_FIRST("mdi-chevron-double-left", 18),
        P2__TIP_LAST("mdi-chevron-double-right", 18),
        P2__TIP_PREV("gmi-arrow-left", 18),
        P2__TIP_NEXT("gmi-arrow-right", 18),

        P2__SMALL_GUI_ICON_30("mdmz-sports_basketball", 30),
        P2__SMALL_GUI_ICON_25("mdmz-sports_basketball", 25),

        P2__ATTENTION_OCT_80("mdomz-report", 80),
        P2__ATTENTION_TRI_65("mdomz-report_problem", 65),


        P2_BTN_BOOKMARK_ADD("mdi-bookmark-plus-outline", 18),
        P2_BTN_BOOKMARK_ADD_B("mdi-bookmark-plus-outline", 25),
        P2_BTN_BOOKMARK_DEL("mdi2b-bookmark-minus-outline", 18),
        P2_BTN_BOOKMARK_DEL_B("mdi2b-bookmark-minus-outline", 25),
        P2_BTN_BOOKMARK_DEL_ALL("mdi2b-bookmark-off-outline", 18),
        P2_BTN_BOOKMARK_DEL_ALL_B("mdi2b-bookmark-off-outline", 25),
        P2_BTN_BOOKMARK_SHOW("mdoal-collections_bookmark", 18),
        P2_BTN_BOOKMARK_SHOW_B("mdoal-collections_bookmark", 25),

        P2_BTN_OWN_FILTER_LOAD("mdi2t-tray-arrow-up", 20),
        P2_BTN_OWN_FILTER_SAVE("mdi2t-tray-arrow-down", 20),
        P2_BTN_OWN_FILTER_NEW("gmi-add", 20),

        P2_BTN_BOOLEAN_ON("mdral-done_outline", 20),
        P2_BTN_BOOLEAN_OFF("gmi-remove-done", 20),
        P2_BTN_SAVE("mdomz-save", 18),
        P2_BTN_OPEN_DIR("mdi2f-folder-open-outline", 18),
        P2_BTN_OPEN_DIR_S("mdi2f-folder-open-outline", 14),
        P2_BTN_OPEN_DIR_B("mdi2f-folder-open-outline", 25),
        P2_BTN_HELP("mdi-help"),
        P2_BTN_PLAY_ALL("gmi-fast-forward", 18),
        P2_BTN_PLAY_ALL_B("gmi-fast-forward", 25),
        P2_BTN_PLAY("mdmz-play_arrow", 18),
        P2_BTN_PLAY_S("mdmz-play_arrow", 14),
        P2_BTN_PLAY_B("mdmz-play_arrow", 25),
        P2_BTN_STOP("mdmz-stop", 18),
        P2_BTN_STOP_S("mdmz-stop", 15),
        P2_BTN_STOP_B("mdmz-stop", 25),
        P2_BTN_PLAY_OUTLINE("mdomz-play_arrow", 18),
        P2_BTN_STOP_OUTLINE("mdomz-stop", 18),
        P2_BTN_PAUSE("mdomz-pause", 18),
        P2_BTN_PAUSE_B("mdomz-pause", 25),
        P2_BTN_EDIT("mdomz-settings", 20),
        P2_BTN_EDIT_S("mdomz-settings", 14),
        P2_BTN_EDIT_B("mdomz-settings", 25),
        P2_BTN_RECORD("gmi-fiber-manual-record", 18),
        P2_BTN_RECORD_S("gmi-fiber-manual-record", 14),
        P2_BTN_RECORD_B("gmi-fiber-manual-record", 25),
        P2_BTN_DOWNLOAD("mdi2d-download-outline", 18),
        P2_BTN_DOWNLOAD_B("mdi2d-download-outline", 25),
        P2_BTN_DOWNLOAD_ALL("mdi2d-download-multiple-outline", 18),
        P2_BTN_DOWNLOAD_ALL_B("mdi2d-download-multiple-outline", 25),
        P2_BTN_UPDATE("mdal-cached", 18),
        P2_BTN_UPDATE_B("mdal-cached", 25),
        P2_BTN_RESET_PATH("mdi2c-cog-counterclockwise", 18),
        P2_BTN_CLEAR_FILTER("mdi2f-filter-remove-outline", 20),
        P2_BTN_CLEAR_FILTER_S("mdi2f-filter-remove-outline", 15),
        P2_BTN_CLEAR_FILTER_B("mdi2f-filter-remove-outline", 25),
        P2_BTN_QUIT_GUI("gmi-power-settings-new", 18),
        P2_BTN_QUIT_GUI_B("gmi-power-settings-new", 25),
        P2_BTN_GRADE("gmi-grade", 18),
        P2_BTN_GRADE_B("gmi-grade", 25),
        P2_BTN_GRADE_OFF("mdi2s-star-off-outline", 18),
        P2_BTN_GRADE_OFF_B("mdi2s-star-off-outline", 25),
        P2_BTN_INFO("mdi-information-variant", 18),
        P2_BTN_INFO_B("mdi-information-variant", 25),
        P2_BTN_INFO_OUTLINE("gmi-info-outline", 18),
        P2_BTN_INFO_OUTLINE_B("gmi-info-outline", 25),

        // ========,
        // Allgemein,
        BTN_CLEAR("gmi-clear", 18),
        BTN_CLEAR_S("gmi-clear", 14),
        BTN_CLEAR_B("gmi-clear", 25),
        BTN_SEARCH("gmi-search", 18),
        BTN_SEARCH_B("gmi-search", 20),

        BTN_NEXT("mdi-chevron-double-right", 18),
        BTN_NEXT_S("mdi-chevron-double-right", 15),
        BTN_PREV("mdi-chevron-double-left", 18),
        BTN_FIRST("gmi-first-page", 18),
        BTN_LAST("gmi-last-page", 18),

        BTN_ROTATE_RIGHT("gmi-rotate-right", 18),
        BTN_ROTATE_LEFT("mdomz-rotate_left", 18),
        BTN_ROTATE_3D("mdi-rotate-3d", 18),
        BTN_ROTATE_3D_B("mdi-rotate-3d", 25),
        BTN_ROTATE_ORBIT("mdi2r-rotate-orbit", 18),
        BTN_ROTATE_ORBIT_B("mdi2r-rotate-orbit", 25),

        BTN_PLUS("mdal-add", 18),
        BTN_PLUS_S("mdal-add", 15),
        BTN_PLUS_B("mdal-add", 25),
        BTN_MINUS("mdmz-minus", 18),
        BTN_MINUS_S("mdmz-minus", 15),
        BTN_MINUS_B("mdmz-minus", 25),
        BTN_PLUS_OUTLINE("mdi-plus-circle-outline", 18),
        BTN_MINUS_OUTLINE("mdi-minus-circle-outline", 18),

        BTN_ARROW_FORWARD("gmi-arrow-forward-ios", 18),
        BTN_ARROW_FORWARD_B("gmi-arrow-forward-ios", 25),
        BTN_ARROW_BACKWARD("gmi-arrow-back-ios", 18),
        BTN_ARROW_BACKWARD_B("gmi-arrow-back-ios", 25),

        BTN_ARROW_UP("mdoal-arrow_upward", 18),
        BTN_ARROW_UP_OUTLINE("mdi2a-arrow-up-bold-outline", 18),
        BTN_ARROW_UP_OUTLINE_B("mdi2a-arrow-up-bold-outline", 25),

        BTN_ARROW_DOWN("mdoal-arrow_downward", 18),
        BTN_ARROW_DOWN_OUTLINE("mdi2a-arrow-down-bold-outline", 18),
        BTN_ARROW_DOWN_OUTLINE_B("mdi2a-arrow-down-bold-outline", 25),

        BTN_ARROW_LEFT("mdoal-arrow_back", 18),
        BTN_ARROW_LEFT_OUTLINE("mdi2a-arrow-left-bold-outline", 18),
        BTN_ARROW_LEFT_OUTLINE_B("mdi2a-arrow-left-bold-outline", 25),

        BTN_ARROW_RIGHT("mdoal-arrow_forward", 18),
        BTN_ARROW_RIGHT_OUTLINE("mdi2a-arrow-right-bold-outline", 18),
        BTN_ARROW_RIGHT_OUTLINE_B("mdi2a-arrow-right-bold-outline", 25),

        BTN_ARROW_LEFT_TOP("mdi2a-arrow-left-top-bold", 18),
        BTN_ARROW_LEFT_TOP_B("mdi2a-arrow-left-top-bold", 25),
        BTN_ARROW_RIGHT_TOP("mdi2a-arrow-right-top-bold", 18),
        BTN_ARROW_RIGHT_TOP_B("mdi2a-arrow-right-top-bold", 25),
        BTN_ARROW_U_LEFT_TOP("mdi2a-arrow-u-left-top-bold", 18),
        BTN_ARROW_U_LEFT_TOP_B("mdi2a-arrow-u-left-top-bold", 25),
        BTN_ARROW_U_RIGHT_TOP("mdi2a-arrow-u-right-top-bold", 18),
        BTN_ARROW_U_RIGHT_TOP_B("mdi2a-arrow-u-right-top-bold", 25),

        BTN_DOUBLE_ARROW_UP("mdi-chevron-double-up", 18),
        BTN_DOUBLE_ARROW_UP_S("mdi-chevron-double-up", 15),
        BTN_DOUBLE_ARROW_DOWN("mdi-chevron-double-down", 18),
        BTN_DOUBLE_ARROW_DOWN_S("mdi-chevron-double-down", 15),
        BTN_DOUBLE_ARROW_LEFT("mdi-chevron-double-left", 18),
        BTN_DOUBLE_ARROW_LEFT_S("mdi-chevron-double-left", 15),
        BTN_DOUBLE_ARROW_RIGHT("mdi-chevron-double-right", 18),
        BTN_DOUBLE_ARROW_RIGHT_S("mdi-chevron-double-right", 15),

        BTN_DOUBLE_ARROW("mdal-double_arrow", 18),

        BTN_TRIANGLE_DOWN("mdi2t-triangle-down", 14),
        BTN_TRIANGLE_DOWN_S("mdi2t-triangle-down", 12),
        BTN_TRIANGLE_UP("mdi2t-triangle", 14),
        BTN_TRIANGLE_UP_S("mdi2t-triangle", 12),
        BTN_TRIANGLE_OUTLINE_DOWN("mdi2t-triangle-down-outline", 14),
        BTN_TRIANGLE_OUTLINE_DOWN_S("mdi2t-triangle-down-outline", 12),
        BTN_TRIANGLE_OUTLINE_UP("mdi-triangle-outline", 14),
        BTN_TRIANGLE_OUTLINE_UP_S("mdi-triangle-outline", 12),


        BTN_TOP("gmi-vertical-align-top", 18),
        BTN_UP("mdoal-arrow_upward", 18),
        BTN_DOWN("mdoal-arrow_downward", 18),
        BTN_BOTTOM("gmi-vertical-align-bottom", 18),
        BTN_UP_OUTLINE("mdi2u-upload-outline", 18),
        BTN_UP_OUTLINE_B("mdi2u-upload-outline", 25),
        BTN_BOTTOM_OUTLINE("mdi2d-download-outline", 18),
        BTN_BOTTOM_OUTLINE_B("mdi2d-download-outline", 25),

        BTN_COPY("mdoal-file_copy", 18),
        BTN_COPY_S("mdoal-file_copy", 15),
        BTN_CLEAN("mdoal-cleaning_services", 18),
        BTN_CLEAN_B("mdoal-cleaning_services", 25),

        BTN_REPLAY("mdi2r-reply", 18),
        BTN_REPLAY_ALL("mdi2r-reply-all", 18),
        BTN_SHARE("mdi2s-share", 18),
        BTN_SHARE_ALL("mdi2s-share-all", 18);

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

        public FontIcon getFontIcon() {
            return getIcon(literal, size);
        }

        public FontIcon getFontIcon(int size) {
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

    public static void setColor(Pane pane) {
        IkonlyFactory.getAllNodes(pane);
        P2DialogExtra.getDialogList().forEach(d -> IkonlyFactory.getAllNodes(d.getStage().getScene().getRoot()));
    }

    public static Button getHelpButton(String header, String helpText) {
        return P2Button.helpButton(P2ICON.P2_BTN_HELP.getFontIcon(), header, helpText);
    }

    public static Button getHelpButton(Stage stage, String header, String helpText) {
        return P2Button.helpButton(stage, P2ICON.P2_BTN_HELP.getFontIcon(), header, helpText);
    }

    public static Button getHelpButton(ObjectProperty<Stage> stage, String header, String helpText) {
        return P2Button.helpButton(stage, P2IconFactory.P2ICON.P2_BTN_HELP.getFontIcon(), header, helpText);
    }

    public static Button getHelpButtonSmall(Stage stage, String header, String helpText) {
        return P2Button.helpButtonSmall(stage, header, helpText);
    }
}
