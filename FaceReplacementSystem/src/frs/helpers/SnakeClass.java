/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.helpers;

import frs.helpers.Point2d;
import frs.helpers.SnakeConfig;
import frs.algorithms.ImageMat;
import ij.Prefs;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import java.awt.Color;
import java.awt.Rectangle;

/**
 *
 * @author User
 */
public class SnakeClass {
  
    Point2d points[];
    Point2d normale[];
    Point2d deplace[];
    double dataDistance;
    double lambda[];
    int etat[];
    int NPT;
    int NMAX = 500000;
    int block, elimination, ARRET;
    boolean closed;
    SnakeConfig config;
    ImageProcessor gradImage;
    ImageProcessor originalImage;
    public SnakeClass(){        
    }
    
    //make null everything first. Clear memory
    public void kill() {
        points = null;
        normale = null;
        deplace = null;
        lambda = null;
        etat = null;
        System.gc(); //runs finalization
    }
    
    /**
     *  Sets the config attribute of the SnakeDriver object
     *
     * @param  sc  The new config value
     */
    public void setConfig(SnakeConfig sc) {
        config = sc;
    }
    
     /**
     *  Get number of points
     *
     * @return    The nbPoints value
     */
    public int getNbPoints() {
        return NPT;
    }
    
    /**
     *  Gets the point attribute of the SnakeClass object
     *
     * @param  i  Description of the Parameter
     * @return    The point value
     */
    public Point2d getPoint(int i) {
        return points[i];
    }

    /**
     *  Gets the points attribute of the SnakeClass object
     *
     * @return    The points value
     */
    public Point2d[] getPoints() {
        return points;
    }

    /**
     *  Gets the config attribute of the SnakeClass object
     *
     * @return    The config value
     */
    public SnakeConfig getConfig() {
        return config;
    }

    /**
     *  Gets the lambda attribute of the SnakeClass object
     *
     * @return    The lambda value
     */
    public double[] getLambda() {
        return lambda;
    }

    /**
     *  Gets the displacement attribute of the SnakeClass object
     *
     * @return    The displacement value
     */
    public Point2d[] getDisplacement() {
        return deplace;
    }

    /**
     *  Is the snake closed
     *
     * @return    Description of the Return Value
     */
    public boolean closed() {
        return closed;
    }

    public void setOriginalImage(ImageProcessor originalImage) {
        this.originalImage = originalImage;
    }

    public static int[][] getFaceedgecoordinates() {
        return faceedgecoordinates;
    }

   //rajan
    public static int[][] getFaceEdgecoordinates2(Rectangle r){
        int [][] ans=new int[r.width][r.height];
        int [][] fec=faceedgecoordinates;
        for(int i=0;i<r.width;i++){
            for(int j=0;j<r.height;j++){
                ans[i][j]=fec[r.x+i][r.y+j];
            }
        }
        //snake has edges information
        int [][] xfilled=ImageMat.holeFill_Horizontally(ans, r.width, r.height);
        int [][] yfilled=ImageMat.holeFill_Vertically(xfilled, r.width, r.height);
        int [][] x2filled= ImageMat.holeFill_Horizontally(yfilled, r.width, r.height);
        return ImageMat.holeFill_Horizontally(x2filled, r.width, r.height);
    }
    
    
    /**
     *  Draw the snake
     *
     * @param  A          Description of the Parameter
     * @param  col        Description of the Parameter
     * @param  linewidth  Description of the Parameter
     */
    public void DrawSnake(ImageProcessor A, Color col, int linewidth) {
        int i;
        int x;
        int y;
        int xx;
        int yy;
        A.setColor(col);
        A.setLineWidth(linewidth);
        for (i = 0; i < NPT - 1; i++) {
            x = (int) (points[i].x);
            y = (int) (points[i].y);
            xx = (int) (points[i + 1].x);
            yy = (int) (points[i + 1].y);
            A.drawLine(x, y, xx, yy);
        }
        if (this.closed()) {
            x = (int) (points[NPT - 1].x);
            y = (int) (points[NPT - 1].y);
            xx = (int) (points[0].x);
            yy = (int) (points[0].y);
            A.drawLine(x, y, xx, yy);
        }
    }
    private static int[][] faceedgecoordinates =new int[2000][2000];
    public void getProbableFaceEdgeCoordinate()
    {
        
        int xx[] = new int[200000];
        int yy[] = new int[200000];
          for (int i = 0; i < NPT; i++) {
           xx[i] = (int)(points[i].x);
                yy[i] = (int)(points[i].y);
                if(xx[i] != 0 && yy[i] != 0)
                {
                    faceedgecoordinates[xx[i]][yy[i]] = 1;
                }
                else
                {
                    faceedgecoordinates[xx[i]][yy[i]] = 0;
                }
          
          }
              
    }
    /**
     *  creation of a polygon ROI
     *
     * @param  imp  image plus
     * @return      roi
     */
    PolygonRoi createRoi() {
        int xx[] = new int[NPT];
        int yy[] = new int[NPT];
        for (int i = 0; i < NPT; i++) {
            xx[i] = (int) (points[i].x);
            yy[i] = (int) (points[i].y);
        }
        PolygonRoi rr = new PolygonRoi(xx, yy, NPT - 1, Roi.FREEROI);
        return rr;
    }

    /**
     *  Initialization of the snake points
     *
     * @param  R  ROI
     */
    public void Init(Roi R) {
        Double pos;
        double Rx;
        double Ry;
        int i = 1;
        double a;
        NPT = 0;

        points = new Point2d[NMAX];
        normale = new Point2d[NMAX];
        deplace = new Point2d[NMAX];
        dataDistance = 0.0;
        etat = new int[NMAX];
        lambda = new double[NMAX];

        for (i = 0; i < NMAX; i++) {
            points[i] = new Point2d();
            normale[i] = new Point2d();
            deplace[i] = new Point2d();
        }


        //Calculate all the points in the ROI
        if (R.getType() == Roi.RECTANGLE) {
            closed = true;
            Rectangle Rect = R.getBounds();
            int xc = Rect.x + Rect.width / 2;
            int yc = Rect.y + Rect.height / 2;
            Rx = ((double) Rect.width) / 2;
            Ry = ((double) Rect.height) / 2;
            double theta = 4.0 / (Rx + Ry);
            i = 0;
            for (a = 2 * Math.PI; a > 0; a -= theta) {
                points[i].x = (int) (xc + Rx * Math.cos(a));
                points[i].y = (int) (yc + Ry * Math.sin(a));
                etat[i] = 0;
                i++;
            }
            NPT = i;
        }
        else {
          
        
        }
        block = 0;
        elimination = 0;
        ARRET = 0;
    }

    /**
     *  regularization of distance between points
     */
    void resample(boolean init) {
         Point2d temp[];
         Point2d Ta;
        int i;
        int j;
        int k;
        int ii;
        int aj;
        double Dmoy;
        double Dtot;
        double DD;
        double Dmin;
        double Dmax;
        double Di;
        double Dmoyg;
        double normtan;
        double D;
        double D1;

        temp = new  Point2d[NMAX];
        Ta = new  Point2d();

        Dtot = 0.0;
        Dmin = 1000.0;
        Dmax = 0.0;
        for (i = 1; i < NPT; i++) {
            Di = distance(i, i - 1);
            Dtot += Di;
            if (Di < Dmin) {
                Dmin = Di;
            }
            if (Di > Dmax) {
                Dmax = Di;
            }
        }
        if (((Dmax / Dmin) > 3.0) || (init)) {
            Dmoyg = 1.0;
            temp[0] = new  Point2d();
            temp[0].x = points[0].x;
            temp[0].y = points[0].y;
            i = 1;
            ii = 1;
            temp[ii] = new  Point2d();
            while (i < NPT) {
                Dmoy = Dmoyg;
                DD = distance(i, i - 1);
                if (DD > Dmoy) {
                    aj = (int) (DD / Dmoy);
                    Ta.x = points[i].x - points[i - 1].x;
                    Ta.y = points[i].y - points[i - 1].y;
                    normtan = Math.sqrt(Ta.x * Ta.x + Ta.y * Ta.y);
                    Ta.x /= normtan;
                    Ta.y /= normtan;
                    for (k = 1; k <= aj; k++) {
                        temp[ii].x = points[i - 1].x + k * Dmoy * Ta.x;
                        temp[ii].y = points[i - 1].y + k * Dmoy * Ta.y;
                        ii++;
                        temp[ii] = new  Point2d();
                    }
                }
                i++;
                if ((DD <= Dmoy) && (i < NPT - 1)) {
                    j = i - 1;
                    D = 0.0;
                    while ((D < Dmoy) && (j < NPT - 1)) {
                        D += distance(j, j + 1);
                        j++;
                    }
                    temp[ii].x = points[j].x;
                    temp[ii].y = points[j].y;
                    ii++;
                    temp[ii] = new  Point2d();
                    i = j + 1;
                }
                if (i == NPT - 1) {
                    i = NPT;
                }
            }
            temp[ii].x = points[NPT - 1].x;
            temp[ii].y = points[NPT - 1].y;
            NPT = ii + 1;
            for (i = 0; i < NPT; i++) {
                points[i].x = temp[i].x;
                points[i].y = temp[i].y;
            }
        }
    }

    /**
     *  main calculus function (matrix inversion)
     *
     * @param  deb  first row
     * @param  fin  last row
     */
    public void calculus(int deb, int fin) {
        int i;
        Point2d bi;
        Point2d temp;
        Point2d debtemp;
        double mi;
        double gi;
        double di;
        double omega;

        omega = 1.8;
        bi = new  Point2d();
        temp = new  Point2d();
        debtemp = new  Point2d();

        debtemp.x = points[deb].x;
        debtemp.y = points[deb].y;

        for (i = deb; i < fin; i++) {
            bi.x = points[i].x + deplace[i].x;
            bi.y = points[i].y + deplace[i].y;
            gi = -lambda[i];
            di = -lambda[i + 1];
            mi = lambda[i] + lambda[i + 1] + 1.0;
            if (i > deb) {
                temp.x = mi * points[i].x + omega * (-gi * points[i - 1].x - mi * points[i].x - di * points[i + 1].x + bi.x);
                temp.y = mi * points[i].y + omega * (-gi * points[i - 1].y - mi * points[i].y - di * points[i + 1].y + bi.y);
            }
            if ((i == deb) && (closed)) {
                temp.x = mi * points[i].x + omega * (-gi * points[fin].x - mi * points[i].x - di * points[i + 1].x + bi.x);
                temp.y = mi * points[i].y + omega * (-gi * points[fin].y - mi * points[i].y - di * points[i + 1].y + bi.y);
            }
            if ((i == deb) && (!closed)) {
                temp.x = points[deb].x * mi;
                temp.y = points[deb].y * mi;
            }
            points[i].x = temp.x / mi;
            points[i].y = temp.y / mi;
        }
        // LAST POINT
        if (closed) {
            i = fin;
            bi.x = points[i].x + deplace[i].x;
            bi.y = points[i].y + deplace[i].y;
            gi = -lambda[i];
            di = -lambda[deb];
            mi = lambda[i] + lambda[deb] + 1.0;
            temp.x = mi * points[i].x + omega * (-gi * points[i - 1].x - mi * points[i].x - di * debtemp.x + bi.x);
            temp.y = mi * points[i].y + omega * (-gi * points[i - 1].y - mi * points[i].y - di * debtemp.y + bi.y);
            points[i].x = temp.x / mi;
            points[i].y = temp.y / mi;
        }
    }
     /**
     *  Description of the Method
     *
     * @return    Description of the Return Value
     */
    public double compute_displacements() {
        double som = 0.0;
        double seuil = config.getGradThreshold();
        double DivForce = config.getMaxDisplacement();
         Point2d displ = new  Point2d();
        double force;
        som = 0;
        for (int i = 0; i < NPT; i++) {
            displ.x = 0.0;
            displ.y = 0.0;
            displ = compute_displ(i, seuil, 1000, 1000, 0);

            force = Math.sqrt(displ.x * displ.x + displ.y * displ.y);
            if (force > DivForce) {
                deplace[i].x = DivForce * (displ.x / force);
                deplace[i].y = DivForce * (displ.y / force);
            } else {
                deplace[i].x = displ.x;
                deplace[i].y = displ.y;
            }
            force = Math.sqrt(deplace[i].x * deplace[i].x + deplace[i].y * deplace[i].y);

            som += force;
        }
        return som;
    }

    /**
     *  Description of the Method
     *
     * @param  image  Description of the Parameter
     */
    public void computeGrad(ImageProcessor image) {
        gradImage = grad2d_deriche(image, config.getAlpha());
    }

    /**
     *  serach for the closest edge along the normale direction
     *
     * @param  num         number for the snake point
     * @param  Edge_Threshold       threshold
     * @param  directions  directions to look for
     * @return             the displacement vector towards the edges
     */
        Point2d compute_displ(int num, double Edge_Threshold, double dist_plus, double dist_minus, int dir) {
        double iy;
        double ix;
        double deplus;
        double demoins;
        double scaleint = config.getMaxSearch();
        double Dist;
        double crp = Double.NaN;
        double crm = Double.NaN;
        double bres;
        double ares;
        double bden;
        double bnum;
        double ii;
        Point2d displacement;
        Point2d pos;
        Point2d norm;
        int scale = 10;
        double image_line[] = new double[(int) (2 * scale * scaleint + 1)];

        pos = points[num];
        norm = normale[num];

        displacement = new Point2d();
        int index = 0;
        double step = 1.0 / (double) scale;
        double deb = -scaleint;
        for (ii = deb; ii < scaleint; ii += step) {
            iy = (pos.y + norm.y * ii);
            ix = (pos.x + norm.x * ii);
            if (ix < 0) {
                ix = 0;
            }
            if (iy < 0) {
                iy = 0;
            }
            if (ix >= gradImage.getWidth()) {
                ix = gradImage.getWidth() - 1;
            }
            if (iy >= gradImage.getHeight()) {
                iy = gradImage.getHeight() - 1;
            }
            image_line[index] = gradImage.getInterpolatedPixel(ix, iy);
            index++;
        }

        // polygon crossing, avoid self-intersecting snake
        for (int i = 0; i < NPT - 1; i++) {
            if ((i != num) && (i != num - 1)) {
                bden = (-norm.x * points[i + 1].y + norm.x * points[i].y + norm.y * points[i + 1].x - norm.y * points[i].x);
                bnum = (-norm.x * pos.y + norm.x * points[i].y + norm.y * pos.x - norm.y * points[i].x);
                if (bden != 0) {
                    bres = (bnum / bden);
                } else {
                    bres = 5.0;
                }
                if ((bres >= 0.0) && (bres <= 1.0)) {
                    ares = (float) (-(-points[i + 1].y * pos.x + points[i + 1].y * points[i].x + points[i].y * pos.x + pos.y * points[i + 1].x - pos.y * points[i].x - points[i].y * points[i + 1].x) / (-norm.x * points[i + 1].y + norm.x * points[i].y + norm.y * points[i + 1].x - norm.y * points[i].x));
                    if ((ares > 0.0) && (ares < crp)) {
                        crp = ares;
                    }
                    if ((ares < 0.0) && (ares > crm)) {
                        crm = ares;
                    }
                }
            }
        }
        double coeff_crossing = 0.9;
        crp = crp * coeff_crossing;
        crm = crm * coeff_crossing;

        deplus = Double.POSITIVE_INFINITY;
        demoins = Double.NEGATIVE_INFINITY;

        boolean edge_found = false;
        for (index = 1; index < 2 * scale * scaleint - 1; index++) {
            // check edge threshold
            // local maximum
            if ((image_line[index] >= Edge_Threshold) && (image_line[index] >= image_line[index - 1]) && (image_line[index] >= image_line[index + 1])) {
                Dist = index * step + deb;
                if ((Dist < 0) && (Dist > demoins)) {
                    demoins = Dist;
                    edge_found = true;
                }
                if ((Dist >= 0) && (Dist < deplus)) {
                    deplus = Dist;
                    edge_found = true;
                }
            }
        }
        etat[num] = 0;
        
        // no edge found
        if (!edge_found) {
            displacement.x = 0.0;
            displacement.y = 0.0;

            return displacement;
        }

        // check edges found against threshold distances plus and minus
        if (deplus > dist_plus) {
            deplus = 2 * scaleint;
        }
        if (demoins < -dist_minus) {
            demoins = -2 * scaleint;
        }
        if (Double.isInfinite(deplus) && Double.isInfinite(demoins)) {
            displacement.x = 0.0;
            displacement.y = 0.0;

            return displacement;
        }

        int direction;
        // go to closest edge
        if (-demoins < deplus) {
            displacement.x = norm.x * demoins;
            displacement.y = norm.y * demoins;
            direction = -1;
        } else {
            displacement.x = norm.x * deplus;
            displacement.y = norm.y * deplus;
            direction = 1;
        }

      
        return (displacement);
    }

    /**
     *  Description of the Method
     */
    public void compute_normales() {
        for (int i = 0; i < NPT; i++) {
            normale[i] = compute_normale(i);
        }
    }

    /**
     *  Description of the Method
     */
    public void compute_lambdas() {
        double force;
        double maxforce = 0.0;
        double minr = config.getRegMin();
        double maxr = config.getRegMax();

        for (int i = 0; i < NPT; i++) {
            force = Math.sqrt(deplace[i].x * deplace[i].x + deplace[i].y * deplace[i].y);
            if (force > maxforce) {
                maxforce = force;
            }
        }

        for (int i = 0; i < NPT; i++) {
            force = Math.sqrt(deplace[i].x * deplace[i].x + deplace[i].y * deplace[i].y);
            lambda[i] = maxr / (1.0 + ((maxr - minr) / minr) * (force / maxforce));
        }
    }

    /**
     *  compute normale
     *
     * @param  np  number for the snake point
     * @return     normal vector
     */
    Point2d compute_normale(int np) {
         Point2d norma;
         Point2d tan;
        double normtan;

        tan = new  Point2d();
        norma = new  Point2d();

        if (np == 0) {
            if (closed) {
                tan.x = points[1].x - points[NPT - 1].x;
                tan.y = points[1].y - points[NPT - 1].y;
            } else {
                tan.x = points[1].x - points[0].x;
                tan.y = points[1].y - points[0].y;
            }
        }
        if (np == NPT - 1) {
            if (closed) {
                tan.x = points[0].x - points[NPT - 2].x;
                tan.y = points[0].y - points[NPT - 2].y;
            } else {
                tan.x = points[NPT - 1].x - points[NPT - 2].x;
                tan.y = points[NPT - 1].y - points[NPT - 2].y;
            }
        }
        if ((np > 0) && (np < NPT - 1)) {
            tan.x = points[np + 1].x - points[np - 1].x;
            tan.y = points[np + 1].y - points[np - 1].y;
        }
        normtan = Math.sqrt(tan.x * tan.x + tan.y * tan.y);
        if (normtan > 0.0) {
            tan.x /= normtan;
            tan.y /= normtan;
            norma.x = -tan.y;
            norma.y = tan.x;
        }
        return (norma);
    }

    /**
     *  destruction
     */
    void destroysnake() {
         Point2d temp[];
         Point2d fo[];
        double lan[];
        int state[];
        int i;
        int j;

        temp = new  Point2d[NPT];
        fo = new  Point2d[NPT];
        lan = new double[NPT];
        state = new int[NPT];

        j = 0;
        for (i = 0; i < NPT; i++) {
            if (etat[i] != 1) {
                temp[j] = new  Point2d();
                temp[j].x = points[i].x;
                temp[j].y = points[i].y;
                state[j] = etat[i];
                fo[j] = new  Point2d();
                fo[j].x = deplace[i].x;
                fo[j].y = deplace[i].y;
                lan[j] = lambda[i];
                j++;
            }
        }
        NPT = j;

        for (i = 0; i < NPT; i++) {
            points[i].x = temp[i].x;
            points[i].y = temp[i].y;
            etat[i] = state[i];
            deplace[i].x = fo[i].x;
            deplace[i].y = fo[i].y;
            lambda[i] = lan[i];
        }
    }

    /**
     *  distance between two points of the snake
     *
     * @param  a  number of first point
     * @param  b  number of second point
     * @return    distance
     */
    double distance(int a, int b) {
        return (Math.sqrt(Math.pow(points[a].x - points[b].x, 2.0) + Math.pow(points[a].y - points[b].y, 2.0)));
    }

    /**
     *  compute new positions of the snake
     */
    void new_positions() {
        calculus(0, NPT - 1);
    }

    /**
     *  Deriche filtering
     *
     * @param  iDep    image
     * @param  alphaD  Description of the Parameter
     * @return         Description of the Return Value
     */
    private ImageProcessor grad2d_deriche(ImageProcessor iDep, double alphaD) {
        ImageProcessor iGrad = new ByteProcessor(iDep.getWidth(), iDep.getHeight());
        int nmem;
        float[] nf_grx = null;
        float[] nf_gry = null;
        float[] a1 = null;
        float[] a2 = null;
        float[] a3 = null;
        float[] a4 = null;
        byte[] result_array = null;

        int icolonnes = 0;
        int icoll = 0;
        int lignes;
        int colonnes;
        int lig_1;
        int lig_2;
        int lig_3;
        int col_1;
        int col_2;
        int col_3;
        int jp1;
        int jm1;
        int im1;
        int ip1;
        int icol_1;
        int icol_2;
        int i;
        int j;
        float ad1;
        float ad2;
        float wd;
        float gzr;
        float gun;
        float an1;
        float an2;
        float an3;
        float an4;
        float an11;

        lignes = iDep.getHeight();
        colonnes = iDep.getWidth();
        nmem = lignes * colonnes;

        lig_1 = lignes - 1;
        lig_2 = lignes - 2;
        lig_3 = lignes - 3;
        col_1 = colonnes - 1;
        col_2 = colonnes - 2;
        col_3 = colonnes - 3;

        /*
         *  alloc temporary buffers
         */
        nf_grx = new float[nmem];
        nf_gry = new float[nmem];

        a1 = new float[nmem];
        a2 = new float[nmem];
        a3 = new float[nmem];
        a4 = new float[nmem];

        ad1 = (float) -Math.exp(-alphaD);
        ad2 = 0;
        an1 = 1;
        an2 = 0;
        an3 = (float) Math.exp(-alphaD);
        an4 = 0;
        an11 = 1;

        /*
         *  FIRST STEP:  Y GRADIENT
         */
        /*
         *  x-smoothing
         */
        for (i = 0; i < lignes; i++) {
            for (j = 0; j < colonnes; j++) {
                a1[i * colonnes + j] = iDep.getPixelValue(j, i);
            }
        }

        for (i = 0; i < lignes; ++i) {
            icolonnes = i * colonnes;
            icol_1 = icolonnes - 1;
            icol_2 = icolonnes - 2;
            a2[icolonnes] = an1 * a1[icolonnes];
            a2[icolonnes + 1] = an1 * a1[icolonnes + 1]
                    + an2 * a1[icolonnes] - ad1 * a2[icolonnes];
            for (j = 2; j < colonnes; ++j) {
                a2[icolonnes + j] = an1 * a1[icolonnes + j] + an2 * a1[icol_1 + j]
                        - ad1 * a2[icol_1 + j] - ad2 * a2[icol_2 + j];
            }
        }

        for (i = 0; i < lignes; ++i) {
            icolonnes = i * colonnes;
            icol_1 = icolonnes + 1;
            icol_2 = icolonnes + 2;
            a3[icolonnes + col_1] = 0;
            a3[icolonnes + col_2] = an3 * a1[icolonnes + col_1];
            for (j = col_3; j >= 0; --j) {
                a3[icolonnes + j] = an3 * a1[icol_1 + j] + an4 * a1[icol_2 + j]
                        - ad1 * a3[icol_1 + j] - ad2 * a3[icol_2 + j];
            }
        }

        icol_1 = lignes * colonnes;

        for (i = 0; i < icol_1; ++i) {
            a2[i] += a3[i];
        }

        /*
         *  FIRST STEP Y-GRADIENT : y-derivative
         */
        /*
         *  columns top - downn
         */
        for (j = 0; j < colonnes; ++j) {
            a3[j] = 0;
            a3[colonnes + j] = an11 * a2[j] - ad1 * a3[j];
            for (i = 2; i < lignes; ++i) {
                a3[i * colonnes + j] = an11 * a2[(i - 1) * colonnes + j]
                        - ad1 * a3[(i - 1) * colonnes + j] - ad2 * a3[(i - 2) * colonnes + j];
            }
        }

        /*
         *  columns down top
         */
        for (j = 0; j < colonnes; ++j) {
            a4[lig_1 * colonnes + j] = 0;
            a4[(lig_2 * colonnes) + j] = -an11 * a2[lig_1 * colonnes + j]
                    - ad1 * a4[lig_1 * colonnes + j];
            for (i = lig_3; i >= 0; --i) {
                a4[i * colonnes + j] = -an11 * a2[(i + 1) * colonnes + j]
                        - ad1 * a4[(i + 1) * colonnes + j] - ad2 * a4[(i + 2) * colonnes + j];
            }
        }

        icol_1 = colonnes * lignes;
        for (i = 0; i < icol_1; ++i) {
            a3[i] += a4[i];
        }

        for (i = 0; i < lignes; ++i) {
            for (j = 0; j < colonnes; ++j) {
                nf_gry[i * colonnes + j] = a3[i * colonnes + j];
            }
        }

        /*
         *  SECOND STEP X-GRADIENT
         */
        for (i = 0; i < lignes; ++i) {
            for (j = 0; j < colonnes; ++j) {
                a1[i * colonnes + j] = (int) (iDep.getPixel(j, i));
            }
        }

        for (i = 0; i < lignes; ++i) {
            icolonnes = i * colonnes;
            icol_1 = icolonnes - 1;
            icol_2 = icolonnes - 2;
            a2[icolonnes] = 0;
            a2[icolonnes + 1] = an11 * a1[icolonnes];
            for (j = 2; j < colonnes; ++j) {
                a2[icolonnes + j] = an11 * a1[icol_1 + j]
                        - ad1 * a2[icol_1 + j] - ad2 * a2[icol_2 + j];
            }
        }

        for (i = 0; i < lignes; ++i) {
            icolonnes = i * colonnes;
            icol_1 = icolonnes + 1;
            icol_2 = icolonnes + 2;
            a3[icolonnes + col_1] = 0;
            a3[icolonnes + col_2] = -an11 * a1[icolonnes + col_1];
            for (j = col_3; j >= 0; --j) {
                a3[icolonnes + j] = -an11 * a1[icol_1 + j]
                        - ad1 * a3[icol_1 + j] - ad2 * a3[icol_2 + j];
            }
        }
        icol_1 = lignes * colonnes;
        for (i = 0; i < icol_1; ++i) {
            a2[i] += a3[i];
        }

        /*
         *  on the columns
         */
        /*
         *  columns top down
         */
        for (j = 0; j < colonnes; ++j) {
            a3[j] = an1 * a2[j];
            a3[colonnes + j] = an1 * a2[colonnes + j] + an2 * a2[j]
                    - ad1 * a3[j];
            for (i = 2; i < lignes; ++i) {
                a3[i * colonnes + j] = an1 * a2[i * colonnes + j] + an2 * a2[(i - 1) * colonnes + j]
                        - ad1 * a3[(i - 1) * colonnes + j] - ad2 * a3[(i - 2) * colonnes + j];
            }
        }

        /*
         *  columns down top
         */
        for (j = 0; j < colonnes; ++j) {
            a4[lig_1 * colonnes + j] = 0;
            a4[lig_2 * colonnes + j] = an3 * a2[lig_1 * colonnes + j] - ad1 * a4[lig_1 * colonnes + j];
            for (i = lig_3; i >= 0; --i) {
                a4[i * colonnes + j] = an3 * a2[(i + 1) * colonnes + j] + an4 * a2[(i + 2) * colonnes + j]
                        - ad1 * a4[(i + 1) * colonnes + j] - ad2 * a4[(i + 2) * colonnes + j];
            }
        }

        icol_1 = colonnes * lignes;
        for (i = 0; i < icol_1; ++i) {
            a3[i] += a4[i];
        }

        for (i = 0; i < lignes; i++) {
            for (j = 0; j < colonnes; j++) {
                nf_grx[i * colonnes + j] = a3[i * colonnes + j];
            }
        }

        /*
         *  SECOND STEP X-GRADIENT : the x-gradient is  done
         */
        /*
         *  THIRD STEP : NORM
         */
        /*
         *  computation of the magnitude
         */
        for (i = 0; i < lignes; i++) {
            for (j = 0; j < colonnes; j++) {
                a2[i * colonnes + j] = nf_gry[i * colonnes + j];
            }
        }
        icol_1 = colonnes * lignes;
        for (i = 0; i < icol_1; ++i) {
            a2[i] = (float) Math.sqrt((a2[i] * a2[i]) + (a3[i] * a3[i]));
        }
        /*
         *  THIRD STEP : the norm is done
         */
        result_array = new byte[nmem];

        double min = a2[0];
        double max = a2[0];
        for (i = 1; i < nmem; i++) {
            if (min > a2[i]) {
                min = a2[i];
            }
            if (max < a2[i]) {
                max = a2[i];
            }
        }

        //Normalisation de gradient de 0 a 255
        for (i = 0; i < nmem; ++i) {
            result_array[i] = (byte) (255 * (a2[i] / (max - min)));
        }

        iGrad.setPixels(result_array);

        return iGrad;
    }

    /**
     *  main function for the snake
     *
     * @return    Description of the Return Value
     */
    public double process() {
        int i;
        double force;
         Point2d displ = new  Point2d();
        double maxforce = 0.0;
        double som = 0.0;
        double seuil = config.getGradThreshold();
        double DivForce = config.getMaxDisplacement();
        double minr = config.getRegMin();
        double maxr = config.getRegMax();
        double alpha = config.getAlpha();

        // EXPERIMENTAL
        double dist_plus = Prefs.get("ABSnake_ThreshDistPos.double", 100);
        double dist_minus = Prefs.get("ABSnake_ThreshDistNeg.double", 100);

        for (i = 0; i < NPT; i++) {
            normale[i] = compute_normale(i);
        }
        block = 0;
        elimination = 0;
        for (i = 0; i < NPT; i++) {
            displ.x = 0.0;
            displ.y = 0.0;
            displ = compute_displ(i, seuil, dist_plus, dist_minus, -1);

            force = Math.sqrt(Math.pow(displ.x, 2.0) + Math.pow(displ.y, 2.0));
            if (force > DivForce) {
                deplace[i].x = DivForce * (displ.x / force);
                deplace[i].y = DivForce * (displ.y / force);
            } else {
                deplace[i].x = displ.x;
                deplace[i].y = displ.y;
            }
            force = Math.sqrt(deplace[i].x * deplace[i].x + deplace[i].y * deplace[i].y);
            if (force > maxforce) {
                maxforce = force;
            }
            som += force;
        }
        dataDistance = som / NPT;

        for (i = 0; i < NPT; i++) {
            force = Math.sqrt(Math.pow(deplace[i].x, 2.0) + Math.pow(deplace[i].y, 2.0));
            lambda[i] = maxr / (1.0 + ((maxr - minr) / minr) * (force / maxforce));
        }
        if (elimination == 1) {
            destroysnake();
        }

        new_positions();
        resample(false);

        return dataDistance;
    }

    /**
     *  SEGMENTATION : inside/outside the snake
     *
     * @param  wi  Description of the Parameter
     * @param  he  Description of the Parameter
     * @return     binarised image (black=object inside snake)
     */
    public ByteProcessor segmentation(int wi, int he, int col) {
        Point2d pos;
        Point2d norm;
        Point2d ref;
        int top;
        int left;
        int right;
        int bottom;
        int i;
        int j;
        int x;
        int y;
        int count;
        double bden;
        double bnum;
        double bres;
        double lnorm;
        double ares;

        pos = new  Point2d();
        norm = new  Point2d();
        ref = new  Point2d();

        ByteProcessor res = new ByteProcessor(wi, he);
        top = 0;
        bottom = 100000;
        left = 100000;
        right = 0;
        for (i = 0; i < NPT; i++) {
            if (points[i].y > top) {
                top = (int) points[i].y;
            }
            if (points[i].y < bottom) {
                bottom = (int) points[i].y;
            }
            if (points[i].x > right) {
                right = (int) points[i].x;
            }
            if (points[i].x < left) {
                left = (int) points[i].x;
            }
        }

        ref.x = 0;
        ref.y = 0;
        for (x = left; x < right; x++) {
            for (y = bottom; y < top; y++) {
                pos.x = x;
                pos.y = y;
                if (inside(pos)) {
                    res.putPixel(x, y, col);
                } else {
                    res.putPixel(x, y, 0);
                }
            }
        }
        return res;
    }

    /**
     *  the point is inside the snake ?
     *
     * @param  pos  the point
     * @return      inside ?
     */
    boolean inside( Point2d pos) {
        int count;
        int i;
        double bden;
        double bnum;
        double bres;
        double ares;
        double lnorm;
        Point2d norm = new  Point2d();
        Point2d ref = new  Point2d();

        ref.x = 0.0;
        ref.y = 0.0;
        norm.x = ref.x - pos.x;
        norm.y = ref.y - pos.y;
        lnorm = Math.sqrt(norm.x * norm.x + norm.y * norm.y);
        norm.x /= lnorm;
        norm.y /= lnorm;

        count = 0;
        for (i = 0; i < NPT - 1; i++) {
            bden = (-norm.x * points[i + 1].y + norm.x * points[i].y + norm.y * points[i + 1].x - norm.y * points[i].x);
            bnum = (-norm.x * pos.y + norm.x * points[i].y + norm.y * pos.x - norm.y * points[i].x);
            if (bden != 0) {
                bres = (bnum / bden);
            } else {
                bres = 5.0;
            }
            if ((bres >= 0.0) && (bres <= 1.0)) {
                ares = -(-points[i + 1].y * pos.x + points[i + 1].y * points[i].x
                        + points[i].y * pos.x + pos.y * points[i + 1].x - pos.y * points[i].x
                        - points[i].y * points[i + 1].x) / (-norm.x * points[i + 1].y
                        + norm.x * points[i].y + norm.y * points[i + 1].x - norm.y * points[i].x);
                if ((ares >= 0.0) && (ares <= lnorm)) {
                    count++;
                }
            }
        }
        // last point
        i = NPT - 1;
        bden = (-norm.x * points[0].y + norm.x * points[i].y + norm.y * points[0].x - norm.y * points[i].x);
        bnum = (-norm.x * pos.y + norm.x * points[i].y + norm.y * pos.x - norm.y * points[i].x);
        if (bden != 0) {
            bres = (bnum / bden);
        } else {
            bres = 5.0;
        }
        if ((bres >= 0.0) && (bres <= 1.0)) {
            ares = -(-points[0].y * pos.x + points[0].y * points[i].x
                    + points[i].y * pos.x + pos.y * points[0].x - pos.y * points[i].x
                    - points[i].y * points[0].x) / (-norm.x * points[0].y
                    + norm.x * points[i].y + norm.y * points[0].x - norm.y * points[i].x);
            if ((ares >= 0.0) && (ares <= lnorm)) {
                count++;
            }
        }
        return (count % 2 == 1);
    }
    
}
