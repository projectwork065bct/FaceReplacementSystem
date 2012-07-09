/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facereplacementsystem;

import CoreAlgorithms.*;
import Helpers.FloatingCoordinate;
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

    //1st step is to set the source and target images
    protected BufferedImage sourceImage;
    protected BufferedImage targetImage;
    //Next step is to define feature points for both source and the target
    protected Point[] sourceFeaturePoints, targetFeaturePoints;
    //Next step is to provide the coordinates of the corners of the rectangles drawn around the faces of source and target
    protected Rectangle sourceFaceRectangle, targetFaceRectangle;
    protected BufferedImage sourceRectangleImage, targetRectangleImage;
    //Next step is to detect the skin pixels in source image. They will be used to extract the source face.
    protected BufferedImage sourceSkinImage;
    protected int[][] sourceSkinMatrix;
    //Next step is to detect the skin pixels of target image. They will be used for color consistency.
    protected BufferedImage targetSkinImage;
    protected int[][] targetSkinMatrix;

    
    
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
    
    public void detectSkin()
    {
        detectSourceSkin();
        detectTargetSkin();
    }
    
    //Next step is to warp the image. The source image should be warped according to the feature points of the target image.
    ImageWarper imageWarper;
    protected BufferedImage uninterpolatedWarpedImage;
    protected Point[] warpedSourceFeaturePoints;
    //Next step is to apply interpolation
    protected BufferedImage interpolatedImage;
    //Next step is to apply color consistency to image
    protected MeanColorShifter meanColorShifter;
    
    public void warp() {
        imageWarper = new ImageWarper(sourceImage, sourceFeaturePoints, targetFeaturePoints);
        interpolatedImage = imageWarper.runGet();
        //This step is just to check the uninterpolated image. It should be commented out
        //uninterpolatedWarpedImage = imageWarper.getUninterpolatedWarpedImage();
    }

    //Next step is to adjust the consistency of the image
    protected BufferedImage colorConsistentImage;
    public void applyColorConsistency() {
        meanColorShifter = new MeanColorShifter(sourceImage, targetImage);
        colorConsistentImage = meanColorShifter.getResultImage();
    }
    
    
    //
    
    //Next step is to replace the face
    protected BufferedImage replacedFaceImage;


    
    
    //The source image and the target image must be set
    public void setSourceImage(BufferedImage sourceImage) {
        this.sourceImage = sourceImage;
    }

    public void setTargetImage(BufferedImage targetImage) {
        this.targetImage = targetImage;
    }

    public BufferedImage getInterpolatedImage() {
        return interpolatedImage;
    }

    public BufferedImage getReplacedFaceImage() {
        return replacedFaceImage;
    }

    public Point[] getSourceFeaturePoints() {
        return sourceFeaturePoints;
    }

    public int[][] getSourceSkinMatrix() {
        return sourceSkinMatrix;
    }

    public Point[] getTargetFeaturePoints() {
        return targetFeaturePoints;
    }

    public int[][] getTargetSkinMatrix() {
        return targetSkinMatrix;
    }

    public BufferedImage getUninterpolatedWarpedImage() {
        return uninterpolatedWarpedImage;
    }

    public Point[] getWarpedSourceFeaturePoints() {
        return warpedSourceFeaturePoints;
    }
}
