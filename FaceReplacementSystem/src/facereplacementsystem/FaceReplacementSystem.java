/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facereplacementsystem;

import CoreAlgorithms.ImageWarper;
import CoreAlgorithms.SkinColorDetector;
import CoreAlgorithms.SkinColorDetectorUsingThreshold;
import Helpers.MeanColorShifter;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author Robik Singh Shrestha
 */
/*
 * This class is the main class of the project. All the intermediate results and
 * final results are saved in this class.
 */
public class FaceReplacementSystem {

    // <editor-fold defaultstate="collapsed" desc="The first step is to set the source and target images">
    //1st step is to set the source and target images
    protected BufferedImage sourceImage;
    protected BufferedImage targetImage;
    // The source image and the target image must be set

    public void setSourceImage(BufferedImage sourceImage) {
        this.sourceImage = sourceImage;
    }

    public void setTargetImage(BufferedImage targetImage) {
        this.targetImage = targetImage;
    }

    public BufferedImage getSourceImage() {
        return sourceImage;
    }

    public BufferedImage getTargetImage() {
        return targetImage;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="The second step is to set the feature points of the source and target faces">
    //Next step is to define feature points for both source and the target
    protected Point[] sourceFeaturePoints, targetFeaturePoints;

    public Point[] getSourceFeaturePoints() {
        return sourceFeaturePoints;
    }

    public Point[] getTargetFeaturePoints() {
        return targetFeaturePoints;
    }

    public void setSourceFeaturePoints(Point[] featurePoints) {
        this.sourceFeaturePoints = featurePoints;
        this.setSourceFaceRectangle(getRectangleUsingFeaturePoints(featurePoints));
    }

    public void setTargetFeaturePoints(Point[] featurePoints) {
        this.targetFeaturePoints = featurePoints;
        this.setTargetFaceRectangle(getRectangleUsingFeaturePoints(featurePoints));
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="The third step is to set the rectangles around the faces of both source and target">
    //Next step is to provide the coordinates of the corners of the rectangles drawn around the faces of source and target
    protected Rectangle sourceFaceRectangle, targetFaceRectangle;
    protected BufferedImage sourceRectangleImage, targetRectangleImage;

    public void setSourceFaceRectangle(Rectangle sourceFaceRectangle) {
        this.sourceFaceRectangle = sourceFaceRectangle;
        this.sourceRectangleImage = sourceImage.getSubimage(sourceFaceRectangle.x, sourceFaceRectangle.y, sourceFaceRectangle.width, sourceFaceRectangle.height);
    }

    public void setTargetFaceRectangle(Rectangle targetFaceRectangle) {
        this.targetFaceRectangle = targetFaceRectangle;
        this.targetRectangleImage = targetImage.getSubimage(targetFaceRectangle.x, targetFaceRectangle.y, targetFaceRectangle.width, targetFaceRectangle.height);
    }

    public BufferedImage getTargetRectangleImage() {
        return targetRectangleImage;
    }

    public BufferedImage getSourceRectangleImage() {
        return sourceRectangleImage;
    }

    public Rectangle getSourceFaceRectangle() {
        return sourceFaceRectangle;
    }

    public Rectangle getTargetFaceRectangle() {
        return targetFaceRectangle;
    }

    public Rectangle getRectangleUsingFeaturePoints(Point[] featurePoints) {
        Rectangle rectangleUsingFeaturePoints;
        int xmin, xmax, ymin, ymax;
        xmin = xmax = featurePoints[0].x;
        ymin = ymax = featurePoints[0].y;
        for (int i = 1; i < featurePoints.length; i++) {
            xmin = featurePoints[i].x < xmin ? featurePoints[i].x : xmin;
            ymin = featurePoints[i].y < ymin ? featurePoints[i].y : ymin;
            xmax = featurePoints[i].x > xmax ? featurePoints[i].x : xmax;
            ymax = featurePoints[i].y > ymax ? featurePoints[i].y : ymax;
        }
        rectangleUsingFeaturePoints = new Rectangle(xmin, ymin, xmax - xmin, ymax - ymin);
        return rectangleUsingFeaturePoints;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="The fourth step is to detect the skin in source and target">
    //Next step is to detect the skin pixels in source image. They will be used to extract the source face.
    protected BufferedImage sourceSkinImage, targetSkinImage;
    protected int[][] sourceSkinMatrix, targetSkinMatrix;

    //Detect the skin region of the source
    public void detectSourceSkin() {
        SkinColorDetector sourceSkinDetector = new SkinColorDetectorUsingThreshold(sourceRectangleImage);
        sourceSkinDetector.detectSkin();
        sourceSkinMatrix = sourceSkinDetector.getSkinMatrix();
        sourceSkinImage = sourceSkinDetector.getSkinImage();
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

    public BufferedImage getSourceSkinImage() {
        return this.sourceSkinImage;
    }

    public BufferedImage getTargetSkinImage() {
        return this.targetSkinImage;
    }

    public int[][] getSourceSkinMatrix() {
        return sourceSkinMatrix;
    }

    public int[][] getTargetSkinMatrix() {
        return targetSkinMatrix;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="The fifth step is to warp the source face according to the fp of target face and interpolate the warped face">
    //Next step is to warp the image. The source image should be warped according to the feature points of the target image.
    protected ImageWarper imageWarper;
    //Next step is to apply interpolation
    protected BufferedImage warpedImage;
    //Next step is to apply color consistency to image
    protected MeanColorShifter meanColorShifter;
    //origin of the warped image
    protected Point warpedOrigin;
    //warp + interpolate

    public void warp(int originIndex) {
        Point[] sfp = new Point[this.sourceFeaturePoints.length];
        Point[] tfp = new Point[this.targetFeaturePoints.length];
        //Translate the feature points so that it is relative to the face rectangle
        for (int i = 0; i < this.sourceFeaturePoints.length; i++) {
            sfp[i] = new Point(this.sourceFeaturePoints[i].x - this.sourceFaceRectangle.x, this.sourceFeaturePoints[i].y - this.sourceFaceRectangle.y);
            tfp[i] = new Point(this.targetFeaturePoints[i].x - this.targetFaceRectangle.x, this.targetFeaturePoints[i].y - this.targetFaceRectangle.y);
        }
        imageWarper = new ImageWarper(sourceSkinImage, sfp, tfp, originIndex);
        warpedImage = imageWarper.runGet();
        this.warpedOrigin = imageWarper.getMappedOrigin();
    }

    public BufferedImage getWarpedImage() {
        return warpedImage;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="The sixth step is to apply color consistency to the image">
    //Next step is to adjust the consistency of the image
    protected BufferedImage colorConsistentImage;
    public void applyColorConsistency() {
        //Adjust the color of the warped image according to the color of the target image
        meanColorShifter = new MeanColorShifter(warpedImage, targetImage);
        colorConsistentImage = meanColorShifter.getResultImage();
    }
    // </editor-fold>
    //Next step is to replace the face
    protected BufferedImage replacedFaceImage;

    public BufferedImage getReplacedFaceImage() {
        return replacedFaceImage;
    }
}
