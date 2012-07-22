/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Components;

import GUI.Components.IOSUIImageView;
import facereplacementsystem.FaceReplacementSystem;
import hu.droidzone.iosui.IOSUIApplication;
import hu.droidzone.iosui.IOSUIButton;
import hu.droidzone.iosui.IOSUIHeader;
import hu.droidzone.iosui.IOSUIView;
import hu.droidzone.iosui.i18n.IOSI18N;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author power
 */
public class ResizableRectangleView extends IOSUIImageView {

    Rectangle rect ;//= new Rectangle(100, 100, 150, 150);
    Color rectColor = Color.YELLOW;
    FaceReplacementSystem frs;
    //constructors

    public ResizableRectangleView(String col, String row) {
        super(col, row);
        initComponents();
    }

    public ResizableRectangleView(int col, int row) {
        super(col, row);
        initComponents();
    }

    private void initComponents() {
        ResizerAdapter resizer = new ResizerAdapter(this);
        addMouseListener(resizer);
        addMouseMotionListener(resizer);
    }

    @Override
    public void paintComponent(Graphics g) {
        //super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.YELLOW);
        g2.draw(rect);
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
//       if(frs.getSourceImage()!=null)
//        {   g.drawImage(frs.getSourceImage(),0,0,null);
//        }
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.YELLOW);
        g2.draw(rect);
//        g2.setColor(Color.red);
//        g2.drawLine(0, 0, 50, 50);
    }

    protected void printRectangleInfo() {
        System.out.println(String.format("The rectangle is (%d,%d) width=%d,height=%d ", rect.x, rect.y, rect.width, rect.height));
    }

    //setters and getters
    
    public void initializeRectangle(String value){
//        this.rect= new Rectangle(100, 100, 150, 150);
        Rectangle actualRectInImage;
        if("source".equals(value))
            actualRectInImage=frs.getSourceFaceRectangle();
        else if("target".equals(value))
            actualRectInImage=frs.getTargetFaceRectangle();
        else actualRectInImage=null;
        
        Rectangle resizedRectangle=new Rectangle();
        int recX=actualRectInImage.x;
        int recY=actualRectInImage.y;
        int width=actualRectInImage.width;
        int height=actualRectInImage.height;
        int stopx=recX+width;
        int stopy=recY+height;
        
        Point a=this.toDrawnImagePoint(new Point(recX,recY));
        Point b=this.toDrawnImagePoint(new Point(stopx,stopy));
        resizedRectangle.x=a.x;resizedRectangle.y=a.y;resizedRectangle.width=b.x-a.x;resizedRectangle.height=b.y-a.y;
        this.rect=resizedRectangle;
        //System.out.printf("%d,%d,%d,%d",rect.x,rect.y,rect.width,rect.height);
    }
    public void setRectangle(Rectangle r) {
        this.rect = r;
    }

    public void setRectColor(Color c) {
        this.rectColor = c;
    }

    public Rectangle getRectangle() {
        return this.rect;
    }

    public static void main(String[] args) {
        IOSUIView content = new IOSUIView("p:g", "p:g");
        ResizableRectangleView rajanPanel1 = new ResizableRectangleView("400px", "400px");
        
        content.addXY(rajanPanel1, 1, 1);

        IOSUIButton btn = new IOSUIButton(new AbstractAction(IOSI18N.get("msg.btn.exit")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        btn.setBackground(new Color(200, 225, 0, 140));
        IOSUIHeader hdr = IOSUIHeader.createApplicationHeader(0, 1, "Rajan's lesson 1 of the IOSUI Application");
        hdr.addXY(btn, 2, 1, "c,c");
        IOSUIApplication.startIOSUIApplication(600, 400, content, hdr);
    }

    public void setFRS(FaceReplacementSystem frs) {
        this.frs = frs;
//        Rectangle actualRectInImage=frs.getSourceFaceRectangle();
//        
//        Rectangle resizedRectangle=new Rectangle();
//        int recX=actualRectInImage.x;
//        int recY=actualRectInImage.y;
//        int width=actualRectInImage.width;
//        int height=actualRectInImage.height;
//        int stopx=recX+width;
//        int stopy=recY+height;
//        
//        System.out.printf("%d,%d,%d,%d",recX,recY,width,height);
//        
//        Point a=this.toDrawnImagePoint(new Point(recX,recY));
//        Point b=this.toDrawnImagePoint(new Point(stopx,stopy));
//        resizedRectangle.x=a.x;resizedRectangle.y=a.y;resizedRectangle.width=a.x-b.x;resizedRectangle.height=a.y-b.y;
//        this.rect=resizedRectangle;
//        
    }
}

class ResizerAdapter extends MouseAdapter {

    ResizableRectangleView component;
    boolean dragging = false;
    // Give user some leeway for selections.
    final int PROX_DIST = 3;

    public ResizerAdapter(ResizableRectangleView r) {
        component = r;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        component.printRectangleInfo();
        System.out.println("Package is ResizableRectangleView");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (component.getCursor() != Cursor.getDefaultCursor()) {
            // If cursor is set for resizing, allow dragging.
            dragging = true;
            System.out.println("Package is ResizableRectangleView");
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragging = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (dragging) {
            Point p = e.getPoint();
            Rectangle r = component.rect;
            int type = component.getCursor().getType();
            int dx = p.x - r.x;
            int dy = p.y - r.y;
            switch (type) {
                case Cursor.N_RESIZE_CURSOR:
                    int height = r.height - dy;
                    r.setRect(r.x, r.y + dy, r.width, height);
                    break;
                case Cursor.NW_RESIZE_CURSOR:
                    int width = r.width - dx;
                    height = r.height - dy;
                    r.setRect(r.x + dx, r.y + dy, width, height);
                    break;
                case Cursor.W_RESIZE_CURSOR:
                    width = r.width - dx;
                    r.setRect(r.x + dx, r.y, width, r.height);
                    break;
                case Cursor.SW_RESIZE_CURSOR:
                    width = r.width - dx;
                    height = dy;
                    r.setRect(r.x + dx, r.y, width, height);
                    break;
                case Cursor.S_RESIZE_CURSOR:
                    height = dy;
                    r.setRect(r.x, r.y, r.width, height);
                    break;
                case Cursor.SE_RESIZE_CURSOR:
                    width = dx;
                    height = dy;
                    r.setRect(r.x, r.y, width, height);
                    break;
                case Cursor.E_RESIZE_CURSOR:
                    width = dx;
                    r.setRect(r.x, r.y, width, r.height);
                    break;
                case Cursor.NE_RESIZE_CURSOR:
                    width = dx;
                    height = r.height - dy;
                    r.setRect(r.x, r.y + dy, width, height);
                    break;
                default:
                    System.out.println("unexpected type: " + type);
            }
            component.repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point p = e.getPoint();
        if (!isOverRect(p)) {
            if (component.getCursor() != Cursor.getDefaultCursor()) {
                // If cursor is not over rect reset it to the default.
                component.setCursor(Cursor.getDefaultCursor());
            }
            return;
        }
        // Locate cursor relative to center of rect.
        int outcode = getOutcode(p);
        Rectangle r = component.rect;
        switch (outcode) {
            case Rectangle.OUT_TOP:
                if (Math.abs(p.y - r.y) < PROX_DIST) {
                    component.setCursor(Cursor.getPredefinedCursor(
                            Cursor.N_RESIZE_CURSOR));
                }
                break;
            case Rectangle.OUT_TOP + Rectangle.OUT_LEFT:
                if (Math.abs(p.y - r.y) < PROX_DIST
                        && Math.abs(p.x - r.x) < PROX_DIST) {
                    component.setCursor(Cursor.getPredefinedCursor(
                            Cursor.NW_RESIZE_CURSOR));
                }
                break;
            case Rectangle.OUT_LEFT:
                if (Math.abs(p.x - r.x) < PROX_DIST) {
                    component.setCursor(Cursor.getPredefinedCursor(
                            Cursor.W_RESIZE_CURSOR));
                }
                break;
            case Rectangle.OUT_LEFT + Rectangle.OUT_BOTTOM:
                if (Math.abs(p.x - r.x) < PROX_DIST
                        && Math.abs(p.y - (r.y + r.height)) < PROX_DIST) {
                    component.setCursor(Cursor.getPredefinedCursor(
                            Cursor.SW_RESIZE_CURSOR));
                }
                break;
            case Rectangle.OUT_BOTTOM:
                if (Math.abs(p.y - (r.y + r.height)) < PROX_DIST) {
                    component.setCursor(Cursor.getPredefinedCursor(
                            Cursor.S_RESIZE_CURSOR));
                }
                break;
            case Rectangle.OUT_BOTTOM + Rectangle.OUT_RIGHT:
                if (Math.abs(p.x - (r.x + r.width)) < PROX_DIST
                        && Math.abs(p.y - (r.y + r.height)) < PROX_DIST) {
                    component.setCursor(Cursor.getPredefinedCursor(
                            Cursor.SE_RESIZE_CURSOR));
                }
                break;
            case Rectangle.OUT_RIGHT:
                if (Math.abs(p.x - (r.x + r.width)) < PROX_DIST) {
                    component.setCursor(Cursor.getPredefinedCursor(
                            Cursor.E_RESIZE_CURSOR));
                }
                break;
            case Rectangle.OUT_RIGHT + Rectangle.OUT_TOP:
                if (Math.abs(p.x - (r.x + r.width)) < PROX_DIST
                        && Math.abs(p.y - r.y) < PROX_DIST) {
                    component.setCursor(Cursor.getPredefinedCursor(
                            Cursor.NE_RESIZE_CURSOR));
                }
                break;
            default:    // center
                component.setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * Make a smaller Rectangle and use it to locate the cursor relative to the
     * Rectangle center.
     */
    private int getOutcode(Point p) {
        Rectangle r = (Rectangle) component.rect.clone();
        r.grow(-PROX_DIST, -PROX_DIST);
        return r.outcode(p.x, p.y);
    }

    /**
     * Make a larger Rectangle and check to see if the cursor is over it.
     */
    private boolean isOverRect(Point p) {
        Rectangle r = (Rectangle) component.rect.clone();
        r.grow(PROX_DIST, PROX_DIST);
        return r.contains(p);
    }
}