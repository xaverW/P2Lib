/*
 * P2tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2lib.icons;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.tools.log.P2Log;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class P2Image {
    public static String BW_WHITE = "__bw_w";
    public static String BW_BLACK = "__bw_b";
    private static boolean checkDone = false;
    private final String longPath;
    private final String fileName;
    private final String fileNameDark;
    private int w = 0;
    private int h = 0;

    public P2Image(String longPath, String fileName) {
        this.longPath = longPath;
        this.fileName = fileName;
        this.fileNameDark = "";
        check();
    }

    public P2Image(String longPath, String fileName, int w, int h) {
        this.longPath = longPath;
        this.fileName = fileName;
        this.fileNameDark = "";
        this.w = w;
        this.h = h;
        check();
    }

    public P2Image(String longPath, String fileName, String fileNameDark, int w, int h) {
        this.longPath = longPath;
        this.fileName = fileName;
        this.fileNameDark = fileNameDark;
        this.w = w;
        this.h = h;
        check();
    }

    public String getLongPath() {
        return longPath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileNameDark() {
        return fileNameDark;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public Image getImage() {
        try {
            if (P2LibConst.darkMode.get() && !fileNameDark.isEmpty()) {
                Image image = new Image(longPath + fileNameDark, w, h, false, true);
                return image;
            } else {
                Image image = new Image(longPath + fileName, w, h, false, true);
                return image;
            }
        } catch (Exception ex) {
            P2Log.errorLog(959587451, ex.getLocalizedMessage());
        }
        return null;
    }

    public Image getBwImage() {
        try {
            String fn = fileName;

            if (P2LibConst.darkMode.get()) {
                fn = fn.replace(".png", BW_BLACK + ".png");
            } else {
                fn = fn.replace(".png", BW_WHITE + ".png");
            }
            Image image = new Image(longPath + fn, w, h, false, true);
            return image;
        } catch (Exception ex) {
            P2Log.errorLog(912547895, ex.getLocalizedMessage());
        }
        return null;
    }

    public Image getImage(int w, int h) {
        setW(w);
        setH(h);
        return getImage();
    }

    public ImageView getImageView(int w, int h) {
        Image img = getImage(w, h);
        if (img == null) {
            return new ImageView();
        }

        return new P2ImageView(this, img);
    }

    public ImageView getImageView() {
        Image img = getImage();
        if (img == null) {
            return new ImageView();
        }

        return new P2ImageView(this, img);
    }

//    public P2ImageView getImageView() {
//        Image img = getImage();
//        if (img == null) {
//            return new P2ImageView(this);
//        }
//
//        if (P2LibConst.blackWhite.get()) {
//            Image imgBw = getBwImage();
//            if (imgBw == null) {
//                return new P2ImageView();
//            }
//            P2ImageView imageView = new P2ImageView(this, imgBw);
//            imageView.setClip(new P2ImageView(this, imgBw));
//            return imageView;
//        }
//
//        P2ImageView imageView = new P2ImageView(this,img);
//        imageView.setClip(new P2ImageView(this, img));
//        return imageView;
//    }

    public static void check() {
        if (checkDone) {
            return;
        }
        checkDone = true; // damit's nur einmal gesetzt wird
        P2LibConst.darkMode.addListener((u, o, n) -> {
            getAllNodes(P2LibConst.primaryStage.getScene().getRoot()); // todo actStage??
        });
        P2LibConst.blackWhite.addListener((u, o, n) -> {
            getAllNodes(P2LibConst.primaryStage.getScene().getRoot()); // todo actStage??
        });
    }

    public static void getAllNodes(Parent root) {
        ArrayList<Node> nodes = new ArrayList<>();
        add(root, nodes);
        int i = 0;
        for (Node node : nodes) {
            if (node.getClass().equals(P2ImageView.class)) {
                ((P2ImageView) node).setColor();
                ++i;
            }
        }
        P2Log.sysLog("Image von --> " + i + " <-- Nodes geändert");
    }

    private static void add(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent)
                add((Parent) node, nodes);
        }
    }
}
