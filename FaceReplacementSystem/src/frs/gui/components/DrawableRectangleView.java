/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.gui.components;

import hu.droidzone.iosui.IOSUIView;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Dell
 */
public class DrawableRectangleView extends IOSUIImageView {

    public DrawableRectangleView(String colSpec, String rowSpec) {
        super(colSpec, rowSpec);
        initComponents();
    }

    public void setRectangle(Rectangle r) {
        this.r = r;
        repaint();
    }

    public Rectangle getRectangle() {
        return this.r;
    }

    private void initComponents() {
        RectangleAdapter rectAdapter = new RectangleAdapter(this);
        addMouseListener(rectAdapter);
        addMouseMotionListener(rectAdapter);
    }
    protected Rectangle r;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (r != null) {
            g.setColor(Color.RED);
            g.drawRect(r.x, r.y, r.width, r.height);
        }
    }
}

class RectangleAdapter extends MouseAdapter {

    DrawableRectangleView drv;
    Point startPoint = new Point(), endPoint = new Point();
    Point currentPoint = new Point();

    public RectangleAdapter(IOSUIView view) {
        drv = (DrawableRectangleView) view;
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
        drv.setRectangle(r);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        endPoint = e.getPoint();
        Rectangle r = new Rectangle(startPoint.x, startPoint.y, endPoint.x - startPoint.x, endPoint.y - startPoint.y);
        drv.setRectangle(r);
    }

    public void drawRectangle(Point finalPoint) {
    }
}
