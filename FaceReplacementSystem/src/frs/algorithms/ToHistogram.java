/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.algorithms;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author ramsharan
 */
public class ToHistogram {
    public int[][] convert(BufferedImage b){
        int[][] his = new int[3][];
        for(int i=0;i<his.length;i++){
            his[i] = new int[256];
        }
        Color c;
        for(int i=0;i<b.getWidth();i++){
            for(int j=0;j<b.getHeight();j++){
                c = new Color(b.getRGB(i, j),true);
                if(c.getAlpha()>200){
                his[0][c.getRed()]++;
                his[1][c.getGreen()]++;
                his[2][c.getBlue()]++;
                }
            }
        }
        return his;
    } 
}
