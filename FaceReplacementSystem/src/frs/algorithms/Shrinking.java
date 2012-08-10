/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.algorithms;

/**
 *
 * @author power
 */
public class Shrinking {
    //This class is not to be used publicly by oother packages unless very necessary
    //use matrix shrinking method of the ImageMat class instead
    int connectivity=1;//1=>eight point connectivitiy
    int width=0;
    int height=0;
    private int[][] shrinkedMatrix=null;
    private int[][] temp=null;
    
    public Shrinking(int connectivity){
        this.connectivity=connectivity;
    }
    public Shrinking(){};
    
    public void process(int [][] matrix,int width,int height){
        this.width=width;
        this.height=height;
        
        temp=new int[width][height];
        shrinkedMatrix=new int[width][height];
        
        for(int i=0;i<width;i++)
            for(int j=0;j<height;j++){
                temp[i][j]=matrix[i][j];//copy to temp
                shrinkedMatrix[i][j]=matrix[i][j];//copy to temp
            }
            shrink();
    }
    private void shrink(){
        int inc=2*connectivity-1;
        for(int i=connectivity;i<width-connectivity;i+=inc){
            for(int j=connectivity;j<height-connectivity;j+=inc){
                shrinkNeighbourOperation(i,j);//change is reflected in shrinkedMatrix
            }
        }
        temp=shrinkedMatrix;
    }
    private void shrinkNeighbourOperation(int x, int y) {
        //change is stored in the shrinked image data read from temp matrix
        int a=1;
        boolean flat=true;
        for(int i=-connectivity;i<connectivity+1;i++){
            for(int j=-connectivity;j<connectivity+1;j++){
                if(temp[x+i][y+j]==0){
                    a=0;
                    flat=false;
                    break;
                }
            }
            if(flat)break;
        }
        if(a<1)
        for(int i=-connectivity;i<connectivity+1;i++){
            for(int j=-connectivity;j<connectivity+1;j++){
                shrinkedMatrix[x+i][y+j]=a;
            }
        }
    }
    
    public int[][] getResult(){
        return shrinkedMatrix;
    }
}
