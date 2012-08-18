/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ACMSnake;

import ij.ImagePlus;
import ij.process.ImageProcessor;

/**
 *
 * @author User
 */
public class SnakeMain {
    private String imagePath;
    private int x, y, width, height;
    public void  initalize(){
        ImagePlus imageplus = new ImagePlus(imagePath);
        ImageProcessor p = imageplus.getProcessor();
        imageplus.setRoi(x, y, width, height);
        SnakeInitializer a = new SnakeInitializer();
        a.set(imageplus);
        a.run(imageplus.getProcessor());     
        int[][] co = SnakeClass.getFaceedgecoordinates();
        for(int hc = 0 ; hc< 2000; hc++)
        {    for(int wc = 0; wc < 2000; wc++)
            {
                if( co[wc][hc] == 1)
                p.putPixel(wc, hc, co[wc][hc]);
            }
        }
        imageplus.show();
    }
    //getters and setters

    public void setHeight(int height) {
        this.height = height;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getWidth() {
        return width;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    
}
    

