/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.gui.components;

/**
 *
 * @author Robik Shrestha
 */
/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import frs.engine.FRSEngine;
import hu.droidzone.iosui.IOSUIView;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class SRGView extends IOSUIImageView {

    protected FRSEngine frs;
    protected String imageStr;
    protected Rectangle seedRect;
    protected Stack<Point> seedPoints;
    protected int cw = 5, ch = 5;//cursor width and height
    protected BufferedImage cursorImg;

    public SRGView(String colSpec, String rowSpec) {
        super(colSpec, rowSpec);
        seedPoints = new Stack();
        initComponents();
        repaint();
    }

    private void initComponents() {
        SRGRectAdapter rectAdapter = new SRGRectAdapter(this);
        addMouseListener(rectAdapter);
        addMouseMotionListener(rectAdapter);
    }

    public void setCursorSize(int w, int h) {
        this.cw = w;
        this.ch = h;
        if (w > 32) {
            w = 32;
        }
        if (h > 32) {
            h = 32;
        }
        Color tc = new Color(0, 0, 0, 0);
        cursorImg = new BufferedImage(32, 32, BufferedImage.TYPE_4BYTE_ABGR);//cursor size is 32X32
        for (int x = 0; x < 32; x++) {
            for (int y = 0; y < 32; y++) {
                cursorImg.setRGB(x, y, tc.getRGB());
            }
        }
        for (int x = 16 - w / 2; x <= 16 + w / 2; x++) {
            for (int y = 16 - h / 2; y <= 16 + h / 2; y++) {
                cursorImg.setRGB(x, y, new Color(0, 0, 255).getRGB());
            }
        }
        this.setCursor(getToolkit().createCustomCursor(cursorImg, new Point(16, 16), "c1"));
    }

    public void defineImage(String s) {
        imageStr = s;
        if (s.compareTo("source") == 0) {
            setImage(frs.getSrcHairImgShowingRegion());
        } else if (s.compareTo("target") == 0) {
            setImage(frs.getTarHairImgShowingRegion());
        }
    }

    public void applySRG() {
        if (imageStr.compareTo("source") == 0) {
            frs.setSourceHairSeeds(seedPoints);
            frs.detectSourceHair();
            setImage(frs.getSrcHairImgShowingRegion());
        } else if (imageStr.compareTo("target") == 0) {
            frs.setTargetHairSeeds(seedPoints);
            frs.detectTargetHair();
            setImage(frs.getTarHairImgShowingRegion());
        }
        repaint();

    }

    public void resetSeedPoints() {
        seedPoints = new Stack();//remove it if u wish to continue to grow from the previous region
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

//        if (seedRect != null) {
//            g.setColor(Color.RED);
//            g.drawRect(seedRect.x, seedRect.y, seedRect.width, seedRect.height);
//        }
    }

    public void setSeed(Point p) {
        Point startP = toActualImagePoint(new Point(p.x - cw / 2, p.y - ch / 2));
        Point endP = toActualImagePoint(new Point(p.x + cw / 2, p.y + ch / 2));
        for (int x = startP.x; x <= endP.x; x++) {
            for (int y = startP.y; y <= endP.y; y++) {
                seedPoints.add(new Point(x, y));
            }
        }
    }

    public void setFRSEngine(FRSEngine frs) {
        this.frs = frs;
    }
}

class SRGRectAdapter extends MouseAdapter {

    int rw, rh;//width and height of rectangle
    SRGView sv;
    Point startPoint = new Point(), endPoint = new Point();
    Point currentPoint = new Point();

    public void setRectangleSize(int w, int h) {
        this.rw = w;
        this.rh = h;
    }

    public SRGRectAdapter(IOSUIView view) {
        sv = (SRGView) view;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        startPoint = e.getPoint();
        sv.setSeed(startPoint);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        sv.resetSeedPoints();
        startPoint = e.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currentPoint = e.getPoint();
        sv.setSeed(currentPoint);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        endPoint = e.getPoint();
        sv.setSeed(endPoint);
        sv.applySRG();
    }
}
