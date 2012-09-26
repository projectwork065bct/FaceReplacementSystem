/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.helpers;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dell
 */
public class DeepCopier {

    public static BufferedImage getBufferedImage(BufferedImage image, int imageType) {
        int width = image.getWidth(), height = image.getHeight();
        BufferedImage deepCopiedImage = new BufferedImage(width, height, imageType);
        for (int x = width - 1; x >= 0; x--) {
            for (int y = height - 1; y >= 0; y--) {
                deepCopiedImage.setRGB(x, y, image.getRGB(x, y));
            }
        }
        return deepCopiedImage;
    }

    public static Point[] getPoints(Point[] point) {
        Point newPoint[] = new Point[point.length];
        for (int i = 0; i < point.length; i++) {
            newPoint[i] = new Point(point[i].x, point[i].y);
        }
        return newPoint;
    }

    public static List<Point> getPoints(List<Point> inPoints) {
        List<Point> points = new ArrayList();
        for (int i = 0; i < inPoints.size(); i++) {
            points.add(inPoints.get(i));
        }
        return points;
    }

    public static int[][] get2DMat(int[][] matrix, int w, int h) {
        int[][] resultMat = new int[w][h];
        for (int i = 0; i < w; i++) {
            resultMat[i] = new int[h];
            for (int j = 0; j < h; j++) {
                resultMat[i][j] = matrix[i][j];
            }

        }
        return resultMat;
    }
}
