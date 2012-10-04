/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 *
 * @author power
 */
public class ImageMat {
    public static BufferedImage maskToBinary(int[] [] mask,int width, int height){
        //convert a matrix 0=black, 1=white, to balck and white image
        int[] white=new int[3];
        white[0]=255;
        white[1]=255;
        white[2]=255;
        BufferedImage b=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster r=b.getRaster();
        for(int i=0;i<width;i++)
            for(int j=0;j<height;j++){
                if(mask[i][j]>0){
                    r.setPixel(i, j, white);
                }
            }
        return b;
    }
    
}
