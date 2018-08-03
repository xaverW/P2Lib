/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de/
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


package de.p2tools.p2Lib;

import de.p2tools.p2Lib.guiTools.pCheckComboBox.PCheckComboBox;
import de.p2tools.p2Lib.guiTools.pNotification.Notification;
import de.p2tools.p2Lib.guiTools.pNotification.NotificationBuilder;
import de.p2tools.p2Lib.guiTools.pNotification.NotifierBuilder;
import de.p2tools.p2Lib.guiTools.pRange.PRangeBox;
import de.p2tools.p2Lib.guiTools.pRange.PTimePeriodBox;
import de.p2tools.p2Lib.guiTools.pToggleSwitch.PToggleSwitch;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Random;

public class Demo extends Application {

    private static final Random RND = new Random();
    private static final Notification[] P_NOTIFICATIONS = {
            NotificationBuilder.create().title("Warnung").message("Eine Warnung").image(Notification.WARNING_ICON).build(),
            NotificationBuilder.create().title("Info").message("Eine Information").image(Notification.INFO_ICON).build(),
            NotificationBuilder.create().title("Erfolgreich").message("Hat geklappt").image(Notification.SUCCESS_ICON).build(),
            NotificationBuilder.create().title("Fehler").message("Hat nicht geklappt").image(Notification.ERROR_ICON).build()
    };

    private Notification.Notifier notifier;
    private Button btnNotification;
    private Stage stage;
    private VBox vBoxCont = new VBox();


    // ******************** Initialization ************************************
    @Override
    public void init() {
        btnNotification = new Button("Notify");
        btnNotification.setOnAction(event -> notifier.notify(P_NOTIFICATIONS[RND.nextInt(4)]));
    }


    // ******************** Application start *********************************
    @Override
    public void start(Stage stage) {
        this.stage = stage;

        vBoxCont.setPadding(new Insets(10));
        vBoxCont.setSpacing(20);
        addCont();

        Scene scene = new Scene(vBoxCont);

        stage.setOnCloseRequest(observable -> notifier.stop());
        stage.setScene(scene);
        stage.show();

        startNotify();
    }

    private void startNotify() {
        Notification.setStage(stage);
        notifier = NotifierBuilder.create()
                .popupLocation(Pos.BOTTOM_RIGHT)
                //.popupLifeTime(Duration.millis(10000))
                //.styleSheet(getClass().getResource("mynotification.css").toExternalForm())
                .build();
        notifier.setOnNotificationPressed(event -> System.out.println("Notification pressed: " + event.NOTIFICATION.TITLE));
        notifier.setOnShowNotification(event -> System.out.println("Notification shown: " + event.NOTIFICATION.TITLE));
        notifier.setOnHideNotification(event -> System.out.println("Notification hidden: " + event.NOTIFICATION.TITLE));
    }

    private void addCont() {
        vBoxCont.getChildren().addAll(btnNotification);


        PToggleSwitch pToggleSwitchOn = new PToggleSwitch("Toggle On:");
        pToggleSwitchOn.setSelected(true);
        pToggleSwitchOn.setTooltip(new Tooltip("Tooltip On"));

        PToggleSwitch pToggleSwitchOff = new PToggleSwitch("Toggle Off:");
        pToggleSwitchOff.setSelected(false);
        pToggleSwitchOff.setTooltip(new Tooltip("Tooltip Off"));
        vBoxCont.getChildren().addAll(pToggleSwitchOn, pToggleSwitchOff);


        PCheckComboBox pCheckComboBox = new PCheckComboBox();
        pCheckComboBox.setTooltip(new Tooltip("Tooltip Off"));
        pCheckComboBox.addItem("Item 1", new SimpleBooleanProperty());
        pCheckComboBox.addItem("Item 2", new SimpleBooleanProperty());
        pCheckComboBox.addItem("Item 3", new SimpleBooleanProperty());
        vBoxCont.getChildren().addAll(pCheckComboBox);

        PTimePeriodBox pTimePeriodBox = new PTimePeriodBox();
        vBoxCont.getChildren().add(pTimePeriodBox);

        PRangeBox pRangeBox = new PRangeBox(0, 150);
        pRangeBox.setVluePrefix("Auszeit: ");
        pRangeBox.setUnitSuffix(" s");
        vBoxCont.getChildren().add(pRangeBox);
    }

    @Override
    public void stop() {
    }

    public static void main(String[] args) {
        launch(args);
    }
}
