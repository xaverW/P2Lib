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


package de.p2tools.p2Lib.guiTools;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class PProgressBar extends StackPane {
    private double progress = 0;
    private String progressText = "";

    private final ProgressBar progressBar = new ProgressBar();
    private final Text text = new Text();


    public PProgressBar() {
        this.getChildren().setAll(progressBar, text);
        this.setPadding(new Insets(10));
        this.setAlignment(progressBar, Pos.CENTER);
        this.setAlignment(text, Pos.CENTER);
        setProgress();
        progressBar.getStyleClass().add("pProgressBar");

        progressBar.setMinWidth(100);
        progressBar.setMaxWidth(Double.MAX_VALUE);
    }

    public void setProgress(final double progress, final String progressText) {
        this.progress = progress;
        this.progressText = progressText;

        setProgress();
    }

    private void setProgress() {
        text.setText(progressText);
        progressBar.setProgress(progress);

        progressBar.setMinHeight(text.getBoundsInLocal().getHeight() + 5);
        progressBar.setMinWidth(text.getBoundsInLocal().getWidth() + 25);
    }
}

