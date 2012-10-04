/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.algorithms;

import frs.helpers.Correlation;
import frs.helpers.DeepCopier;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rajan Prasad Upadhyay
 */
public class Blender2 {
    /*
     * option parameters
     */

    public Blender2() {
    }
    private int windowWidth = 1;
    private int blendLimit = 5;
    private int flag = 0;
    private int algorithm = 1;
    private int averagingAlgoritm = 3;
    /*
     * Required Datas
     */
    BufferedImage faceReplacedImage;
    BufferedImage faceOnly;
    Point shiftVector;
    List<Point> boundary;//list of boundary points shifted
    //the final image to be returend
    BufferedImage result;
    Raster r;//raster of faceReplacedImage
    WritableRaster wr;//raster of result

    /*
     * Essential functions to get result
     */
    public BufferedImage processAndGetResult() {
        findBoundary();
        intermediateProcess();
        return result;
    }

    /*
     * setters
     */
    public void setWindowWidth(int width) {
        /*
         * width=1 corresponds to N8 neighbourhood
         */
        windowWidth = width;
    }

    public void setBlendLimit(int limit) {
        /**
         * How many points above and below the boundary point the window
         * function should be applied
         */
        blendLimit = limit;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    private void setAlgorithm(int a) {
        algorithm = a;
    }

    public void setAveragingAlgorithm(int a) {
        this.averagingAlgoritm = a;
    }

    public void setFaceReplacedImage(BufferedImage finalImage) {
        faceReplacedImage = DeepCopier.getBufferedImage(finalImage, BufferedImage.TYPE_INT_RGB);
    }

    public void setFaceOnly(BufferedImage faceOnly) {
        this.faceOnly = faceOnly;
    }

    public void setShiftVector(Point shiftVector) {
        this.shiftVector = shiftVector;
    }

    //helper functions
    private void findBoundary() {
        boundary = extractBoundaryVerticalHorizontal(faceOnly);
        int length = boundary.size();
        result = DeepCopier.getBufferedImage(faceReplacedImage, BufferedImage.TYPE_INT_RGB);
        Point p;
        //shift the points from local to global coordinates
        for (int i = 0; i < boundary.size(); i++) {
            p = boundary.get(i);
            p.x += shiftVector.x;
            p.y += shiftVector.y;
        }
    }

    private void intermediateProcess() {
        r = faceReplacedImage.getRaster();
        wr = result.getRaster();
        if (algorithm == 1) {
            Algorithm1();
        } else {
            Algorithm2();
        }

    }

    private void Algorithm1() {
        int length = boundary.size();
        Point p = null;
        for (int i = 0; i < length; i++) {//i=number of points in the boundary
            p = boundary.get(i);
            if (p != null && p.x > 5) {
                for (int j = -blendLimit; j < blendLimit; j++) {//
                    for (int k = -blendLimit; k < blendLimit; k++) {
                        if (false) {
                            try {
                                BlendPixel4Log(p.x, p.y);
                                BlendPixel4Log(p.x, p.y);
                            } catch (Exception e) {
                                continue;
                            }
                        } else {
                            try {
                                BlendPixel3(p.x + j, p.y);
                                BlendPixel3(p.x, p.y + k);
                            } catch (Exception e) {
                                continue;
                            }
                        }
                    }
                }

            }
        }
    }

    //dont call this algorithm2
    private void Algorithm2() {
        //BufferedImage a=deepCopy(replacedImage);
        /*
         * tanh 1/5:0.1974, 2/5:0.3799, 3/5:0.5370 blend with these weightage
         */
        int length = boundary.size();
        int mat[][] = new int[3][3];
        int verticalScore = 0;
        int horizontalScore = 0;
        Correlation cor = new Correlation();
        int[] left = new int[3];
        int[] right = new int[3];
        int[] up = new int[3];
        int[] down = new int[3];

        /*
         * for each point in boundary generate N8() correlate with direction
         * finder check which score is greater interpolate in the other
         * direction
         */
        Point p;
        int[] m = new int[255];
        for (int len = 0; len < length; len++) {
            p = boundary.get(len);
            mat = getMatrix(r, p);
            cor.setMat1(mat);
            cor.setMat2(Correlation.horizontalLineDetector);
            horizontalScore = cor.getScore();
            cor.setMat2(Correlation.verticalLineDetector);
            verticalScore = cor.getScore();
            try {
                if (verticalScore > horizontalScore) {
                    //interpolate in horizontal direction
                    left = r.getPixel(p.x - 1, p.y, m);
                    right = r.getPixel(p.x + 1, p.y, m);
                    m[0] = (int) (left[0] * 0.1974 + right[0] * (1 - 0.1974));
                    m[1] = (int) (left[1] * 0.1974 + right[1] * (1 - 0.1974));
                    m[2] = (int) (left[2] * 0.1974 + right[2] * (1 - 0.1974));
                    wr.setPixel(p.x, p.y, m);
                } else {
                    //interpolate in vertical direction
                    down = r.getPixel(p.x, p.y - 1, m);
                    up = r.getPixel(p.x, p.y + 1, m);
                    m[0] = (int) (down[0] * 0.1974 + up[0] * (1 - 0.1974));
                    m[1] = (int) (down[1] * 0.1974 + up[1] * (1 - 0.1974));
                    m[2] = (int) (down[2] * 0.1974 + up[2] * (1 - 0.1974));
                    wr.setPixel(p.x, p.y, m);
                }
            } catch (Exception e) {
                System.out.println(" Teh down error " + (p.x) + "," + (p.y));
            }
        }

    }

    private List<Point> extractBoundaryVerticalHorizontal(BufferedImage warpedImage) {
        int width = warpedImage.getWidth();
        int height = warpedImage.getHeight();
        int[][] mat = new int[warpedImage.getWidth()][warpedImage.getHeight()];
        //Color col=new Color(255,255,255,0);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color c = new Color(warpedImage.getRGB(i, j), true);
                if (c.getAlpha() < 100) {
                    mat[i][j] = 0;
                } else {
                    mat[i][j] = 1;
//                    warpedImage.setRGB(i, j,col.getRGB());
                }
            }
        }
        //binary matrix is ready

        int[][] boundaryHo = findBoundaryHorizontalFromMatrix(mat, width, height);
        int[][] boundaryVer = findBoundaryVerticalFromMatrix(mat, width, height);
        List<Point> bou = new ArrayList();

        {
            //horizontalPoints
            int x = 0, y = 0;
            for (int j = 0; j < height - 1; j++) {
                for (int i = 0; i < 2; i++) {
                    try {
                        x = boundaryHo[i][j];
                        y = j;
                        bou.add(new Point(x, y));
                    } catch (Exception e) {
                        System.out.println("The error Blending is " + "(" + x + "," + y + ")");
                    }
                }
            }
            //verticalPoints
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < width - 1; j++) {//int i=0;i<2;i++
                    try {
                        x = j;//boundaryHo[j][i]
                        y = boundaryVer[j][i];
                        if (x > 0 && y > 0) {
                            bou.add(new Point(x, y));
                        }
                    } catch (Exception e) {
                        System.out.println("The error Blending is " + "(" + x + "," + y + ")");
                    }
                }
            }
        }
        return bou;
    }

    private int[][] findBoundaryHorizontalFromMatrix(int[][] binaryMatrix, int w, int h) {
        //        int width=w-1;
        //        int height=h-1;
        int[][] boundary = new int[2][h];
        System.out.println("the width is " + w + " the height is " + h);
        if (binaryMatrix == null) {
            System.out.println("the binary matrix passed to findBoundary is null");

        } else {
            for (int y = 0; y < h; y++) {
                boundary[0][y] = -1;
                boundary[1][y] = -1;
                int xMin = 0, xMax = w - 1;
                for (xMin = 0; xMin < w; xMin++) {
                    //boundary[0][x] = yMin;
                    if (binaryMatrix[xMin][y] > 0 && xMin < w / 4) {
                        boundary[0][y] = xMin;
                        break;
                    }
                }

                for (xMax = w - 1; xMax >= 0; xMax--) {
                    //boundary[1][x] = yMax;
                    if (binaryMatrix[xMax][y] > 0 && xMax > w / 2) {
                        boundary[1][y] = xMax;
                        break;
                    }
                }
                //System.out.println("yMin = "+yMin+" yMax = "+yMax);
            }
        }


        return boundary;
    }

    private int[][] findBoundaryVerticalFromMatrix(int[][] binaryMatrix, int w, int h) {
//        int width=w-1;
//        int height=h-1;
        int[][] boundary = new int[w][2];
        //System.out.println("the width is "+w+" the height is "+h);
        if (binaryMatrix == null) {
            System.out.println("the binary matrix passed to findBoundary is null");

        } else {
            for (int x = 0; x < w; x++) {
                boundary[x][0] = -1;
                boundary[x][1] = -1;
                int yMin = 0, yMax = h - 1;
                for (yMin = 0; yMin < h; yMin++) {
                    //boundary[0][x] = yMin;
                    if (binaryMatrix[x][yMin] > 0) {
                        boundary[x][0] = yMin;
                        break;
                    }
                }

                for (yMax = h - 1; yMax >= 0; yMax--) {
                    //boundary[1][x] = yMax;
                    if (binaryMatrix[x][yMax] > 0) {
                        boundary[x][1] = yMax;
                        break;
                    }
                }
                //System.out.println("yMin = "+yMin+" yMax = "+yMax);
            }
        }


        return boundary;
    }

    private void BlendPixel4Log(int x, int y) {
        //logh 1=.7816, 1-logh 1=0.2384
        int[] abc = new int[4];
        abc[0] = 255;
        abc[1] = 255;
        abc[2] = 255;
        abc[3] = 255;
        if (r == null) {
            return;
        }
        int[] left = r.getPixel(x - 1, y, abc);
        int[] right = r.getPixel(x + 1, y, abc);
        int[] up = r.getPixel(x, y - 1, abc);
        int[] down = r.getPixel(x, y + 1, abc);
        abc[0] = (int) (0.7816 * (left[0] + down[0]) + 0.2384 * (right[0] + up[0]));
        abc[1] = (int) (0.7816 * (left[1] + down[1]) + 0.2384 * (right[1] + up[1]));
        abc[2] = (int) (0.7816 * (left[2] + down[2]) + 0.2384 * (right[2] + up[2]));
        wr.setPixel(x, y, abc);
    }

    private void BlendPixel3(int x, int y) {
        //blends the pixel according to 10 connectivity
        int[] avg = new int[4];
        avg[0] = 255;
        avg[1] = 255;
        avg[2] = 255;
        avg[3] = 255;
        int neighbourSize = 3;//Note neighbourSize =1 implies eightconnectivity
        List<int[]> area = new ArrayList();
        for (int i = -neighbourSize; i <= neighbourSize; i++) {
            for (int j = -neighbourSize; j <= neighbourSize; j++) {
                area.add(r.getPixel(x + i, y + j, avg));
            }
        }
        if (averagingAlgoritm == 1) {
            wr.setPixel(x, y, getAverage1(area));
        } else if (averagingAlgoritm == 2) {
            wr.setPixel(x, y, getAverage2(area));
        } else {
            wr.setPixel(x, y, getAverage3(area));
        }
    }
    //un weighted average

    private int[] getAverage1(List<int[]> pixelrgbValues) {
        //the int[] is pixel value in rgb format
        //System.out.println("The weighted average is taking place;");
        int[] ans = new int[3];
        ans[0] = 0;
        ans[1] = 0;
        ans[2] = 0;

        int length = pixelrgbValues.size();
        int count = 1;
        int[] pointer = null;
        for (int i = 0; i < length; i++) {
            pointer = pixelrgbValues.get(i);
            if (pointer[0] - 100 < pointer[1]) {//to reject the totally red pixels
                ans[0] += pointer[0];
                ans[1] += pointer[1];
                ans[2] += pointer[2];
                count++;
            }
        }
        ans[0] /= (count);
        ans[1] /= (count);
        ans[2] /= (count);
        return ans;
    }
    //weighted average

    private int[] getAverage2(List<int[]> pixelrgbValues) {
        //the int[] is pixel value in rgb format
        //System.out.println("The weighted average is taking place;");
        int[] ans = new int[3];
        ans[0] = 0;
        ans[1] = 0;
        ans[2] = 0;
        int[] weight1 = {1, 2, 4, 8, 4, 2, 1,
            2, 4, 8, 16, 8, 4, 2,
            4, 8, 16, 32, 16, 8, 4,
            8, 16, 32, 64, 32, 16, 8,
            4, 8, 16, 32, 16, 8, 4,
            2, 4, 8, 16, 8, 4, 2,
            1, 2, 4, 8, 4, 2, 1};
        
        int totalWeightSum = 0;
        for (int i = 0; i < weight1.length; i++) {
            totalWeightSum += weight1[i];
        }
        int length = pixelrgbValues.size();
        //int count = 1;
        int[] pointer = null;
        for (int i = 0; i < length; i++) {
            pointer = pixelrgbValues.get(i);
            if (pointer[0] - 100 < pointer[1]) {//to reject the totally red pixels
                ans[0] += pointer[0] * weight1[i];
                ans[1] += pointer[1] * weight1[i];
                ans[2] += pointer[2] * weight1[i];
                //count++;
            }
        }
        ans[0] /= (totalWeightSum);
        ans[1] /= (totalWeightSum);
        ans[2] /= (totalWeightSum);
        return ans;
    }
    //directinal weighted average

    private int[] getAverage3(List<int[]> pixelrgbValues) {
        //the int[] is pixel value in rgb format
        //System.out.println("The weighted average is taking place;");
        int[] ans = new int[3];
        ans[0] = 0;
        ans[1] = 0;
        ans[2] = 0;
        int[] weight2 = {1, 2, 3, 4, 5, 6, 7,
            2, 3, 4, 5, 6, 7, 8,
            3, 4, 5, 6, 7, 8, 9,
            4, 5, 6, 7, 8, 9, 10,
            5, 6, 7, 8, 9, 10, 11,
            6, 7, 8, 9, 10, 11, 12,
            7, 8, 9, 10, 11, 12, 13};
        int totalWeightSum = 1;
        for (int i = 0; i < weight2.length; i++) {
            totalWeightSum += weight2[i];
        }
        int length = pixelrgbValues.size();
        //int count = 1;
        int[] pointer = null;
        for (int i = 0; i < length; i++) {
            pointer = pixelrgbValues.get(i);
            if (pointer[0] - 100 < pointer[1]) {//to reject the totally red pixels
                ans[0] += pointer[0] * weight2[i] * 5;
                ans[1] += pointer[1] * weight2[i] * 5;
                ans[2] += pointer[2] * weight2[i] * 5;
                //count++;
            }
        }
        ans[0] /= (totalWeightSum * 5);
        ans[1] /= (totalWeightSum * 5);
        ans[2] /= (totalWeightSum * 5);
        return ans;
    }
    //for algorithm2

    private static int[][] getMatrix(Raster r, Point p) {
        int[][] a = new int[3][3];
        int[] b = new int[4];
        b[0] = 255;
        b[1] = 255;
        b[2] = 255;
        int[] c;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                //try
                {
                    //System.out.println(""+(p.x+i)+", "+(p.y+j));
                    c = r.getPixel(p.x + i, p.y + j, b);
                    a[i + 1][j + 1] = (c[0] + c[1] + c[2]) / 3;
                }
//                catch(Exception e){
//                    System.out.println(""+(p.x+i)+", "+(p.y+j));
//                }
            }
        }
        return a;
    }
}
