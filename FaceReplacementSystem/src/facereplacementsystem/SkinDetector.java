/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facereplacementsystem;

import java.awt.image.BufferedImage;

/**
 *
 * @author Dell
 * @description
 * This class can be used to detect skin region in a bufferedimage.
 * if skin[][]=1, then, the pixel belongs to skin region.
 * if skin[][]=0, then the pixel does not belong to skin region.
 */
public class SkinDetector {
    protected BufferedImage image;
    protected int[][] skin;

    
    //Constructors
    public SkinDetector()
    {}
    
    public SkinDetector(BufferedImage image)
    {this.image = image;}
    
    public void detectSkin()
    {}
    
    //Setters and getters
    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public int[][] getSkin() {
        return skin;
    }

    public void setSkin(int[][] skin) {
        this.skin = skin;
    }
    
    
    
}
