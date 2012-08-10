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
public class SquarePolynomial implements CurveInterface{
    Point []points;
    double [] coefficients=new  double[3];
    //long a;
    
    public static void main(String [] args){
        SquarePolynomial sp=new SquarePolynomial();
        List<Point> p=new ArrayList<Point>();
        Point[] a=new Point[3];
        a[0]=new Point(276,330);
        a[1]=new Point(323,346);
        a[2]=new Point(355,328);
        p.add(a[0]);
        p.add(a[1]);
        p.add(a[2]);
        Point[] b=new Point[2];
        b[0]=a[0];b[1]=a[2];
        sp.setPoints(p.toArray(a));
        List <Point> ans=sp.getPoints(b);
        System.out.println("size= "+ans.size());
        for(int i=0;i<ans.size();i++){
            System.out.println(ans.get(i).x+" "+ans.get(i).y);
        }
        System.out.println("finished");
        
//        long large=12345678901245687L;
//        double largedouble=(double)large;
//        System.out.println(largedouble);
    }
    
    
    
    private void calculateCoefficients(){
        int n=points.length;
        int xi=0;
        int yi=0;
        int xi2=0,yi2=0,xi3=0,xi4=0,xiyi=0,xi2yi=0;
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
         List<Double> ans=new ArrayList<Double>();
        for(int i=0;i<3;i++){
            ans.add(coef.get(i, 0));
            coefficients[i]=coef.get(i,0);
        }
        for(int i=0;i<coefficients.length;i++){
            System.out.println(coefficients[i]);
        }
    }
    private Point[] getDistantPoints(Point[] p){
        float d1,d2,d3;
        d1=(float) p[0].distance(p[1]);
        d2=(float) p[1].distance(p[2]);
        d3=(float) p[2].distance(p[3]);
        List<Point> d=new ArrayList<Point>();
        if(d1>d2&&d1>d3){
            d.add(p[0]);
            d.add(p[1]);
        }else if(d2>d3){
            d.add(p[1]);
            d.add(p[2]);
        }else{
            d.add(p[2]);
            d.add(p[3]);
        }
        
        return d.toArray(p);
    }
    private int evaluate(int x){
        double y=coefficients[0]+coefficients[1]*x+coefficients[2]*x*x;
        return (int)Math.round(y);
    }
    
    @Override
    public void setPoints(Point []p){
        this.points=p;
    }
    @Override
    public List<Point> getPoints(Point[] p){//pass two points and get points between them based on the calculated polynomial
       // Point[] distantPoints=this.getDistantPoints(points);
        //int[] coef=this.calculateCoefficients();
        //int dist=(distantPoints[0].x-distantPoints[1].x);
        calculateCoefficients();
        Point point1,point2;
        if(p[0].x<p[1].x){
            point1=p[0];point2=p[1];
        }else{
            point1=p[1];point2=p[0];
        }
        List<Point> ans=new ArrayList();
        int val=0;
        for(int i=0;i<=(point2.x-point1.x);i++){
            val=point1.x+i;
            ans.add(new Point(val,evaluate(val)));
        }
        return ans;
    }
    
    /*
     * First pass the sample points setPoints
     * then pass two point and get list of points between them; getPoint(Point[])
     */
}
