/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

/**
 *
 * @author Robik Singh Shrestha
 *
 * @description
 *
 * This class consists of static functions which allow you to convert color from
 * one color model to another
 */
public class ColorModelConverter_1 {

    public static final int Y = 0, Cb = 1, Cr = 2;
    public static final int R = 0, G = 1, B = 2;
    public static final int H = 0, S = 1, V = 2;
    protected static BufferedImage image;

    public static int[][][] getRGB() {
        //Find out the RGB values of the pixels of the image
        Raster raster = image.getRaster();
        int w = ColorModelConverter.image.getWidth();
        int h = ColorModelConverter.image.getHeight();
        int RGB[][][] = new int[w][h][3];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                RGB[x][y][ColorModelConverter.R] = raster.getSample(x, y, ColorModelConverter.R);
                RGB[x][y][ColorModelConverter.G] = raster.getSample(x, y, ColorModelConverter.G);
                RGB[x][y][ColorModelConverter.B] = raster.getSample(x, y, ColorModelConverter.B);
            }
        }
        return RGB;
    }

    public static int[][][] getRGB(BufferedImage image) {
        ColorModelConverter.image = image;
        return getRGB();
    }

    public static float[][][] getYCbCr(BufferedImage image) {
        ColorModelConverter.image = image;
        return getYCbCr();
    }

    public static float[][][] getYCbCr() {
        //get YCbCr values of the image
        float YCbCr[][][] = new float[image.getWidth()][image.getHeight()][3];
        int RGB[][][] = getRGB(image);
        int w = image.getWidth(), h = image.getHeight();
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int red = RGB[x][y][ColorModelConverter.R], green = RGB[x][y][ColorModelConverter.G], blue = RGB[x][y][ColorModelConverter.B];
                float YValue = YCbCr[x][y][ColorModelConverter.Y] = (float) (0.299 * red + .587 * green + .114 * blue);
                YCbCr[x][y][ColorModelConverter.Cb] = (float) (-.16874 * red - .33126 * green + .5 * blue);
                YCbCr[x][y][ColorModelConverter.Cr] = (float) (.5 * red - .41869 * green - .08131 * blue);
                //YCbCr[x][y][ColorModelConverter.Cb] = blue - YValue;
                //YCbCr[x][y][ColorModelConverter.Cr] = red - YValue;
            }
        }
        return YCbCr;
    }
}
