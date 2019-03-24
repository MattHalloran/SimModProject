
/**
 * Holds all of the methods for generating 
 * random numbers
 * 
 * @author Matt Halloran
 * @version 3-23-19
 */

public class SimLib_Random
{
    static long[] random_numbers = {1, 1973272912, 281629770,  20006270,1280689831,2096730329,1933576050,
            913566091, 246780520,1363774876, 604901985,1511192140,1259851944,
            824064364, 150493284, 242708531,  75253171,1964472944,1202299975,
            233217322,1911216000, 726370533, 403498145, 993232223,1103205531,
            762430696,1922803170,1385516923,  76271663, 413682397, 726466604,
            336157058,1432650381,1120463904, 595778810, 877722890,1046574445,
            68911991,2088367019, 748545416, 622401386,2122378830, 640690903,
            1774806513,2132545692,2079249579,  78130110, 852776735,1187867272,
            1351423507,1645973084,1997049139, 922510944,2045512870, 898585771,
            243649545,1004818771, 773686062, 403188473, 372279877,1901633463,
            498067494,2087759558, 493157915, 597104727,1530940798,1814496276,
            536444882,1663153658, 855503735,  67784357,1432404475, 619691088,
            119025595, 880802310, 176192644,1116780070, 277854671,1366580350,
            1142483975,2026948561,1053920743, 786262391,1792203830,1494667770,
            1923011392,1433700034,1244184613,1147297105, 539712780,1545929719,
            190641742,1645390429, 264907697, 620389253,1502074852, 927711160,
            364849192,2049576050, 638580085, 547070247 };
    /**
	 * 
	 * @param a
	 * @param b
	 * @param index
	 * @return a double with an observation from a (continuous) uniform 
	 * distribution between a and b (both double arguments). As before, 
	 * stream is an int between 1 and 100 giving the random-number index to be used.
	 */
    public static double Uniform(double a, double b, int stream)
    {
        return a + lcgrand(stream) * (b - a);
    } 

	/**
	 * 
	 * @param mean
	 * @param index user-specified random-number stream number,
	 * @return a double with an observation from an exponential distribution with mean
	 *“mean” (a double argument).
	 */
    public static double Expon(double mean, int stream) // Exponential variate generation function. 
    {
        return -mean * Math.log(lcgrand(stream));

    }

    /* Generate the next random number. */
    /**
	 * random-number generator
	 * @param index
	 * @return a double with an observation from a (continuous) 
	 * uniform distribution between 0 and 1, using stream “stream” (an int argument).
	 */
    public static double lcgrand(int stream)
    {
        final long MODLUS = 2147483647;
        final long MULT1  = 24112;
        final long MULT2  = 26143;

        // Bit operators
        //
        // << - left shift  ex. to shift 1 n bits to the left 1 << n, i.e. 1 << 4  turns 0001 into 0001 0000
        // >> - right shift ex. to shift 16 n bits to right 16 >> n, i.e. 16 >> 4 turns 0001 0000 to 0001
        // &  - bitwise and ex. 5 & 3 is equivalent to 0101 & 0011 which results in 0001 = 1
        // ^  - bitwise or  ex. 5 | 3 is equivalent to 0101 | 0011 which results in 0111 = 7
        // ~  - unary operator does bit flip not ex. ~5 = ~(101) = (010) = 2

        long zi, lowprd, hi31;

        zi     = random_numbers[stream];
        lowprd = (zi & 65535) * MULT1;
        hi31   = (zi >> 16) * MULT1 + (lowprd >> 16);
        zi     = ((lowprd & 65535) - MODLUS) + ((hi31 & 32767) << 16) + (hi31 >> 15);

        if (zi < 0)
        {
            zi += MODLUS;
        }

        lowprd = (zi & 65535) * MULT2;
        hi31   = (zi >> 16) * MULT2 + (lowprd >> 16);
        zi     = ((lowprd & 65535) - MODLUS) + ((hi31 & 32767) << 16) + (hi31 >> 15);

        if (zi < 0) 
        {
            zi += MODLUS;
        }

        random_numbers[stream] = zi;
        return (zi >> 7 | 1) / 16777216.0;
    }

    /**
	 * “sets” the random-number seed for stream “stream” to the int argument zset.
	 * @param zset
	 * @param stream
	 */
    public static void lcgrandst (long zset, int stream) // Set the current zrng for stream "stream" to zset. 
    {
        random_numbers[stream] = zset;
    }

    /**
	 * could be used to restart a subsequent simulation (using lcgrandst) 
	 * from where the current one left off, as far as random-number usage is concerned.
	 * @param index
	 * @return an int with the current underlying integer for the 
	 * random-number generator for index “index”;
	 */
    public static long lcgrandgt (int stream) // Return the current zrng for stream "stream". 
    {
        return random_numbers[stream];
    }
    
	/**
	 * 
	 * @param m
	 * @param mean
	 * @param index
	 * @return a double with an observation from an m-Erlang distribution 
	 * with mean “mean” using random-number index "index"
	 */
	private double Erlang(int m, double mean, int index) 
	{
		int   i;
	    double mean_exponential, sum;
	
	    mean_exponential = mean / m;
	    sum = 0.0;
	    for (i = 1; i <= m; ++i)
	        sum += Expon(mean_exponential, index);
	    return sum;
	}
	
	/**
	 * 
	 * @param prob_distrib
	 * @param index
	 * @return
	 */
	private int RandomInteger(float prob_distrib[], int index) 
	{
	    int   i;
	    double u = lcgrand(index);
	
	    for (i = 0; u > prob_distrib[i]; i++)
	        ;
	    return i;
	}

}
