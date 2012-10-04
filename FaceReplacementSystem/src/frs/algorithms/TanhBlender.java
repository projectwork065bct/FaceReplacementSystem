/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.algorithms;

import frs.helpers.DeepCopier;
import frs.helpers.MatrixAndImage;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Robik Shrestha
 */
public class TanhBlender {

    protected BufferedImage frontImg, backImg, resultImg, tempImg;
    protected int[][] backImgMat;
    protected Point shiftPt;
    protected float weight[];
    protected int windowSize;
    protected int boundarySize;
    protected int iterations;
    protected int alpha = 150;
    protected Set<Point> boundary;
    float totalWeight = 0;
    static int cnt = 0;

    public TanhBlender(BufferedImage frontImg, BufferedImage backImg, Point shiftPt, int windowSize, int boundarySize, int iterations) {
        this.frontImg = frontImg;
        this.backImg = backImg;
        this.shiftPt = shiftPt;
        this.windowSize = windowSize;
        this.boundarySize = boundarySize;
        this.iterations = iterations;
    }

    public BufferedImage runAndGet() {
        run();
        return getResult();
    }

    public void run() {
        boundary = MatrixAndImage.extractBoundary(frontImg, alpha);
        calcWeight();
        replace();
        blend();
    }

    public void calcWeight() {
        weight = new float[1 + windowSize / 2];
        weight[0] = 1;
        totalWeight = 1;
        for (int i = 1; i < weight.length; i++) {
            weight[i] = (float) Math.tanh((double) 1 / i);
        //    System.out.println("The weights are: " + weight[i]);
            totalWeight += 2 * weight[i];
        }
        totalWeight = (float) Math.pow(totalWeight, 2);
        //System.out.println("total weight = " + totalWeight);
    }

    //Put frontImg on top of resultImg at shiftPt
    public void replace() {
        backImgMat = MatrixAndImage.imageToBinaryMatrix(backImg);
        resultImg = DeepCopier.getBufferedImage(backImg, BufferedImage.TYPE_INT_ARGB);

        Graphics g = resultImg.getGraphics();
        g.drawImage(frontImg, shiftPt.x, shiftPt.y, null);
        tempImg = DeepCopier.getBufferedImage(resultImg, BufferedImage.TYPE_INT_ARGB);
    }

    public void blend() {
        System.out.println("no. of points in boundary " + boundary.size());
        for (int i = 0; i < iterations; i++) {
            Iterator<Point> iterator = boundary.iterator();
            while (iterator.hasNext()) {
                Point p = iterator.next();
                for (int delX = -boundarySize / 2; delX <= boundarySize / 2; delX++) {
                    for (int delY = -boundarySize / 2; delY <= boundarySize / 2; delY++) {
                        blendPoint(new Point(p.x + delX, p.y + delY));
                    }
                }
            }
            tempImg = DeepCopier.getBufferedImage(resultImg, BufferedImage.TYPE_INT_ARGB);
        }
        System.out.println("no. of times exception was thrown " + cnt);
    }

    protected void blendPoint(Point p) {
        int min = -windowSize / 2, max = windowSize / 2;
        float weightX, weightY;
        float sumYR = 0, sumYG = 0, sumYB = 0, sumYA = 0;
        for (int delY = min; delY <= max; delY++) {
            float sumXR = 0, sumXG = 0, sumXB = 0, sumXA = 0;
            //Interpolation along x-axis
            for (int delX = min; delX <= max; delX++) {
                Point n = new Point(p.x + delX, p.y + delY);
                Color c;
                try {
                    c = new Color(tempImg.getRGB(n.x, n.y), true);
                } catch (Exception e) {
                    continue;
                }
                weightX = weight[Math.abs(delX)];
                sumXR += weightX * c.getRed();
                sumXG += weightX * c.getGreen();
                sumXB += weightX * c.getBlue();
                sumXA += weightX * c.getAlpha();
            }//end of for delX
            weightY = weight[Math.abs(delY)];
            sumYR += sumXR * weightY;
            sumYG += sumXG * weightY;
            sumYB += sumXB * weightY;
            sumYA += sumXA * weightY;
        }
        sumYR /= totalWeight;
        sumYG /= totalWeight;
        sumYB /= totalWeight;
        sumYA /= totalWeight;
        try {
            Color c2 = new Color((int) sumYR, (int) sumYG, (int) sumYB, (int) sumYA);
            resultImg.setRGB(p.x, p.y, c2.getRGB());
        } catch (Exception e) {
            //System.out.println("sumYR " + sumYR + " sumYG " + sumYG + " sumYB " + sumYB + " sumYA " + sumYA);
            cnt++;
        }

    }

    public BufferedImage getResult() {
        return resultImg;
    }
}
