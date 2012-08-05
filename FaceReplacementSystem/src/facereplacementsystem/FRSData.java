/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facereplacementsystem;

import CoreAlgorithms.ImageWarper;
import Helpers.MeanColorShifter;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Dell
 */
public class FRSData {
    //Images

    protected int originIndex;
    protected BufferedImage sourceImage, targetImage;
    protected Point[] sourceFeaturePoints, targetFeaturePoints;
    protected BufferedImage rotatedSourceImage;//Image obtained after rotating the source image according to eye axis
    protected Point[] rotatedSrcFP;
    //Next step is to provide the coordinates of the corners of the rectangles drawn around the faces of source and target
    protected Rectangle sourceFaceRectangle, targetFaceRectangle;
    protected Point rotatedRectXY;
    protected BufferedImage sourceRectangleImage, targetRectangleImage;//Images of rectangles drawn around the faces
    protected BufferedImage srcRectImgWithFP;
    protected Point sourceRectangleFP[], targetRectangleFP[];//feature points with reference to origin of the that the user has drawn rectangles
    //Edges after chin curves are detected
    protected List<Point> sourceRectLeftEdge = null, sourceRectRightEdge = null, targetRectLeftEdge = null, targetRectRightEdge = null;
    //non-skin pixels are transparent
    protected BufferedImage sourceSkinImage, targetSkinImage;
    protected int[][] sourceSkinMatrix, targetSkinMatrix;
    protected int[][] shrunkMatrix, grownMatrix;
    protected BufferedImage shrunkSrcImage, grownSrcImage;
    protected int[][] srcMatrixWithChin;
    //Boundary is filled
    protected BufferedImage sourceImageWithCurve;
    protected int[][] sourceBoundaryFilledFaceMatrix = null;
    protected BufferedImage sourceBoundaryFilledImage;
    //Next step is to warp the image. The source image should be warped according to the feature points of the target image.
    protected ImageWarper imageWarper;
    //Next step is to apply interpolation
    protected BufferedImage warpedImage;
    //origin of the warped image
    protected Point warpedOrigin;
    //warp + interpolated
    protected int[][] warpedSkinMatrix;
    //Next step is to apply color consistency to image
    protected MeanColorShifter meanColorShifter;
    protected BufferedImage colorConsistentImage;
    protected Point replacementPoint;
    protected BufferedImage blendedImage = null;
    protected Stack<Point> sourceHairSeeds;
    protected int[][] sourceHairMatrix;
    protected BufferedImage sourceHairImage;
    protected BufferedImage srcHairImgShowingRegion;
    protected BufferedImage srcHairImgAfterWarping;
    protected int[][] srcHairMatrixAfterWarping;
    protected Stack<Point> targetHairSeeds;
    protected int[][] targetHairMatrix;
    protected BufferedImage targetHairImage;
    protected String srgMode = "add";//add or remove 
    protected float srgThreshold = (float) 0.5;//0 - 1
    protected BufferedImage tarHairImgShowingRegion;
    protected BufferedImage replacedFaceWithSourceHair;
    protected BufferedImage replacedFaceWithTargetHair;
    protected BufferedImage replacedFaceImage;
    protected String whichHairStr;//it can be "source" or "target"

    public BufferedImage getBlendedImage() {
        return blendedImage;
    }

    public void setBlendedImage(BufferedImage blendedImage) {
        this.blendedImage = blendedImage;
    }

    public BufferedImage getColorConsistentImage() {
        return colorConsistentImage;
    }

    public void setColorConsistentImage(BufferedImage colorConsistentImage) {
        this.colorConsistentImage = colorConsistentImage;
    }

    public ImageWarper getImageWarper() {
        return imageWarper;
    }

    public void setImageWarper(ImageWarper imageWarper) {
        this.imageWarper = imageWarper;
    }

    public MeanColorShifter getMeanColorShifter() {
        return meanColorShifter;
    }

    public void setMeanColorShifter(MeanColorShifter meanColorShifter) {
        this.meanColorShifter = meanColorShifter;
    }

    public BufferedImage getReplacedFaceImage() {
        return replacedFaceImage;
    }

    public void setReplacedFaceImage(BufferedImage replacedFaceImage) {
        this.replacedFaceImage = replacedFaceImage;
    }

    public BufferedImage getReplacedFaceWithSourceHair() {
        return replacedFaceWithSourceHair;
    }

    public void setReplacedFaceWithSourceHair(BufferedImage replacedFaceWithSourceHair) {
        this.replacedFaceWithSourceHair = replacedFaceWithSourceHair;
    }

    public BufferedImage getReplacedFaceWithTargetHair() {
        return replacedFaceWithTargetHair;
    }

    public void setReplacedFaceWithTargetHair(BufferedImage replacedFaceWithTargetHair) {
        this.replacedFaceWithTargetHair = replacedFaceWithTargetHair;
    }

    public Point getReplacementPoint() {
        return replacementPoint;
    }

    public void setReplacementPoint(Point replacementPoint) {
        this.replacementPoint = replacementPoint;
    }

    public BufferedImage getRotatedSourceImage() {
        return rotatedSourceImage;
    }

    public void setRotatedSourceImage(BufferedImage rotatedSourceImage) {
        this.rotatedSourceImage = rotatedSourceImage;
    }

    public Point[] getRotatedSrcFP() {
        return rotatedSrcFP;
    }

    public void setRotatedSrcFP(Point[] rotatedSrcFP) {
        this.rotatedSrcFP = rotatedSrcFP;
    }

    public int[][] getSourceBoundaryFilledFaceMatrix() {
        return sourceBoundaryFilledFaceMatrix;
    }

    public void setSourceBoundaryFilledFaceMatrix(int[][] sourceBoundaryFilledFaceMatrix) {
        this.sourceBoundaryFilledFaceMatrix = sourceBoundaryFilledFaceMatrix;
    }

    public BufferedImage getSourceBoundaryFilledImage() {
        return sourceBoundaryFilledImage;
    }

    public void setSourceBoundaryFilledImage(BufferedImage sourceBoundaryFilledImage) {
        this.sourceBoundaryFilledImage = sourceBoundaryFilledImage;
    }

    public Rectangle getSourceFaceRectangle() {
        return sourceFaceRectangle;
    }

    public void setSourceFaceRectangle(Rectangle sourceFaceRectangle) {
        this.sourceFaceRectangle = sourceFaceRectangle;
    }

    public Point[] getSourceFeaturePoints() {
        return sourceFeaturePoints;
    }

    public void setSourceFeaturePoints(Point[] sourceFeaturePoints) {
        this.sourceFeaturePoints = sourceFeaturePoints;
    }

    public int[][] getSourceHairMatrix() {
        return sourceHairMatrix;
    }

    public void setSourceHairMatrix(int[][] sourceHairMatrix) {
        this.sourceHairMatrix = sourceHairMatrix;
    }

    public Stack<Point> getSourceHairSeeds() {
        return sourceHairSeeds;
    }

    public void setSourceHairSeeds(Stack<Point> sourceHairSeeds) {
        this.sourceHairSeeds = sourceHairSeeds;
    }

    public BufferedImage getSourceImage() {
        return sourceImage;
    }

    public void setSourceImage(BufferedImage sourceImage) {
        this.sourceImage = sourceImage;
        createSourceHairMatrix();
    }

    public List<Point> getSourceRectLeftEdge() {
        return sourceRectLeftEdge;
    }

    public void setSourceRectLeftEdge(List<Point> sourceRectLeftEdge) {
        this.sourceRectLeftEdge = sourceRectLeftEdge;
    }

    public List<Point> getSourceRectRightEdge() {
        return sourceRectRightEdge;
    }

    public void setSourceRectRightEdge(List<Point> sourceRectRightEdge) {
        this.sourceRectRightEdge = sourceRectRightEdge;
    }

    public Point[] getSourceRectangleFP() {
        return sourceRectangleFP;
    }

    public void setSourceRectangleFP(Point[] sourceRectangleFP) {
        this.sourceRectangleFP = sourceRectangleFP;
    }

    public BufferedImage getSourceRectangleImage() {
        return sourceRectangleImage;
    }

    public void setSourceRectangleImage(BufferedImage sourceRectangleImage) {
        this.sourceRectangleImage = sourceRectangleImage;
    }

    public BufferedImage getSourceSkinImage() {
        return sourceSkinImage;
    }

    public void setSourceSkinImage(BufferedImage sourceSkinImage) {
        this.sourceSkinImage = sourceSkinImage;
    }

    public int[][] getSourceSkinMatrix() {
        return sourceSkinMatrix;
    }

    public void setSourceSkinMatrix(int[][] sourceSkinMatrix) {
        this.sourceSkinMatrix = sourceSkinMatrix;
    }

    public Rectangle getTargetFaceRectangle() {
        return targetFaceRectangle;
    }

    public void setTargetFaceRectangle(Rectangle targetFaceRectangle) {
        this.targetFaceRectangle = targetFaceRectangle;
    }

    public Point[] getTargetFeaturePoints() {
        return targetFeaturePoints;
    }

    public void setTargetFeaturePoints(Point[] targetFeaturePoints) {
        this.targetFeaturePoints = targetFeaturePoints;
    }

    public int[][] getTargetHairMatrix() {
        return targetHairMatrix;
    }

    public void setTargetHairMatrix(int[][] targetHairMatrix) {
        this.targetHairMatrix = targetHairMatrix;
    }

    public Stack<Point> getTargetHairSeeds() {
        return targetHairSeeds;
    }

    public void setTargetHairSeeds(Stack<Point> targetHairSeeds) {
        this.targetHairSeeds = targetHairSeeds;
    }

    public BufferedImage getTargetImage() {
        return targetImage;
    }

    public void setTargetImage(BufferedImage targetImage) {
        this.targetImage = targetImage;
        createTargetHairMatrix();
    }

    public List<Point> getTargetRectLeftEdge() {
        return targetRectLeftEdge;
    }

    public void setTargetRectLeftEdge(List<Point> targetRectLeftEdge) {
        this.targetRectLeftEdge = targetRectLeftEdge;
    }

    public List<Point> getTargetRectRightEdge() {
        return targetRectRightEdge;
    }

    public void setTargetRectRightEdge(List<Point> targetRectRightEdge) {
        this.targetRectRightEdge = targetRectRightEdge;
    }

    public Point[] getTargetRectangleFP() {
        return targetRectangleFP;
    }

    public void setTargetRectangleFP(Point[] targetRectangleFP) {
        this.targetRectangleFP = targetRectangleFP;
    }

    public BufferedImage getTargetRectangleImage() {
        return targetRectangleImage;
    }

    public void setTargetRectangleImage(BufferedImage targetRectangleImage) {
        this.targetRectangleImage = targetRectangleImage;
    }

    public BufferedImage getTargetSkinImage() {
        return targetSkinImage;
    }

    public void setTargetSkinImage(BufferedImage targetSkinImage) {
        this.targetSkinImage = targetSkinImage;
    }

    public int[][] getTargetSkinMatrix() {
        return targetSkinMatrix;
    }

    public void setTargetSkinMatrix(int[][] targetSkinMatrix) {
        this.targetSkinMatrix = targetSkinMatrix;
    }

    public BufferedImage getWarpedImage() {
        return warpedImage;
    }

    public void setWarpedImage(BufferedImage warpedImage) {
        this.warpedImage = warpedImage;
    }

    public Point getWarpedOrigin() {
        return warpedOrigin;
    }

    public void setWarpedOrigin(Point warpedOrigin) {
        this.warpedOrigin = warpedOrigin;
    }

    public int[][] getWarpedSkinMatrix() {
        return warpedSkinMatrix;
    }

    public void setWarpedSkinMatrix(int[][] warpedSkinMatrix) {
        this.warpedSkinMatrix = warpedSkinMatrix;
    }

    public int[][] getGrownMatrix() {
        return grownMatrix;
    }

    public void setGrownMatrix(int[][] grownMatrix) {
        this.grownMatrix = grownMatrix;
    }

    public BufferedImage getGrownSrcImage() {
        return grownSrcImage;
    }

    public void setGrownSrcImage(BufferedImage grownSrcImage) {
        this.grownSrcImage = grownSrcImage;
    }

    public int[][] getShrunkMatrix() {
        return shrunkMatrix;
    }

    public void setShrunkMatrix(int[][] shrunkMatrix) {
        this.shrunkMatrix = shrunkMatrix;
    }

    public BufferedImage getShrunkSrcImage() {
        return shrunkSrcImage;
    }

    public void setShrunkSrcImage(BufferedImage shrunkSrcImage) {
        this.shrunkSrcImage = shrunkSrcImage;
    }

    public BufferedImage getSourceImageWithCurve() {
        return sourceImageWithCurve;
    }

    public void setSourceImageWithCurve(BufferedImage sourceImageWithCurve) {
        this.sourceImageWithCurve = sourceImageWithCurve;
    }

    public Point getRotatedRectXY() {
        return rotatedRectXY;
    }

    public void setRotatedRectXY(Point rotatedRectXY) {
        this.rotatedRectXY = rotatedRectXY;
    }

    public int[][] getSrcMatrixWithChin() {
        return srcMatrixWithChin;
    }

    public void setSrcMatrixWithChin(int[][] srcMatrixWithChin) {
        this.srcMatrixWithChin = srcMatrixWithChin;
    }

    public BufferedImage getSrcRectImgWithFP() {
        return srcRectImgWithFP;
    }

    public void setSrcRectImgWithFP(BufferedImage srcRectImgWithFP) {
        this.srcRectImgWithFP = srcRectImgWithFP;
    }

    public BufferedImage getTargetHairImage() {
        return targetHairImage;
    }

    public void setTargetHairImage(BufferedImage targetHairImage) {
        this.targetHairImage = targetHairImage;
    }

    public BufferedImage getSourceHairImage() {
        return sourceHairImage;
    }

    public void setSourceHairImage(BufferedImage sourceHairImage) {
        this.sourceHairImage = sourceHairImage;
    }

    public BufferedImage getSrcHairImgShowingRegion() {
        return srcHairImgShowingRegion;
    }

    public void setSrcHairImgShowingRegion(BufferedImage srcHairImgShowingRegion) {
        this.srcHairImgShowingRegion = srcHairImgShowingRegion;
    }

    public BufferedImage getTarHairImgShowingRegion() {
        return tarHairImgShowingRegion;
    }

    public void setTarHairImgShowingRegion(BufferedImage tarHairImgShowingRegion) {
        this.tarHairImgShowingRegion = tarHairImgShowingRegion;
    }

    public int getOriginIndex() {
        return originIndex;
    }

    public void setOriginIndex(int originIndex) {
        this.originIndex = originIndex;
    }

    public BufferedImage getSrcHairImgAfterWarping() {
        return srcHairImgAfterWarping;
    }

    public void setSrcHairImgAfterWarping(BufferedImage srcHairImgAfterWarping) {
        this.srcHairImgAfterWarping = srcHairImgAfterWarping;
    }

    public int[][] getSrcHairMatrixAfterWarping() {
        return srcHairMatrixAfterWarping;
    }

    public void setSrcHairMatrixAfterWarping(int[][] srcHairMatrixAfterWarping) {
        this.srcHairMatrixAfterWarping = srcHairMatrixAfterWarping;
    }

    public String getWhichHairStr() {
        return whichHairStr;
    }

    public void setWhichHairStr(String whichHairStr) {
        this.whichHairStr = whichHairStr;
    }

    public String getSrgMode() {
        return srgMode;
    }

    public void setSrgMode(String srgMode) {
        if (srgMode.toLowerCase().compareTo("add") == 0 || srgMode.toLowerCase().compareTo("remove") == 0) {
            this.srgMode = srgMode;
        }
    }

    public float getSrgThreshold() {
        return srgThreshold;
    }

    public void setSrgThreshold(float srgThreshold) {
        if (srgThreshold >= 0 && srgThreshold <= 1) {
            this.srgThreshold = srgThreshold;
        }
    }

    //Other initialization functions
    public void createSourceHairMatrix() {
        sourceHairMatrix = new int[sourceImage.getWidth()][sourceImage.getHeight()];
        for (int x = 0; x < sourceImage.getWidth(); x++) {
            sourceHairMatrix[x] = new int[sourceImage.getHeight()];
            for (int y = 0; y < sourceImage.getHeight(); y++) {
                sourceHairMatrix[x][y] = 0;
            }
        }
    }

    public void createTargetHairMatrix() {
        targetHairMatrix = new int[targetImage.getWidth()][targetImage.getHeight()];
        for (int x = 0; x < targetImage.getWidth(); x++) {
            targetHairMatrix[x] = new int[targetImage.getHeight()];
            for (int y = 0; y < targetImage.getHeight(); y++) {
                targetHairMatrix[x][y] = 0;
            }
        }
    }
}
