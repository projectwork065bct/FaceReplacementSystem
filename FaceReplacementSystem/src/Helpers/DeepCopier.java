/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import java.awt.image.BufferedImage;

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
}
