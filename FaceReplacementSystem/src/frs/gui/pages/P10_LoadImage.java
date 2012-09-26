package frs.gui.pages;

import frs.gui.components.IOSUIImageView;
import frs.main.RFApplication;
import hu.droidzone.iosui.IOSUIButton;
import hu.droidzone.iosui.IOSUIView;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

public class P10_LoadImage extends RFPage {

    protected BufferedImage sourceImage, targetImage;
    protected IOSUIButton loadSourceBtn, loadTargetBtn;
    protected IOSUIImageView sourceView, targetView;

    public P10_LoadImage(RFApplication app) {
        super(app, "Load Images");
        initializeComponents();
        helpText.setText("Please load the images. The face in the source image replaces the face in the target image.");
    }

    public void initializeComponents() {
        mainView = new IOSUIView("350px,50px,350px", "350px,50px");
        initializeImageViews();
        initializeLoadBtns();
        mainView.addXY(sourceView, 1, 1);
        mainView.addXY(targetView, 3, 1);
        mainView.addXY(loadSourceBtn, 1, 2);
        mainView.addXY(loadTargetBtn, 3, 2);
        addXY(mainView, 1, 1);
    }

    public void initializeImageViews() {
        sourceView = new IOSUIImageView("350px", "350px");
        targetView = new IOSUIImageView("350px", "350px");
    }

    //It loads the images
    public void initializeLoadBtns() {
        loadSourceBtn = new IOSUIButton(new AbstractAction("Load Source") {

            @Override
            public void actionPerformed(ActionEvent e) {
                sourceImage = chooseImage();
                frs.setSrcImg(sourceImage);
                sourceView.setImage(frs.getSrcImg());
            }
        });

        loadTargetBtn = new IOSUIButton(new AbstractAction("Load Target") {

            @Override
            public void actionPerformed(ActionEvent e) {
                targetImage = chooseImage();
                targetView.setImage(targetImage);
                frs.setTargetImage(targetImage);
            }
        });
    }

    public BufferedImage chooseImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File file = chooser.getSelectedFile();
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException ex) {
            Logger.getLogger(P10_LoadImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }

    @Override
    public void goNext() {
        pc.navigateTo(new P20_FP(app, P20_FP.SOURCE));
    }
}
