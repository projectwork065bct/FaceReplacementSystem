package Helpers;


import java.util.ArrayList;
import java.util.List;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ramsharan
 */
public class Statistics_1 {
    /** it contains the frequency of corresponding value*/
    private int[] frequency;
    /**it contains the total frequency*/
    private int totalFrequency;
    /** it sets the frequency of corresponding value
     *it takes integer array as input
     *the array contains the frequency of corresponding value 
     */
    public void setFrequency(int[] frequency){
        this.frequency=frequency;
        totalFrequency=0;
        //calculating the total frequency
        for(int i=0;i<frequency.length;i++)
            totalFrequency+=frequency[i];
    }
    /**it provides mean of given data
     */
    public float getMean(){
        float mean;
        int summation=0;
        for(int i=0;i<frequency.length;i++){
            if(frequency[i]!=0){
                summation+=i*frequency[i];
            }
        }
        mean=(float)summation/totalFrequency;
        return mean;
    }
    
    /**it provides the median of given data*/
    public float getMedian(){
        float median;
        //it contains the cumulative frequency of the given data
        int[] cf=new int[frequency.length];
        //calculating cumulative frequency
        cf[0]=frequency[0];
        for(int i=1;i<frequency.length;i++){
            cf[i]=cf[i-1]+frequency[i];
        }
        if((totalFrequency%2)!=0){
            median=getValueAt((totalFrequency+1)/2,cf);
        }
        else {
            median=(float) ((getValueAt(totalFrequency/2, cf)+getValueAt(totalFrequency/2+1, cf))/2.0);
        }
        
        return median;
    }
    /**it provides the value at given position using the cumulative frequency*/
    private int getValueAt(int n, int[] cf) {
        int i;
        for(i=0;i<cf.length;i++){
            if(n<=cf[i]) break;
        }
        return i;
        
    }
    
    /**it provides mode of given data
     *the mode can be one or more
     * i.e. the mode is not unique
     */
    public List<Integer> getMode(){
        int highestFrequency=0;
        //calculating highest frequency
        for(int i=0;i<frequency.length;i++){
            if(highestFrequency<frequency[i]) highestFrequency=frequency[i];
        }
        List<Integer> mode = new ArrayList();
        for(int i=0;i<frequency.length;i++){
            if(highestFrequency==frequency[i]) mode.add(i);
        }
        return mode;
    }
    
    /**it provides standard deviation of given data*/
    public float getStandardDeviation(){
        //it contains the value of standard deviation
        float sd;
        float summation=0;
        float mean=getMean();
        for(int i=0;i<frequency.length;i++){
            if(frequency[i]!=0) summation+=frequency[i]*Math.pow(i-mean, 2);
        }
        sd=(float) Math.sqrt(summation/totalFrequency);
        return sd;
    }
    
}
