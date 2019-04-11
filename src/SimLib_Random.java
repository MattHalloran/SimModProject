import java.util.Random;

/**
 * Holds all of the methods for generating 
 * random numbers
 * 
 * @author Matt Halloran, Troy Pastirko, Ryan Ciocci
 * @version 3-23-19
 */

public class SimLib_Random
{
	private static Random ran = new Random();
    
    /**
     * Uses a normal distribution with mean 0 and sd 1 to return a random number 
     * @return
     */
    public static double Uniform()
    {
    	double v1, v2, s;
        do {
          v1 = 2 * ran.nextDouble() - 1;   // between -1.0 and 1.0
          v2 = 2 * ran.nextDouble() - 1;   // between -1.0 and 1.0
          s = v1 * v1 + v2 * v2;
        } while (s >= 1 || s == 0);
        return v1 * Math.sqrt(-2 * Math.log(s)/s);
    }
    
    /**
     * Uses a normal distribution to return a random number
     * @param mean
     * @param sd
     * @return
     */
    public static double Uniform(double mean, double sd)
    {
    	return sd * Uniform() + mean;
    } 
    
    /**
     * Uses an exponential distribution to return a random number
     * @param mean
     * @return
     */
    public static double Expon(double mean)
    {
    	return -mean * Math.log(1 - ran.nextDouble());
    }
    
	/**
	 * 
	 * @param m
	 * @param mean
	 * @return a double with an observation from an m-Erlang distribution
	 */
	public static double Erlang(int m, double mean) 
	{
		int   i;
	    double mean_exponential, sum;
	
	    mean_exponential = mean / m;
	    sum = 0.0;
	    for (i = 1; i <= m; ++i)
	        sum += Expon(mean_exponential);
	    return sum;
	}
	
	public static <T> T RandomValue(T[] array)
	{
	    return array[ran.nextInt(array.length)];
	}
}
