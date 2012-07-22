/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CoreAlgorithms;

import Helpers.DeepCopier;
import Helpers.FloatingCoordinate;
import java.awt.Color;
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
 * 1. Create an object of this class: ImageWarper imageWarper = new
 * ImageWarper(image,sourceFeaturePoints, targetFeaturePoints);
 *
 * 2. Get the warped Image: BufferedImage warpedImage = imageWarper.runGet();
 */
public class ImageWarper {

    protected BufferedImage image;
    protected Point[] sourceFeaturePoints, targetFeaturePoints;
    protected Warper warper;
    protected Interpolator interpolator;
    protected BufferedImage warpedImage;
    protected BufferedImage uninterpolatedWarpedImage;
    protected int originIndex;

    public ImageWarper(BufferedImage image, Point[] sourceFeaturePoints, Point[] targetFeaturePoints, int originIndex) {
        this.image = DeepCopier.getBufferedImage(image, image.getType());
        this.sourceFeaturePoints = sourceFeaturePoints;
        this.targetFeaturePoints = targetFeaturePoints;
        this.originIndex = originIndex;
    }

    //It returns the image which has been warped by applying reverse mapping but is not yet interpolated
    public BufferedImage getWarpedButNotInterpolatedImage() {
        warper = new Warper(sourceFeaturePoints, targetFeaturePoints, image.getWidth(), image.getHeight(), originIndex);
        FloatingCoordinate reverseMap[][] = warper.runGet();
        BufferedImage uninterpolatedImage = new BufferedImage(warper.getWarpedWidth(), warper.getWarpedHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < warper.getWarpedWidth(); x++) {
            Color c = new Color(0, 0, 0, 0);
            for (int y = 0; y < warper.getWarpedHeight(); y++) {
                int srcX = (int) reverseMap[x][y].x;
                int srcY = (int) reverseMap[x][y].y;
                if (isWithinBounds(image, srcX, srcY)) {
                    int rgbAtSource = image.getRGB((int) reverseMap[x][y].x, (int) reverseMap[x][y].y);
                    uninterpolatedImage.setRGB(x, y, rgbAtSource);
                } else {
                    uninterpolatedImage.setRGB(x, y, c.getRGB());
                }
            }
        }
        return uninterpolatedImage;
    }

    protected boolean isWithinBounds(BufferedImage image1, int x, int y) {
        if (x >= 0 && x < image1.getWidth() && y >= 0 && y < image1.getHeight()) {
            return true;
        } else {
            return false;
        }
    }
    

    public BufferedImage runGet() {
        run();
        return this.getWarpedImage();
    }

    public void run() {
        warper = new Warper(sourceFeaturePoints, targetFeaturePoints, image.getWidth(), image.getHeight(), originIndex);
        FloatingCoordinate reverseMap[][] = warper.runGet();
        Interpolator newInterpolator = new Interpolator(image, reverseMap, warper.getWarpedWidth(), warper.getWarpedHeight());
        warpedImage = newInterpolator.runGet();
    }

    /*
     * public void findUninterpolatedWarpedImage() { uninterpolatedWarpedImage =
     * new BufferedImage(warper.getWarpedWidth(), warper.getWarpedHeight(),
     * BufferedImage.TYPE_INT_ARGB); Point warpedMatrix[][] =
     * warper.getWarpedMatrix(); for (int x = 0; x < image.getWidth(); x++) {
     * int curX, curY; for (int y = 0; y < image.getHeight(); y++) { try { curX
     * = (int) warpedMatrix[x][y].x; curY = (int) warpedMatrix[x][y].y;
     * uninterpolatedWarpedImage.setRGB(curX, curY, image.getRGB(x, y)); } catch
     * (Exception e) { continue; } } }
    }
     */
    /*
     * public BufferedImage getUninterpolatedWarpedImage() { return
     * this.uninterpolatedWarpedImage;
    }
     */
    public BufferedImage getWarpedImage() {
        return this.warpedImage;
    }

    //It returns the coordinate of the point mapped to warped image
    public Point getMappedPoint(Point point) {
        Point mappedPoint;
        return warper.getMappedPoint(point);
    }
    
    public Point getMappedOrigin() {
        return warper.getMappedOrigin();
    }
    public Warper getWarper()
    {return warper;}
}
