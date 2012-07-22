package GUI.Pages;

import GUI.Library.RFApplication;
import GUI.temp.FeaturePoints;
import java.awt.Point;

public class RFPage2 extends RFPage {

    FeaturePoints fp = new FeaturePoints("600px", "400px");
    
	public RFPage2(RFApplication app) {
		super(app, "Fourth Page");
//		IOSUITextArea ta = new IOSUITextArea(new Font("Verdana", Font.BOLD, 22));
//		ta.setText("Fourth Page Content");
//		ta.setForeground(Color.WHITE);
//		addXY(ta,1,1,"f,f");
                fp.setFRS(frs);
                fp.setImage(frs.getSourceImage());
                //rrv.setBackground(Color.red);
                addXY(fp, 1, 1, "f,f");
		helpText.setText("Specify the feature points on the face.");
	}

	@Override
	public void goNext() {
            setSourceFeaturePoints();
            pc.navigateTo(new RFPage3(app));
	}
        
        //Saves the feature points in the source image
        public void setSourceFeaturePoints()
        {
        Point[] featurePoints =fp.getFeaturePoints();
            Point[] tempFeaturePoints = new Point[featurePoints.length];
            for(int i=0;i<featurePoints.length;i++){
                tempFeaturePoints[i]=fp.toActualImagePoint(featurePoints[i]);
            }
            frs.setSourceFeaturePoints(tempFeaturePoints);
        }

	
}
