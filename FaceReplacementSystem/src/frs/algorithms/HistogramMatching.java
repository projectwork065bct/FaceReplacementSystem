//this file shows how to implement histogram class
package frs.algorithms;

import java.awt.Color;
import java.awt.image.BufferedImage;


public class HistogramMatching {

    /**
     * @param args the command line arguments
     */
    
    public static BufferedImage changeColorToTarget(BufferedImage source, BufferedImage target){
        histogram h = new histogram();
        ToHistogram hi = new ToHistogram();
        BufferedImage result = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        
        int[][] sourceHis=hi.convert(source);
        int[][] targetHis=hi.convert(target);
        int [][] resultHis = new int[3][];
        for(int i=0;i<resultHis.length;i++){
            resultHis[i] = new int[256];
        }
        
        resultHis[0] = h.matchedHistogram(sourceHis[0], targetHis[0]);
        resultHis[1] = h.matchedHistogram(sourceHis[1], targetHis[1]);
        resultHis[2] = h.matchedHistogram(sourceHis[2], targetHis[2]);
        
        Color c1,c2;
        for(int i=0;i<source.getWidth();i++){
            for(int j=0;j<source.getHeight();j++){
                
                c1 = new Color(source.getRGB(i, j),true);
                if(c1.getAlpha()>150){
                //System.out.println(resultHis[1][c1.getGreen()]+" "+resultHis[2][c1.getBlue()]);
                c2 = new Color(resultHis[0][c1.getRed()],resultHis[1][c1.getGreen()] ,resultHis[2][c1.getBlue()], c1.getAlpha() );
                result.setRGB(i, j, c2.getRGB());
                }
            }
        }
        return result;
//        try {
//            System.out.println(ImageIO.write(s, "jpg", new File("C:\\Users\\ramsharan\\Desktop\\Major\\imagesForMajor\\rectangle\\result2afterhistogrammatching.jpg")));
//        } catch (IOException ex) {
//            Logger.getLogger(HistogramMatching.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
//    public static void main(String[] args) {
//        // TODO code application logic here
//        
//        
//        BufferedImage s=null;
//        try {
//             s = ImageIO.read(new File("C:\\Users\\ramsharan\\Desktop\\Major\\imagesForMajor\\rectangle\\boy.png"));
//        } catch (IOException ex) {
//            Logger.getLogger(HistogramMatching.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        BufferedImage t=null;
//        try {
//             t = ImageIO.read(new File("C:\\Users\\ramsharan\\Desktop\\Major\\imagesForMajor\\rectangle\\bo1.png"));
//        } catch (IOException ex) {
//            Logger.getLogger(HistogramMatching.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        BufferedImage r = fun(t, s);
//        try {
//            ImageIO.write(r, "png", new File("C:\\Users\\ramsharan\\Desktop\\Major\\imagesForMajor\\rectangle\\result3afterhistogrammatching.jpg"));
//        } catch (IOException ex) {
//            Logger.getLogger(HistogramMatching.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
