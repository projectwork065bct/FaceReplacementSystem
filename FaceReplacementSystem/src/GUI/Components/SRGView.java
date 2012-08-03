/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Components;

import facereplacementsystem.FRSEngine;
import hu.droidzone.iosui.IOSUIView;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;

/**
 *
 * @author Dell
 */
public class SRGView extends IOSUIImageView {

    public static final int ERASER = 1;
    public static final int GROW = 2;
    protected int mode = GROW;
    protected FRSEngine frs;
    protected String imageStr;
    protected Rectangle seedRect;
    protected Stack<Point> seedPoints;

    public SRGView(String colSpec, String rowSpec) {
        super(colSpec, rowSpec);
        seedPoints = new Stack();
        initComponents();
        setMode(GROW);
    }

    private void initComponents() {
        SRGRectAdapter rectAdapter = new SRGRectAdapter(this);
        addMouseListener(rectAdapter);
        addMouseMotionListener(rectAdapter);
    }

    public void setMode(int mode) {
        this.mode = mode;
        if (mode == GROW) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        }
    }

    public void defineImage(String s) {
        imageStr = s;
        frs.setWhichHairStr(imageStr);
        if (s.compareTo("source") == 0) {
            setImage(frs.getSrcHairImgShowingRegion());
        } else if (s.compareTo("target") == 0) {
            setImage(frs.getTarHairImgShowingRegion());
        }
    }

    public void applySRG() {
        for (int x = seedRect.x; x < seedRect.width + seedRect.x; x++) {
            for (int y = seedRect.y; y < seedRect.height + seedRect.y; y++) {
                seedPoints.push(toActualImagePoint(new Point(x, y)));
            }
        }
        if (imageStr.compareTo("source") == 0) {
            frs.setSourceHairSeeds(seedPoints);
            frs.detectSourceHair();
            setImage(frs.getSrcHairImgShowingRegion());
        } else if (imageStr.compareTo("target") == 0) {
            frs.setTargetHairSeeds(seedPoints);
            frs.detectTargetHair();
            setImage(frs.getTarHairImgShowingRegion());
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
//        if (seedRect != null) {
//            g.setColor(Color.RED);
//            g.drawRect(seedRect.x, seedRect.y, seedRect.width, seedRect.height);
//        }
    }

    public void setRectangle(Rectangle r) {
        this.seedRect = r;
        applySRG();
        repaint();
    }

    public Rectangle getRectangle() {
        return this.seedRect;
    }

    public void setFRSEngine(FRSEngine frs) {
        this.frs = frs;
    }
}

class SRGRectAdapter extends MouseAdapter {

    SRGView sv;
    Point startPoint = new Point(), endPoint = new Point();
    Point currentPoint = new Point();

    public SRGRectAdapter(IOSUIView view) {
        sv = (SRGView) view;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        startPoint = e.getPoint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startPoint = e.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currentPoint = e.getPoint();
        Rectangle r = new Rectangle(startPoint.x, startPoint.y, currentPoint.x - startPoint.x, currentPoint.y - startPoint.y);
        sv.setRectangle(r);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        endPoint = e.getPoint();
        Rectangle r = new Rectangle(startPoint.x, startPoint.y, endPoint.x - startPoint.x, endPoint.y - startPoint.y);
        sv.setRectangle(r);
    }

    public void drawRectangle(Point finalPoint) {
    }
}
