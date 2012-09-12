/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.helpers;


import frs.helpers.SnakeConfigDriver;
import frs.helpers.SnakeConfig;
import ij.ImagePlus;
import ij.ImageStack;
import ij.Prefs;
import ij.gui.Roi;
import ij.plugin.frame.RoiManager;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author User
 */
public class SnakeInitializer {
    //variable declaration
    ImagePlus imp;
    //  for the intermediate results :
    ImageStack pile = null;
    ImageStack pile_resultat = null;
    ImageStack pile_seg = null;
    int currentSlice = -1;
    // Dimensions for intermediate results :
    int pileSize = 0;
    int lengthOfPile = 0;
    int heightOfPile = 0;
    // ROI original coordinates
    int nbRois;
    Roi rorig = null;
    Roi processRoi = null;
    Color colorDraw = null;
    
    SnakeConfigDriver configDriver;
    // number of iterations
    int ite = 50;
    // step to display snake
    int step = 1;
    // threshold of edges
    int threholdOfEdge = 5;
    // how far to look for edges
    int DistMAX = Prefs.getInt("SnakeInitializerDistSearch.int", 100);
    // maximum displacement
    double force = 5.0;
    // regularization factors, min and max
    double reg = 5.0;
    double regmin, regmax;
    // first and last slice to process
    int slice1, slice2;
    // this will save the Roi of the final result
    boolean savecoords = false;    
    boolean propagate = true;
   
    
    public void run(ImageProcessor ip) throws IOException {
        // original stack
        pile = imp.getStack();
        // sizes of the stack
        pileSize = pile.getSize();
        lengthOfPile = pile.getWidth();
        heightOfPile = pile.getHeight();
        slice1 = 1;
        slice2 = pileSize;
        //parameters set this is set from textboxes
        //threholdOfEdge = 3;
        //ite = 10;
        
        step =1;
        colorDraw = Color.red;
        savecoords = true;
        //many rois coz ROI gets updated regularly
        RoiManager roimanager = RoiManager.getInstance();
        if (roimanager == null) {
           roimanager = new RoiManager(false);
           rorig = imp.getRoi();
           try{
                roimanager.remove(0);
              }
          catch(Exception e)
              {  }                
                roimanager.add(imp, rorig, 0);                           
        }
        
        nbRois = roimanager.getCount();
        /**.
         * Original ROI is saved to RoisOrig and CurrentRoi after each processing
         * is saved to RoisCurrent.
         * .*/
        final Roi[] RoisOrig = roimanager.getRoisAsArray();
        final Roi[] RoisCurrent = new Roi[nbRois];
        Roi[] RoisResult = new Roi[nbRois];
        for (int i = 0; i < nbRois; i++) {
            RoisCurrent[i] = RoisOrig[i];
        }        
            configDriver = new SnakeConfigDriver();
            AdvancedParameters();
            regmin = reg / 2.0;
            regmax = reg;
            // init result
            pile_resultat = new ImageStack(lengthOfPile, heightOfPile, java.awt.image.ColorModel.getRGBdefault());
            // update of the display
            String label = "" + imp.getTitle();
            for (int z = 0; z < pileSize; z++) {
                pile_resultat.addSlice(label, pile.getProcessor(z + 1).duplicate().convertToRGB());
            }
            final AtomicInteger k = new AtomicInteger(0);
            final SnakeClass[] snakes = new SnakeClass[RoisOrig.length];
            // for all slices
            // display in RGB color
            final ColorProcessor[] image = new ColorProcessor[RoisOrig.length];
            final ImagePlus[] pluses = new ImagePlus[RoisOrig.length];

            int sens = slice1 < slice2 ? 1 : -1;
            for (int z = slice1; z != (slice2 + sens); z += sens) {
                final int zz = z;
                k.set(0);

                for (int i = 0; i < RoisOrig.length; i++) {
                    image[i] = (ColorProcessor) (pile_resultat.getProcessor(zz).duplicate());
                    pluses[i] = new ImagePlus("Roi " + i, image[i]);
                }
                Roi roi = null;                
                            for (int i = k.getAndIncrement(); i < RoisOrig.length; i = k.getAndIncrement()) {
                                if (propagate) {
                                    roi = RoisCurrent[i];
                                } else {
                                    roi = RoisOrig[i];
                                }                               
                                snakes[i] = processSnake(pluses[i], roi, zz, i + 1);
                                
                           } // for roi
                // display + rois
                ColorProcessor imageDraw = (ColorProcessor) (pile_resultat.getProcessor(zz).duplicate());
                for (int i = 0; i < RoisOrig.length; i++) {
                    snakes[i].DrawSnake(imageDraw, colorDraw, 1);
                    pluses[i].hide();
                    RoisResult[i] = snakes[i].createRoi();
                    RoisResult[i].setName("res-" + i);
                    RoisCurrent[i] = snakes[i].createRoi();                   

                }
                pile_resultat.setPixels(imageDraw.getPixels(), z);


                if (savecoords) {
                    for (int i = 0; i < RoisOrig.length; i++) {                        
                            snakes[i].getProbableFaceEdgeCoordinate();                            
                    }
            
                }
            } 
    }// save coord
                    
    
  private void AdvancedParameters() {       
        configDriver.setMaxDisplacement(Prefs.get("SnakeInitiializerDisplMin.double", 0.1), Prefs.get("SnakeInitiializerDisplMax.double", 2.0));
        configDriver.setInvAlphaD(Prefs.get("SnakeInitiializerInvAlphaMin.double", 0.5), Prefs.get("SnakeInitiializerInvAlphaMax.double", 2.0));
        configDriver.setReg(Prefs.get("SnakeInitiializerRegMin.double", 0.1), Prefs.get("SnakeInitiializerRegMax.double", 2.0));
        configDriver.setStep(Prefs.get("SnakeInitiializerMulFactor.double", 0.99));
    }
   
    public SnakeClass processSnake(ImagePlus plus, Roi roi, int numSlice, int numRoi) throws IOException {
        int i;
        SnakeConfig config;
        processRoi = roi;

        // initialization of the snake
        SnakeClass snake = new SnakeClass();
        snake.Init(processRoi);
        snake.setOriginalImage(pile.getProcessor(numSlice));
 
        double InvAlphaD = configDriver.getInvAlphaD(false);
        double regMax = configDriver.getReg(false);
        double regMin = configDriver.getReg(true);
        double DisplMax = configDriver.getMaxDisplacement(false);
        double mul = configDriver.getStep();

        config = new SnakeConfig(threholdOfEdge, DisplMax, DistMAX, regMin, regMax, 1.0 / InvAlphaD);
        snake.setConfig(config);
        // compute image gradient
        snake.computeGrad(pile.getProcessor(numSlice));
        double dist0 = 0.0;
        double dist;
        for (i = 0; i < ite; i++) {
            
            // each iteration
            dist = snake.process();
            if ((dist >= dist0) && (dist < force)) {
                snake.computeGrad(pile.getProcessor(numSlice));
                config.update(mul);
            }
            dist0 = dist;
            //Intermediate results of the steps given in step
            if ((step > 0) && ((i % step) == 0)) {
                ColorProcessor image2 = (ColorProcessor) (pile_resultat.getProcessor(numSlice).duplicate());
                snake.DrawSnake(image2, colorDraw, 1);
                plus.setProcessor("", image2);                
                //Intermediate Results
                BufferedImage intermediateImage = image2.getBufferedImage(); // Set this image to the draw panel to show the intermediate results
                //display in the window
            }
        }// end iteration
        return snake;
    }
    public void set(ImagePlus imp)
    {
        this.imp = imp;        
    }

    public void setIte(int ite) {
        this.ite = ite;
    }

    public void setThreholdOfEdge(int threholdOfEdge) {
        this.threholdOfEdge = threholdOfEdge;
    }
    

}
