/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.helpers;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

/**
 *
 * @author Robik Shrestha
 */
public class MatrixOperations {

    //The sub matrix is the part of "matrix" ("matrix" has a width,w and height, h) starting from (xStart, yStart) to (xEnd, yEnd)
    public static int[][] getSubMatrix(Rectangle r, int[][] matrix, int w, int h) {
        int xStart = r.x;
        int xEnd = r.x + r.width;
        int yStart = r.y;
        int yEnd = r.y + r.height;

        int[][] subMatrix = new int[xEnd - xStart + 1][yEnd - yStart - 1];
        for (int i = 0; i < (xEnd - xStart + 1); i++) {
            subMatrix[i] = new int[yEnd - yStart + 1];
            for (int j = 0; j < (yEnd - yStart + 1); j++) {
                subMatrix[i][j] = matrix[xStart + i][yStart + j];
            }
        }
        return subMatrix;
    }

    //Result = Subtractor - Subtractee
    //All the matrices have same number of rows and columns
    //Note here, 1-0 = 1, 1-1=0, 0-0=0 but any -ve result is considered to be 0!!!!!
    public static int[][] subtract(int[][] subtractor, int[][] subtractee, int w, int h) {
        int[][] result = new int[w][h];
        for (int i = 0; i < w; i++) {
            result[i] = new int[h];
            for (int j = 0; j < h; j++) {
                result[i][j] = subtractor[i][j] - subtractee[i][j];
                if (result[i][j] < 0) {
                    result[i][j] = 0;
                }
            }
        }
        return result;
    }

    //It resets the elements (makes the elements zero) of matrix matching the coordinates of points in the list "toBeErased"
    //Note: it directly operates with the reference!
    public static void erase(int[][] matrix, List<Point> toBeErased) {
        for (int i = 0; i < toBeErased.size(); i++) {
            Point p = toBeErased.get(i);
            try {
                matrix[p.x][p.y] = 0;
            } catch (Exception e) {
                continue;
            }
        }
    }
}
