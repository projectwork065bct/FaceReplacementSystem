/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CoreAlgorithms;

import Helpers.ColorModelConverter;
import java.awt.image.BufferedImage;

/**
 *
 * @author Rajan and Robik
 */
public class SkinColorDetectorUsingThreshold extends SkinColorDetector {

    public SkinColorDetectorUsingThreshold(BufferedImage image) {
        super(image);
    }

    @Override
    public void detectSkin() {
        YCbCr = ColorModelConverter.getYCbCr(skinImage);
        detectSkinUsingYCbCr();
        generateSkinImage();
    }

    //Apply global thresholding to extract skin pixels
    protected void detectSkinUsingYCbCr() {
        float Y, Cb, Cr;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Y = (int) YCbCr[x][y][0];
                Cb = (int) YCbCr[x][y][1];
                Cr = (int) YCbCr[x][y][2];
                if (Y > 10 && Y < 240 && Cb >= -50 && Cb <= -5 && Cr > 0 && Cr < 50) {
                    skinMatrix[x][y] = 1;
                } else {
                    skinMatrix[x][y] = 0;
                }

            }
        }
    }
}
