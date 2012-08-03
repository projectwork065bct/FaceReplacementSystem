
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facereplacementsystem;

import CoreAlgorithms.*;
import CurveFitting.SquarePolynomial_002;
import DataStructures.FeaturePoint;
import H_Blending.Blender;
import H_Matrix.ImageMat;
import Helpers.*;
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
    //It rotates the image and stores the result in the rotatedSourceImage
    public void rotateSource() {
        int delY = sourceFeaturePoints[FeaturePoint.RIGHT_EYE].y - sourceFeaturePoints[FeaturePoint.LEFT_EYE].y;
        int delX = sourceFeaturePoints[FeaturePoint.RIGHT_EYE].x - sourceFeaturePoints[FeaturePoint.LEFT_EYE].x;
        float theta = (float) -Math.atan2(delY, delX);
        BufferedImage srcImg = DeepCopier.getBufferedImage(sourceImage, BufferedImage.TYPE_INT_ARGB);
        AffineTransform transformImg = new AffineTransform();
        transformImg.rotate(theta, sourceFeaturePoints[FeaturePoint.LEFT_EYE].x, sourceFeaturePoints[FeaturePoint.LEFT_EYE].y);
        AffineTransformOp op = new AffineTransformOp(transformImg, AffineTransformOp.TYPE_BILINEAR);
        rotatedSourceImage = op.filter(srcImg, null);
        //Rotate all the feature points
        rotatedSrcFP = new Point[sourceFeaturePoints.length];
        Point leftEye = sourceFeaturePoints[FeaturePoint.LEFT_EYE];
        for (int i = 0; i < sourceFeaturePoints.length; i++) {
            rotatedSrcFP[i] = Transformer.rotatePoint(sourceFeaturePoints[i], theta, leftEye);
        }
    }

    public void findSourceRectImage() {
        sourceRectangleImage = rotatedSourceImage.getSubimage(sourceFaceRectangle.x, sourceFaceRectangle.y, sourceFaceRectangle.width, sourceFaceRectangle.height);
    }
    //Find the feature points with respect to the source rectangle

    public void findSourceRectFP() {
        sourceRectangleFP = new Point[sourceFeaturePoints.length];
        //Translate the feature points to coordinate system of rectangle
        for (int i = 0; i < sourceRectangleFP.length; i++) {
            sourceRectangleFP[i] = new Point();
            sourceRectangleFP[i].x = rotatedSrcFP[i].x - sourceFaceRectangle.x;
            sourceRectangleFP[i].y = rotatedSrcFP[i].y - sourceFaceRectangle.y;
        }
    }

    public void findTargetRectImage() {
        targetRectangleImage = targetImage.getSubimage(targetFaceRectangle.x, targetFaceRectangle.y, targetFaceRectangle.width, targetFaceRectangle.height);
    }

    //Find the feature points of the target
    public void findTargetRectFP() {
        targetRectangleFP = new Point[targetFeaturePoints.length];
        for (int i = 0; i < targetRectangleFP.length; i++) {
            targetRectangleFP[i] = new Point();
            targetRectangleFP[i].x = targetFeaturePoints[i].x - targetFaceRectangle.x;
            targetRectangleFP[i].y = targetFeaturePoints[i].y - targetFaceRectangle.y;
        }
    }
//It draws the feature points on the source rectangle image (rectangle image which is obtained after rotation about left eye through angle between two eyes)

    public void drawFPOnSrcRectImage() {
        srcRectImgWithFP = DeepCopier.getBufferedImage(sourceRectangleImage, sourceRectangleImage.getType());
        for (int i = 0; i < sourceRectangleFP.length; i++) {
            srcRectImgWithFP.getGraphics().fillOval(sourceRectangleFP[i].x - 2, sourceRectangleFP[i].y - 2, 5, 5);
        }
    }

//It draws feature points on rotated image of source
    public void drawFPOnRotatedImage() {
        for (int i = 0; i < rotatedSrcFP.length; i++) {
            rotatedSourceImage.getGraphics().fillOval(rotatedSrcFP[i].x - 5, rotatedSrcFP[i].y - 5, 10, 10);
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Detect the skin in source and target">
    //Detect the skin region of the source
    public void detectSourceSkin() {
        SkinColorDetector sourceSkinDetector = new SkinColorDetectorUsingThreshold(sourceRectangleImage);
        sourceSkinDetector.detectSkin();
        sourceSkinMatrix = sourceSkinDetector.getSkinMatrix();
        //findSourceCurves();
        //filterSourceCurve();
        sourceSkinImage = sourceSkinDetector.getSkinImage();
    }

    public void shrinkSource() {
        int shrinkCount = 5;
        int w = sourceFaceRectangle.width;
        int h = sourceFaceRectangle.height;
        shrunkMatrix = ImageMat.shrinkMatrix(sourceSkinMatrix, w, h, shrinkCount);
        shrunkSrcImage = MatrixAndImage.getImage(sourceRectangleImage, shrunkMatrix);
    }

    public void growSource() {
        int shrinkCount = 5;
        int w = sourceFaceRectangle.width;
        int h = sourceFaceRectangle.height;
        grownMatrix = ImageMat.growMatrix(shrunkMatrix, w, h, shrinkCount);
        grownSrcImage = MatrixAndImage.getImage(sourceRectangleImage, grownMatrix);
    }

    public void detectTargetSkin() {
        SkinColorDetector targetSkinDetector = new SkinColorDetectorUsingThreshold(targetRectangleImage);
        targetSkinDetector.detectSkin();
        targetSkinMatrix = targetSkinDetector.getSkinMatrix();
        targetSkinImage = targetSkinDetector.getSkinImage();
    }

    public void detectSkin() {
        detectSourceSkin();
        detectTargetSkin();
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Find chin curves in the source">
    public void findSourceCurves() {
        SquarePolynomial_002 leftCurve = new SquarePolynomial_002();
        Point[] leftCurvePoints = {sourceRectangleFP[FeaturePoint.CHIN], sourceRectangleFP[FeaturePoint.LEFT_CHIN],
            sourceRectangleFP[FeaturePoint.LEFT_CHEEK]};
        //setting the example points, at least 3 required
        leftCurve.setPoints(leftCurvePoints);
        Point[] leftCurveEnds = {sourceRectangleFP[FeaturePoint.LEFT_CHEEK], sourceRectangleFP[FeaturePoint.CHIN]};
        sourceRectLeftEdge = leftCurve.getPoints(leftCurveEnds);//getting the fitted Points

        SquarePolynomial_002 rightCurve = new SquarePolynomial_002();
        Point[] rightCurvePoints = {sourceRectangleFP[FeaturePoint.CHIN], sourceRectangleFP[FeaturePoint.RIGHT_CHIN],
            sourceRectangleFP[FeaturePoint.RIGHT_CHEEK]};
        rightCurve.setPoints(rightCurvePoints);
        Point[] rightCurveEnds = {sourceRectangleFP[FeaturePoint.CHIN], sourceRectangleFP[FeaturePoint.RIGHT_CHEEK]};
        sourceRectRightEdge = rightCurve.getPoints(rightCurveEnds);
    }

    //Draw the chin curve in the source image
    public void drawSourceCurves() {
        findSourceCurves();
        List<Point> sourceLeftEdge = new ArrayList(sourceRectLeftEdge.size());
        List<Point> sourceRightEdge = new ArrayList(sourceRectRightEdge.size());
        sourceImageWithCurve = DeepCopier.getBufferedImage(rotatedSourceImage, rotatedSourceImage.getType());
        for (int i = 0; i < sourceRectLeftEdge.size(); i++) {
            sourceLeftEdge.add(new Point(sourceRectLeftEdge.get(i).x + sourceFaceRectangle.x, sourceRectLeftEdge.get(i).y + sourceFaceRectangle.y));
            sourceImageWithCurve.setRGB(sourceLeftEdge.get(i).x, sourceLeftEdge.get(i).y, Color.RED.getRGB());
        }
        //sourceImageWithCurve.getGraphics().fillOval(sourceLeftEdge.get(0).x-2, sourceLeftEdge.get(0).y-2, 5,5);

        for (int i = 0; i < sourceRectRightEdge.size(); i++) {
            sourceRightEdge.add(new Point(sourceRectRightEdge.get(i).x + sourceFaceRectangle.x, sourceRectRightEdge.get(i).y + sourceFaceRectangle.y));
            sourceImageWithCurve.setRGB(sourceRightEdge.get(i).x, sourceRightEdge.get(i).y, Color.RED.getRGB());

        }
        //sourceImageWithCurve.getGraphics().fillOval(sourceRightEdge.get(sourceRightEdge.size()-1).x-2, sourceRightEdge.get(sourceRightEdge.size()-1).y-2, 5,5);
    }

    public void useSrcCurves() {
        srcMatrixWithChin = new int[sourceFaceRectangle.width][sourceFaceRectangle.height];
        for (int x = 0; x < sourceRectangleImage.getWidth(); x++) {
            srcMatrixWithChin[x] = new int[sourceRectangleImage.getHeight()];
            for (int y = 0; y < sourceRectangleImage.getHeight(); y++) {
                srcMatrixWithChin[x][y] = grownMatrix[x][y];
            }
        }
        useLeftSrcCurve();
        useRightSrcCurve();
    }

    protected void useLeftSrcCurve() {
        //Set the left and bottom pixels below the chin points  to 0 for left edge
        //The point in the chin itself is set to 1
        Point lcp;//left curve point
        for (int i = 0; i < sourceRectLeftEdge.size(); i++) {
            lcp = sourceRectLeftEdge.get(i);
            for (int x = 0; x < lcp.x; x++) {
                try {
                    srcMatrixWithChin[x][lcp.y] = 0;
                } catch (Exception e) {
                    continue;
                }
            }
            for (int y = lcp.y + 1; y < sourceRectangleImage.getHeight(); y++) {
                try {
                    srcMatrixWithChin[lcp.x][y] = 0;
                } catch (Exception e) {
                    continue;
                }
            }
            try {
                srcMatrixWithChin[lcp.x][lcp.y] = 1;
                //System.out.printf("\nIt is possible to fill at (%d,%d)\n", lcp.x, lcp.y);
            } catch (Exception e) {
                //System.out.printf("\nCan't fill srcMatrixWithChin at (%d,%d) \n", lcp.x, lcp.y);
                continue;
            }
        }
    }

    protected void useRightSrcCurve() {
        //Set the right and bottom pixels below the chin points to 0
        //The point in the chin itself is set to 1
        Point rcp;//right curve point
        for (int i = sourceRectRightEdge.size() - 1; i >= 0; i--) {
            rcp = sourceRectRightEdge.get(i);
            for (int x = rcp.x; x < sourceRectangleImage.getWidth(); x++) {
                try {
                    srcMatrixWithChin[x][rcp.y] = 0;
                } catch (Exception e) {
                    continue;
                }
            }
            for (int y = rcp.y + 1; y < sourceRectangleImage.getHeight(); y++) {
                try {
                    srcMatrixWithChin[rcp.x][y] = 0;
                } catch (Exception e) {
                    continue;
                }
            }
            try {
                srcMatrixWithChin[rcp.x][rcp.y] = 1;
            } catch (Exception e) {
                continue;
            }
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Extract the boundary around the face">
    public void findSourceBoundaryFilledMatrix() {
        int w = sourceFaceRectangle.width;
        int h = sourceFaceRectangle.height;
        int fineMatrix[][] = ImageMat.holeFillAccordingToBoundary(srcMatrixWithChin, w, h);
        int inverseMatrix[][] = ImageMat.invertMatrix(fineMatrix, w, h);
        int finerMatrix[][] = ImageMat.holeFillAccordingToBoundary(inverseMatrix, h, w);
        sourceBoundaryFilledFaceMatrix = ImageMat.invertMatrix(finerMatrix, h, w);

    }

    //Construct an image from sourceBoundaryFilledFaceMatrix
    public void findSourceBoundaryFilledImage() {
        sourceBoundaryFilledImage = DeepCopier.getBufferedImage(sourceRectangleImage, BufferedImage.TYPE_INT_ARGB);
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
//        Point[] sfp = new Point[this.sourceFeaturePoints.length];
//        Point[] tfp = new Point[this.targetFeaturePoints.length];
//        //Translate the feature points so that it is relative to the face rectangle
//        for (int i = 0; i < this.sourceFeaturePoints.length; i++) {
//            sfp[i] = new Point(this.sourceFeaturePoints[i].x - this.sourceFaceRectangle.x, this.sourceFeaturePoints[i].y - this.sourceFaceRectangle.y);
//            tfp[i] = new Point(this.targetFeaturePoints[i].x - this.targetFaceRectangle.x, this.targetFeaturePoints[i].y - this.targetFaceRectangle.y);
//        }
        this.originIndex = originIndex;
        imageWarper = new ImageWarper(sourceBoundaryFilledImage, sourceRectangleFP, targetRectangleFP, originIndex);
        warpedImage = imageWarper.runGet();
        warpedSkinMatrix = MatrixAndImage.getBinaryMatrix(warpedImage);
        this.warpedOrigin = imageWarper.getMappedOrigin();
        //warpedImage.getGraphics().setColor(Color.RED);
        //warpedImage.getGraphics().fillOval(warpedOrigin.x - 5, warpedOrigin.y - 5, 10, 10);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Apply color consistency">
    public void applyColorConsistency() {
        //Adjust the color of the warped image according to the color of the target image
        meanColorShifter = new MeanColorShifter(warpedImage, targetSkinImage);
        colorConsistentImage = meanColorShifter.runGet();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Shift replacement point">
    //It finds out the best point at which the image is to be replaced
    public void shiftReplacementPoint() {
        Shifter shifter = new Shifter();
        shifter.setSource(warpedSkinMatrix);
        shifter.setSourcePastePoint(warpedOrigin);
        shifter.setTarget(targetSkinMatrix);
        Point targetOriginPoint = new Point(targetFeaturePoints[FeaturePoint.CHIN].x - targetFaceRectangle.x, targetFeaturePoints[FeaturePoint.CHIN].y - targetFaceRectangle.y);
        shifter.setTargetPastePoint(targetOriginPoint);
        replacementPoint = shifter.getPastingPoint(3, 3);
        replacementPoint.x += targetFaceRectangle.x;
        replacementPoint.y += targetFaceRectangle.y;
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

    public void detectSourceHair() {
        SeedRegionGrowing srg = new SeedRegionGrowing(sourceImage, sourceHairSeeds);
        //srg.growRegion();
        sourceHairMatrix = srg.getBinaryMatrix();
        srg.createImageShowingRegion(Color.red);
        srcHairImgShowingRegion = srg.getImageShowingRegion();
        sourceHairImage = srg.getBinaryImage();
    }

    public void detectTargetHair() {
        SeedRegionGrowing srg = new SeedRegionGrowing(targetImage, targetHairSeeds);
        srg.growRegion();
        targetHairMatrix = srg.getBinaryMatrix();
        srg.createImageShowingRegion(Color.red);
        tarHairImgShowingRegion = srg.getImageShowingRegion();
    }

    public void addSourceHairToReplacedFace() {
        int cnt = 0;
        ImageWarper srcHairWarper = new ImageWarper(sourceHairImage, sourceFeaturePoints, targetFeaturePoints, originIndex);
        srcHairImgAfterWarping = srcHairWarper.runGet();
        Point warpedHairOrigin = srcHairWarper.getMappedOrigin();
        srcHairMatrixAfterWarping = MatrixAndImage.getBinaryMatrix(srcHairImgAfterWarping);
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
        for (int x = 0; x < targetImage.getWidth(); x++) {
            for (int y = 0; y < targetImage.getHeight(); y++) {
                if (targetHairMatrix[x][y] == 1) {
                    //bring the hair of the target image forward
                    replacedFaceImage.setRGB(x, y, targetImage.getRGB(x, y));
                }
            }
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Replace Face">
    public void replaceFace() {
        replacedFaceImage = DeepCopier.getBufferedImage(targetImage, BufferedImage.TYPE_INT_ARGB);
        //Point replacementPoint = new Point(bestPointToReplace.x - warpedOrigin.x, bestPointToReplace.y - warpedOrigin.y);
        for (int x = 0; x < colorConsistentImage.getWidth(); x++) {
            for (int y = 0; y < colorConsistentImage.getHeight(); y++) {
                Color c = new Color(colorConsistentImage.getRGB(x, y), true);
                if (c.getAlpha() < 255) {
                    continue;
                }
                try {
                    replacedFaceImage.setRGB(x - warpedOrigin.x + replacementPoint.x, y - warpedOrigin.y + replacementPoint.y, c.getRGB());
                    //replacedFaceImage.setRGB(x - warpedOrigin.x + targetFeaturePoints[FeaturePoint.CHIN].x, y - warpedOrigin.y + targetFeaturePoints[FeaturePoint.CHIN].y, c.getRGB());
                    //replacedFaceImage.setRGB(x - warpedOrigin.x + replacementPoint.x, y - warpedOrigin.y + replacementPoint.y, c.getRGB());
                } catch (Exception e) {
                    continue;
                }
            }
        }
        //replacedFaceImage.getGraphics().fillOval(targetFeaturePoints[FeaturePoint.CHIN].x - 5, targetFeaturePoints[FeaturePoint.CHIN].y - 5, 10, 10);
        //replacedFaceImage.getGraphics().fillOval(actualTargetFeaturePoints[FeaturePoint.CHIN].x - 5, actualTargetFeaturePoints[FeaturePoint.CHIN].y - 5, 10, 10);

    }
    //</editor-fold>
}
