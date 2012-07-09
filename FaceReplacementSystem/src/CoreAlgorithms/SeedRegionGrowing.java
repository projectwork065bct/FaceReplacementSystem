/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CoreAlgorithms;

import Helpers.ColorModelConverter;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Robik Singh Shrestha
 * 
 * 1. Provide the image and seedStack via constructor
 * 2. Retrieve the binaryMatrix. 1= similar to the seeds, 0=different from seeds
 */
public class SeedRegionGrowing {

    protected Stack<Point> seedStack;//Stack of coordinates
    protected int windowSize = 1;//8 connectivity
    protected List<Point> region;
    protected BufferedImage image;//It is the image which is to be check
    public static final int INSIDE = 1, OUTSIDE = 0, UNDECIDED = 2;
    protected int[][] binaryMatrix;
    protected BufferedImage binaryImage;
    protected float[][][] YCbCr;
    protected int minX, maxX, minY, maxY;
    //The following variables are for the condition that the pixel lies within the range of colors of  seed pixels
    protected int minR, maxR, minG, maxG, minB, maxB;
    protected float Ymin, Ymax, Cbmin, Cbmax, Crmin, Crmax;
    protected int seedCount = 0;

    //Constructor
    public SeedRegionGrowing(BufferedImage image, Stack<Point> seedStack) {
        this.image = image;
        this.seedStack = seedStack;
        initializeBinaryImage();
        findYCbCr();
        initializeSeedPixels();
        findImageSize();
        initializeRange();
        growRegion();
        createBinaryImage();
    }

    //Initially, non of the pixels are decided
    protected void initializeBinaryImage() {
        binaryMatrix = new int[image.getWidth()][image.getHeight()];
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                binaryMatrix[x][y] = UNDECIDED;
            }
        }
    }

    protected void findYCbCr() {
        YCbCr = ColorModelConverter.getYCbCr(image);
    }
    //The seed pixels are sure to be inside the desirable region, that's why they are called "seed", got it?

    protected void initializeSeedPixels() {
        Point point;
        //All the seed regions are inside the region
        for (int i = 0; i < seedStack.size(); i++) {
            point = seedStack.get(i);
            binaryMatrix[point.x][point.y] = INSIDE;
        }
        seedCount = seedStack.size();
    }

    //Image size
    protected void findImageSize() {
        //Find out the maximum and minimum limits
        minX = 0;
        maxX = image.getWidth() - 1;
        minY = 0;
        maxY = image.getHeight() - 1;
    }

    protected void initializeRange() {
        Point point;
        float Y, Cb, Cr;
        for (int i = 0; i < seedStack.size(); i++) {
            point = seedStack.get(i);
            Y = YCbCr[point.x][point.y][ColorModelConverter.Y];
            Cb = YCbCr[point.x][point.y][ColorModelConverter.Cb];
            Cr = YCbCr[point.x][point.y][ColorModelConverter.Cr];
            if (Y > Ymax) {
                Ymax = Y;
            }
            if (Y < Ymin) {
                Ymin = Y;
            }
            if (Cb > Cbmax) {
                Cbmax = Cb;
            }
            if (Cb < Cbmin) {
                Cbmin = Cb;
            }
            if (Cr > Crmax) {
                Crmax = Cr;
            }
            if (Cr < Crmin) {
                Crmin = Cr;
            }
        }
        /*
         * int RGB, R, G, B; Color c; for (int i = 0; i < seedStack.size(); i++)
         * { point = seedStack.get(i); RGB = image.getRGB(point.x, point.y); c =
         * new Color(RGB); R = c.getRed(); G = c.getGreen(); B = c.getBlue(); if
         * (R > maxR) { maxR = R; } if (R < minR) { minR = R; } if (G > maxG) {
         * maxG = G; } if (G < minG) { minG = G; } if (B > maxB) { maxB = B; }
         * if (B < minB) { minB = B; } }
         */
    }

    public void growRegion() {
        if (seedStack.isEmpty()) {
            return;
        }
        Point point;
        while (!seedStack.isEmpty()) {
            point = seedStack.pop();
            List<Point> neighborList = findNeighbors(point);
            Point neighbor;
            for (int i = 0; i < neighborList.size(); i++) {
                neighbor = neighborList.get(i);
                if (binaryMatrix[neighbor.x][neighbor.y] != UNDECIDED) {
                    continue;//If it has already been decided, then no need to worry about it again
                }
                if (satisfiesCondition(neighbor)) {
                    seedStack.push(neighbor);
                    binaryMatrix[neighbor.x][neighbor.y] = INSIDE;
                } else {
                    binaryMatrix[neighbor.x][neighbor.y] = OUTSIDE;
                }
            }

        }
    }
    
    public void createBinaryImage()
    {
        binaryImage = image;
        for(int x=0;x<image.getWidth();x++)
        {
            for(int y=0;y<image.getHeight();y++)
            {
                if(binaryMatrix[x][y]==1)
                {
                    binaryImage.setRGB(x,y,Color.RED.getRGB());
                }
            }
                      
        }
    }

    //Returns a list of neighbors
    public List<Point> findNeighbors(Point point) {
        List<Point> neighborList = new LinkedList();
        for (int delX = -windowSize; delX <= windowSize; delX++) {
            for (int delY = -windowSize; delY <= windowSize; delY++) {
                int x = point.x + delX;
                int y = point.y + delY;
                if (x == point.x && y == point.y) {
                    continue;//I am not a neighbor of myself! because I am myself!
                }
                if (!insideImage(x, y)) {
                    continue;//If it is not inside the image then continue
                }
                neighborList.add(new Point(point.x + delX, point.y + delY));
            }
        }
        return neighborList;
    }

    //The condition to be satisfied
    protected boolean satisfiesCondition(Point point) {
        return isWithinRange(point);
        //return isNearToMean(point);
    }

    protected boolean isWithinRange(Point point) {
        int x = point.x, y = point.y;
        float Y = YCbCr[x][y][ColorModelConverter.Y];
        float Cb = YCbCr[x][y][ColorModelConverter.Cb];
        float Cr = YCbCr[x][y][ColorModelConverter.Cr];
        if (Y >= Ymin && Y <= Ymax && Cb >= Cbmin && Cb <= Cbmax && Cr >= Crmin && Cr <= Crmax) {
            return true;
        } else {
            return false;
        }
        /*
         * int RGB = image.getRGB(point.x, point.y); Color c = new Color(RGB);
         * int R = c.getRed(), G = c.getGreen(), B = c.getBlue(); if (R >= minR
         * && R <= maxR && G >= minG && G <= maxG && B >= minB && B <= maxB) {
         * return true; } else { return false; }
         */
    }

    protected Boolean insideImage(int x, int y) {
        if (x >= minX && x <= maxX && y >= minY && y <= maxY) {
            return true;
        } else {
            return false;
        }
    }

    public int[][] getBinaryMatrix() {
        return this.binaryMatrix;
    }

    public BufferedImage getBinaryImage()
    {
        return binaryImage;
    }
    /////////////May be useful....
    protected boolean isNearToMean(Point point) {
        int RGB, R, G, B;
        Color c;
        //Color value of the pixel to be tested
        RGB = image.getRGB(point.x, point.y);
        c = new Color(RGB);
        R = c.getRed();
        G = c.getGreen();
        B = c.getBlue();
        if (R >= (meanR - 3 * Math.abs(stdDevR)) && R <= (meanR + 3 * Math.abs(stdDevR))
                && G >= (meanG - 3 * Math.abs(stdDevG)) && G <= (meanG + 3 * Math.abs(stdDevG))
                && B >= (meanB - 3 * Math.abs(stdDevB)) && B <= (meanB + 3 * Math.abs(stdDevB))) {
            updateStatistics(point);
            return true;

        } else {
            return false;
        }
    }

    //return true if the pixel is within the standard deviation limits of the neighboring pixels
    protected boolean isWithinStdDev(Point point) {
        List<Point> neighborList = findNeighbors(point);
        int RGB, R, G, B;
        Color c;
        //Color value of the pixel to be tested
        RGB = image.getRGB(point.x, point.y);
        c = new Color(RGB);
        R = c.getRed();
        G = c.getGreen();
        B = c.getBlue();
        //8 connected neighbor
        Point neighbor;
        int neighborRGB, neighborR, neighborG, neighborB;
        float minNeighborR, minNeighborG, minNeighborB;
        float maxNeighborR, maxNeighborG, maxNeighborB;

        for (int i = 0; i < neighborList.size(); i++) {
            neighbor = neighborList.get(i);
            //If the neigbbor is also inside the region then,
            if (binaryMatrix[neighbor.x][neighbor.y] == INSIDE) {
                neighborRGB = image.getRGB(neighbor.x, neighbor.y);
                c = new Color(neighborRGB);
                neighborR = c.getRed();
                neighborG = c.getGreen();
                neighborB = c.getBlue();

                //find the range
                minNeighborR = neighborR - Math.abs(stdDevR);
                maxNeighborR = neighborR + Math.abs(stdDevR);
                minNeighborG = neighborG - Math.abs(stdDevG);
                maxNeighborG = neighborG + Math.abs(stdDevG);
                minNeighborB = neighborB - Math.abs(stdDevB);
                maxNeighborB = neighborR + Math.abs(stdDevB);

                //If the color values are inside the range, then return true
                if (R >= minNeighborR && R <= maxNeighborR
                        && G >= minNeighborG && G <= maxNeighborG
                        && B >= minNeighborB && B <= maxNeighborB) {
                    updateStatistics(point);
                    return true;
                }
            }
        }
        return false;
    }

    //Find new values of mean and standard deviation
    protected void updateStatistics(Point point) {
        int RGB = image.getRGB(point.x, point.y);
        Color c = new Color(RGB);
        int R = c.getRed();
        int G = c.getGreen();
        int B = c.getBlue();
        meanR = (meanR * seedCount + R) / (seedCount + 1);
        meanG = (meanG * seedCount + G) / (seedCount + 1);
        meanB = (meanB * seedCount + B) / (seedCount + 1);
        //Instead of finding new std. dev., just approximate it with new mean
        stdDevR = stdDevR * stdDevR * (seedCount) + (R - meanR) * (R - meanR);
        stdDevR /= (seedCount + 1);
        stdDevR = (float) Math.sqrt(stdDevR);

        stdDevG = stdDevG * stdDevG * (seedCount) + (G - meanG) * (G - meanG);
        stdDevG /= (seedCount + 1);
        stdDevG = (float) Math.sqrt(stdDevG);

        stdDevB = stdDevB * stdDevB * (seedCount) + (B - meanB) * (B - meanB);
        stdDevB /= (seedCount + 1);
        stdDevB = (float) Math.sqrt(stdDevB);
    }

    protected void findMeanOfSeeds() {
        Point point;
        int RGB, R, G, B;
        Color c;
        //Calculate total sum
        for (int i = 0; i < seedStack.size(); i++) {
            point = seedStack.get(i);
            RGB = image.getRGB(point.x, point.y);
            c = new Color(RGB);
            R = c.getRed();
            G = c.getGreen();
            B = c.getBlue();
            sumR += R;
            sumG += G;
            sumB += B;
        }
        meanR = sumR / seedCount;
        meanG = sumG / seedCount;
        meanB = sumB / seedCount;
    }

    protected void findStdDevOfSeeds() {
        Point point;
        int RGB, R, G, B;
        Color c;
        //Find the std. dev.
        float squareR = 0, squareG = 0, squareB = 0;
        for (int i = 0; i < seedStack.size(); i++) {
            point = seedStack.get(i);
            RGB = image.getRGB(point.x, point.y);
            c = new Color(RGB);
            R = c.getRed();
            G = c.getGreen();
            B = c.getBlue();
            squareR += (R - meanR) * (R - meanR);
            squareG += (G - meanG) * (G - meanG);
            squareB += (B - meanB) * (B - meanB);
        }
        stdDevR = (float) Math.sqrt(squareR / seedCount);
        stdDevG = (float) Math.sqrt(squareG / seedCount);
        stdDevB = (float) Math.sqrt(squareB / seedCount);
    }
    //The following variables are for the condition that the pixel lies within GLOBAL std. dev. of its neighboring pixels
    protected float meanR, meanG, meanB, stdDevR, stdDevG, stdDevB;
    protected float sumR, sumG, sumB;
}
