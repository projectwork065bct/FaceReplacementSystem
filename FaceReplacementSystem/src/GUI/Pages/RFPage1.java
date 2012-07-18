package GUI.Pages;

import hu.droidzone.iosui.IOSUIButton;
import java.awt.Color;
import java.awt.Font;

import hu.droidzone.iosui.IOSUILabel;
import hu.droidzone.iosui.IOSUITextArea;
import hu.droidzone.iosui.IOSUIView;
import GUI.Library.RFApplication;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import GUI.Library.IOSUIImageView;

public class RFPage1 extends RFPage {

    protected BufferedImage sourceImage, targetImage;
    protected IOSUIView mainView;
    protected IOSUIButton loadSourceBtn, loadTargetBtn;
    protected IOSUIImageView sourceView, targetView;

    public RFPage1(RFApplication app) {
        super(app, "First Page");
        initializeComponents();
//		IOSUITextArea ta = new IOSUITextArea(new Font("Verdana", Font.BOLD, 22));
//		ta.setText("First Page Content");
//		ta.setForeground(Color.WHITE);
//		addXY(ta,1,1,"f,f");

        helpText.setText("HelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpText");
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
                frs.setSourceImage(sourceImage);
                sourceView.setImage(frs.getSourceImage());
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
            Logger.getLogger(RFPage1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }

    @Override
    public void goNext() {
        pc.navigateTo(new RFPage2(app));
    }
}
