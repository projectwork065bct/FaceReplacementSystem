/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package H_Matrix;

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
    public static int[][] BinaryToMask(BufferedImage b){
        //pass a bufferedImage with either all 0 or all 255 value
        int width=b.getWidth();
        int height=b.getHeight();
        int [][] mask=new int[width][height];
        WritableRaster r=b.getRaster();
        int [] a =new int[3];
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                a=r.getPixel(i, j, a);
                if(a[0]>10){
                    mask[i][j]=1;
                }
            }
        }
        return mask;
    }
    
    /**
     * It finds out the boundary of a binary image, expressed in the form of a 
     * 2D matrix. The value boundary[y][0] gives the left boundary pixel.
     * The value boundary[y][1] gives the right boundary pixel.
     * If the boundary point is not found, then its value is -1.
     */
    public static int[][] findBoundary(int[][] binaryMatrix, int w, int h) {
        int[][] boundary = new int[2][h];
        //System.out.println("the width is "+w+" the height is "+h);
        if(binaryMatrix==null){
          //  System.out.println("the binary matrix passed to findBoundary is null");
            
        }else
        for (int y = 0; y < h; y++) {
            boundary[0][y] = -1;
            boundary[1][y] = -1;
            int xMin=0,xMax=w-1;
            for (xMin = 0;  xMin < w ; xMin++) {
                //boundary[0][y] = xMin;
                if(binaryMatrix[xMin][y] > 0 && xMin<w/4){
                    boundary[0][y]=xMin;break;
                }
            }

            for (xMax = w-1; xMax >= 0  ; xMax--) {
                //boundary[1][y] = xMax;
                if(binaryMatrix[xMax][y] >0 && xMax>w/2){
                    boundary[1][y]=xMax;
                    break;
                }
            }
        }
        return boundary;
    }
    public static  int[][] holeFillAccordingToBoundary(int [][] ImageMatrix,int w, int h ){
        int [][] mask=ImageMatrix;//unfilled
        int [][] b=ImageMat.findBoundary(mask, w, h);
        for(int y=0;y<h;y++)
        {
            if(b[0][y]>-1){
                for(int x=b[0][y];x<b[1][y];x++){
                    mask[x][y]=1;
                }
            }
        }
        return mask;
    }
    
    
    public static int [][] invertMatrix(int [][] matrix,int width, int height){
        //pass originalMatrix and its width and its height
        int[][] inverse=new int [height][width];
        for(int i=0;i<width;i++)
            for(int j=0;j<height;j++){
                try{
                inverse[j][i]=matrix[i][j];
                }catch(Exception e){
                    System.out.println("exception at x="+i+" y="+j);
                }
                
            }
        return inverse;
    }
    
    public static int[][] shrinkMatrix(int[][] mat,int width, int height, int count){
        MatrixShrinker ms=new MatrixShrinker();
        int [][] ans=new int[width][height];
        for(int i=0;i<width;i++)
            for(int j=0;j<height;j++){
                ans[i][j]=mat[i][j];//copy to temp
            }
       for(int i=0;i<count;i++){
           ms.process(ans, width, height);
           ans=ms.getResult();
       }
       return ans;
    }
    public static int[][] growMatrix(int[][] mat,int width, int height, int count){
        MatrixGrower mg=new MatrixGrower();
        int [][] ans=new int[width][height];
        for(int i=0;i<width;i++)
            for(int j=0;j<height;j++){
                ans[i][j]=mat[i][j];//copy to temp
            }
       for(int i=0;i<count;i++){
           mg.process(ans, width, height);
           ans=mg.getResult();
       }
       return ans;
    }
}
