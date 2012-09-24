
package frs.algorithms;


public class histogram {
    
    //it returns map of svalue and tvalue
    //shistogram=>source histogram
    //thistogram=>target histogram
    public int[] matchedHistogram(int[] shistogram, int[] thistogram){
        float[] nscf=getNormalizedCF(shistogram);
        float[] ntcf=getNormalizedCF(thistogram);
        int[] mhistogram = new int[shistogram.length];
        float tempncf;
        int j=0;
        for(int i=0;i<shistogram.length;i++){
            if(shistogram[i]>0){
                tempncf = nscf[i];
                while(true){
                    if(tempncf<=ntcf[j]){
                        mhistogram[i]=j;
                        break;
                    }
                    else j++;
                }
            }
            else {
                mhistogram[i]=-1;
            }
        }
        return mhistogram;
    }
    
    //it returns normalized cummulative frequency
    public float[] getNormalizedCF(int[] hist){
        int[] cf = getCF(hist);
        float[] ncf = new float[cf.length];
        float largest = (float)cf[cf.length-1];
        //calculating normalized cummulative frequency
        for(int i=0;i<cf.length;i++){
            ncf[i]=cf[i]/largest;
        }
        return ncf;
    }
    
    //it returns cummulative frequency
    public int[] getCF(int[] hist){
        int[] cf = new int[hist.length];
        //calculating cummulative frequency
        cf[0]=hist[0];
        for(int i=1;i<cf.length;i++){
            cf[i]=cf[i-1]+hist[i];
        }
        return cf;
    }
    
}
