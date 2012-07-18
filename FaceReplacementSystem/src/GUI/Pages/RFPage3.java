package GUI.Pages;

import java.awt.Color;
import java.awt.Font;

import hu.droidzone.iosui.IOSUILabel;
import hu.droidzone.iosui.IOSUITextArea;
import GUI.Library.RFApplication;
import GUI.temp.ResizableRectangleView;
import java.awt.Point;
import java.awt.Rectangle;

public class RFPage3 extends RFPage {
ResizableRectangleView rrv = new ResizableRectangleView("600px", "400px");
	public RFPage3(RFApplication app) {
            super(app, "Third Page");
//		IOSUITextArea ta = new IOSUITextArea(new Font("Verdana", Font.BOLD, 22));
//		ta.setText("Second Page Content");
//		ta.setForeground(Color.WHITE);
//		addXY(ta,1,1,"f,f");
        rrv.setFRS(frs);
        rrv.setImage(frs.getTargetImage());
        rrv.initializeRectangle("target");
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
            resized.x=a.x;resized.y=a.y;resized.width=b.x-a.x;resized.height=b.y-a.y;
            frs.setTargetFaceRectangle(resized);
            
            //detect skin color
            //frs.detectSkin();
            //warp image
            //frs.warp();
            //color consistency
            //frs.applyColorConsistency();
            //replace face
            //
            //
            
            pc.navigateTo(new RFPage6(app));
	}
}
