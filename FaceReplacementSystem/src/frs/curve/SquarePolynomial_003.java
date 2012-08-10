/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.curve;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author power
 */
public class SquarePolynomial_003 implements CurveInterface{
    private boolean invert=false; 
    
    private SquarePolynomial_002 sq2=new SquarePolynomial_002();
    private Point getMinXPoint(Point[] a){
        Point temp;
        temp=a[0];
        for(int i=0;i<a.length;i++){
            if(a[i].x<temp.x){
                temp=a[i];
            }
        }
        return temp;
    }
    private Point getMaxXPoint(Point[] a){
        Point temp;
        temp=a[0];
        for(int i=0;i<a.length;i++){
            if(a[i].x>temp.x){
                temp=a[i];
            }
        }
        return temp;
    }
    private Point getMinYPoint(Point[] a){
        Point temp;
        temp=a[0];
        for(int i=0;i<a.length;i++){
            if(a[i].y<temp.y){
                temp=a[i];
            }
        }
        return temp;
    }
    private Point getMaxYPoint(Point[] a){
        Point temp;
        temp=a[0];
        for(int i=0;i<a.length;i++){
            if(a[i].y>temp.y){
                temp=a[i];
            }
        }
        return temp;
    }
    private int getXSeparation(Point[] a){
        Point a1=getMinXPoint(a);
        Point a2=getMaxXPoint(a);
        return a2.x-a1.x;
    }
    private int getYSeparation(Point[] a){
        Point a1=getMinYPoint(a);
        Point a2=getMaxYPoint(a);
        return a2.y-a1.y;
    }
    private Point[] invertPoints(Point[] p){
        Point [] a=new Point[p.length];
        int count=a.length;
        for(int i=0;i<count;i++){
            a[i]=new Point(p[i].y,p[i].x);
        }
        return a;
    }
    private List<Point> unInvertPoints(List<Point> p){
        List <Point> a=new ArrayList<Point>();
        int count=p.size();
        for(int i=0;i<count;i++){
            a.add(new Point(p.get(i).y,p.get(i).x));
        }
        return a;
    }
    @Override
    public void setPoints(Point[] a) {
        int xd=this.getXSeparation(a);
        int yd=this.getYSeparation(a);
        if(yd>xd){
            this.invert=true;
            sq2.setPoints(invertPoints(a));
        }else{
            sq2.setPoints(a);
        }
    }

    @Override
    public List<Point> getPoints(Point[] a) {
        List<Point> points;
        if(this.invert){
            points=sq2.getPoints(invertPoints(a));
            return unInvertPoints(points);
        }else{
            points=sq2.getPoints(a);
            return points;
        }
    }
    
}
