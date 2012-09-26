/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.algorithms;

import frs.helpers.DeepCopier;

/**
 *
 * @author Robik Shrestha
 */
public class MatFiller {

    protected int[][] matrix;
    protected int w, h;

    public MatFiller(int[][] matrix, int w, int h) {
        this.matrix = DeepCopier.get2DMat(matrix, w, h);
        this.w = w;
        this.h = h;
        fill();
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void fill() {
        fillX();
        fillY();
    }

    //Note: S stands for Start and F stands for Finish
    public void fillX() {
        //For every column
        for (int y = 0; y < h; y++) {
            int xMin = w, xMax = -1;//no initial filling
            //Start from left to right to find the pixel which is set to 1
            for (int xS = 0; xS < w; xS++) {
                //If any pixel is set then,
                if (matrix[xS][y] == 1) {
                    xMin = xS;
                    for (int xF = w - 1; xF > xMin; xF--) {
                        if (matrix[xF][y] == 1) {
                            xMax = xF;
                            //Set all the pixels to 1 in that particular column
                            for (int x = xMin; x <= xMax; x++) {
                                matrix[x][y] = 1;
                            }
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    public void fillY() {
        //For every column
        for (int x = 0; x < w; x++) {
            int yMin = h, yMax = -1;//no initial filling
            //Start from top to bottom to find the pixel which is set to 1
            for (int yS = 0; yS < h; yS++) {
                //If any pixel is set then,
                if (matrix[x][yS] == 1) {
                    yMin = yS;
                    for (int yF = h - 1; yF > yMin; yF--) {
                        if (matrix[x][yF] == 1) {
                            yMax = yF;
                            //Set all the pixels to 1 in that particular column
                            for (int y = yMin; y <= yMax; y++) {
                                matrix[x][y] = 1;
                            }
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }
}
