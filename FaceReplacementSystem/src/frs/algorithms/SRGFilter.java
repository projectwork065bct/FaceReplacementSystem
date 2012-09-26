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
/*
 *
 * The filter is applied to fill the holes when SRG is applied.
 */
public class SRGFilter {

    protected int[][] matrix;
    protected int[][] filteredMatrix;
    protected int w, h;//width and height of the matrix
    protected int filterLength;

    public SRGFilter(int[][] matrix, int w, int h, int filterLength) {
        this.w = w;
        this.h = h;
        this.filterLength = filterLength;
        this.matrix = DeepCopier.get2DMat(matrix, w, h);
        this.filteredMatrix = DeepCopier.get2DMat(matrix, w, h);
    }

    public void applyFilter() {
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if (matrix[x][y] == 1) {
                    for (int delX = filterLength; delX >= 2; delX--) {
                        try {
                            if (matrix[x + delX][y] == 1) {
                                for (int fillX = 1; fillX < delX; fillX++) {
                                    filteredMatrix[x + fillX][y] = 1;
                                }
                                break;
                            }
                        } catch (Exception e) {
                            continue;
                        }
                    }
                    for (int delY = filterLength; delY >= 2; delY--) {
                        try {
                            if (matrix[x][y + delY] == 1) {
                                for (int fillY = 1; fillY < delY; fillY++) {
                                    filteredMatrix[x][y + delY] = 1;
                                }
                                break;
                            }
                        } catch (Exception e) {
                            continue;
                        }
                    }
                }
            }
        }
    }

    public int[][] getFilteredMatrix() {
        return filteredMatrix;
    }
}
