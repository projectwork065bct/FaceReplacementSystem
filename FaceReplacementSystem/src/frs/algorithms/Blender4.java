/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.algorithms;

import frs.helpers.DeepCopier;
import frs.helpers.MatrixAndImage;
import frs.helpers.MatrixOperations;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author Rajan Prasad Upadhyay
 */
public class Blender4 {
//input = target Image, warped sourceFace Image
    //output= faceReplaced Blended Image
    //variables to be set
    BufferedImage replacedImage;
    BufferedImage face;//argb
    Point shiftVector;
    int iterations=5;//decides iterations to be full opaque
    
    //output
    BufferedImage result;
    
    //intermediate data
    WritableRaster wr=null;
    int faceWidth;
    int faceHeight;
    int [][] boundaryMat;

    //setters and getters
    public void setFace(BufferedImage face) {
        this.face = face;
        this.faceWidth=face.getWidth();
        this.faceHeight=face.getHeight();
    }
    public void setShiftVector(Point shiftVector) {
        this.shiftVector = shiftVector;
    }
    public void setReplacedImage(BufferedImage replacedImage) {
        this.replacedImage = replacedImage;
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
        adjustData();
        interpolate();
    }
    public BufferedImage getBlendedImage(){
        return result;
    }
    
    
    //helper functions
    private void adjustData(){
        result=DeepCopier.getBufferedImage(replacedImage, BufferedImage.TYPE_INT_RGB);
        wr=result.getRaster();
        
        int[][] faceMat=MatrixAndImage.imageToBinaryMatrix(face);
        try{
            ImageIO.write(ImageMat.maskToBinary(faceMat, faceWidth, faceHeight), "jpg", new File("faceMat.jpg"));
        }catch(Exception e){
            System.out.println("Error saving face mat ");
        }
        int [][] afterShrinking1=null;
        int shrink1Limit=2;
        int [][] afterShrinking2=null;
        Shrinking shr=new Shrinking();
        //after shrinking
        shr.process(faceMat, faceWidth, faceHeight);
        afterShrinking1=shr.getResult();
        try{
            ImageIO.write(ImageMat.maskToBinary(afterShrinking1, faceWidth, faceHeight), "jpg", new File("afterSh.jpg"));
        }catch(Exception e){
            System.out.println("Error saving face mat ");
        }
        //faceMat=afterShrinking1;
        int alpha=0;
        for (int i=0;i<shrink1Limit;i++){
            shr.process(afterShrinking1, faceWidth, faceHeight);
            afterShrinking1=shr.getResult();
            //afterShrinking1=afterShrinking2;
        }
        //change the alpha value of eroded region to zero
        boundaryMat=MatrixOperations.subtract(faceMat, afterShrinking1, faceWidth, faceHeight);
        BufferedImage b=ImageMat.maskToBinary(boundaryMat, faceWidth, faceHeight);
        try{
            ImageIO.write(b, "jpg", new File("boundaryMat.jpg"));
        }catch(Exception e){
            System.out.println("Error in  saving image...");
        }
    }
    private void interpolate(){
        System.out.println("entering interpolation");
        int up=0;
        int down=0;
        int[] updownInterpolated=new int[3];
        int left=0;
        int right=0;
        int[] leftRightInterpolated=new int[3];
        int lookLimit=255/iterations+2;
        
        int [] a=new int[4];for(int p=0;p<4;p++){a[p]=255;}
        for(int i=0;i<faceWidth;i++){
            for(int j=0;j<faceHeight;j++){
                try{
                    if(boundaryMat[i][j]>0){
                        //find upper 0 and lower zero distance
                        //use normalized distance as inverse weight function
                            //ie distance with lower zero is used as upper intensity weight and vice versa
                        //calculate interpolate weight 
                        //plot
                       // System.out.println("\t finding new boundary "+i+","+j);
                        for(;up<lookLimit;up++){
                            try{
                                if(boundaryMat[i][j-up]==0){
                                    break;
                                }
                            }catch(Exception e){
                                up=0;
                                //System.out.println("catching up "+e.getMessage());
                                break;
                            }
                        }
                        for(;down<lookLimit;down++){
                            try{
                                if(boundaryMat[i][j+down]==0){
                                    break;
                                }
                            }catch(Exception e){
                                down=0;
                                //System.out.println("catching e down "+e.getMessage());
                                break;
                            }
                        }
                        for(;left<lookLimit;left++){
                            try{
                                if(boundaryMat[i-left][j]==0){
                                    break;
                                }
                            }catch(Exception e){
                                left=0;
                                //System.out.println("catching e left "+e.getMessage());
                                break;
                            }
                        }
                        for(;right<lookLimit;right++){
                            try{
                                if(boundaryMat[i+right][j]==0){
                                    break;
                                }
                            }catch(Exception e){
                                right=0;
                                System.out.println("catching e right "+e.getMessage());
                                break;
                            }
                        }
                        if(up>=lookLimit){up=0;}
                        if(down>=lookLimit){down=0;}
                        if(left>=lookLimit){left=0;}
                        if(right>=lookLimit){right=0;}

                        if(up>0 && down>0 && left>0 && right>0){
                            int[] upi=wr.getPixel(shiftVector.x+i, shiftVector.y+j-up, a);
                            int[] downi=wr.getPixel(shiftVector.x+i, shiftVector.y+j+down, a);
                            int[] lefti=wr.getPixel(shiftVector.x+i-left, shiftVector.y+j, a);
                            int[] righti=wr.getPixel(shiftVector.x+i+right, shiftVector.y+j, a);

                            int[] pix=new int[3];
                            for(int n=0;n<3;n++){//for each intensity plane
                                updownInterpolated[n]=(upi[n]*down+downi[n]*up)/(up+down);
                                leftRightInterpolated[n]=(lefti[n]*right+righti[n]*left)/(left+right);
                                pix[n]=(updownInterpolated[n]+leftRightInterpolated[n])/2;
                            }
                            //System.out.println("changing whole");
                            wr.setPixel(shiftVector.x+i, shiftVector.y+j, pix);
                        }else if(up>0 && down>0){
                            int[] upi=wr.getPixel(shiftVector.x+i, shiftVector.y+j-up, a);
                            int[] downi=wr.getPixel(shiftVector.x+i, shiftVector.y+j+down, a);
                            int[] pix=new int[3];
                            for(int n=0;n<3;n++){//for each intensity plane
                                updownInterpolated[n]=(upi[n]*down+downi[n]*up)/(up+down);
                                pix[n]=(updownInterpolated[n]);
                            }
                            //System.out.println("changing up down");
                            wr.setPixel(shiftVector.x+i, shiftVector.y+j, pix);
                        }else if(left>0 && right>0){
                            int[] lefti=wr.getPixel(shiftVector.x+i-left, shiftVector.y+j, a);
                            int[] righti=wr.getPixel(shiftVector.x+i+right, shiftVector.y+j, a);
                            int[] pix=new int[3];
                            for(int n=0;n<3;n++){//for each intensity plane
                                leftRightInterpolated[n]=(lefti[n]*right+righti[n]*left)/(left+right);
                                pix[n]=(leftRightInterpolated[n]);
                            }
                            //System.out.println("changing leftright");
                            wr.setPixel(shiftVector.x+i, shiftVector.y+j, pix);
                        }

                    }//if
                }catch(Exception e){
                    System.out.println("some error in blender4 "+e.getMessage());
                }
            }//for
        }
    }
}
