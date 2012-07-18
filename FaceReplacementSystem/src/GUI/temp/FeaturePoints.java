/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.temp;

import GUI.Library.IOSUIImageView;
import facereplacementsystem.FaceReplacementSystem;
import hu.droidzone.iosui.IOSUIApplication;
import hu.droidzone.iosui.IOSUIButton;
import hu.droidzone.iosui.IOSUIHeader;
import hu.droidzone.iosui.IOSUIView;
import hu.droidzone.iosui.i18n.IOSI18N;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;



/**
 *
 * @author ramsharan
 */
public class FeaturePoints extends IOSUIImageView{
    Point [] featurePoints;
    facereplacementsystem.FaceReplacementSystem frs;
    
    public FeaturePoints(String colspecs,String rowspecs){
        super(colspecs,rowspecs);
        initComponents();
    }

    private void initComponents() {
        FeatureAdapter fa = new FeatureAdapter(this);
        addMouseListener(fa);
        addMouseMotionListener(fa);
        setFeaturePoints();
        
    }
    
    @Override
    public void paint(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.red);
        g.drawLine(41,41, 50, 50);
        drawFeaturePoints(g);
    }
    
    protected void setFeaturePoints(){
        
        featurePoints = new Point[8];
        
        featurePoints[0]=new Point(140, 140);
        featurePoints[1]=new Point(170, 140);
        featurePoints[2]=new Point(155, 170);
        featurePoints[3]=new Point(155, 185);
        featurePoints[4]=new Point(145, 180);
        featurePoints[5]=new Point(165, 180);
        featurePoints[6]=new Point(130, 170);
        featurePoints[7]=new Point(180, 170);
        
    }
    protected void drawFeaturePoints(Graphics g){
        int width=4;
        System.out.println("i ma at drfepo"+featurePoints.length);
        //for marking first feature point
        for(int i=0;i<featurePoints.length;i++){
            g.drawLine(featurePoints[i].x-width, featurePoints[i].y-width, featurePoints[i].x+width, featurePoints[i].y+width);
            g.drawLine(featurePoints[i].x-width, featurePoints[i].y+width, featurePoints[i].x+width, featurePoints[i].y-width);
            g.drawString(""+i,featurePoints[i].x+width+2 ,featurePoints[i].y+width+2 );
        }
        
    }
    
    public static void main(String [] args){
        IOSUIView content = new IOSUIView("p:g", "p:g");
        FeaturePoints rajanPanel1 = new FeaturePoints("400px", "400px");
        
        content.addXY(rajanPanel1, 1, 1);

        IOSUIButton btn = new IOSUIButton(new AbstractAction(IOSI18N.get("msg.btn.exit")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        btn.setBackground(new Color(200, 225, 0, 140));
        IOSUIHeader hdr = IOSUIHeader.createApplicationHeader(0, 1, "settting feature points");
        hdr.addXY(btn, 2, 1, "c,c");
        IOSUIApplication.startIOSUIApplication(600, 400, content, hdr);
    }

    public void setFRS(FaceReplacementSystem frs) {
        this.frs=frs;
    }
    
    
}
class FeatureAdapter extends MouseAdapter{

    FeaturePoints fp;
    int width;
    int position=-1;
    boolean draggable=false;
    
    FeatureAdapter(FeaturePoints fp) {
        this.fp=fp;
        this.width=3;
    }
    public int whichFeaturePoint(Point p,int width){
        int xmin,xmax,ymin,ymax;
        for(int i=0;i<fp.featurePoints.length;i++){
            xmin=fp.featurePoints[i].x-width;
            xmax=fp.featurePoints[i].x+width;
            ymin=fp.featurePoints[i].y-width;
            ymax=fp.featurePoints[i].y+width;
            
            if(p.x>=xmin&&p.x<=xmax){
                if(p.y>=ymin&&p.y<=ymax){
                    return i;
                }
            }
        }
        return -1;
    }
    
    @Override
    public void mouseClicked(MouseEvent e){
        System.out.println("mouse clicked");
//        fp.featurePoints[0]=e.getPoint();
//        fp.repaint();
    }
    @Override
    public void mousePressed(MouseEvent e){
        System.out.println("mouse pressed");
        if(position!=-1){
            draggable=true;
            System.out.println("draggable is true mouse pressed");
        }
    }
    @Override
    public void mouseDragged(MouseEvent e){
        System.out.println("mouse dragged");
        if(draggable==true){
            System.out.println("draggable is true");
            fp.featurePoints[position]=e.getPoint();
            fp.repaint();
        }
    }
    @Override
    public void mouseReleased(MouseEvent e){
        System.out.println("mouse released");
        draggable=false;
    }
    @Override
    public void mouseMoved(MouseEvent e){
       System.out.println("mouse moved");
       position = whichFeaturePoint(e.getPoint(), width);
       System.out.println(position);
    }
}