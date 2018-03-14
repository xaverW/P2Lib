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

import de.p2tools.p2Lib.tools.Log;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;

public class ImgTools {

    public static long JPEG_MAX_DIMENSION = 65500L;

    public static String fileType(File f) {
        return fileType(f.getName());
    }

    public static String fileType(String f) {
        String n = f;
        String suffix = null;
        int i = n.lastIndexOf('.');
        if (i > 0 && i < n.length() - 1) {
            suffix = n.substring(i + 1).toLowerCase();
        }
        if (suffix.equals("jpeg") || suffix.equals(ImgFile.IMAGE_FORMAT_JPG)) {
            return ImgFile.IMAGE_FORMAT_JPG;
        }
        if (suffix.equals(ImgFile.IMAGE_FORMAT_PNG)) {
            return ImgFile.IMAGE_FORMAT_PNG;
        }
        return "";
    }

    public static Color getColor(BufferedImage img) {
        Raster rast = img.getRaster();
        long r = 0, g = 0, b = 0;
        long count = 0;
        try {
            for (int x = rast.getMinX(); x < (rast.getMinX() + rast.getWidth()); x++) {
                for (int y = rast.getMinY(); y < (rast.getMinY() + rast.getHeight()); y++) {
                    r += rast.getSample(x, y, 0);
                    g += rast.getSample(x, y, 1);
                    b += rast.getSample(x, y, 2);
                    ++count;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int rr = (int) (r / count), gg = (int) (g / count), bb = (int) (b / count);
        Color ret = new Color(rr, gg, bb);
        return ret;
    }

    public static void changeToGrayscale(BufferedImage img) {
        //get image width and height
        int width = img.getWidth();
        int height = img.getHeight();
        try {
            //convert to grayscale
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int p = img.getRGB(x, y);

                    int a = (p >> 24) & 0xff;
                    int r = (p >> 16) & 0xff;
                    int g = (p >> 8) & 0xff;
                    int b = p & 0xff;

                    //calculate average
                    int avg = (r + g + b) / 3;

                    //replace RGB value with avg
                    p = (a << 24) | (avg << 16) | (avg << 8) | avg;

                    img.setRGB(x, y, p);
                }
            }
        } catch (Exception e) {
            Log.errorLog(698741254, e, ImgFile.class.toString());
        }
    }

    public static BufferedImage scaleBufferedImage(BufferedImage src, int w, int h) {
//        BufferedImage outImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
//        int x, y;
//        int ww = src.getWidth();
//        int hh = src.getHeight();
//        for (x = 0; x < w; x++) {
//            for (y = 0; y < h; y++) {
//                int col = src.getRGB(x * ww / w, y * hh / h);
//                outImg.setRGB(x, y, col);
//            }
//        }

        // todo testen was schneller ist
        Image scaledImage = src.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        BufferedImage outImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = outImg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(scaledImage, 0, 0, null);
        g.dispose();


        return outImg;
    }

    /**
     * @param pixelW
     * @param pixelH
     * @return filesize [kByte]
     */
    public static long getImgFileSize(int pixelW, int pixelH) {
        final long SIZE = 170;
        long sumPixel = pixelW * pixelH;
        long fSize = (SIZE * sumPixel / 1_000_000); // filesize kB and with jpg-compression
        return fSize;
    }

    public static String getImgFileSizeStr(int pixelW, int pixelH) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);

        long fSize = getImgFileSize(pixelW, pixelH);
        String fileSize = numberFormat.format(fSize) + " KByte";
        if (fSize > 1024) {
            fSize /= 1024;
            fileSize = numberFormat.format(fSize) + " MByte";
        }
        if (fSize > 1024) {
            fSize /= 1024;
            fileSize = numberFormat.format(fSize) + " GByte";
        }

        return fileSize;
    }
}
