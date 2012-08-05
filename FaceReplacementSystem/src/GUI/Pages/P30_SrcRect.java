package GUI.Pages;

import GUI.Components.RFApplication;
import GUI.Components.ResizableRectangleView;
import H_Matrix.ImageMat;
import Helpers.DeepCopier;
import Helpers.Transformer;
import hu.droidzone.iosui.IOSUIButton;
import hu.droidzone.iosui.IOSUIView;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.AbstractAction;

public class P30_SrcRect extends RFPage {

    ResizableRectangleView rrv = new ResizableRectangleView("500px", "400px");
    IOSUIView buttonsView;
    IOSUIButton detectSkinBtn, showOriginalBtn, shrinkBtn, growBtn;
    IOSUIButton drawChinBtn;
    IOSUIButton drawRotatedSrcFaceBtn;

    public P30_SrcRect(final RFApplication app) {
        super(app, "Draw Rectangle");
        initializeComponents();
    }

    protected void initializeComponents() {

        mainView = new IOSUIView("600px,20px,100px,60px", "400px");
        buttonsView = new IOSUIView("100px", "40px,40px,40px,40px,40px,40px");
        initializeButtons();
        frs.rotateSource();
        //frs.drawFPOnRotatedImage();
        rrv.setImage(frs.getRotatedSourceImage());
        rrv.initializeRectangle(Transformer.getRectangleUsingFP(frs.getRotatedSrcFP()));
        mainView.addXY(rrv, 1, 1, "f,f");
        mainView.addXY(buttonsView, 3, 1, "f,f");
        addXY(mainView, 1, 1);
        helpText.setText("Adjust the rectangle around the face.");
    }

    protected void initializeButtons() {
        detectSkinBtn = new IOSUIButton(new AbstractAction("Detect Skin") {

            @Override
            public void actionPerformed(ActionEvent e) {
                setSourceRectangle();
                frs.detectSourceSkin();
                rrv.setImage(frs.getSourceSkinImage());
            }
        });
        buttonsView.addXY(detectSkinBtn, 1, 1);
        showOriginalBtn = new IOSUIButton(new AbstractAction("Show Original") {

            @Override
            public void actionPerformed(ActionEvent e) {
                rrv.setImage(frs.getRotatedSourceImage());
            }
        });
        buttonsView.addXY(showOriginalBtn, 1, 2);
        shrinkBtn = new IOSUIButton(new AbstractAction("Shrink") {

            @Override
            public void actionPerformed(ActionEvent e) {
                frs.shrinkSource();
                rrv.setImage(frs.getShrunkSrcImage());
                growBtn.enable(true);
            }
        });
        buttonsView.addXY(shrinkBtn, 1, 3);

        growBtn = new IOSUIButton(new AbstractAction("Grow") {

            @Override
            public void actionPerformed(ActionEvent e) {
                frs.growSource();
                rrv.setImage(frs.getGrownSrcImage());
            }
        });
        buttonsView.addXY(growBtn, 1, 4);
        growBtn.enable(false);

        drawChinBtn = new IOSUIButton(new AbstractAction("Draw Chin*") {

            @Override
            public void actionPerformed(ActionEvent e) {
               /* setSourceRectangle();
                frs.detectSourceSkin();
                
                frs.shrinkSource();
                frs.growSource();
                
                frs.findSourceCurves();
                
                
                frs.useSrcCurves();
                
                frs.drawSourceCurves();
                rrv.setImage(frs.getSourceImageWithCurve());*/
                
//                frs.findSourceBoundaryFilledMatrix();
//                frs.findSourceBoundaryFilledImage();
//                rrv.setImage(frs.getSourceBoundaryFilledImage());
//                rrv.setImage(frs.getSourceRectangleImage());
                
                
                setSourceRectangle();
                frs.detectSourceSkin();
                frs.shrinkSource();
                frs.growSource();
                frs.findSourceCurves();
                frs.useSrcCurves();
                frs.findSourceBoundaryFilledMatrix();
                frs.findSourceBoundaryFilledImage();
                rrv.setImage(frs.getSourceBoundaryFilledImage());
            }
        });
        buttonsView.addXY(drawChinBtn, 1, 5);

        drawRotatedSrcFaceBtn = new IOSUIButton(new AbstractAction("Draw Face") {

            @Override
            public void actionPerformed(ActionEvent e) {
                setSourceRectangle();
                frs.drawFPOnSrcRectImage();
                rrv.setImage(frs.getSrcRectImgWithFP());
            }
        });
        buttonsView.addXY(drawRotatedSrcFaceBtn, 1, 6);
        
        IOSUIButton chinCurve=new IOSUIButton(new AbstractAction("Draw Curve"){

            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedImage b=DeepCopier.getBufferedImage(frs.getSourceImage(), frs.getSourceImage().getType());
                Graphics g=b.getGraphics();
                List<Point> le=frs.getSourceRectLeftEdge();
                for(int i=0;i<le.size();i++){
                    g.drawOval(le.get(i).x, le.get(i).y, 1, 1);
                }
            }
            
        });
        //buttonsView.addXY(chinCurve, 2, 5);
    }

    @Override
    public void goNext() {
        setSourceRectangle();
        frs.detectSourceSkin();
        frs.shrinkSource();
        frs.growSource();
        frs.findSourceCurves();
        frs.useSrcCurves();
        frs.findSourceBoundaryFilledMatrix();
        frs.findSourceBoundaryFilledImage();
        pc.navigateTo(new P40_TarFP(app));
    }

    //Saves the rectangle drawn around the face
    //Also extracts the subimage inside the rectangle
    public void setSourceRectangle() {
        Rectangle r = rrv.getRectangle();
        Rectangle resized = new Rectangle();
        int stopx = r.x + r.width;
        int stopy = r.y + r.height;
        Point a = rrv.toActualImagePoint(new Point(r.x, r.y));
        Point b = rrv.toActualImagePoint(new Point(stopx, stopy));
        resized.x = a.x;
        resized.y = a.y;
        resized.width = b.x - a.x;
        resized.height = b.y - a.y;
        frs.setSourceFaceRectangle(resized);
        frs.findSourceRectImage();
        frs.findSourceRectFP();
    }
}
