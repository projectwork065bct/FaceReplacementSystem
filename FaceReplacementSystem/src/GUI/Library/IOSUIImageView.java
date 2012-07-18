/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Library;

import hu.droidzone.iosui.IOSUIView;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;

/**
 *
 * @author Dell
 */
public class IOSUIImageView extends IOSUIView {

    protected BufferedImage image;
    protected int w, h;
    protected int imageW, imageH;
    protected int newImageW, newImageH;
    float aspectRatio;

    
    public IOSUIImageView(String colSpec, String rowSpec) {
        super(colSpec, rowSpec);
        
    }
    
    public IOSUIImageView(int col,int row)
    {
        super(col,row);
    }
    
    public void setImage(BufferedImage image) {
        //LayoutManager l=getLayout();
        //System.out.println(l.);
        System.out.println("actual width = "+this.getWidth()+" height = "+this.getHeight());
        this.image = image;
        imageW = image.getWidth();
        imageH = image.getHeight();
        aspectRatio = (float) imageW / imageH;
        adjustImageSize();
        repaint();
    }

    //Resize Image according to the aspect ratio
    public void adjustImageSize() {
        w =(int) this.getPreferredSize().getWidth();
        h =(int) this.getPreferredSize().getHeight();
        newImageW = w;
        newImageH = (int) (newImageW / aspectRatio);
        if (newImageH > h) {
            newImageH = h;
            newImageW = (int) (aspectRatio * newImageH);
        }
    }

//    public void setWH(int width,int height){
//        this.w=width;
//        this.h=height;
//    }
//    @Override
//    public Dimension getPreferredSize(){
//        return new Dimension(w,h);
//   }
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, (int)this.getPreferredSize().getWidth(), (int)this.getPreferredSize().getHeight());
        if (image != null) {
            g.drawImage(image, 0, 0, newImageW, newImageH, null);
        }
    }
    

}
