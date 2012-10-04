/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.helpers;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Dell
 */
public class MatrixAndImage {

    public static int[][] imageToBinaryMatrix(BufferedImage image) {
        int binaryMatrix[][];
        int w = image.getWidth();
        int h = image.getHeight();
        binaryMatrix = new int[w][h];
        for (int x = 0; x < w; x++) {
            binaryMatrix[x] = new int[h];
            for (int y = 0; y < h; y++) {
                Color c = new Color(image.getRGB(x, y), true);
                if (c.getAlpha() < 150) {
                    continue;
                }
                binaryMatrix[x][y] = 1;
            }
        }
        return binaryMatrix;
    }

    //All the pixels marked "0" are made transparent
    public static BufferedImage matrixToImage(BufferedImage image, int[][] matrix) {
        BufferedImage binaryImage = DeepCopier.getBufferedImage(image, BufferedImage.TYPE_INT_ARGB);
        Color c;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (matrix[x][y] <= 0) {//If the pixel value is less than or equal to 0 then make it transparent
                    c = new Color(image.getRGB(x, y), true);
                    binaryImage.setRGB(x, y, ColorModelConverter.getTransparentColor(c).getRGB());
                }
            }
        }
        return binaryImage;
    }

    //All the matrix cells valued 1 or more are highlighted with the color
    public static BufferedImage getHighlightedImage(BufferedImage image, int[][] matrix, Color c) {
        BufferedImage highlightedImg = DeepCopier.getBufferedImage(image, image.getType());
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (matrix[x][y] >= 1) {
                    highlightedImg.setRGB(x, y, c.getRGB());
                }
            }
        }
        return highlightedImg;
    }
    //Extracts boundary

    public static Set<Point> extractBoundary(BufferedImage img, int maxAlpha) {
        Set<Point> boundary = new HashSet<Point>();
        boundary.addAll(extractBoundaryX(img, maxAlpha));
        boundary.addAll(extractBoundaryY(img, maxAlpha));
        return boundary;
    }

    public static Set<Point> extractBoundaryX(BufferedImage img, int maxAlpha) {
        Set<Point> boundary = new HashSet<Point>();
        for (int y = 0; y < img.getHeight(); y++) {
            for (int xMin = 0; xMin < img.getWidth(); xMin++) {
                Color c = new Color(img.getRGB(xMin, y), true);
                if (c.getAlpha() > maxAlpha) {
                    boundary.add(new Point(xMin, y));
                    break;
                }
            }
        }

        for (int y = 0; y < img.getHeight(); y++) {
            for (int xMax = img.getWidth() - 1; xMax >= 0; xMax--) {
                Color c = new Color(img.getRGB(xMax, y), true);
                if (c.getAlpha() > maxAlpha) {
                    boundary.add(new Point(xMax, y));
                    break;
                }
            }
        }
        return boundary;

    }

    public static Set<Point> extractBoundaryY(BufferedImage img, int maxAlpha) {
        Set<Point> boundary = new HashSet<Point>();
        for (int x = 0; x < img.getWidth(); x++) {
            for (int yMin = 0; yMin < img.getHeight(); yMin++) {
                Color c = new Color(img.getRGB(x, yMin), true);
                if (c.getAlpha() > maxAlpha) {
                    boundary.add(new Point(x, yMin));
                    break;
                }
            }
        }

        for (int x = 0; x < img.getWidth(); x++) {
            for (int yMax = img.getHeight() - 1; yMax >= 0; yMax--) {
                Color c = new Color(img.getRGB(x, yMax), true);
                if (c.getAlpha() > maxAlpha) {
                    boundary.add(new Point(x, yMax));
                    break;
                }
            }
        }
        return boundary;

    }
}
