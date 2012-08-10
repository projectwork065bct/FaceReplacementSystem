package frs.gui.pages;

import frs.gui.components.FeaturePointsView;
import frs.main.RFApplication;
import java.awt.Point;

public class P20_SrcFP extends RFPage {

    FeaturePointsView fp = new FeaturePointsView("600px", "400px");
   
    public P20_SrcFP(RFApplication app) {
        super(app, "Specify Feature Points");
        fp.setImage(frs.getSrcImg());
        addXY(fp, 1, 1, "f,f");
        helpText.setText("Specify the feature points on the face.");
    }

    @Override
    public void goNext() {
        setSourceFeaturePoints();
        pc.navigateTo(new P30_SrcRect(app));
    }

    //Saves the feature points in the source image
    public void setSourceFeaturePoints() {
        Point[] featurePoints = fp.getFeaturePoints();//feature Points as they are drawn, scaled instance
        Point[] tempFeaturePoints = new Point[featurePoints.length];
        for (int i = 0; i < featurePoints.length; i++) {
            tempFeaturePoints[i] = fp.toActualImagePoint(featurePoints[i]);//map point to original image size
        }
        frs.setSrcFP(tempFeaturePoints);
    }
}
