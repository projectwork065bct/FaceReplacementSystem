/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.curve;

import Jama.Matrix;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author power
 */
public class SquarePolynomial_002 implements CurveInterface {
    Point[] points;
    double [] coefficients=new  double[3];
    Point averagePoint;

    
    private Point getAveragePoint(Point[] a){
        Point avg=new Point();
        double x=0,y=0;
        int number=a.length;
        for(int i=0;i<number;i++){
            x+=a[i].x;
            y+=a[i].y;
        }
        x=x/number;
        y=y/number;
        avg.x=(int)Math.round(x);
        avg.y=(int)Math.round(y);
        return avg;
    }
    private Point normalize(Point p){
        int x=0,y=0;
        
        x=p.x-averagePoint.x;
        y=p.y-averagePoint.y;
        return new Point(x,y);
    }
    private List<Point> normalize(List<Point> p1){
        List<Point> p2=new ArrayList();
        for(int i=0;i<p1.size();i++){
            p2.add(normalize(p1.get(i)));
        }
        return p2;
    }
    private void setNormalizedPoints(Point[] a){
        points=new Point[a.length];
        int n=points.length;
        int x=0,y=0;
        //System.out.println(" The size is "+n);
        for(int i=0;i<n;i++){
            //System.out.println(i);
            x=a[i].x-averagePoint.x;
            y=a[i].y-averagePoint.y;
            points[i]=new Point(x,y);
        }
    }
    private void calculate(){
        long n=points.length;
        Long xi=0l;
        Long yi=0l;
        Long xi2=0l,yi2=0l,xi3=0l,xi4=0l,xiyi=0l,xi2yi=0l;
        for(int i=0;i<n;i++){
            xi+=points[i].x;
            xi2+=points[i].x*points[i].x;
            xi3+=points[i].x*points[i].x*points[i].x;
            xi4+=points[i].x*points[i].x*points[i].x*points[i].x;
            yi+=points[i].y;
            yi2+=points[i].y*points[i].y;
            xiyi+=points[i].x*points[i].y;
            xi2yi+=points[i].x*points[i].x*points[i].y;
            
        }
        double[][] array={{n,xi,xi2},{xi,xi2,xi3},{xi2,xi3,xi4}};
        double[][] result={{yi},{xiyi},{xi2yi}};
        Matrix A=new Matrix(array);
        Matrix B=new Matrix(result);
        Matrix coef=A.inverse().times(B);
         //List<Double> ans=new ArrayList<Double>();
        for(int i=0;i<3;i++){
           // ans.add(coef.get(i, 0));
            coefficients[i]=coef.get(i,0);
        }
//        for(int i=0;i<coefficients.length;i++){
//            System.out.println(coefficients[i]);
//        }
    }
    private int evaluate(int x){
        double y=coefficients[0]+coefficients[1]*x+coefficients[2]*x*x;
        return (int)Math.round(y);
    }
    private List<Point> deNormalize(List<Point> p){
        List<Point> ans=new ArrayList();
        int x=0,y=0;
        for(int i=0;i<p.size();i++){
            ans.add(new Point(p.get(i).x+averagePoint.x,p.get(i).y+averagePoint.y));
        }
        return ans;
    }
    @Override
    public void setPoints(Point[] a) {
        averagePoint=getAveragePoint(a);
        //System.out.println("The average Point is "+averagePoint.x+","+averagePoint.y);
        setNormalizedPoints(a);
    }

    @Override
    public List<Point> getPoints(Point[] p) {
        calculate();
        Point point1,point2;
        
        if(p[0].x<p[1].x){
            point1=p[0];point2=p[1];
        }else{
            point1=p[1];point2=p[0];
        }
        point1=normalize(point1);
        point2=normalize(point2);
        List<Point> ans=new ArrayList();
        int val=0;
        for(int i=0;i<=(point2.x-point1.x);i++){
            val=point1.x+i;
            ans.add(new Point(val,evaluate(val)));
        }
        return deNormalize(ans);
        
    }
    
}
