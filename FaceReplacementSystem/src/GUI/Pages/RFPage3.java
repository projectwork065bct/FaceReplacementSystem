package GUI.Pages;

import GUI.Components.RFApplication;
import GUI.Components.ResizableRectangleView;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

public class RFPage3 extends RFPage {

    ResizableRectangleView rrv = new ResizableRectangleView("600px", "400px");
    
    public RFPage3(RFApplication app) {
        super(app, "Second Page");
//		IOSUITextArea ta = new IOSUITextArea(new Font("Verdana", Font.BOLD, 22));
//		ta.setText("Second Page Content");
//		ta.setForeground(Color.WHITE);
//		addXY(ta,1,1,"f,f");
        rrv.setFRS(frs);
        rrv.setImage(frs.getSourceImage());
        rrv.initializeRectangle("source");
        rrv.setBackground(Color.red);
        addXY(rrv, 1, 1, "f,f");
        helpText.setText("Draw a rectangle around the face.");
    }

    @Override
    public void goNext() {
        setSourceRectangle();
        pc.navigateTo(new RFPage4(app));
    }
    
    //Saves the rectangle drawn around the face
    public void setSourceRectangle()
    {
        Rectangle r=rrv.getRectangle();
        Rectangle resized=new Rectangle();
        int recX=r.x;
        int recY=r.y;
        int width=r.width;
        int height=r.height;
        int stopx=recX+width;
        int stopy=recY+height;
        Point a=rrv.toActualImagePoint(new Point(recX,recY));
        Point b=rrv.toActualImagePoint(new Point(stopx,stopy));
        resized.x=a.x;resized.y=a.y;resized.width=b.x-a.x;resized.height=b.y-a.y;
        frs.setSourceFaceRectangle(resized);
    }
}
