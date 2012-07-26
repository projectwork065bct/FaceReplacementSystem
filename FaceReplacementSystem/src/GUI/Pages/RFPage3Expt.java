package GUI.Pages;

import DataStructures.FeaturePoint;
import GUI.Components.IOSUIImageView;
import GUI.Components.RFApplication;
import Helpers.DeepCopier;
import Helpers.Transformer;
import com.sun.xml.internal.ws.api.FeatureConstructor;
import hu.droidzone.iosui.IOSUIButton;
import hu.droidzone.iosui.IOSUIView;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.media.jai.GeometricOpImage;
import javax.swing.AbstractAction;

public class RFPage3Expt extends RFPage {

    IOSUIView mainView;
    IOSUIImageView imgView;
    IOSUIView buttonsView;
    IOSUIButton rotateBtn;
    IOSUIButton curveBtn;//chin curve
    AffineTransform transform;

    public RFPage3Expt(RFApplication app) {
        super(app, "Second Page");
        mainView = new IOSUIView("500px,100px", "400px");
        imgView = new IOSUIImageView("500px", "400px");
        mainView.addXY(imgView, 1, 1);
        buttonsView = new IOSUIView("100px", "100px,100px,100px,100px");
        rotateBtn = new IOSUIButton(new AbstractAction("Rotate") {

            @Override
            public void actionPerformed(ActionEvent e) {
                calcAffineTransform();
                rotateImage();
            }
        });
        curveBtn = new IOSUIButton(new AbstractAction("Draw Curve") {

            @Override
            public void actionPerformed(ActionEvent e) {
                drawCurve();
            }
        });

        buttonsView.addXY(rotateBtn, 1, 1);
        buttonsView.addXY(curveBtn, 1, 2);
        mainView.addXY(buttonsView, 2, 1);
        addXY(mainView, 1, 1);
        helpText.setText("The image will be rotated according to the coordinates of eyes");
    }

    @Override
    public void goNext() {
    }

    @Override
    public void pageCameIn() {
        app.btnNext.setVisible(false);
    }

    @Override
    public void pageRemoved() {
        app.btnNext.setVisible(true);
    }

    private void calcAffineTransform() {
        transform = new AffineTransform();
    }

    private void rotateImage() {
        Point sfp[] = frs.getSourceFeaturePoints();

        int delY = sfp[FeaturePoint.RIGHT_EYE].y - sfp[FeaturePoint.LEFT_EYE].y;
        int delX = sfp[FeaturePoint.RIGHT_EYE].x - sfp[FeaturePoint.LEFT_EYE].x;
        System.out.printf("left eye = (%d,%d) and right eye = (%d,%d)", sfp[FeaturePoint.LEFT_EYE].x, sfp[FeaturePoint.LEFT_EYE].y, sfp[FeaturePoint.RIGHT_EYE].x, sfp[FeaturePoint.RIGHT_EYE].y);
        float theta = (float) -Math.atan2(delY, delX);
        System.out.println("theta = " + theta);
        BufferedImage srcImg = DeepCopier.getBufferedImage(frs.getSourceRectangleImage(), BufferedImage.TYPE_INT_ARGB);
        AffineTransform transformImg = new AffineTransform();
        transformImg.rotate(theta, sfp[FeaturePoint.LEFT_EYE].x, sfp[FeaturePoint.LEFT_EYE].y);
        AffineTransformOp op = new AffineTransformOp(transformImg, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage rotatedImg = op.filter(srcImg, null);

        Point chin = sfp[FeaturePoint.CHIN];
        Point rectChin = new Point(chin.x - frs.getSourceFaceRectangle().x, chin.y - frs.getSourceFaceRectangle().y);
        Point leftEye = sfp[FeaturePoint.LEFT_EYE];
        Point rectLeftEye = new Point(leftEye.x - frs.getSourceFaceRectangle().x, leftEye.y - frs.getSourceFaceRectangle().y);
        Point rotatedChin = Transformer.rotatePoint(rectChin, theta, leftEye);
        rotatedImg.getGraphics().setColor(Color.RED);
        rotatedImg.getGraphics().fillOval(rotatedChin.x - 4, rotatedChin.y - 4, 8, 8);
        imgView.setImage(rotatedImg);
    }

    private void drawCurve() {
        frs.FindSourceCurves();
        BufferedImage sourceImg = DeepCopier.getBufferedImage(frs.getSourceImage(), BufferedImage.TYPE_INT_ARGB);
        List<Point> sle = frs.getSourceLeftEdge();
        List<Point> sre = frs.getSourceRightEdge();
        for (int i = 0; i < sle.size(); i++) {
            sourceImg.setRGB(sle.get(i).x, sle.get(i).y, Color.red.getRGB());
            
        }
        for(int i=0;i<sre.size();i++)
        {
            sourceImg.setRGB(sre.get(i).x, sre.get(i).y, Color.red.getRGB());
        }
        imgView.setImage(sourceImg);
    }
}