package frs.gui.pages;

import frs.gui.components.FeaturePointsView;
import frs.main.RFApplication;
import java.awt.Point;

public class P40_TarFP extends RFPage {

    FeaturePointsView fp = new FeaturePointsView("600px", "400px");

    public P40_TarFP(RFApplication app) {
        super(app, "Specify Feature Points");
        fp.setImage(frs.getTargetImage());
        addXY(fp, 1, 1, "f,f");
        helpText.setText("Specify the feature points on the face.");
    }

    @Override
    public void goNext() {
        setTargetFeaturePoints();
        pc.navigateTo(new P50_TarRect(app));
    }

    //Saves the target feature points
    public void setTargetFeaturePoints() {
        Point[] featurePoints = fp.getFeaturePoints();
        Point[] tempFeaturePoints = new Point[featurePoints.length];
        for (int i = 0; i < featurePoints.length; i++) {
            tempFeaturePoints[i] = fp.toActualImagePoint(featurePoints[i]);
        }
        frs.setTarFP(tempFeaturePoints);
    }
}
