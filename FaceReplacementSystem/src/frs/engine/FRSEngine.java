
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.engine;

import frs.curve.SquarePolynomial_002;
import frs.algorithms.*;
import frs.dataTypes.FeaturePoint;
import frs.helpers.ColorModelConverter;
import frs.helpers.DeepCopier;
import frs.helpers.GeometricTransformation;
import frs.helpers.MatrixAndImage;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

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
    public void detectSourceSkin() {
        SkinColorDetector sourceSkinDetector = new SkinColorDetectorUsingThreshold(srcRectImg);
        sourceSkinDetector.detectSkin();
        srcSkinMatrix = sourceSkinDetector.getSkinMatrix();//size =FaceRectangle
        //findSourceCurves();
        //filterSourceCurve();
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
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Find chin curves in the source">
    public void findSourceCurves() {
        SquarePolynomial_002 leftCurve = new SquarePolynomial_002();
        Point[] leftCurvePoints = {srcRectFP[FeaturePoint.CHIN], srcRectFP[FeaturePoint.LEFT_CHIN],
            srcRectFP[FeaturePoint.LEFT_CHEEK]};
        //setting the example points, at least 3 required
        leftCurve.setPoints(leftCurvePoints);
        Point[] leftCurveEnds = {srcRectFP[FeaturePoint.LEFT_CHEEK], srcRectFP[FeaturePoint.CHIN]};
        srcRectLeftEdge = leftCurve.getPoints(leftCurveEnds);//getting the fitted Points

        SquarePolynomial_002 rightCurve = new SquarePolynomial_002();
        Point[] rightCurvePoints = {srcRectFP[FeaturePoint.CHIN], srcRectFP[FeaturePoint.RIGHT_CHIN],
            srcRectFP[FeaturePoint.RIGHT_CHEEK]};
        rightCurve.setPoints(rightCurvePoints);
        Point[] rightCurveEnds = {srcRectFP[FeaturePoint.CHIN], srcRectFP[FeaturePoint.RIGHT_CHEEK]};
        srcRectRightEdge = rightCurve.getPoints(rightCurveEnds);
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
        useLeftSrcCurve();
        useRightSrcCurve();
    }

    protected void useLeftSrcCurve() {
        //Set the left and bottom pixels below the chin points  to 0 for left edge
        //The point in the chin itself is set to 1
        Point lcp;//left curve point
        for (int i = 0; i < srcRectLeftEdge.size(); i++) {
            lcp = srcRectLeftEdge.get(i);
            for (int x = 0; x < lcp.x; x++) {
                try {
                    srcMatrixWithChinCurve[x][lcp.y] = 0;
                } catch (Exception e) {
                    continue;
                }
            }
            for (int y = lcp.y + 1; y < srcRectImg.getHeight(); y++) {
                try {
                    srcMatrixWithChinCurve[lcp.x][y] = 0;
                } catch (Exception e) {
                    continue;
                }
            }
            try {
                srcMatrixWithChinCurve[lcp.x][lcp.y] = 1;
                //System.out.printf("\nIt is possible to fill at (%d,%d)\n", lcp.x, lcp.y);
            } catch (Exception e) {
                //System.out.printf("\nCan't fill srcMatrixWithChinCurve at (%d,%d) \n", lcp.x, lcp.y);
                continue;
            }
        }
    }

    protected void useRightSrcCurve() {
        //Set the right and bottom pixels below the chin points to 0
        //The point in the chin itself is set to 1
        Point rcp;//right curve point
        for (int i = srcRectRightEdge.size() - 1; i >= 0; i--) {
            rcp = srcRectRightEdge.get(i);
            for (int x = rcp.x; x < srcRectImg.getWidth(); x++) {
                try {
                    srcMatrixWithChinCurve[x][rcp.y] = 0;
                } catch (Exception e) {
                    continue;
                }
            }
            for (int y = rcp.y + 1; y < srcRectImg.getHeight(); y++) {
                try {
                    srcMatrixWithChinCurve[rcp.x][y] = 0;
                } catch (Exception e) {
                    continue;
                }
            }
            try {
                srcMatrixWithChinCurve[rcp.x][rcp.y] = 1;
            } catch (Exception e) {
                continue;
            }
        }
    }

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Extract the boundary around the face">
    public void findSourceBoundaryFilledMatrix() {
        int w = srcFaceRect.width;
        int h = srcFaceRect.height;
        int[][] erectMatrix;//=ImageMat.invertMatrix(finerMatrix, h, w);
        erectMatrix = ImageMat.boundaryFilledTwoWay(srcMatrixWithChinCurve, w, h);
        sourceBoundaryFilledFaceMatrix = erectMatrix;//ImageMat.holeFillAccordingToBoundary(erectMatrix, w, h);

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
    public void applyColorConsistency() {
        //Adjust the color of the warped image according to the color of the target image
        meanColorShifter = new MeanColorShifter(warpedImage, tarSkinImg);
        colorConsistentImage = meanColorShifter.runGet();
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
        blendedImage = Blender.getBlendedImage2(warpedImage, replacedFaceImage, shiftPoint);
        replacedFaceImage = blendedImage;
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Detect Hair">

    public void detectSourceHair() {
        SeedRegionGrowing srg = new SeedRegionGrowing(srcImg, sourceHairSeeds, srgThreshold);
        //srg.growRegion();
        int[][] hairMatrix = srg.getBinaryMatrix();
        int value = 1;
        if (srgMode.toLowerCase().compareTo("add") == 0) {
            value = 1;
        }
        if (srgMode.toLowerCase().compareTo("remove") == 0) {
            value = 0;
        }
        for (int x = 0; x < srcImg.getWidth(); x++) {
            for (int y = 0; y < srcImg.getHeight(); y++) {
                if (hairMatrix[x][y] == SeedRegionGrowing.INSIDE) {
                    sourceHairMatrix[x][y] = value;
                }
            }
        }
        //sourceHairMatrix = srg.imageToBinaryMatrix();
        srcHairImgShowingRegion = MatrixAndImage.getHighlightedImage(srcImg, sourceHairMatrix, Color.red);
        //sourceHairImage = MatrixAndImage.matrixToImage(srcImg, sourceHairMatrix);
    }

    public void detectTargetHair() {
        SeedRegionGrowing srg = new SeedRegionGrowing(tarImg, targetHairSeeds, srgThreshold);
        int[][] hairMatrix = srg.getBinaryMatrix();
        int value = 1;
        if (srgMode.toLowerCase().compareTo("add") == 0) {
            value = 1;//1=add
        }
        if (srgMode.toLowerCase().compareTo("remove") == 0) {
            value = 0;//0 means remove (make it transparent)
        }
        for (int x = 0; x < tarImg.getWidth(); x++) {
            for (int y = 0; y < tarImg.getHeight(); y++) {
                if (hairMatrix[x][y] == SeedRegionGrowing.INSIDE) {
                    targetHairMatrix[x][y] = value;
                }
            }
        }
        //targetHairMatrix = srg.imageToBinaryMatrix();
        tarHairImgShowingRegion = MatrixAndImage.getHighlightedImage(tarImg, targetHairMatrix, Color.red);
        //targetHairImage = MatrixAndImage.matrixToImage(tarImg, targetHairMatrix);
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
    public void replaceFace() {
        replacedFaceImage = DeepCopier.getBufferedImage(tarImg, BufferedImage.TYPE_INT_ARGB);
        //Point replacementPoint = new Point(bestPointToReplace.x - warpedOrigin.x, bestPointToReplace.y - warpedOrigin.y);
        for (int x = 0; x < colorConsistentImage.getWidth(); x++) {
            for (int y = 0; y < colorConsistentImage.getHeight(); y++) {
                Color c = new Color(colorConsistentImage.getRGB(x, y), true);
                if (c.getAlpha() < 255) {
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
