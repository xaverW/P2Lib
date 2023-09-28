/*
 * P2Tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de
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

package de.p2tools.p2lib.guitools.pnotification;

import de.p2tools.p2lib.guitools.P2GuiTools;
import de.p2tools.p2lib.tools.PException;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.event.WeakEventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

public class P2Notify {

    public final String title;
    public final String message;
    public final Image image;

    public static Stage stage = null;
    public static Stage stageRef = null;
    public static final double ICON_WIDTH = 24;
    public static final double ICON_HEIGHT = 24;
    public static double width = 300;
    public static double offsetX = 15; // The horizontal shift required - The default is 0 px.
    public static double offsetY = 50; // The vertical shift required - The default is 25 px. - Linux braucht 40
    public static double spacingY = 10; // The spacing between multiple Notifications - The default is 5 px.
    public static Pos popupLocation = Pos.BOTTOM_RIGHT;
    public static Duration popupLifetime = Duration.millis(8_000);
    public static Duration popupAnimationTime = Duration.millis(500);
    public static final ObservableList<Popup> popups = FXCollections.observableArrayList();
    public static Scene scene = null;

    private HBox hBoxBottom = null;
    private double height = 80;

    public P2Notify(final String title, final String message, final Image image) {
        this.title = title;
        this.message = message;
        this.image = image;
        if (stage == null) {
            PException.throwPException(912036478, "Notification: stage not set");
        }
        if (scene == null) {
            scene = stage.getScene();
        }
    }

    public P2Notify(final String title, final String message, final Image image, HBox hBoxBottom) {
        this.title = title;
        this.message = message;
        this.image = image;
        this.hBoxBottom = hBoxBottom;
        if (stage == null) {
            PException.throwPException(912036478, "Notification: stage not set");
        }
        if (scene == null) {
            scene = stage.getScene();
        }
    }

    public static void setStage(Stage stage) {
        P2Notify.stage = stage;
    }

    /**
     * Show the given Notification on the screen
     *
     * @param p2Notify
     */
    public void notify(final P2Notify p2Notify) {
        showPopup(p2Notify);
        P2NotificationFactory.preOrder(popupLocation, spacingY);
    }

    /**
     * Creates and shows a popup with the data from the given Notification object
     *
     * @param p2Notify
     */
    private void showPopup(final P2Notify p2Notify) {
        final Popup popup = new Popup();

        Button btnClose = new Button();
        btnClose.getStyleClass().add("close-button");
        btnClose.setOnAction(a -> {
            closeNotification(popup, p2Notify);
        });

        Label title = new Label(p2Notify.title);
        title.getStyleClass().add("title");

        ImageView icon = new ImageView(p2Notify.image);
        icon.setFitWidth(ICON_WIDTH);
        icon.setFitHeight(ICON_HEIGHT);

        Label message = new Label(p2Notify.message, icon);
        message.getStyleClass().add("message");

        HBox hBox = new HBox();
        hBox.getChildren().addAll(title, P2GuiTools.getHBoxGrower(), btnClose);

        final VBox popupLayout = new VBox();
        popupLayout.setSpacing(10);
        popupLayout.setPadding(new Insets(10, 10, 10, 10));
        popupLayout.getChildren().addAll(hBox, message);
        if (hBoxBottom != null) {
            popupLayout.getChildren().add(hBoxBottom);
        }

        StackPane popupContent = new StackPane();
        popupContent.setPrefSize(width, height);
        popupContent.getStyleClass().add("p2Notify");
        popupContent.getChildren().addAll(popupLayout);
        popupContent.setPrefHeight(Region.USE_COMPUTED_SIZE);

        popup.getContent().add(popupContent);
        popup.addEventHandler(MouseEvent.MOUSE_PRESSED, new WeakEventHandler<>(event ->
                fireNotificationEvent(new P2NotificationEvent(p2Notify, this, popup,
                        P2NotificationEvent.NOTIFICATION_PRESSED))
        ));
        popups.add(popup);

        // Add a timeline for popup fade out
        KeyValue fadeOutBegin = new KeyValue(popup.opacityProperty(), 1.0);
        KeyValue fadeOutEnd = new KeyValue(popup.opacityProperty(), 0.0);

        KeyFrame kfBegin = new KeyFrame(Duration.ZERO, fadeOutBegin);
        KeyFrame kfEnd = new KeyFrame(popupAnimationTime, fadeOutEnd);

        Timeline timeline = new Timeline(kfBegin, kfEnd);
        timeline.setDelay(popupLifetime);
        timeline.setOnFinished(actionEvent -> Platform.runLater(() -> {
            closeNotification(popup, p2Notify);
        }));

//            if (stage.isShowing()) {
//                stage.toFront();
//            } else {
//                stage.show();
//            }

        popup.show(stage);
        fireNotificationEvent(new P2NotificationEvent(p2Notify, this, popup, P2NotificationEvent.SHOW_NOTIFICATION));
        timeline.play();
    }

    void closeNotification(Popup POPUP, P2Notify NOTIFICATION) {
        POPUP.hide();
        popups.remove(POPUP);
        fireNotificationEvent(new P2NotificationEvent(NOTIFICATION, this, POPUP, P2NotificationEvent.HIDE_NOTIFICATION));
    }

    /**
     * @param STAGE_REF      The Notification will be positioned relative to the given Stage.<br>
     *                       If null then the Notification will be positioned relative to the primary Screen.
     * @param POPUP_LOCATION The default is TOP_RIGHT of primary Screen.
     */
    public static void setPopupLocation(final Stage STAGE_REF, final Pos POPUP_LOCATION) {
        if (null != STAGE_REF) {
            stageRef = STAGE_REF;
        }
        popupLocation = POPUP_LOCATION;
    }

    /**
     * Sets the Notification's owner stage so that when the owner
     * stage is closed Notifications will be shut down as well.<br>
     * This is only needed if <code>setPopupLocation</code> is called
     * <u>without</u> a stage reference.
     *
     * @param OWNER
     */
    public static void setNotificationOwner(final Stage OWNER) {
//            INSTANCE.stage.initOwner(OWNER);
    }

    /**
     * @param OFFSET_X The horizontal shift required.
     *                 <br> The default is 0 px.
     */
    public static void setOffsetX(final double OFFSET_X) {
        offsetX = OFFSET_X;
    }

    /**
     * @param OFFSET_Y The vertical shift required.
     *                 <br> The default is 25 px.
     */
    public static void setOffsetY(final double OFFSET_Y) {
        offsetY = OFFSET_Y;
    }

    /**
     * @param WIDTH The default is 300 px.
     */
    public static void setWidth(final double WIDTH) {
        width = WIDTH;
    }

    /**
     * @param HEIGHT The default is 80 px.
     */
    public void setHeight(final double HEIGHT) {
        height = HEIGHT;
    }

    public double getHeight() {
        return height;
    }

    /**
     * @param SPACING_Y The spacing between multiple Notifications.
     *                  <br> The default is 5 px.
     */
    public static void setSpacingY(final double SPACING_Y) {
        spacingY = SPACING_Y;
    }

    public void stop() {
        popups.clear();
//            stage.close(); // ist jetzt die rootStage
    }

    /**
     * Returns the PDuration that the notification will stay on screen before it
     * will fade out. The default is 5000 ms
     *
     * @return the PDuration the popup notification will stay on screen
     */
    public Duration getPopupLifetime() {
        return popupLifetime;
    }

    /**
     * Defines the PDuration that the popup notification will stay on screen before it
     * will fade out. The parameter is limited to values between 2 and 20 seconds.
     *
     * @param POPUP_LIFETIME
     */
    public void setPopupLifetime(final Duration POPUP_LIFETIME) {
        popupLifetime = Duration.millis(clamp(2000, 20000, POPUP_LIFETIME.toMillis()));
    }

    /**
     * Returns the PDuration that it takes to fade out the notification
     * The parameter is limited to values between 0 and 1000 ms
     *
     * @return the PDuration that it takes to fade out the notification
     */
    public Duration getPopupAnimationTime() {
        return popupAnimationTime;
    }

    /**
     * Defines the PDuration that it takes to fade out the notification
     * The parameter is limited to values between 0 and 1000 ms
     * Default value is 500 ms
     *
     * @param POPUP_ANIMATION_TIME
     */
    public void setPopupAnimationTime(final Duration POPUP_ANIMATION_TIME) {
        popupAnimationTime = Duration.millis(clamp(0, 1000, POPUP_ANIMATION_TIME.toMillis()));
    }

    /**
     * Returns true if the popup parent stage is always on top
     *
     * @return true if the popup parent stage is always on top
     */
    public boolean isAlwaysOnTop() {
        return stage.isAlwaysOnTop();
    }

    /**
     * Enables/Disables always on top for the popup parent stage
     *
     * @param ALWAYS_ON_TOP
     */
    public void setAlwaysOnTop(final boolean ALWAYS_ON_TOP) {
        stage.setAlwaysOnTop(ALWAYS_ON_TOP);
    }

    /**
     * Makes sure that the given VALUE is within the range of MIN to MAX
     *
     * @param MIN
     * @param MAX
     * @param VALUE
     * @return
     */
    private double clamp(final double MIN, final double MAX, final double VALUE) {
        if (VALUE < MIN) return MIN;
        if (VALUE > MAX) return MAX;
        return VALUE;
    }

    // ******************** Event handling ********************************
    private final ObjectProperty<EventHandler<P2NotificationEvent>> onNotificationPressed = new ObjectPropertyBase<EventHandler<P2NotificationEvent>>() {
        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "onNotificationPressed";
        }
    };
    private final ObjectProperty<EventHandler<P2NotificationEvent>> onShowNotification = new ObjectPropertyBase<EventHandler<P2NotificationEvent>>() {
        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "onShowNotification";
        }
    };
    private final ObjectProperty<EventHandler<P2NotificationEvent>> onHideNotification = new ObjectPropertyBase<EventHandler<P2NotificationEvent>>() {
        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "onHideNotification";
        }
    };

    public final ObjectProperty<EventHandler<P2NotificationEvent>> onNotificationPressedProperty() {
        return onNotificationPressed;
    }

    public final EventHandler<P2NotificationEvent> getOnNotificationPressed() {
        return onNotificationPressedProperty().get();
    }

    public final void setOnNotificationPressed(EventHandler<P2NotificationEvent> value) {
        onNotificationPressedProperty().set(value);
    }

    public final ObjectProperty<EventHandler<P2NotificationEvent>> onShowNotificationProperty() {
        return onShowNotification;
    }

    public final EventHandler<P2NotificationEvent> getOnShowNotification() {
        return onShowNotificationProperty().get();
    }

    public final void setOnShowNotification(EventHandler<P2NotificationEvent> value) {
        onShowNotificationProperty().set(value);
    }

    public final ObjectProperty<EventHandler<P2NotificationEvent>> onHideNotificationProperty() {
        return onHideNotification;
    }

    public final EventHandler<P2NotificationEvent> getOnHideNotification() {
        return onHideNotificationProperty().get();
    }

    public final void setOnHideNotification(EventHandler<P2NotificationEvent> value) {
        onHideNotificationProperty().set(value);
    }

    public void fireNotificationEvent(final P2NotificationEvent EVENT) {
        final EventType<? extends Event> TYPE = EVENT.getEventType();
        final EventHandler<P2NotificationEvent> HANDLER;

        if (P2NotificationEvent.NOTIFICATION_PRESSED == TYPE) {
            HANDLER = getOnNotificationPressed();

        } else if (P2NotificationEvent.SHOW_NOTIFICATION == TYPE) {
            HANDLER = getOnShowNotification();

        } else if (P2NotificationEvent.HIDE_NOTIFICATION == TYPE) {
            HANDLER = getOnHideNotification();

        } else {
            HANDLER = null;
        }
        if (null == HANDLER) return;
        HANDLER.handle(EVENT);
    }
}
