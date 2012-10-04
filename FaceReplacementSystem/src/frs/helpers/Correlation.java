/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.helpers;

/**
 *
 * @author Rajan Prasad Upadhyay
 */
public class Correlation {
    public static int [][] verticalLineDetector={{-1,2,-1},{-1,2,-1},{-1,2,-1}};
    public static int [][] horizontalLineDetector={{-1,-1,-1},{2,2,2},{-1,-1,-1}};
    
    private int [][] mat1 = null;
    private int [][] mat2 = null;
    /*
     * the matrix should always be square and same size
     */
    
    public void setMat1(int [][] mat1){
        this.mat1=mat1;
    }
    public void setMat2(int [][] mat2){
        this.mat2=mat2;
    }
    
    public int getScore(){
        int score = 0;
        if(mat1 == null || mat2 == null || mat1.length != mat2.length){
            return score;
        }else{
            try{
                int l=(int) Math.sqrt(mat1.length);
                for(int i=0;i<l;i++){
                    for(int j=0;j<l;j++){
                        score+=mat1[i][j]*mat2[i][j];
                    }
                }
                return score;
            }catch(Exception e){
                return score;
            }
        }
    }
}
