package GUI.Pages;

import GUI.Components.RFApplication;
import GUI.Components.SRGView;
import Helpers.DeepCopier;
import hu.droidzone.iosui.IOSUIButton;
import hu.droidzone.iosui.IOSUIView;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.Stack;
import javax.swing.AbstractAction;

public class P60_Hair extends RFPage {
    
    SRGView srgView = new SRGView("600px", "400px");
    IOSUIView buttonsView = new IOSUIView("120px", "50px,50px,50px");
    IOSUIButton growBtn;
    Stack<Point> seedPoints;
    IOSUIButton chooseSrcBtn;
    IOSUIButton chooseTarBtn;
    
    public P60_Hair(RFApplication app) {
        super(app, "Specify hair region");
        frs.setTarHairImgShowingRegion(DeepCopier.getBufferedImage(frs.getTargetImage(), BufferedImage.TYPE_INT_ARGB));
        frs.setSrcHairImgShowingRegion(DeepCopier.getBufferedImage(frs.getSourceImage(), BufferedImage.TYPE_INT_ARGB));
        srgView.setFRSEngine(frs);
        srgView.defineImage("target");
        mainView = new IOSUIView("20px,600px,20px,120px,20px", "400px");
        mainView.addXY(srgView, 2, 1);
        initializeBtns();
        mainView.addXY(buttonsView, 4, 1);
        srgView.setImage(frs.getTargetImage());
        addXY(mainView, 1, 1);
        helpText.setText("Place seeds for hair region extraction.");
        seedPoints = new Stack();
    }
    
    @Override
    public void goNext() {
        pc.navigateTo(new P70_Replace(app));
    }
    
    protected void initializeBtns() {
        chooseSrcBtn = new IOSUIButton(new AbstractAction("Use Source Hair") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                srgView.defineImage("source");
            }
        });
        buttonsView.addXY(chooseSrcBtn,1,1);
        
        
        chooseTarBtn = new IOSUIButton(new AbstractAction("Use Target Hair") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                srgView.defineImage("target");
            }
        });
        buttonsView.addXY(chooseTarBtn,1,2);
    }
}
