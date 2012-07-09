/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import java.awt.Color;
import java.awt.image.BufferedImage;
/**
 *
 * @author Robik Singh Shrestha
 */
/*
 * This class finds out the mean color of the referenceImage and the
 * imageToBeShifted.
 *
 * It shifts the mean color of the imageToBeShifted to mean color of the
 * referenceImage
 */
public class MeanColorShifter {

    protected BufferedImage sourceImage, targetImage, resultImage;
    protected int[][] sourceFrequency, targetFrequency;
    protected int meanSource[], meanTarget[];
    protected int differenceInMean[];

    public MeanColorShifter(BufferedImage sourceImage, BufferedImage targetImage) {
        this.sourceImage = sourceImage;
        this.targetImage = targetImage;
    }

    public BufferedImage runGet() {
        findFrequency();
        findMean();
        shiftColors();
        return this.getResultImage();
    }
    //It finds out the frequencies of RGB values for source and frequency

    public void findFrequency() {
        sourceFrequency = findRGBFrequency(sourceImage);
        targetFrequency = findRGBFrequency(targetImage);
    }

    //It finds out the mean R,G and B values of the source and target image
    public void findMean() {
        meanSource = new int[3];
        meanTarget = new int[3];
        differenceInMean = new int[3];
        Statistics stats = new Statistics();
        //for each R,G and B
        for (int i = 0; i < 3; i++) {
            stats.setFrequency(sourceFrequency[i]);
            meanSource[i] = (int) stats.getMean();
            stats.setFrequency(targetFrequency[i]);
            meanTarget[i] = (int) stats.getMean();
            differenceInMean[i] = meanTarget[i] - meanSource[i];
        }
    }

    //It shifts the colors of the source to target (by difference between the means of the two)
    public void shiftColors() {
        resultImage = sourceImage;
        for (int x = 0; x < resultImage.getWidth(); x++) {
            for (int y = 0; y < resultImage.getHeight(); y++) {
                Color c = new Color(resultImage.getRGB(x, y), true);
                if (c.getAlpha() > 0) {
                    int shiftedR = c.getRed() + differenceInMean[0];
                    int shiftedG = c.getGreen() + differenceInMean[1];
                    int shiftedB = c.getBlue() + differenceInMean[2];

                    //make sure that the color values are within 0 to 255
                    if (shiftedR > 255) {
                        shiftedR = 255;
                    }
                    if (shiftedR < 0) {
                        shiftedR = 0;
                    }
                    if (shiftedG > 255) {
                        shiftedG = 255;
                    }
                    if (shiftedG < 0) {
                        shiftedG = 0;
                    }
                    if (shiftedB > 255) {
                        shiftedB = 255;
                    }
                    if (shiftedB < 0) {
                        shiftedB = 0;
                    }

                    resultImage.setRGB(x, y, new Color(shiftedR, shiftedG, shiftedB).getRGB());
                }
            }
        }
    }

    public BufferedImage getResultImage() {
        return this.resultImage;
    }

    //It finds out the frequencies of R,G and B in the image (for the pixels whose values are >0 in the binary matrix)
    public int[][] findRGBFrequency(BufferedImage image) {
        int[][] frequency = new int[3][256];
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color c = new Color(image.getRGB(x, y), true);
                if (c.getAlpha() > 0) {
                    frequency[0][c.getRed()]++;
                    frequency[1][c.getGreen()]++;
                    frequency[2][c.getBlue()]++;
                }
            }
        }
        return frequency;
    }
}
