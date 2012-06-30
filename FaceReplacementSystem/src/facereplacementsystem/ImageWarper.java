/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facereplacementsystem;

import Helpers.FloatingCoordinate;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 *
 * @author Robik Singh Shrestha
 */
/**
 * You need to set the source image for this class. The feature points of the
 * source and the target should also be set. Then, source face is warped
 * according to the target face feature points. Interpolation is performed.
 * Finally the image is returned.
 */
/*
 * How to use it?
 * 
 * 1. Create an object of this class:
 * ImageWarper imageWarper = new ImageWarper(image,sourceFeaturePoints, targetFeaturePoints);
 * 
 * 2. Get the warped Image:
 * BufferedImage warpedImage = imageWarper.runGet();
 */
public class ImageWarper {

    protected BufferedImage image;
    protected Point[] sourceFeaturePoints, targetFeaturePoints;
    protected Warper warper;
    protected Interpolator interpolator;
    protected BufferedImage warpedImage;

    public ImageWarper(BufferedImage image, Point[] sourceFeaturePoints, Point[] targetFeaturePoints) {
        this.image = image;
        this.sourceFeaturePoints = sourceFeaturePoints;
        this.targetFeaturePoints = targetFeaturePoints;
    }

    public BufferedImage runGet() {
        run();
        return this.getWarpedImage();
    }

    public void run() {
        warper = new Warper(sourceFeaturePoints, targetFeaturePoints, image.getWidth(), image.getHeight());
        FloatingCoordinate reverseMap[][] = warper.runGet();
        Interpolator interpolator = new Interpolator(image, reverseMap, warper.getWarpedWidth(), warper.getWarpedHeight());
        warpedImage = interpolator.runGet();
    }

    public BufferedImage getWarpedImage() {
        return this.warpedImage;
    }
}
