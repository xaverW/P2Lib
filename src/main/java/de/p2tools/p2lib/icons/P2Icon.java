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
import javafx.scene.CacheHint;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.net.URL;

public abstract class P2Icon {
    private final String path;
    private final String longPath;
    private final String fileName;
    private final String fileNameDark;
    private String url = "";
    private int w = 0;
    private int h = 0;

//    public P2Icon(String longPath, String path, String fileName) {
//        this.longPath = longPath;
//        this.path = path;
//        this.fileName = fileName;
//        this.fileNameDark = "";
//    }

    public P2Icon(String longPath, String path, String fileName, int w, int h) {
        this.longPath = longPath;
        this.path = path;
        this.fileName = fileName;
        this.fileNameDark = "";
        this.w = w;
        this.h = h;
    }

    public P2Icon(String longPath, String path, String fileName, String fileNameDark, int w, int h) {
        this.longPath = longPath;
        this.path = path;
        this.fileName = fileName;
        this.fileNameDark = fileNameDark;
        this.w = w;
        this.h = h;
    }

    public String getPath() {
        return path;
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

    public String getPathFileName() {
        return path + fileName;
    }

    public String getPathFileNameDark() {
        return path + fileNameDark;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Image getImage() {
        if (url.isEmpty()) {
            return null;
        }
        return new Image(url, w, h, false, true);
    }

    public Image getImage(int w, int h) {
        setW(w);
        setH(h);
        if (url.isEmpty()) {
            return null;
        }
        return new Image(url, w, h, false, true);
    }

    public ImageView getImageView() {
        Image img = getImage();
        if (img == null) {
            return new ImageView();
        }

        ImageView imageView = new ImageView(img);
        imageView.setClip(new ImageView(img));

        if (P2LibConst.blackWhite.get()) {
            ColorAdjust monochrome = new ColorAdjust();
            monochrome.setSaturation(-1.0);

            Blend blush = new Blend(
                    BlendMode.MULTIPLY,
                    monochrome,
                    new ColorInput(
                            0,
                            0,
                            imageView.getImage().getWidth(),
                            imageView.getImage().getHeight(),
                            Color.DARKGRAY
                    )
            );
            imageView.setEffect(blush);
            imageView.setCache(true);
            imageView.setCacheHint(CacheHint.SPEED);
        }

        return imageView;
    }

    public ImageView getImageView_() {
        Image img = getImage();
        if (img == null) {
            return new ImageView();
        }
        return new ImageView(img);
    }

    public ImageView getImageView(int w, int h) {
        setW(w);
        setH(h);
        Image img = getImage();
        if (img == null) {
            return new ImageView();
        }
        return new ImageView(img);
    }

    public String genUrl(Class<?>... clazzAr) {
        String fileName = getFileNameDark().isEmpty() ? getFileName() : getFileNameDark();
        String p;
        p = getPath() + fileName;
        if (searchUrl(p, clazzAr)) {
            return getUrl();
        }

        p = getLongPath() + fileName;
        if (searchUrl(p, clazzAr)) {
            return getUrl();
        }

        p = "/" + getLongPath() + fileName;
        if (searchUrl(p, clazzAr)) {
            return getUrl();
        }

        return "";
    }

    public boolean searchUrl(String p, Class<?>... clazzAr) {
        URL url;

        // getResource
        for (Class<?> clazz : clazzAr) {
            url = clazz.getResource(p);
            if (set(url, p, "clazz.getResource: " + clazz)) return true;
        }

        url = this.getClass().getResource(p);
        if (set(url, p, "this.getClass().getResource")) return true;

        // getClassLoader
        for (Class<?> clazz : clazzAr) {
            url = clazz.getClassLoader().getResource(p);
            if (set(url, p, "clazz.getClassLoader().getResource: " + clazz)) return true;
        }

        url = ClassLoader.getSystemResource(p);
        if (set(url, p, "ClassLoader.getSystemResource")) return true;

        url = this.getClass().getClassLoader().getResource(p);
        if (set(url, p, "this.getClass().getClassLoader().getResource")) return true;

        return false;
    }

    public boolean set(URL url, String p, String caller) {
        if (url != null) {
            setUrl(url.toExternalForm());
//            System.out.println("------------------>");
//            System.out.println("caller: " + caller);
//            System.out.println("path: " + p);
//            System.out.println("url: " + url);
            return true;
        }
        return false;
    }
}
