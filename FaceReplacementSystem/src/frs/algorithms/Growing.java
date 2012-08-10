/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.algorithms;

/**
 *
 * @author power
 */
public class Growing {
    //This class is not to be used publicly by oother packages
    int connectivity=1;//1=>eight point connectivitiy
    int width=0;
    int height=0;
    private int[][] grownMatrix=null;
    private int[][] temp=null;
    
    public Growing(int connectivity){
        this.connectivity=connectivity;
    }
    public Growing(){};
    
    public void process(int [][] matrix,int width,int height){
        this.width=width;
        this.height=height;
        temp=new int[width][height];
        grownMatrix=new int[width][height];
        
        for(int i=0;i<width;i++)
            for(int j=0;j<height;j++){
                temp[i][j]=matrix[i][j];//copy to temp
                grownMatrix[i][j]=matrix[i][j];//copy to temp
            }
            grow();
    }
    private void grow(){
        int inc=2*connectivity-1;
        for(int i=connectivity;i<width-connectivity;i+=inc){
            for(int j=connectivity;j<height-connectivity;j+=inc){
                growNeighbourOperation(i,j);//change is reflected in grownMatrix
            }
        }
        temp=grownMatrix;
    }
    private void growNeighbourOperation(int x, int y) {
        //change is stored in the shrinked image data read from temp matrix
        int a=0;
        boolean flag=false;
        for(int i=-connectivity;i<connectivity+1;i++){
            for(int j=-connectivity;j<connectivity+1;j++){
                if(temp[x+i][y+j]>0){
                    a=1;
                    flag=false;
                    break;
                }
            }
            if(flag)break;
        }
        if(a>0)
        for(int i=-connectivity;i<connectivity+1;i++){
            for(int j=-connectivity;j<connectivity+1;j++){
                grownMatrix[x+i][y+j]=a;
            }
        }
    }
    
    public int[][] getResult(){
        return grownMatrix;
    }
}
