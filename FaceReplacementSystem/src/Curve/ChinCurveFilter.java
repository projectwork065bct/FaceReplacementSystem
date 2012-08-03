/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Curve;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

/**
 *
 * @author power
 */
public class ChinCurveFilter {
    /*
     * First set all the parameters using the setters and then if filteed face
     * rectabgle is needed, call filteredFaceImage else if whole image rectabgle
     * is needed with call other
     */

    private Rectangle rectangle = null;
    private int[][] wholeImageMatrix = null;
    private int width = 0, height = 0;
    private List<Point> rightChinBoundary = null;
    private List<Point> leftChinBoundary = null;
    //main functions

    public int[][] filteredMatrix() {

        //pass whole image matrix
        int[][] mat = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                mat[i][j] = this.wholeImageMatrix[i][j];
            }
        }
        List<Point> list = this.leftChinBoundary;
        for (int i = 0; i < list.size(); i++) {
            int ystart = list.get(i).y;
            int x = list.get(i).x;
            for (int j = ystart; j < rectangle.y + rectangle.height; j++) {
                //wrf.setPixel(x, j, black);
                mat[i][j] = 0;
            }
        }
        list = this.rightChinBoundary;
        for (int i = 0; i < list.size(); i++) {
            int ystart = list.get(i).y;
            int x = list.get(i).x;
            for (int j = ystart; j < rectangle.y + rectangle.height; j++) {
                mat[i][j] = 0;
            }
        }
        return mat;
        //returns matrix of size of whole Image;
    }

    public int[][] filteredFaceImage(int[][] faceMat) {//size should be same of rectangle
        //pass unprocessed face (size=rectabtle)
        List<Point> list = this.leftChinBoundary;
        for (int i = 0; i < list.size(); i++) {
            int ystart = list.get(i).y;
            int x = list.get(i).x;
            for (int j = ystart; j < rectangle.y + rectangle.height; j++) {
                //wrf.setPixel(x, j, black);
                faceMat[i][j] = 0;
            }
        }
        list = this.rightChinBoundary;
        for (int i = 0; i < list.size(); i++) {
            int ystart = list.get(i).y;
            int x = list.get(i).x;
            for (int j = ystart; j < rectangle.y + rectangle.height; j++) {
                faceMat[i][j] = 0;
            }
        }
        return faceMat;
        //returns matrix of size of whole Image;
    }

    //setters
    public void setLeftChinBoundary(List<Point> leftChinBoundary) {
        this.leftChinBoundary = leftChinBoundary;
    }

    public void setRightChinBoundary(List<Point> rightChinBoundary) {
        this.rightChinBoundary = rightChinBoundary;
    }

    public void setWoleImageMatrix(int[][] woleImageMatrix, int width, int height) {
        this.wholeImageMatrix = woleImageMatrix;
        this.width = width;
        this.height = height;
        setRectangle(new Rectangle(0,0,width,height));
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }
}
