/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
 * https://sourceforge.net/projects/mtplayer/
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

package de.p2tools.p2Lib.dialog;


import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.tools.ProgramTools;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.List;

public class ProgInfoDialog extends PDialogExtra {

    private final Button btnOk = new Button("Ok");
    private final Button btnGc = new Button("Speicher aufräumen");
    private final Button btnDuration = new Button("Laufzeiten ausgeben");
    private final ProgressBar progressBar = new ProgressBar();
    private final Label lblMemInfo = new Label("");

    private final Runtime rt = Runtime.getRuntime();
    private static final int MEGABYTE = 1000 * 1000;

    public ProgInfoDialog() {
        super(null, "Speicherverbrauch des Programms", false);

        addButtons(btnOk);
        init(getVBoxCompleteDialog(), true);
    }

    public ProgInfoDialog(boolean showDialog) {
        super(null, "Speicherverbrauch des Programms", false);

        addButtons(btnOk);
        init(getVBoxCompleteDialog(), showDialog);
    }


    @Override
    protected void make() {
        final String line = "<==/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\==>";
//        btnOk.setMinWidth(P2LibConst.MIN_BUTTON_WIDTH);
//        btnGc.setMinWidth(P2LibConst.MIN_BUTTON_WIDTH);
//        btnDuration.setMinWidth(P2LibConst.MIN_BUTTON_WIDTH);

        btnOk.setOnAction(a -> close());
        btnGc.setOnAction(a -> System.gc());
        btnDuration.setOnAction(a -> {
            List list = PDuration.getCounter();
            list.add(0, new String(line));
            list.add(new String(line));
            list.add(new String(" "));
            list.add(new String(" "));
            PLog.sysLog(list);
        });
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        int row = 0;

        // Java
        Text text = new Text("Java Informationen");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));
        gridPane.add(text, 0, row, 3, 1);

        gridPane.add(new Label("Version:"), 0, ++row);
        gridPane.add(new Label(System.getProperty("java.version")), 1, row, 2, 1);

        gridPane.add(new Label("Type:"), 0, ++row);
        String strVmType = System.getProperty("java.vm.name");
        strVmType += " (" + System.getProperty("java.vendor") + ")";
        gridPane.add(new Label(strVmType), 1, row, 2, 1);


        // Memory
        gridPane.add(new Label(" "), 0, ++row);
        text = new Text("Speicherverbrauch des Programms");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));
        gridPane.add(text, 0, ++row, 3, 1);

        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setMinWidth(10);
        lblMemInfo.setMaxWidth(Region.USE_PREF_SIZE);

        gridPane.add(btnGc, 0, ++row);
        gridPane.add(progressBar, 1, row);
        gridPane.add(lblMemInfo, 2, row);


        // Laufzeiten
        gridPane.add(new Label(" "), 0, ++row);
        text = new Text("Laufzeiten des Programms");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));
        gridPane.add(text, 0, ++row, 3, 1);
        gridPane.add(btnDuration, 0, ++row);


        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(),
                PColumnConstraints.getCcPrefSize());

        getVboxCont().getChildren().add(gridPane);

        //Update every second...
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(1000), ae -> getMem()));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setDelay(Duration.seconds(1));
        timeline.play();
    }

    private void getMem() {
        final long maxMem;
        if (ProgramTools.getOs() == ProgramTools.OperatingSystemType.LINUX) {
            maxMem = rt.totalMemory();
        } else {
            maxMem = rt.maxMemory();
        }
        final long totalMemory = rt.totalMemory();
        final long freeMemory = rt.freeMemory();
        final long usedMem = totalMemory - freeMemory;

        final long used = usedMem / MEGABYTE;
        final long total = maxMem / MEGABYTE;

        double usedD = 1.0 * usedMem / totalMemory;
        progressBar.setProgress(usedD);

        final String info = used + " von " + total + "MB";
        lblMemInfo.setText(info);
    }

}
