/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.algorithms;
import frs.helpers.ColorModelConverter;
import java.awt.image.BufferedImage;

/**
 *
 * @author Rajan and Robik
 */
public class SkinColorDetectorUsingThreshold extends SkinColorDetector {

    public SkinColorDetectorUsingThreshold(BufferedImage image) {
        super(image);
    }

    @Override
    public void detectSkin() {
        YCbCr = ColorModelConverter.getYCbCr(skinImage);
        detectSkinUsingYCbCr();
        generateSkinImage();
    }

    //Apply global thresholding to extract skin pixels
    protected void detectSkinUsingYCbCr() {
        float Y, Cb, Cr;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Y = (int) YCbCr[x][y][0];
                Cb = (int) YCbCr[x][y][1];
                Cr = (int) YCbCr[x][y][2];
                if (Y > 50 && Y < 240 && Cb >= -50 && Cb <= -5 && Cr > 0 && Cr < 50) {
                    skinMatrix[x][y] = 1;
                } else {
                    skinMatrix[x][y] = 0;
                }

            }
        }
        
//        int [][] shrinked=ImageMat.shrinkMatrix(skinMatrix, width, height, 5);
//        int [][] grown=ImageMat.growMatrix(shrinked, width, height, 5);
//        skinMatrix=grown;
    }
    protected void detectSkinUsingYCbCr3(){
        float Y, Cb, Cr;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Y = (int) YCbCr[x][y][0];
                Cb = (int) YCbCr[x][y][1];
                Cr = (int) YCbCr[x][y][2];
                if(y>120){
                    if(Cb>-5){//cb>0=>black
                        skinMatrix[x][y] = 0;
                    }else{
                        if(Cr>0){
                            skinMatrix[x][y] = 1;
                        }else{
                            skinMatrix[x][y] = 0;
                        }
                        
                    }
                }else{
                    skinMatrix[x][y] = 0;
                }

            }
        }
    }
}
