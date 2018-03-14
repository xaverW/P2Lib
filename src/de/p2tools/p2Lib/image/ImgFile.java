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


package de.p2tools.p2Lib.image;

import de.p2tools.p2Lib.dialog.PAlert;
import de.p2tools.p2Lib.tools.Log;
import javafx.application.Platform;

import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

public class ImgFile {

    public enum ImgFormat {

        JPG("jpg"), PNG("png");
        private final String suff;

        ImgFormat(String suff) {
            this.suff = suff;
        }

        @Override
        public String toString() {
            return suff;
        }
    }

    public static final String IMAGE_FORMAT_JPG = "jpg";
    public static final String IMAGE_FORMAT_PNG = "png";

    public static final BufferedImage cloneImage(BufferedImage image) {
        BufferedImage clone = new BufferedImage(image.getWidth(),
                image.getHeight(), image.getType());
        Graphics2D g2d = clone.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return clone;
    }


    public static BufferedImage getBufferedImage(File source) {
        BufferedImage img = null;
        ImageReader reader = getReader(source);
        try {
            img = reader.read(0);
        } catch (Exception ex) {
            Log.errorLog(461214587, ex);
        }
        reader.dispose();
        return img;
    }

    public static BufferedImage getBufferedImage(int destWidth, int destHeight, String borderColorStr) {
        Color borderColor;
        try {
            javafx.scene.paint.Color fx = javafx.scene.paint.Color.web(borderColorStr);
            borderColor = new java.awt.Color((float) fx.getRed(),
                    (float) fx.getGreen(),
                    (float) fx.getBlue(),
                    (float) fx.getOpacity());
        } catch (Exception ex) {
            borderColor = Color.BLACK;
        }

        final BufferedImage imgOut = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = imgOut.createGraphics();
        g.setColor(borderColor);
        g.fillRect(0, 0, imgOut.getWidth(), imgOut.getHeight());
        g.dispose();

        return imgOut;
    }


    public static RenderedImage getRenderedImage(File file) {
        RenderedImage img;
        ImageReader reader = getReader(file);
        try {
            img = reader.readAsRenderedImage(0, null);
        } catch (Exception e) {
            System.out.println(e.getMessage() + "BildArchiv_ - getRenderedImage");
            return null;
        }
        reader.dispose();
        return img;
    }

    private static ImageReader getReader(File source) {
        try {
            Iterator readers = ImageIO.getImageReadersByFormatName(ImgTools.fileType(source));
            ImageReader reader = (ImageReader) readers.next();
            ImageInputStream iis = ImageIO.createImageInputStream(source);
            reader.setInput(iis, true);
            return reader;
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n" + "Funktionen - getReader");
            return null;
        }
    }

    public static void writeImage(BufferedImage img, String dest, String suffix, float jpgCompression) {
        writeImage(img, Paths.get(dest),
                suffix.equals(ImgFormat.JPG.suff) ? ImgFormat.JPG : ImgFormat.PNG, jpgCompression);
    }

    public static void writeImage(BufferedImage bufferedImage, Path dest, ImgFormat suffix, float jpgCompression) {
        ImageWriter imageWriter = null;
        FileOutputStream fileOutputStream = null;
        ImageOutputStream imageOutputStream = null;
        try {
            if (suffix.equals(ImgFormat.PNG)) {
                imageWriter = ImageIO.getImageWritersBySuffix(ImgFormat.PNG.suff).next();
                imageWriter.setOutput(imageOutputStream);

                fileOutputStream = new FileOutputStream(dest.toFile());
                imageOutputStream = ImageIO.createImageOutputStream(fileOutputStream);
                imageWriter.write(new IIOImage(bufferedImage, null, null));

            } else {
//                ImageIO.write(bufferedImage, ImgFormat.JPG.suff, dest.toFile());

                imageWriter = ImageIO.getImageWritersBySuffix(IMAGE_FORMAT_JPG).next();
                ImageWriteParam iwparam = imageWriter.getDefaultWriteParam();
                iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                iwparam.setCompressionQuality(jpgCompression);

                fileOutputStream = new FileOutputStream(dest.toFile());
                imageOutputStream = ImageIO.createImageOutputStream(fileOutputStream);
                imageWriter.setOutput(imageOutputStream);
                imageWriter.write(null, new IIOImage(bufferedImage, null, null), iwparam);
            }
        } catch (Exception e) {
            Log.errorLog(784520369, e, ImgFile.class.toString());
            Platform.runLater(() ->
                    PAlert.showErrorAlert("Speichern", "Das Bild konnte nicht gespeichert werden." +
                            "\n\n")
            );
        } finally {
            try {
                imageOutputStream.flush();
                imageWriter.dispose();
                imageOutputStream.close();
            } catch (Exception ex) {
            }
        }
    }

}
