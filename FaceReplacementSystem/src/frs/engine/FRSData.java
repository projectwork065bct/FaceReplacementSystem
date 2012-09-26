/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.engine;

import frs.algorithms.ImageWarper;
import frs.algorithms.MeanColorShifter;
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

    protected int originIndex;
    protected BufferedImage srcImg, tarImg;//Original images loaded by the user
    protected Point[] srcFP, tarFP;//Feature Points (top left corner of the images are taken as the origins)
    protected BufferedImage rotatedSrcImg;//Image obtained after rotating the source image according to eye axis
    protected Point[] rotatedSrcFP;//Feature points of the rotated source image
    protected Rectangle srcFaceRect, tarFaceRect;//Rectangles drawn around the face of both the images
    protected BufferedImage srcRectImg, tarRectImg;//Images of rectangles drawn around the faces
    protected Point srcRectFP[], tarRectFP[];//feature points with reference to origin of the that the user has drawn rectangles
    protected BufferedImage srcRectImgWithFP;//Image showing feature points of source
    protected List<Point> srcRectLeftEdge = null, srcRectRightEdge = null, tarRectLeftEdge = null, tarRectRightEdge = null;//Edges after chin curves are detected
    protected BufferedImage srcSkinImg, tarSkinImg;//Image which shows skin inside the rectangle drawn around the images
    protected int[][] srcSkinMatrix, tarSkinMatrix;//Matrix with skin pixels set to 1 and non-skin pixels set to 0
    protected int[][] shrunkMatrix, grownMatrix;
    protected BufferedImage shrunkSrcImage, grownSrcImage;
    protected int[][] srcMatrixWithChinCurve;//The chin curve pixels are set to 1
    protected BufferedImage srcImgWithCurve;
    protected List<Point> srcRightCurve, srcLeftCurve;
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
    protected String whichHairStr="target";//it can be "source" or "target"

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

    public BufferedImage getRotatedSrcImg() {
        return rotatedSrcImg;
    }

    public void setRotatedSrcImg(BufferedImage rotatedSrcImg) {
        this.rotatedSrcImg = rotatedSrcImg;
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
        return srcFaceRect;
    }

    public void setSourceFaceRectangle(Rectangle sourceFaceRectangle) {
        this.srcFaceRect = sourceFaceRectangle;
    }

    public Point[] getSrcFP() {
        return srcFP;
    }

    public void setSrcFP(Point[] srcFP) {
        this.srcFP = srcFP;
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

    public BufferedImage getSrcImg() {
        return srcImg;
    }

    public void setSrcImg(BufferedImage srcImg) {
        this.srcImg = srcImg;
        createSourceHairMatrix();
    }

    public List<Point> getSourceRectLeftEdge() {
        return srcRectLeftEdge;
    }

    public void setSourceRectLeftEdge(List<Point> sourceRectLeftEdge) {
        this.srcRectLeftEdge = sourceRectLeftEdge;
    }

    public List<Point> getSourceRectRightEdge() {
        return srcRectRightEdge;
    }

    public void setSourceRectRightEdge(List<Point> sourceRectRightEdge) {
        this.srcRectRightEdge = sourceRectRightEdge;
    }

    public Point[] getSrcRectFP() {
        return srcRectFP;
    }

    public void setSrcRectFP(Point[] srcRectFP) {
        this.srcRectFP = srcRectFP;
    }

    public BufferedImage getSrcRectImg() {
        return srcRectImg;
    }

    public void setSrcRectImg(BufferedImage srcRectImg) {
        this.srcRectImg = srcRectImg;
    }

    public BufferedImage getSourceSkinImage() {
        return srcSkinImg;
    }

    public void setSourceSkinImage(BufferedImage sourceSkinImage) {
        this.srcSkinImg = sourceSkinImage;
    }

    public int[][] getSourceSkinMatrix() {
        return srcSkinMatrix;
    }

    public void setSourceSkinMatrix(int[][] sourceSkinMatrix) {
        this.srcSkinMatrix = sourceSkinMatrix;
    }

    public Rectangle getTarFaceRect() {
        return tarFaceRect;
    }

    public void setTarFaceRect(Rectangle tarFaceRect) {
        this.tarFaceRect = tarFaceRect;
    }

    public Point[] getTarFP() {
        return tarFP;
    }

    public void setTarFP(Point[] tarFP) {
        this.tarFP = tarFP;
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
        return tarImg;
    }

    public void setTargetImage(BufferedImage targetImage) {
        this.tarImg = targetImage;
        createTargetHairMatrix();
    }

    public List<Point> getTargetRectLeftEdge() {
        return tarRectLeftEdge;
    }

    public void setTargetRectLeftEdge(List<Point> targetRectLeftEdge) {
        this.tarRectLeftEdge = targetRectLeftEdge;
    }

    public List<Point> getTargetRectRightEdge() {
        return tarRectRightEdge;
    }

    public void setTargetRectRightEdge(List<Point> targetRectRightEdge) {
        this.tarRectRightEdge = targetRectRightEdge;
    }

    public Point[] getTargetRectangleFP() {
        return tarRectFP;
    }

    public void setTargetRectangleFP(Point[] targetRectangleFP) {
        this.tarRectFP = targetRectangleFP;
    }

    public BufferedImage getTarRectImg() {
        return tarRectImg;
    }

    public void setTarRectImg(BufferedImage tarRectImg) {
        this.tarRectImg = tarRectImg;
    }

    public BufferedImage getTargetSkinImage() {
        return tarSkinImg;
    }

    public void setTargetSkinImage(BufferedImage targetSkinImage) {
        this.tarSkinImg = targetSkinImage;
    }

    public int[][] getTarSkinMatrix() {
        return tarSkinMatrix;
    }

    public void setTarSkinMatrix(int[][] tarSkinMatrix) {
        this.tarSkinMatrix = tarSkinMatrix;
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
        return srcImgWithCurve;
    }

    public void setSourceImageWithCurve(BufferedImage sourceImageWithCurve) {
        this.srcImgWithCurve = sourceImageWithCurve;
    }

    public int[][] getSrcMatrixWithChinCurve() {
        return srcMatrixWithChinCurve;
    }

    public void setSrcMatrixWithChinCurve(int[][] srcMatrixWithChinCurve) {
        this.srcMatrixWithChinCurve = srcMatrixWithChinCurve;
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
        sourceHairMatrix = new int[srcImg.getWidth()][srcImg.getHeight()];
        for (int x = 0; x < srcImg.getWidth(); x++) {
            sourceHairMatrix[x] = new int[srcImg.getHeight()];
            for (int y = 0; y < srcImg.getHeight(); y++) {
                sourceHairMatrix[x][y] = 0;
            }
        }
    }

    public void createTargetHairMatrix() {
        targetHairMatrix = new int[tarImg.getWidth()][tarImg.getHeight()];
        for (int x = 0; x < tarImg.getWidth(); x++) {
            targetHairMatrix[x] = new int[tarImg.getHeight()];
            for (int y = 0; y < tarImg.getHeight(); y++) {
                targetHairMatrix[x][y] = 0;
            }
        }
    }
}
