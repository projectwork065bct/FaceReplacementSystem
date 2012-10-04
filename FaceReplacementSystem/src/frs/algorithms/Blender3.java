/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.algorithms;

import frs.helpers.DeepCopier;
import frs.helpers.MatrixAndImage;
import frs.helpers.MatrixOperations;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author Rajan Prasad Upadhyay
 */
public class Blender3 {
    //input = target Image, warped sourceFace Image
    //output= faceReplaced Blended Image
    
    BufferedImage targetImage;
    BufferedImage face;//argb
    Point shiftVector;
    
    //output
    BufferedImage result;
    
    //intermediate data
    int iterations=5;//decides iterations to be full opaque
    WritableRaster wr=null;
    int faceWidth;
    int faceHeight;

    //setters and getters
    public void setFace(BufferedImage face) {
        this.face = DeepCopier.getBufferedImage(face, face.getType());
        this.faceWidth=face.getWidth();
        this.faceHeight=face.getHeight();
    }
    public void setShiftVector(Point shiftVector) {
        this.shiftVector = shiftVector;
    }
    public void setTargetImage(BufferedImage targetImage) {
        this.targetImage = targetImage;
    }
    public void setIterations(int iterations){
        this.iterations=iterations;
    }
    public int getIterations(){
        return this.iterations;
    }
    /*public methods
     * Before these, the data should be set using the setters
     */
    
    public void process(){
        //public function to do the processing
        adjustData();
        adjustAlpha();
        Graphics g=result.getGraphics();
        g.drawImage(face, shiftVector.x, shiftVector.y, null);
        
    }
    public BufferedImage getReplacedResult(){
        return result;
    }
    public BufferedImage getAlphaBlendedFace(){
        return face;
    }
    
    //helper methods
    private void adjustData(){
        try{
            result=DeepCopier.getBufferedImage(targetImage, BufferedImage.TYPE_INT_ARGB);
            wr=face.getRaster();
        }catch(Exception e){
            System.err.println("Blender3.java error, all data are not set properly, null pointer");
        }
    }
    private void adjustAlpha(){
        //erode , subtract and decrease transparency of sourceFace
        int[][] faceMat=MatrixAndImage.imageToBinaryMatrix(face);
         try{
            ImageIO.write(ImageMat.maskToBinary(faceMat, faceWidth, faceHeight), "jpg", new File("z_b3_faceMat.jpg"));
        }catch(Exception e){
            System.out.println("Error saving face mat ");
        }
        int [][] afterShrinking1=null;
        int shrink1Limit=1;
        int [][] afterShrinking2=null;
        Shrinking shr=new Shrinking();
        //after shrinking
        shr.process(faceMat, faceWidth, faceHeight);
        afterShrinking1=shr.getResult();
        int alpha=0;
        for (int i=0;i<shrink1Limit;i++){
            shr.process(afterShrinking1, faceWidth, faceHeight);
            afterShrinking1=shr.getResult();
        }
        //change the alpha value of eroded region to zero
        int [][] m=MatrixOperations.subtract(faceMat, afterShrinking1, faceWidth, faceHeight);
        adjustAlpha(m, 0);
        faceMat=null;
        shr.process(afterShrinking1, faceWidth, faceHeight);
        afterShrinking2=shr.getResult();
         try{
            ImageIO.write(ImageMat.maskToBinary(m, faceWidth, faceHeight), "jpg", new File("z_b3_boundary.jpg"));
        }catch(Exception e){
            System.out.println("Error saving face mat ");
        }

        int increment=255/iterations;
        alpha+=increment;
        for(int i=0;i<iterations;i++){
            adjustAlpha(MatrixOperations.subtract(afterShrinking1, afterShrinking2, faceWidth, faceHeight),alpha);
            afterShrinking1=afterShrinking2;
            shr.process(afterShrinking1, faceWidth, faceHeight);
            afterShrinking2=shr.getResult();
            alpha+=increment;
        }
    }
    
    private void adjustAlpha(int [][] region, int alpha){
        int [] p=new int[4];
        p[0]=255;p[1]=255;p[2]=255;p[3]=255;
        int []q;
        for(int i=0;i<faceWidth;i++){
            for(int j=0;j<faceHeight;j++){
                if(region[i][j]>0){
                    q=wr.getPixel(i, j, p);
                    q[3]=alpha;
                    wr.setPixel(i, j, q);
                }
            }
        }
    }
}
