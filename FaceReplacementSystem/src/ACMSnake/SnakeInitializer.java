/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ACMSnake;


import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.Prefs;
import ij.gui.Roi;
import ij.measure.Calibration;
import ij.plugin.frame.RoiManager;
import ij.process.Blitter;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;
import java.awt.Color;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author User
 */
public class SnakeInitializer {
    //variable declaration
    ImagePlus imp;
    // Sauvegarde de la pile de debut et de resultat :
    ImageStack pile = null;
    ImageStack pile_resultat = null;
    ImageStack pile_seg = null;
    int currentSlice = -1;
    // Sauvegarde des dimensions de la pile :
    int profondeur = 0;
    int largeur = 0;
    int hauteur = 0;
    // ROI originale et courante
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
    // misc options
    boolean showgrad = false;
    boolean savecoords = false;
    boolean createsegimage = false;
    boolean advanced = false;
    boolean propagate = true;
    boolean movie = false;
    
    public void run(ImageProcessor ip) {
        // original stack
        pile = imp.getStack();
        // sizes of the stack
        profondeur = pile.getSize();
        largeur = pile.getWidth();
        hauteur = pile.getHeight();
        slice1 = 1;
        slice2 = profondeur;
        Calibration cal=imp.getCalibration();
        double resXY=cal.pixelWidth;
//parameters set
        threholdOfEdge = 3;
        ite = 10;
        step =1;
        colorDraw = Color.red;
        savecoords = true;
        createsegimage = false;
  //       many rois
        RoiManager roimanager = RoiManager.getInstance();
        if (roimanager == null) {
           roimanager = new RoiManager(false);
            roimanager.setVisible(false);           
            rorig = imp.getRoi();
            if (rorig == null) {
                System.out.print("Roi required");
                } else {
                try{
                roimanager.remove(0);
                        }
                catch(Exception e)
                {
                    
                }
                
                roimanager.add(imp, rorig, 0);
               
            }
        }
        
        nbRois = roimanager.getCount();
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
            pile_resultat = new ImageStack(largeur, hauteur, java.awt.image.ColorModel.getRGBdefault());
            if (createsegimage) {
                pile_seg = new ImageStack(largeur, hauteur);
            }
            // update of the display
            String label = "" + imp.getTitle();
            for (int z = 0; z < profondeur; z++) {
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

                if (createsegimage) {
                    ByteProcessor seg = new ByteProcessor(pile_seg.getWidth(), pile_seg.getHeight());
                    ByteProcessor tmp;
                    for (int i = 0; i < RoisOrig.length; i++) {
                        tmp = snakes[i].segmentation(seg.getWidth(), seg.getHeight(), i + 1);
                        seg.copyBits(tmp, 0, 0, Blitter.ADD);
                    }
                    seg.resetMinAndMax();
                    pile_seg.addSlice("Seg " + z, seg);
                   }

                if (savecoords) {
                    for (int i = 0; i < RoisOrig.length; i++) {                        
                            snakes[i].getProbableFaceEdgeCoordinate();                            
                    }
            
                }
            } 
    }// save coord
                    
    
  private void AdvancedParameters() {
        // see advanced dialog class
        configDriver.setMaxDisplacement(Prefs.get("ABSnake_DisplMin.double", 0.1), Prefs.get("ABSnake_DisplMax.double", 2.0));
        configDriver.setInvAlphaD(Prefs.get("ABSnake_InvAlphaMin.double", 0.5), Prefs.get("ABSnake_InvAlphaMax.double", 2.0));
        configDriver.setReg(Prefs.get("ABSnake_RegMin.double", 0.1), Prefs.get("ABSnake_RegMax.double", 2.0));
        configDriver.setStep(Prefs.get("ABSnake_MulFactor.double", 0.99));
    }
    public SnakeClass processSnake(ImagePlus plus, Roi roi, int numSlice, int numRoi) {
        int i;
        SnakeConfig config;
        processRoi = roi;

        // initialisation of the snake
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
            if (IJ.escapePressed()) {
                break;
            }
            // each iteration
            dist = snake.process();
            if ((dist >= dist0) && (dist < force)) {
                //System.out.println("update " + config.getAlpha());
                snake.computeGrad(pile.getProcessor(numSlice));
                config.update(mul);
            }
            dist0 = dist;
            // display of the snake
            if ((step > 0) && ((i % step) == 0)) {
                IJ.showStatus("Show intermediate result (iteration n" + (i + 1) + ")");
                ColorProcessor image2 = (ColorProcessor) (pile_resultat.getProcessor(numSlice).duplicate());
                snake.DrawSnake(image2, colorDraw, 1);
                plus.setProcessor("", image2);
                plus.setTitle(imp.getTitle() + " roi " + numRoi + " (iteration n" + (i + 1) + ")");
                plus.updateAndRepaintWindow();
            }
        }// end iteration

       

        return snake;
    }
    public void set(ImagePlus imp)
    {
        this.imp = imp;
        //return DOES_8G + DOES_16 + DOES_32 + NO_CHANGES;
    }

}
