/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Dell
 */
public class SkinMatrixProvider {

    public static int[][] getSkinMatrix(BufferedImage image) {
        int skinMatrix[][];
        int w = image.getWidth();
        int h = image.getHeight();
        skinMatrix = new int[w][h];
        for (int x = 0; x < w; x++) {
            skinMatrix[x] = new int[h];
            for (int y = 0; y < h; y++) {
                Color c = new Color(image.getRGB(x, y), true);
                if (c.getAlpha() < 255) {
                    continue;
                }
                skinMatrix[x][y] = 1;
            }
        }
        return skinMatrix;
    }
}
