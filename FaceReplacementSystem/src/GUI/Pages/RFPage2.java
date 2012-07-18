package GUI.Pages;

import GUI.Library.RFApplication;
import GUI.temp.ResizableRectangleView;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

public class RFPage2 extends RFPage {

    ResizableRectangleView rrv = new ResizableRectangleView("600px", "400px");
    
    public RFPage2(RFApplication app) {
        super(app, "Second Page");
//		IOSUITextArea ta = new IOSUITextArea(new Font("Verdana", Font.BOLD, 22));
//		ta.setText("Second Page Content");
//		ta.setForeground(Color.WHITE);
//		addXY(ta,1,1,"f,f");
        rrv.setFRS(frs);
        rrv.setImage(frs.getSourceImage());
        rrv.setBackground(Color.red);
        addXY(rrv, 1, 1, "f,f");
        helpText.setText("Draw rectangle around the face");
    }

    @Override
    public void goNext() {
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
        resized.x=a.x;resized.y=a.y;resized.width=a.x-b.x;resized.height=a.y-b.y;
        frs.setSourceFaceRectangle(resized);
        System.out.println("rect height = "+frs.getSourceFaceRectangle().height);
        pc.navigateTo(new RFPage3(app));
    }
}
