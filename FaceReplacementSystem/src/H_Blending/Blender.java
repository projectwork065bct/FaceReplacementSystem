/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package H_Blending;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author power
 */
public class Blender {
    static Raster replacedImageRaster=null;//read values from here
    static WritableRaster toBlendImage=null;//write values to here;
    static BufferedImage resultImage=null;
    
    private static BufferedImage BlendPoints_Average(BufferedImage replacedImage,List<Point> points){
        
        Point []n=new Point[4];
        int size=points.size();
        //replacedImage= target image with source warped face drawn over it
        WritableRaster wr=replacedImage.getRaster();
        int[] abc=null;
        int [] black=new int[3];black[0]=0;black[1]=0;black[2]=0;
        int [] white=new int[3];white[0]=255;white[1]=255;white[2]=255;
        
        Point mainPoint;
        int []average=new int[3];
        for(int i=0;i<size;i++){
            mainPoint=points.get(i);
            n[0]=new Point(mainPoint.x-1,mainPoint.y);//left point
            n[1]=new Point(mainPoint.x+1,mainPoint.y);//right point
            n[2]=new Point(mainPoint.x,mainPoint.y-1);//up point
            n[3]=new Point(mainPoint.x,mainPoint.y+1);//down point
            try{
                average=getAveragePixel(wr, mainPoint, n);
                wr.setPixel(mainPoint.x, mainPoint.y,average);
                wr.setPixel(mainPoint.x+1, mainPoint.y, average);
                wr.setPixel(mainPoint.x-1, mainPoint.y, average);
                wr.setPixel(mainPoint.x, mainPoint.y+1, average);
                wr.setPixel(mainPoint.x, mainPoint.y-1, average);
                
            }catch(Exception e){
                System.out.println("error at blending__"+e.getMessage());
                System.out.println(" The error Point is -- ("+mainPoint.x+","+mainPoint.y+")");
            }
        }
        return replacedImage;
    }
    private static BufferedImage BlendPoints_Average2(BufferedImage replacedImage,List<Point> points){
        //going to blend 5px up, down , left, right of the main boundary points
        int length=points.size();
        Point p=null;
        replacedImageRaster=replacedImage.getRaster();
        BufferedImage finalImage=deepCopy(replacedImage);
        toBlendImage=finalImage.getRaster();
        int blendLimit=5;
        for(int i=0;i<length;i++){
            p=points.get(i);
            if(p!=null){
                try{//blending in x axis
                    for(int j=-blendLimit;j<blendLimit;j++){
                        for(int k=-blendLimit;k<blendLimit;k++){
                            if(j==0&&k==0){
                                BlendPixel4Log(p.x,p.y);
                                BlendPixel4Log(p.x,p.y);
                            }else{
                                BlendPixel3(p.x+j, p.y);
                                BlendPixel3(p.x, p.y+k);
                            }
                        }
                    }
                }catch(Exception e){
                    System.out.println("There is an error in BlendPoints_Average2 ");
                }
            }
        }
        return finalImage;
    }
    private static void BlendPixel(int x,int y){
        int[] avg0=new int[3];
        int[] avg1=new int[3];
        avg0=getAverage(replacedImageRaster.getPixel(x-1, y, avg0),replacedImageRaster.getPixel(x, y, avg0),replacedImageRaster.getPixel(x+1, y, avg0));
        avg1=getAverage(replacedImageRaster.getPixel(x, y-1, avg0),replacedImageRaster.getPixel(x, y, avg0),replacedImageRaster.getPixel(x, y+1, avg0));
        int [] avg=new int[3];
        for(int i=0;i<3;i++){
            avg[i]=(avg0[i]+avg1[i])/2;
        }
        toBlendImage.setPixel(x, y,avg );
    }
    private static void BlendPixel2(int x,int y){
        int [] black=new int[3];black[0]=0;black[1]=0;black[2]=0;
        int [] white=new int[3];white[0]=255;white[1]=255;white[2]=255;
        toBlendImage.setPixel(x, y,black );
    }
    private static void BlendPixel3(int x,int y){
        //blends the pixel according to 10 connectivity
        int [] avg=new int[3];avg[0]=255;avg[1]=255;avg[2]=255;
        int neighbourSize=3;//Note neighbourSize =1 implies eightconnectivity
        List<int[]> area=new ArrayList();
        for(int i=-neighbourSize;i<neighbourSize;i++){
            for(int j=-neighbourSize;j<neighbourSize;j++){
                area.add(replacedImageRaster.getPixel(x+i, y+j, avg));
            }
        }
        toBlendImage.setPixel(x, y,getAverage2(area) );
    }
    private static void BlendPixel4Log(int x, int y){
        //logh 1=.7816, 1-logh 1=0.2384
        int [] abc=new int[3];abc[0]=255;abc[1]=255;abc[2]=255;
        int[] left=replacedImageRaster.getPixel(x-1, y, abc);
        int[] right=replacedImageRaster.getPixel(x+1, y, abc);
        int[] up=replacedImageRaster.getPixel(x, y-1, abc);
        int[] down=replacedImageRaster.getPixel(x, y+1, abc);
        abc[0]=(int) (0.7816*(left[0]+down[0])+0.2384*(right[0]+up[0]));
        abc[1]=(int) (0.7816*(left[1]+down[1])+0.2384*(right[1]+up[1]));
        abc[2]=(int) (0.7816*(left[2]+down[2])+0.2384*(right[2]+up[2]));
        
        toBlendImage.setPixel(x, y, abc);
    }
    private static int[] getAverage(int [] left, int[] main, int [] right){
        int [] ans=new int[3];
        for(int i =0;i<3;i++){
            ans[i]=(left[i]+main[i]+right[i])/3;
        }
        return ans;
    }
     private static int[] getAverage2(List<int[]> pixelrgbValues){
         //the int[] is pixel value in rgb format
        int [] ans=new int[3];ans[0]=0;ans[1]=0;ans[2]=0;
        int length=pixelrgbValues.size();
        int count=1;
        int[] pointer=null;
        for(int i=0;i<length;i++){
            pointer=pixelrgbValues.get(i);
            if(pointer[0]-100<pointer[1]){//to reject the totally red pixels
            ans[0]+=pointer[0];
            ans[1]+=pointer[1];
            ans[2]+=pointer[2];
            count++;
            }
        }
        ans[0]/=count;
        ans[1]/=count;
        ans[2]/=count;
        return ans;
    }
    
    //this is the main function
    public static BufferedImage getBlendedImage(BufferedImage faceOnly,Point wChinPoint, BufferedImage targetImage, Point targetChinPoint){
        
        //extract boundary of the passed face rgba image
        //List<Point> boundary=Blender.extractBoundaryVerticalHorizontal(faceOnly);
        List<Point> boundary=Blender.extractBoundaryVerticalHorizontal(faceOnly);
        int length=boundary.size();
        
        ///////To Replace the reddish region around the boundary
        Color col=new Color(255,255,255,0);
        Point pointer;
        for(int i=0;i<length;i++){
            pointer=boundary.get(i);
            faceOnly.setRGB(pointer.x<0?0:pointer.x, pointer.y<0?0:pointer.y, col.getRGB());
        }
        BufferedImage result=deepCopy(targetImage);
        Graphics g=result.getGraphics();
        System.out.println("The target chinPoint is ("+targetChinPoint.x+","+targetChinPoint.y+")");
        //calculate the delX and delY to shift the warped face image
        int shiftx=targetChinPoint.x-wChinPoint.x;
        int shifty=targetChinPoint.y-wChinPoint.y;
        //draw face image to the target image
        g.drawImage(faceOnly, shiftx, shifty, null);
        
        //shift the boundary
        Point p;
        for(int i=0;i<boundary.size();i++){
            p=boundary.get(i);
            p.x+=shiftx;
            p.y+=shifty;
        }
        return Blender.BlendPoints_Average2(result, boundary);
        //return faceOnly;
    }
    public static BufferedImage getBlendedImage2(BufferedImage faceOnly,BufferedImage replacedImage,Point shiftVector){
        /*
         * Pass the warped source Face =faceOnly
         * replacedImage =warpedface Drawn on the targetImage
         * shiftVector=How much the warped face is shifted before being drawn in the target
         */
        List<Point> boundary=Blender.extractBoundaryVerticalHorizontal(faceOnly);
        int length=boundary.size();
        BufferedImage result2=deepCopy(replacedImage);
        
        Point p;
        for(int i=0;i<boundary.size();i++){
            p=boundary.get(i);
            p.x+=shiftVector.x;
            p.y+=shiftVector.y;
        }
        return Blender.BlendPoints_Average2(result2, boundary);
        
    }
    private static int[] getAveragePixel(WritableRaster wr,Point mainPoint, Point[] fourNeighbours){
        int []ans=new int[3];ans[0]=255;ans[1]=255;ans[2]=255;
        int [] main=wr.getPixel(mainPoint.x, mainPoint.y, ans);
        int [] left=wr.getPixel(fourNeighbours[0].x, fourNeighbours[0].y, ans);
        int [] right=wr.getPixel(fourNeighbours[1].x, fourNeighbours[1].y, ans);
        int [] up=wr.getPixel(fourNeighbours[2].x, fourNeighbours[2].y, ans);
        int [] down=wr.getPixel(fourNeighbours[3].x, fourNeighbours[3].y, ans);
        
        ans[0]=(2*left[0]+2*right[0]+2*up[0]+2*down[0]+4*main[0])/12;
        ans[1]=(2*left[1]+2*right[1]+2*up[1]+2*down[1]+4*main[1])/12;
        ans[2]=(2*left[2]+2*right[2]+2*up[2]+2*down[2]+4*main[2])/12;
        
        return ans;
    }
    
    public static List<Point> extractBoundaryVerticalHorizontal(BufferedImage warpedImage){
        int width=warpedImage.getWidth();
        int height=warpedImage.getHeight();
        int[][] mat=new int[warpedImage.getWidth()][warpedImage.getHeight()];
        //Color col=new Color(255,255,255,0);
        for(int i=0;i<width;i++)
            for(int j=0;j<height;j++){
                Color c=new Color(warpedImage.getRGB(i, j),true);
                if(c.getAlpha()<100){
                    mat[i][j]=0;
                }else{
                    mat[i][j]=1;
//                    warpedImage.setRGB(i, j,col.getRGB());
                }
            }
        //binary matrix is ready
        
        int [][] boundaryHo=findBoundaryHorizontalFromMatrix(mat,width,height);
        int [][] boundaryVer=findBoundaryVerticalFromMatrix(mat,width,height);
        List <Point> bou=new ArrayList();
        
       {
           //horizontalPoints
           int x=0,y=0;
            for(int j=0;j<height-1;j++){
                for(int i=0;i<2;i++){
                    try{
                        x=boundaryHo[i][j];
                        y=j;
                        bou.add(new Point(x,y));
                    }
                    catch(Exception e){
                        System.out.println("The error Blending is " +"("+x+","+y+")");
                    }
                }
            }
            //verticalPoints
            for(int i=0;i<2;i++){
                for(int j=0;j<width-1;j++){//int i=0;i<2;i++
                    try{
                        x=j;//boundaryHo[j][i]
                        y=boundaryVer[j][i];
                        if(x>0&&y>0)
                        bou.add(new Point(x,y));
                    }
                    catch(Exception e){
                        System.out.println("The error Blending is " +"("+x+","+y+")");
                    }
                }
            }
        }
       return bou;
    }
    public static List<Point> extractBoundaryHorizontal(BufferedImage warpedImage){
        int width=warpedImage.getWidth();
        int height=warpedImage.getHeight();
        int[][] mat=new int[warpedImage.getWidth()][warpedImage.getHeight()];
        
        for(int i=0;i<width;i++)
            for(int j=0;j<height;j++){
                Color c=new Color(warpedImage.getRGB(i, j),true);
                if(c.getAlpha()<100){
                    mat[i][j]=0;
                }else{
                    mat[i][j]=1;
                }
            }
        //binary matrix is ready
        int [][] boundary=findBoundaryHorizontalFromMatrix(mat,width,height);
        List <Point> bou=new ArrayList();
        
       {
           int x=0,y=0;
            for(int j=0;j<height-1;j++){
                for(int i=0;i<2;i++){
                    try{
                        x=boundary[i][j];
                        y=j;
                        bou.add(new Point(x,y));
                    }
                    catch(Exception e){
                        System.out.println("The error Blending is " +"("+x+","+y+")");
                    }
                }
            }
        }
       return bou;
    }
    public static List<Point> extractBoundaryVertical(BufferedImage warpedImage){
        int width=warpedImage.getWidth();
        int height=warpedImage.getHeight();
        int[][] mat=new int[warpedImage.getWidth()][warpedImage.getHeight()];
        
        for(int i=0;i<width;i++)
            for(int j=0;j<height;j++){
                Color c=new Color(warpedImage.getRGB(i, j),true);
                if(c.getAlpha()<100){
                    mat[i][j]=0;
                }else{
                    mat[i][j]=1;
                }
            }
        //binary matrix is ready
        int [][] boundary=findBoundaryHorizontalFromMatrix(mat,width,height);
        List <Point> bou=new ArrayList();
        
       {
           int x=0,y=0;
            for(int j=0;j<height-1;j++){
                for(int i=0;i<2;i++){
                    try{
                        x=boundary[i][j];
                        y=j;
                        bou.add(new Point(x,y));
                    }
                    catch(Exception e){
                        System.out.println("The error Blending is " +"("+x+","+y+")");
                    }
                }
            }
        }
       return bou;
    }
    private static int[][] findBoundaryHorizontalFromMatrix(int[][] binaryMatrix, int w, int h) {
//        int width=w-1;
//        int height=h-1;
        int[][] boundary = new int[2][h];
        System.out.println("the width is "+w+" the height is "+h);
        if(binaryMatrix==null){
            System.out.println("the binary matrix passed to findBoundary is null");
            
        }else
        for (int y = 0; y < h; y++) {
            boundary[0][y] = -1;
            boundary[1][y] = -1;
            int xMin=0,xMax=w-1;
            for (xMin = 0;  xMin < w ; xMin++) {
                //boundary[0][x] = yMin;
                if(binaryMatrix[xMin][y] > 0 && xMin<w/4){
                    boundary[0][y]=xMin;break;
                }
            }

            for (xMax = w-1; xMax >= 0  ; xMax--) {
                //boundary[1][x] = yMax;
                if(binaryMatrix[xMax][y] >0 && xMax>w/2){
                    boundary[1][y]=xMax;
                    break;
                }
            }
            //System.out.println("yMin = "+yMin+" yMax = "+yMax);
        }
            
        
        return boundary;
    }
    private static int[][] findBoundaryVerticalFromMatrix(int[][] binaryMatrix, int w, int h) {
//        int width=w-1;
//        int height=h-1;
        int[][] boundary = new int[w][2];
        //System.out.println("the width is "+w+" the height is "+h);
        if(binaryMatrix==null){
            System.out.println("the binary matrix passed to findBoundary is null");
            
        }else
        for (int x = 0; x < w; x++) {
            boundary[x][0] = -1;
            boundary[x][1] = -1;
            int yMin=0,yMax=h-1;
            for (yMin = 0;  yMin < h ; yMin++) {
                //boundary[0][x] = yMin;
                if(binaryMatrix[x][yMin] > 0 ){
                    boundary[x][0]=yMin;break;
                }
            }

            for (yMax = h-1; yMax >= 0  ; yMax--) {
                //boundary[1][x] = yMax;
                if(binaryMatrix[x][yMax] >0){
                    boundary[x][1]=yMax;
                    break;
                }
            }
            //System.out.println("yMin = "+yMin+" yMax = "+yMax);
        }
            
        
        return boundary;
    }
    
    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}