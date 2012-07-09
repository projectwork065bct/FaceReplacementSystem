/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CoreAlgorithms;

import Helpers.ColorModelConverter;
import Helpers.Statistics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Robik Shrestha
 */
public class SkinColorDetectorUsingStatistics extends SkinColorDetector {

    protected int[] yFrequency, cbFrequency, crFrequency;
    protected float yMean, cbMean, crMean;
    protected float ySD, cbSD, crSD;//SD = Standard Deviation
    protected float yLow, yHigh, cbLow, cbHigh, crLow, crHigh;

    public SkinColorDetectorUsingStatistics(BufferedImage image) {
        super(image);
    }

    @Override
    public void detectSkin() {
        YCbCr = ColorModelConverter.getYCbCr(skinImage);
        findYCbCrFrequency();
        removeNonSkinColor();
        findMeanAndSDOfSkinColor();
        findYCbCrRange();
        applyThreshold();
        generateSkinImage();
    }

    public void findYCbCrFrequency() {
        int w = skinImage.getWidth(), h = skinImage.getHeight();
        yFrequency = new int[256];
        cbFrequency = new int[256];
        crFrequency = new int[256];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                yFrequency[(int) Math.floor(YCbCr[x][y][0])]++;
                cbFrequency[128 + (int) Math.floor(YCbCr[x][y][1])]++;
                crFrequency[128 + (int) Math.floor(YCbCr[x][y][2])]++;
            }
        }
    }

    //It keeps the frequencies of non-skin colors to 0
    public void removeNonSkinColor() {
        int yMin = 20, yMax = 230, cbMin = -50, cbMax = -1, crMin = 1, crMax = 50;
        int colorY, colorCb, colorCr;
        for (int x = 0; x < skinImage.getWidth(); x++) {
            for (int y = 0; y < skinImage.getHeight(); y++) {
                colorY = (int) YCbCr[x][y][ColorModelConverter.Y];
                colorCb = (int) YCbCr[x][y][ColorModelConverter.Cb];
                colorCr = (int) YCbCr[x][y][ColorModelConverter.Cr];
                if (colorY <= yMin || colorY >= yMax) {
                    yFrequency[colorY] = 0;
                    skinMatrix[x][y] = -1;
                }
                if (colorCb <= cbMin || colorCb >= cbMax) {
                    cbFrequency[128 + colorCb] = 0;
                    skinMatrix[x][y] = -1;
                }
                if (colorCr <= crMin || colorCr >= crMax) {
                    crFrequency[128 + colorCr] = 0;
                    skinMatrix[x][y] = -1;
                }
            }
        }
    }

    public void findMeanAndSDOfSkinColor() {
        Statistics statistics = new Statistics();
        statistics.setFrequency(yFrequency);
        yMean = statistics.getMean();
        ySD = statistics.getStandardDeviation();
        statistics.setFrequency(cbFrequency);
        cbMean = statistics.getMean() - 128;
        cbSD = statistics.getStandardDeviation();
        statistics.setFrequency(crFrequency);
        crMean = statistics.getMean() - 128;
        crSD = statistics.getStandardDeviation();
    }

    public void findYCbCrRange() {
        float yLevel=(float) 2, cbLevel=(float) 2, crLevel=(float) 2;
        yLow = yMean - yLevel*ySD;
        yHigh = yMean + yLevel*ySD;
        cbLow = cbMean - cbLevel*cbSD;
        cbHigh = cbMean + cbLevel*cbSD;
        crLow = crMean - crLevel*crSD;
        crHigh = crMean + crLevel*crSD;

    }

    public void applyThreshold() {
        int Y, Cb, Cr;
        for (int x = 0; x < skinImage.getWidth(); x++) {
            for (int y = 0; y < skinImage.getHeight(); y++) {
                Y = (int) YCbCr[x][y][0];
                Cb = (int) YCbCr[x][y][1];
                Cr = (int) YCbCr[x][y][2];
                if (Y >= yLow && Y <= yHigh && Cb >= cbLow && Cb <= cbHigh && Cr >= crLow && Cr <= crHigh) {
                    skinMatrix[x][y] = 1;
                }
            }
        }
    }
}
