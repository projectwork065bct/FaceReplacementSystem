
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.engine;

import frs.algorithms.*;
import frs.curve.BestSideCurves;
import frs.curve.Curve;
import frs.dataTypes.FeaturePoint;
import frs.gui.pages.P80_Replace;
import frs.helpers.*;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Dell
 */
public class FRSEngine extends FRSData {

    // <editor-fold defaultstate="collapsed" desc="Rotate source image according to eye axis">
    //Rotate the source around left eye by the angle between left eye and right eye
    //It rotates the image and stores the result in the rotatedSrcImg
    public void rotateSource() {
        int delY = srcFP[FeaturePoint.RIGHT_EYE].y - srcFP[FeaturePoint.LEFT_EYE].y;
        int delX = srcFP[FeaturePoint.RIGHT_EYE].x - srcFP[FeaturePoint.LEFT_EYE].x;
        float theta = (float) -Math.atan2(delY, delX);
        BufferedImage srcImg = DeepCopier.getBufferedImage(this.srcImg, BufferedImage.TYPE_INT_ARGB);
        AffineTransform transformImg = new AffineTransform();
        transformImg.rotate(theta, srcFP[FeaturePoint.LEFT_EYE].x, srcFP[FeaturePoint.LEFT_EYE].y);
        AffineTransformOp op = new AffineTransformOp(transformImg, AffineTransformOp.TYPE_BILINEAR);
        rotatedSrcImg = op.filter(srcImg, null);
        //Rotate all the feature points
        rotatedSrcFP = new Point[srcFP.length];
        Point leftEye = srcFP[FeaturePoint.LEFT_EYE];
        for (int i = 0; i < srcFP.length; i++) {
            rotatedSrcFP[i] = GeometricTransformation.rotatePoint(srcFP[i], theta, leftEye);
        }
    }

    public void findSourceRectImage() {
        srcRectImg = rotatedSrcImg.getSubimage(srcFaceRect.x, srcFaceRect.y, srcFaceRect.width, srcFaceRect.height);
    }
    //Find the feature points with respect to the source rectangle

    public void findSourceRectFP() {
        srcRectFP = new Point[srcFP.length];
        //Translate the feature points to coordinate system of rectangle
        for (int i = 0; i < srcRectFP.length; i++) {
            srcRectFP[i] = new Point();
            srcRectFP[i].x = rotatedSrcFP[i].x - srcFaceRect.x;
            srcRectFP[i].y = rotatedSrcFP[i].y - srcFaceRect.y;
        }
    }

    public void findTargetRectImage() {
        tarRectImg = tarImg.getSubimage(tarFaceRect.x, tarFaceRect.y, tarFaceRect.width, tarFaceRect.height);
    }

    //Find the feature points of the target
    public void findTargetRectFP() {
        tarRectFP = new Point[tarFP.length];
        for (int i = 0; i < tarRectFP.length; i++) {
            tarRectFP[i] = new Point();
            tarRectFP[i].x = tarFP[i].x - tarFaceRect.x;
            tarRectFP[i].y = tarFP[i].y - tarFaceRect.y;
        }
    }
//It draws the feature points on the source rectangle image (rectangle image which is obtained after rotation about left eye through angle between two eyes)

    public void drawFPOnSrcRectImage() {
        srcRectImgWithFP = DeepCopier.getBufferedImage(srcRectImg, srcRectImg.getType());
        for (int i = 0; i < srcRectFP.length; i++) {
            srcRectImgWithFP.getGraphics().fillOval(srcRectFP[i].x - 2, srcRectFP[i].y - 2, 5, 5);
        }
    }

//It draws feature points on rotated image of source
    public void drawFPOnRotatedImage() {
        for (int i = 0; i < rotatedSrcFP.length; i++) {
            rotatedSrcImg.getGraphics().fillOval(rotatedSrcFP[i].x - 5, rotatedSrcFP[i].y - 5, 10, 10);
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Detect the skin in source and target">
    //Detect the skin region of the source
    //One method is to apply threshold values of YCbCr Color
    public void detectSourceSkin() {
        SkinColorDetector sourceSkinDetector = new SkinColorDetectorUsingThreshold(srcRectImg);
        sourceSkinDetector.detectSkin();
        srcSkinMatrix = sourceSkinDetector.getSkinMatrix();//size =FaceRectangle
        //findSourceCurves();
        //filterSourceCurve();
        grownMatrix = DeepCopier.get2DMat(srcSkinMatrix, srcFaceRect.width, srcFaceRect.height);
        srcSkinImg = sourceSkinDetector.getSkinImage();
    }

    public void shrinkSource() {
        int shrinkCount = 5;
        int w = srcFaceRect.width;
        int h = srcFaceRect.height;
        shrunkMatrix = ImageMat.shrinkMatrix(srcSkinMatrix, w, h, shrinkCount);
        shrunkSrcImage = MatrixAndImage.matrixToImage(srcRectImg, shrunkMatrix);
    }

    public void growSource() {
        int shrinkCount = 5;
        int w = srcFaceRect.width;
        int h = srcFaceRect.height;
        grownMatrix = ImageMat.growMatrix(shrunkMatrix, w, h, shrinkCount);
        grownSrcImage = MatrixAndImage.matrixToImage(srcRectImg, grownMatrix);
    }

    public void detectTargetSkin() {
        SkinColorDetector targetSkinDetector = new SkinColorDetectorUsingThreshold(tarRectImg);
        targetSkinDetector.detectSkin();
        tarSkinMatrix = targetSkinDetector.getSkinMatrix();
        tarSkinImg = targetSkinDetector.getSkinImage();
    }

    public void detectSkin() {
        detectSourceSkin();
        detectTargetSkin();
    }

    //Another method of extracting the face is Snake Algorithm
    public void applySnakeToSrc(int threshold, int iteration) {
        try {
            ImageIO.write(srcImg, "jpg", new File(TempImagePath));
        } catch (IOException ex) {
            Logger.getLogger(FRSEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        ImagePlus imageplus = new ImagePlus(TempImagePath);
        imageplus.setRoi(srcFaceRect.x, srcFaceRect.y, srcFaceRect.width, srcFaceRect.height);
        SnakeInitializer a = new SnakeInitializer();
        a.setIte(iteration);
        a.setThreholdOfEdge(threshold);
        a.set(imageplus);
        try {
            a.run(imageplus.getProcessor());
        } catch (IOException ex) {
            Logger.getLogger(FRSEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        grownMatrix = SnakeClass.getFaceEdgecoordinates2(srcFaceRect);
        //sourceBoundaryFilledFaceMatrix = SnakeClass.getFaceEdgecoordinates2(srcFaceRect);
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Find chin curves in the source">
    public void findSourceCurves() {
        Curve leftCurve = new Curve();
        Point[] leftCurvePoints = {srcRectFP[FeaturePoint.CHIN], srcRectFP[FeaturePoint.LEFT_CHIN],
            srcRectFP[FeaturePoint.LEFT_CHEEK]};
        //setting the example points, at least 3 required
        leftCurve.setPoints(leftCurvePoints);
        Point[] leftCurveEnds = {srcRectFP[FeaturePoint.LEFT_CHEEK], srcRectFP[FeaturePoint.CHIN]};
        srcRectLeftEdge = leftCurve.getPoints(leftCurveEnds);//getting the fitted Points

        Curve rightCurve = new Curve();
        Point[] rightCurvePoints = {srcRectFP[FeaturePoint.CHIN], srcRectFP[FeaturePoint.RIGHT_CHIN],
            srcRectFP[FeaturePoint.RIGHT_CHEEK]};
        rightCurve.setPoints(rightCurvePoints);
        Point[] rightCurveEnds = {srcRectFP[FeaturePoint.CHIN], srcRectFP[FeaturePoint.RIGHT_CHEEK]};
        srcRectRightEdge = rightCurve.getPoints(rightCurveEnds);
        //findSrcSideCurves();
    }

    public void findSrcSideCurves() {
        BestSideCurves bsc = new BestSideCurves(srcRectImg, srcRectFP);
        srcRightCurve = bsc.getRightCurve();
        srcLeftCurve = bsc.getLeftCurve();

    }

    //Draw the chin curve in the source image
    public void drawSourceCurves() {
        findSourceCurves();
        List<Point> sourceLeftEdge = new ArrayList(srcRectLeftEdge.size());
        List<Point> sourceRightEdge = new ArrayList(srcRectRightEdge.size());
        srcImgWithCurve = DeepCopier.getBufferedImage(rotatedSrcImg, rotatedSrcImg.getType());
        for (int i = 0; i < srcRectLeftEdge.size(); i++) {
            sourceLeftEdge.add(new Point(srcRectLeftEdge.get(i).x + srcFaceRect.x, srcRectLeftEdge.get(i).y + srcFaceRect.y));
            srcImgWithCurve.setRGB(sourceLeftEdge.get(i).x, sourceLeftEdge.get(i).y, Color.RED.getRGB());
        }
        //sourceImageWithCurve.getGraphics().fillOval(sourceLeftEdge.get(0).x-2, sourceLeftEdge.get(0).y-2, 5,5);

        for (int i = 0; i < srcRectRightEdge.size(); i++) {
            sourceRightEdge.add(new Point(srcRectRightEdge.get(i).x + srcFaceRect.x, srcRectRightEdge.get(i).y + srcFaceRect.y));
            srcImgWithCurve.setRGB(sourceRightEdge.get(i).x, sourceRightEdge.get(i).y, Color.RED.getRGB());

        }
        //sourceImageWithCurve.getGraphics().fillOval(sourceRightEdge.get(sourceRightEdge.size()-1).x-2, sourceRightEdge.get(sourceRightEdge.size()-1).y-2, 5,5);
    }

    public void useSrcCurves() {
        srcMatrixWithChinCurve = new int[srcFaceRect.width][srcFaceRect.height];
        for (int x = 0; x < srcRectImg.getWidth(); x++) {
            srcMatrixWithChinCurve[x] = new int[srcRectImg.getHeight()];
            for (int y = 0; y < srcRectImg.getHeight(); y++) {
                srcMatrixWithChinCurve[x][y] = grownMatrix[x][y];
            }
        }
        int xMin = srcRectLeftEdge.get(0).x;
        int xMax = srcRectRightEdge.get(0).x;
        int yMin = srcRectLeftEdge.get(0).y;
        if (srcRectRightEdge.get(0).y < yMin) {
            yMin = srcRectRightEdge.get(0).y;
        }

        for (int x = 0; x < srcRectImg.getWidth(); x++) {
            for (int y = yMin; y < srcRectImg.getHeight(); y++) {
                srcMatrixWithChinCurve[x][y] = 0;
            }
        }

        //Set the curve points to 1
        Point lcp, rcp;
        for (int i = 0; i < srcRectLeftEdge.size(); i++) {
            lcp = srcRectLeftEdge.get(i);
            try {
                srcMatrixWithChinCurve[lcp.x][lcp.y] = 1;
            } catch (Exception e) {
                continue;
            }
        }
        for (int i = 0; i < srcRectRightEdge.size(); i++) {
            rcp = srcRectRightEdge.get(i);
            try {
                srcMatrixWithChinCurve[rcp.x][rcp.y] = 1;
            } catch (Exception e) {
                continue;
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Extract the boundary around the face">
    private String TempImagePath = "a.jpg";

    public void findSourceBoundaryFilledMatrix() {
        int w = srcFaceRect.width;
        int h = srcFaceRect.height;
        int[][] erectMatrix;//=ImageMat.invertMatrix(finerMatrix, h, w);
        MatFiller matFiller = new MatFiller(srcMatrixWithChinCurve, w, h);
        //sourceBoundaryFilledFaceMatrix = matFiller.getMatrix();
        erectMatrix = ImageMat.boundaryFilledTwoWay(srcMatrixWithChinCurve, w, h);
        sourceBoundaryFilledFaceMatrix = erectMatrix;//ImageMat.holeFillAccordingToBoundary(erectMatrix, w, h);
    }

    public void removeSrcHairFromFace() {
        int[][] srcHairRectMat = MatrixOperations.getSubMatrix(srcFaceRect, sourceHairMatrix, srcImg.getWidth(), srcImg.getHeight());
        sourceBoundaryFilledFaceMatrix = MatrixOperations.subtract(sourceBoundaryFilledFaceMatrix, srcHairRectMat, srcFaceRect.width, srcFaceRect.height);
    }

    public void removeTarHairFromFace() {
        int[][] tarHairRectMat = MatrixOperations.getSubMatrix(tarFaceRect, targetHairMatrix, tarImg.getWidth(), tarImg.getHeight());
        tarSkinMatrix = MatrixOperations.subtract(tarSkinMatrix, tarHairRectMat, tarFaceRect.width, tarFaceRect.height);
    }
    //Construct an image from sourceBoundaryFilledFaceMatrix

    public void findSourceBoundaryFilledImage() {
        sourceBoundaryFilledImage = DeepCopier.getBufferedImage(srcRectImg, BufferedImage.TYPE_INT_ARGB);
        Color actualColor;
        Color transparentColor;
        for (int x = 0; x < sourceBoundaryFilledImage.getWidth(); x++) {
            for (int y = 0; y < sourceBoundaryFilledImage.getHeight(); y++) {
                if (sourceBoundaryFilledFaceMatrix[x][y] == 0) {
                    actualColor = new Color(sourceBoundaryFilledImage.getRGB(x, y));
                    transparentColor = ColorModelConverter.getTransparentColor(actualColor);
                    sourceBoundaryFilledImage.setRGB(x, y, transparentColor.getRGB());
                }
            }
        }
 if(flagForCurve){
//        for (int i = 0; i < srcRightCurve.size(); i++) {
//            Point p = srcRightCurve.get(i);
//            try {
//                sourceBoundaryFilledImage.setRGB(p.x, p.y, new Color(255, 0, 0).getRGB());
//            } catch (Exception e) {
//                continue;
//            }
//        }
//
//        for (int i = 0; i < srcLeftCurve.size(); i++) {
//            Point p = srcLeftCurve.get(i);
//            try {
//                sourceBoundaryFilledImage.setRGB(p.x, p.y, new Color(255, 0, 0).getRGB());
//            } catch (Exception e) {
//                continue;
//            }
//        }
 }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Warp">
    public void warp(int originIndex) {
//        Point[] sfp = new Point[this.srcFP.length];
//        Point[] tfp = new Point[this.tarFP.length];
//        //Translate the feature points so that it is relative to the face rectangle
//        for (int i = 0; i < this.srcFP.length; i++) {
//            sfp[i] = new Point(this.srcFP[i].x - this.srcFaceRect.x, this.srcFP[i].y - this.srcFaceRect.y);
//            tfp[i] = new Point(this.tarFP[i].x - this.tarFaceRect.x, this.tarFP[i].y - this.tarFaceRect.y);
//        }
        this.originIndex = originIndex;
        imageWarper = new ImageWarper(sourceBoundaryFilledImage, srcRectFP, tarRectFP, originIndex);
        warpedImage = imageWarper.runGet();
        warpedSkinMatrix = MatrixAndImage.imageToBinaryMatrix(warpedImage);
        this.warpedOrigin = imageWarper.getMappedOrigin();
        //warpedImage.getGraphics().setColor(Color.RED);
        //warpedImage.getGraphics().fillOval(warpedOrigin.x - 5, warpedOrigin.y - 5, 10, 10);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Apply color consistency">
    public void applyColorConsistency_meanShift() {
        //Adjust the color of the warped image according to the color of the target image
         meanColorShifter = new MeanColorShifter(warpedImage, tarSkinImg);
       colorConsistentImage = meanColorShifter.runGet();
        //colorConsistentImage = HistogramMatching.changeColorToTarget(warpedImage, tarSkinImg);
    }
    public void applyColorConsistency_HistogramMatch() {
        colorConsistentImage = HistogramMatching.changeColorToTarget(warpedImage, tarSkinImg);
        
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Shift replacement point">
    //It finds out the best point at which the image is to be replaced
    public void shiftReplacementPoint() {
        Shifter shifter = new Shifter();
        shifter.setSource(warpedSkinMatrix);
        shifter.setSourcePastePoint(warpedOrigin);
        shifter.setTarget(tarSkinMatrix);
        Point targetOriginPoint = new Point(tarFP[FeaturePoint.CHIN].x - tarFaceRect.x, tarFP[FeaturePoint.CHIN].y - tarFaceRect.y);
        shifter.setTargetPastePoint(targetOriginPoint);
        replacementPoint = shifter.getPastingPoint(3, 3);
        replacementPoint.x += tarFaceRect.x;
        replacementPoint.y += tarFaceRect.y;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Blending">
    /*
     * for blending you should pass the result image after replacement, the
     * warped face region, and the shiftvector replacedImage = image after
     * drawing the warped source face in the target warpedFace= rgba image of
     * rectangle region only, with warping applied Point shiftvector=
     * Point(xShift,yShift), where shifts are the respective shift in x and y
     * which is made while drawing the warped Face region to the target Image
     */
    public void blend() {
        Point shiftPoint = new Point(replacementPoint.x - warpedOrigin.x, replacementPoint.y - warpedOrigin.y);
        //blendedImage = Blender.getBlendedImage2(warpedImage, replacedFaceImage, shiftPoint);

//        Blender2 b = new Blender2();
//        b.setFaceReplacedImage(replacedFaceImage);
//        b.setFaceOnly(AlphaBlendedFace);
//        b.setShiftVector(shiftPoint);
//        b.setAveragingAlgorithm(blendingAlgorithmSelector);
//        blendedImage = b.processAndGetResult();
//        replacedFaceImage = blendedImage;
        //blendedImage=replacedFaceImage;
        Blender4 b=new Blender4();
        b.setFace(colorConsistentImage);
        b.setShiftVector(shiftPoint);
        b.setReplacedImage(replacedFaceImage);
        b.setIterations(blendIterations);
        b.process();
        blendedImage = b.getBlendedImage();
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Detect Hair">
    public void detectSourceHair() {

        int value = 1;
        //apply SRG if the region is to be grown
        if (srgMode.toLowerCase().compareTo("add") == 0) {
            SeedRegionGrowing srg = new SeedRegionGrowing(srcImg, sourceHairSeeds, srgThreshold);
            //srg.growRegion();
            int[][] hairMatrix = srg.getBinaryMatrix();
            value = 1;
            for (int x = 0; x < srcImg.getWidth(); x++) {
                for (int y = 0; y < srcImg.getHeight(); y++) {
                    if (hairMatrix[x][y] == SeedRegionGrowing.INSIDE) {
                        sourceHairMatrix[x][y] = value;
                    }
                }
            }
            SRGFilter srgFilter = new SRGFilter(sourceHairMatrix, srcImg.getWidth(), srcImg.getHeight(), 15);
            srgFilter.applyFilter();
            sourceHairMatrix = srgFilter.getFilteredMatrix();
        } //apply eraser if the seeds are to be removed
        else if (srgMode.toLowerCase().compareTo("remove") == 0) {
            MatrixOperations.erase(sourceHairMatrix, sourceHairSeeds);
        }


        //sourceHairMatrix = srg.imageToBinaryMatrix();
        srcHairImgShowingRegion = MatrixAndImage.getHighlightedImage(srcImg, sourceHairMatrix, Color.red);
        //sourceHairImage = MatrixAndImage.matrixToImage(srcImg, sourceHairMatrix);
        sourceHairSeeds = null;
    }

    public void detectTargetHair() {
        int value = 1;
        //apply SRG if the region is to be grown
        if (srgMode.toLowerCase().compareTo("add") == 0) {
            SeedRegionGrowing srg = new SeedRegionGrowing(tarImg, targetHairSeeds, srgThreshold);
            //srg.growRegion();
            int[][] hairMatrix = srg.getBinaryMatrix();
            for (int x = 0; x < tarImg.getWidth(); x++) {
                for (int y = 0; y < tarImg.getHeight(); y++) {
                    if (hairMatrix[x][y] == SeedRegionGrowing.INSIDE) {
                        targetHairMatrix[x][y] = value;
                    }
                }
            }
            SRGFilter srgFilter = new SRGFilter(targetHairMatrix, tarImg.getWidth(), tarImg.getHeight(), 15);
            srgFilter.applyFilter();
            targetHairMatrix = srgFilter.getFilteredMatrix();
        } //apply eraser if the seeds are to be removed
        else if (srgMode.toLowerCase().compareTo("remove") == 0) {
            MatrixOperations.erase(targetHairMatrix, targetHairSeeds);
        }


        //sourceHairMatrix = srg.imageToBinaryMatrix();
        tarHairImgShowingRegion = MatrixAndImage.getHighlightedImage(tarImg, targetHairMatrix, Color.red);
        //sourceHairImage = MatrixAndImage.matrixToImage(srcImg, sourceHairMatrix);
        targetHairSeeds = null;
    }

    public void addSourceHairToReplacedFace() {
        int cnt = 0;
        sourceHairImage = MatrixAndImage.matrixToImage(srcImg, sourceHairMatrix);
        ImageWarper srcHairWarper = new ImageWarper(sourceHairImage, srcFP, tarFP, originIndex);
        srcHairImgAfterWarping = srcHairWarper.runGet();
        Point warpedHairOrigin = srcHairWarper.getMappedOrigin();
        srcHairMatrixAfterWarping = MatrixAndImage.imageToBinaryMatrix(srcHairImgAfterWarping);
        for (int x = 0; x < srcHairImgAfterWarping.getWidth(); x++) {
            for (int y = 0; y < srcHairImgAfterWarping.getHeight(); y++) {
                if (new Color(srcHairImgAfterWarping.getRGB(x, y), true).getAlpha() > 250) {
                    cnt++;
                    try {
                        replacedFaceImage.setRGB(x - warpedHairOrigin.x + replacementPoint.x, y - warpedHairOrigin.y + replacementPoint.y, srcHairImgAfterWarping.getRGB(x, y));
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
        }
        System.out.println("no. of hair pixels in source " + cnt);
    }

    public void addTargetHairToReplacedFace() {
        for (int x = 0; x < tarImg.getWidth(); x++) {
            for (int y = 0; y < tarImg.getHeight(); y++) {
                if (targetHairMatrix[x][y] == 1) {
                    //bring the hair of the target image forward
                    replacedFaceImage.setRGB(x, y, tarImg.getRGB(x, y));
                }
            }
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Replace Face">
    public void replaceFace2() {
        replacedFaceImage = DeepCopier.getBufferedImage(tarImg, BufferedImage.TYPE_INT_ARGB);
        Blender3 b3=new Blender3();
        //b3.setFace(colorConsistentImage);
        b3.setFace(colorConsistentImage);
        b3.setTargetImage(tarImg);
        Point shiftVector=new Point(replacementPoint.x - warpedOrigin.x,replacementPoint.y - warpedOrigin.y);
        b3.setShiftVector(shiftVector);
        b3.setIterations(blendIterations);
        b3.process();
        BufferedImage temp=b3.getReplacedResult();
        if(temp==null){
            System.out.println("There is problem in Blender3");
        }else{
            replacedFaceImage=temp;
        }
        AlphaBlendedFace=b3.getAlphaBlendedFace();
    }

    public void replaceUsingTanBlending() {
        //replacedFaceImage = DeepCopier.getBufferedImage(tarImg, BufferedImage.TYPE_INT_ARGB);
        Point shiftPoint = new Point(replacementPoint.x - warpedOrigin.x, replacementPoint.y - warpedOrigin.y);
        TanhBlender blender = new TanhBlender(colorConsistentImage, tarImg, shiftPoint, 7,10,5);
        replacedFaceImage = blender.runAndGet();
    }

    public void overlayReplace() {
        replacedFaceImage = DeepCopier.getBufferedImage(tarImg, BufferedImage.TYPE_INT_ARGB);
        Graphics g = replacedFaceImage.getGraphics();
        Point shiftVector = new Point(replacementPoint.x - warpedOrigin.x, replacementPoint.y - warpedOrigin.y);
        g.drawImage(colorConsistentImage, shiftVector.x, shiftVector.y, null);
    }

    public void replaceFace() {
        replacedFaceImage = DeepCopier.getBufferedImage(tarImg, BufferedImage.TYPE_INT_ARGB);
        //Point replacementPoint = new Point(bestPointToReplace.x - warpedOrigin.x, bestPointToReplace.y - warpedOrigin.y);
        for (int x = 0; x < colorConsistentImage.getWidth(); x++) {
            for (int y = 0; y < colorConsistentImage.getHeight(); y++) {
                Color c = new Color(colorConsistentImage.getRGB(x, y), true);
                if (c.getAlpha() < 150) {
                    continue;
                }
                try {
                    replacedFaceImage.setRGB(x - warpedOrigin.x + replacementPoint.x, y - warpedOrigin.y + replacementPoint.y, c.getRGB());
                    //replacedFaceImage.setRGB(x - warpedOrigin.x + tarFP[FeaturePoint.CHIN].x, y - warpedOrigin.y + tarFP[FeaturePoint.CHIN].y, c.getRGB());
                    //replacedFaceImage.setRGB(x - warpedOrigin.x + replacementPoint.x, y - warpedOrigin.y + replacementPoint.y, c.getRGB());
                } catch (Exception e) {
                    continue;
                }
            }
        }
        //replacedFaceImage.getGraphics().fillOval(tarFP[FeaturePoint.CHIN].x - 5, tarFP[FeaturePoint.CHIN].y - 5, 10, 10);
        //replacedFaceImage.getGraphics().fillOval(actualTargetFeaturePoints[FeaturePoint.CHIN].x - 5, actualTargetFeaturePoints[FeaturePoint.CHIN].y - 5, 10, 10);

    }
    //</editor-fold>
}
