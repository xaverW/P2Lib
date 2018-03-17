/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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


import de.p2tools.p2Lib.tools.Functions;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class MemoryUsageDialog extends MTDialogExtra {

    private final Button btnOk = new Button("Ok");
    private final Button btnGc = new Button("Speicher aufräumen");
    private final ProgressBar progressBar = new ProgressBar();
    private final Label lblMemInfo = new Label("");

    private final Runtime rt = Runtime.getRuntime();
    private static final int MEGABYTE = 1000 * 1000;
    private final Color GRAY = Color.DARKSLATEGRAY;


    public MemoryUsageDialog() {
        super(null, null, "Speicherverbrauch des Programms", false);

        getTilePaneOk().getChildren().addAll(btnGc, btnOk);
        init(getvBoxDialog(), true);
    }


    @Override
    public void make() {
        btnOk.setOnAction(a -> close());
        btnGc.setOnAction(a -> System.gc());

        final GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        int row = 0;

        // Java
        Text text = new Text("Java Informationen");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));
        gridPane.add(text, 0, row, 2, 1);

        text = new Text("Version:");
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, 0, ++row);

        text = new Text(System.getProperty("java.version"));
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, 1, row);

        text = new Text("Type:");
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, 0, ++row);

        String strVmType = System.getProperty("java.vm.name");
        strVmType += " (" + System.getProperty("java.vendor") + ")";
        text = new Text(strVmType);
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, 1, row);

        gridPane.add(new Label(" "), 1, ++row);

        // Memory
        text = new Text("Speicherverbrauch des Programms");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));
        gridPane.add(text, 0, ++row, 2, 1);

        progressBar.setPrefWidth(200);
        GridPane.setHalignment(btnGc, HPos.RIGHT);
        gridPane.add(progressBar, 0, ++row);
        gridPane.add(btnGc, 1, row);
        gridPane.add(lblMemInfo, 0, ++row, 2, 1);

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
        if (Functions.getOs() == Functions.OperatingSystemType.LINUX) {
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
