package frs.algorithms;


import java.awt.Point;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ramsharan
 */
public class Shifter {
    private int source[][];
    private int target[][];
    private Point sourcePastePoint;
    private Point targetPastePoint;
    
    private int matchingDegreeValue;
    private Point pastingPoint;
    private int targetFacePixelNo;
    
    public Point getPastingPoint(int height,int width){
        int xStart,xEnd;
        int yStart,yEnd;
        pastingPoint=new Point();
        pastingPoint.x=targetPastePoint.x;
        pastingPoint.y=targetPastePoint.y;
        xStart=targetPastePoint.x-width;
        xEnd=targetPastePoint.x+width;
        yStart=targetPastePoint.y-height;
        yEnd=targetPastePoint.y+height;
        //least possible value of matching degree value
        matchingDegreeValue=-(source.length*source[0].length+target.length*target[0].length);
        this.setTargetFacePixelNo();
        
        int temporaryMatchingDegreeValue;
        for(int i=yStart;i<=yEnd;i++){
            for(int j=xStart;j<=xEnd;j++){
                //System.out.println("x= "+j+" y= "+i+" source[0].length = ");
                targetPastePoint.x=j;
                targetPastePoint.y=i;
                temporaryMatchingDegreeValue=getMatchDegree();
                if(temporaryMatchingDegreeValue>matchingDegreeValue){
                    pastingPoint.x=j;
                    pastingPoint.y=i;
                    matchingDegreeValue=temporaryMatchingDegreeValue;
                }
                //System.out.println("the matching degree value is "+temporaryMatchingDegreeValue);
                //System.out.println("the target paste point is "+targetPastePoint.x+" "+targetPastePoint.y);
            }
        }
        return pastingPoint;
    }
    public int getMatchDegree(){
        //shift source 
        //GLOBAL:finding number of face pixels in the target image
        //finding matching degree value
        int matchedValue=0,unmatchedValue=0;
        int xShift=targetPastePoint.x-sourcePastePoint.x;
        int yShift=targetPastePoint.y-sourcePastePoint.y;
        int shiftedSourcePointX;
        int shiftedSourcePointY;
        int targetFacePixelNumber=this.targetFacePixelNo;
        
        for(int i=0;i<source.length;i++){
            for(int j=0;j<source[0].length;j++){
                //System.out.println("x= "+j+" y= "+i+" source[0].length = "+source[0].length);
                shiftedSourcePointX=j+xShift;
                shiftedSourcePointY=i+yShift;
                
                if(shiftedSourcePointX<0||shiftedSourcePointY<0||shiftedSourcePointX>(target[0].length-1)||shiftedSourcePointY>(target.length-1)){
                    if(source[i][j]==1) unmatchedValue++;
                }
                else{
                    if(source[i][j]!=target[shiftedSourcePointY][shiftedSourcePointX]){
                        unmatchedValue++;
                        if(target[shiftedSourcePointY][shiftedSourcePointX]==1) targetFacePixelNumber--;
                    }
                    else{
                        if(source[i][j]==1){
                            matchedValue++;
                            targetFacePixelNumber--;
                        }
                    }
                }
            }
        }
        return (matchedValue-unmatchedValue-targetFacePixelNumber);
    }
    private void setTargetFacePixelNo(){
        targetFacePixelNo=0;
        for(int i=0;i<target.length;i++)
            for(int j=0;j<target[0].length;j++)
                if(target[i][j]==1) targetFacePixelNo++;
    }
    
    public void setSource(int[][] source) {
        this.source = source;
    }

    public void setSourcePastePoint(Point sourcePastePoint) {
        this.sourcePastePoint = sourcePastePoint;
    }

    public void setTarget(int[][] target) {
        this.target = target;
    }

    public void setTargetPastePoint(Point targetPastePoint) {
        this.targetPastePoint = new Point(targetPastePoint);
    }
    
}
