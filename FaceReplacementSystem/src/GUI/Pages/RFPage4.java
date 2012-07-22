package GUI.Pages;

import GUI.Components.RFApplication;
import GUI.Components.FeaturePointsView;
import GUI.Components.ResizableRectangleView;
import java.awt.Color;
import java.awt.Point;

public class RFPage4 extends RFPage {

    FeaturePointsView fp = new FeaturePointsView("600px", "400px");

    public RFPage4(RFApplication app) {
        super(app, "Second Page");
//		IOSUITextArea ta = new IOSUITextArea(new Font("Verdana", Font.BOLD, 22));
//		ta.setText("Second Page Content");
//		ta.setForeground(Color.WHITE);
//		addXY(ta,1,1,"f,f");
        fp.setFRS(frs);
        fp.setImage(frs.getTargetImage());
        addXY(fp, 1, 1, "f,f");

        helpText.setText("Specify the feature points on the face.");
    }

    @Override
    public void goNext() {
        setTargetFeaturePoints();
        pc.navigateTo(new RFPage5(app));
    }

    //Saves the target feature points
    public void setTargetFeaturePoints() {
        Point[] featurePoints = fp.getFeaturePoints();
        Point[] tempFeaturePoints = new Point[featurePoints.length];
        for (int i = 0; i < featurePoints.length; i++) {
            tempFeaturePoints[i] = fp.toActualImagePoint(featurePoints[i]);
        }
        frs.setTargetFeaturePoints(tempFeaturePoints);
    }
}
