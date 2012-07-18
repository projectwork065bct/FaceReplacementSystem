package GUI.Pages;

import GUI.Library.RFApplication;
import GUI.temp.FeaturePoints;
import GUI.temp.ResizableRectangleView;
import java.awt.Color;
import java.awt.Point;

public class RFPage5 extends RFPage {

    FeaturePoints fp = new FeaturePoints("600px", "400px");
    
    public RFPage5(RFApplication app) {
        super(app, "Second Page");
//		IOSUITextArea ta = new IOSUITextArea(new Font("Verdana", Font.BOLD, 22));
//		ta.setText("Second Page Content");
//		ta.setForeground(Color.WHITE);
//		addXY(ta,1,1,"f,f");
        fp.setFRS(frs);
        fp.setImage(frs.getTargetImage());
        addXY(fp, 1, 1, "f,f");
        
        helpText.setText("Draw rectangle around the face");
    }

    @Override
    public void goNext() {
        
        Point[] featurePoints =fp.getFeaturePoints();
            Point[] tempFeaturePoints = new Point[featurePoints.length];
            for(int i=0;i<featurePoints.length;i++){
                tempFeaturePoints[i]=fp.toActualImagePoint(featurePoints[i]);
            }
            
            frs.setTargetFeaturePoints(tempFeaturePoints);
        
         pc.navigateTo(new RFPage3(app));
    }
}
