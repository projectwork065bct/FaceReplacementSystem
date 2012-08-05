/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Components;

import DataStructures.FeaturePoint;
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
public class FeaturePointsView extends IOSUIImageView {

    Point[] featurePoints;
    
    public FeaturePointsView(String colspecs, String rowspecs) {
        super(colspecs, rowspecs);
        initComponents();
    }

    private void initComponents() {
        FeatureAdapter fa = new FeatureAdapter(this);
        addMouseListener(fa);
        addMouseMotionListener(fa);
        setFeaturePoints();

    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.red);
        //g.drawLine(41,41, 50, 50);
        drawFeaturePoints(g);
    }

    public Point[] getFeaturePoints() {
        return featurePoints;
    }

//    protected void setFeaturePoints() {
//        featurePoints = new Point[8];
//        featurePoints[FeaturePoint.LEFT_EYE] = new Point(140, 140);
//        featurePoints[FeaturePoint.RIGHT_EYE] = new Point(170, 140);
//        featurePoints[FeaturePoint.CENTER_OF_LIP] = new Point(155, 170);
//        featurePoints[FeaturePoint.CHIN] = new Point(155, 185);
//        featurePoints[FeaturePoint.LEFT_CHIN] = new Point(145, 180);
//        featurePoints[FeaturePoint.RIGHT_CHIN] = new Point(165, 180);
//        featurePoints[FeaturePoint.LEFT_CHEEK] = new Point(130, 170);
//        featurePoints[FeaturePoint.RIGHT_CHEEK] = new Point(180, 170);
//    }

    protected void setFeaturePoints() {
        featurePoints = new Point[8];
        featurePoints[FeaturePoint.LEFT_EYE] = new Point(140, 140);
        featurePoints[FeaturePoint.RIGHT_EYE] = new Point(200, 140);
        featurePoints[FeaturePoint.CENTER_OF_LIP] = new Point(170, 220);
        featurePoints[FeaturePoint.CHIN] = new Point(170, 260);
        featurePoints[FeaturePoint.LEFT_CHIN] = new Point(145, 240);
        featurePoints[FeaturePoint.RIGHT_CHIN] = new Point(195, 240);
        featurePoints[FeaturePoint.LEFT_CHEEK] = new Point(110, 220);
        featurePoints[FeaturePoint.RIGHT_CHEEK] = new Point(230, 220);
    }

    protected void drawFeaturePoints(Graphics g) {
        int width = 4;
        //for marking first feature point
        for (int i = 0; i < featurePoints.length; i++) {
            g.drawLine(featurePoints[i].x - width, featurePoints[i].y - width, featurePoints[i].x + width, featurePoints[i].y + width);
            g.drawLine(featurePoints[i].x - width, featurePoints[i].y + width, featurePoints[i].x + width, featurePoints[i].y - width);
            g.drawString("" + i, featurePoints[i].x + width + 2, featurePoints[i].y + width + 2);
        }

    }

    public static void main(String[] args) {
        IOSUIView content = new IOSUIView("p:g", "p:g");
        FeaturePointsView rajanPanel1 = new FeaturePointsView("400px", "400px");

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

}

class FeatureAdapter extends MouseAdapter {

    FeaturePointsView fp;
    int width;
    int position = -1;
    boolean draggable = false;

    FeatureAdapter(FeaturePointsView fp) {
        this.fp = fp;
        this.width = 5;
    }

    public int whichFeaturePoint(Point p, int width) {
        int xmin, xmax, ymin, ymax;
        for (int i = 0; i < fp.featurePoints.length; i++) {
            xmin = fp.featurePoints[i].x - width;
            xmax = fp.featurePoints[i].x + width;
            ymin = fp.featurePoints[i].y - width;
            ymax = fp.featurePoints[i].y + width;

            if (p.x >= xmin && p.x <= xmax) {
                if (p.y >= ymin && p.y <= ymax) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
//        fp.featurePoints[0]=e.getPoint();
//        fp.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (position != -1) {
            draggable = true;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (draggable == true) {
            fp.featurePoints[position] = e.getPoint();
            fp.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        draggable = false;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        position = whichFeaturePoint(e.getPoint(), width);
    }
}