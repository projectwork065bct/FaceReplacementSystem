/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CoreAlgorithms;

import Helpers.ColorModelConverter;
import Helpers.DeepCopier;
import DataStructures.FloatingCoordinate;
import H_Matrix.ImageMat;
import Helpers.Statistics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;

/**
 *
 * @author Robik Shrestha @description This class can be used to detect skin
 * region in a bufferedimage. if skin[][]=1, then, the pixel belongs to skin
 * region. if skin[][]=0, then the pixel does not belong to skin region.
 */
public abstract class SkinColorDetector {

    protected BufferedImage skinImage;
    protected int[][] skinMatrix;
    protected float YCbCr[][][];
    protected int width, height;

    public SkinColorDetector() {
    }

    public SkinColorDetector(BufferedImage image) {
        this.skinImage = DeepCopier.getBufferedImage(image,BufferedImage.TYPE_INT_ARGB);
        this.width = image.getWidth();
        this.height = image.getHeight();
        skinMatrix = new int[width][height];
    }

    //If the skinMatrix[x][y]=0, then it makes that pixel transparent
    public void generateSkinImage() {
        Color actualColor;
        Color transparentColor;
        int RGB;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (skinMatrix[x][y] <= 0) {
                    RGB = skinImage.getRGB(x, y);
                    actualColor = new Color(RGB);
                    transparentColor = new Color(actualColor.getRed(), actualColor.getGreen(), actualColor.getBlue(), 0);
                    skinImage.setRGB(x, y, transparentColor.getRGB());
                    //skinImage.setRGB(x,y,Color.WHITE.getRGB());
                }
            }
        }
    }

    //Setters and getters
    public int[][] getSkinMatrix() {
        return skinMatrix;
    }

    public abstract void detectSkin();
    
    public BufferedImage getSkinImage()
    {
        return skinImage;
    }
}
