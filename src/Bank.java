/**
 * 
 */

import java.util.ArrayList;

public class Bank
{
//	private static String inFileLocation = "./InFiles/Bank.in",
//						  outFileLocation = "./OutFiles/Bank.out";
    static ArrayList<Double> timeArrival;

    /* Declare non-simlib global variables. */

    static int   minTellers, maxTellers, numTellers, shortestLength, shortestQueue;
    static double lengthDoorsOpen;

    static final int IDLE = 0,
    				 BUSY = 1,
    				 STREAM_INTERARRIVAL = 1,
    				 STREAM_SERVICE = 2;

    static double meanInterArrival, meanService;
    static int nDelaysRequired;

    static int nEvents;
    static int serverStatus;
    static int numInQ;
    static double timeLastEvent;
    static int nCustsDelayed;
    static double areaNumInQ;
    static double areaServerStatus;
    static double[] timeNextEvent = new double[3];
    static double totalOfDelays;

    /**
     * Main
     * 
     * 1) The first arival event is created in Initialize
     * 2) The FIFO queue timeArrival holds "arrival times" that are waiting for server
     */
    public static void main(String[] args) 
    {
        nEvents = 2;
        
        minTellers = 5;
        maxTellers = 5;
        meanInterArrival = 1.0;
        meanService = 4.5;
        lengthDoorsOpen = 8.0;    // in hours
        
        numTellers = minTellers;
        
    	SimData.Initialize(maxTellers, numTellers);
        SimData.SetClosingTime(60 * lengthDoorsOpen);

        // Write report heading and input parameters. 
        System.out.printf("Multiteller bank with separate queues & jockeying\n\n");
        System.out.printf("Number of tellers%16d to%3d\n\n",minTellers, maxTellers );
        System.out.printf("Mean interarrival time%11.3f minutes\n\n", meanInterArrival);
        System.out.printf("Mean service time%16.3f minutes\n\n", meanService);
        System.out.printf("Bank closes after%16.3f hours\n\n\n\n", lengthDoorsOpen);

        double nextArrivalTime = Integer.MIN_VALUE, nextDepartureTime = -1;
        // Run the simulation while more delays are still needed 
        while(SimData.StoreOpen() || SimData.customersInStore() > 0)
        {
        	System.out.println(SimData.GetSimTime());
        	
        	//New customers only enter when the store is still open
        	if(SimData.StoreOpen() && nextArrivalTime < nextDepartureTime)
        	{
        		nextArrivalTime = SimData.GetSimTime() + SimLib_Random.Expon(meanInterArrival, STREAM_INTERARRIVAL);
        		Arrive(nextArrivalTime);
        	}
        	else if(SimData.customersInStore() > 0)
        	{
        		nextDepartureTime = SimData.GetSimTime() + SimLib_Random.Expon(meanService, STREAM_SERVICE);
        		Depart(nextDepartureTime);
        	}
        }
        Report();
    }

    /**
     * Updates queues and statistics for an event arrival
     */
    private static void Arrive(double time)
    {   
		Customer arrivingCustomer = new Customer(time, SimData.GetShortestLine());
		SimData.InsertInQueue(arrivingCustomer, SimData.Order.LAST, arrivingCustomer.GetTeller());
		if(SimData.GetQueueSize(arrivingCustomer.GetTeller()) > 1)
		{
			nCustsDelayed++;
		}
    }

    private static void Depart(double time)
    {   
		int finishingTeller = SimData.GetDepartingQueue();
		Customer leavingCustomer = SimData.RemoveFromQueue(SimData.Order.FIRST, finishingTeller, time);
		totalOfDelays += leavingCustomer.getTimeLeft() - leavingCustomer.GetTimeArrived();
		
        // Let a customer from the end of another queue jockey to the end of this queue, if possible. 
        Jockey(leavingCustomer.GetTeller());
    }

    /**
     * Jockey customers to the end of queue "teller" from the end of another queue, if possible.
     *  
     */
    private static void Jockey(int teller)
    {
    	//Only jockeys if the difference in queue size is at least this
    	int minDifference = 2;
    	
    	boolean done = false;
    	while(!done)
    	{
    		int shorterQueueSize = SimData.GetQueueSize(teller);
    		int longestQueueIndex = SimData.GetLongestLine();
    		int longestQueueSize = SimData.GetQueueSize(longestQueueIndex);
    		if(longestQueueSize - shorterQueueSize >= minDifference)
    			SimData.JockeyCustomer(longestQueueIndex, teller);
    		else
    			done = true;
    	}
    }
    
    /**
     * Displays simulation data
     */
    private static void Report()
    {
    	System.out.println("Finished the simulation");
    	double averageDelay = 0, averageNum = 0, serverUtilization = 0;
    	if(nCustsDelayed != 0)
    		averageDelay = totalOfDelays / nCustsDelayed;
    	if(SimData.GetSimTime() != 0)
    	{
    		averageNum = areaNumInQ / SimData.GetSimTime();
    		serverUtilization = areaServerStatus / SimData.GetSimTime();
    	}
    	System.out.printf("\n\nAverage delay in queue%11.3f minutes\n\n", averageDelay);
        System.out.printf("Average number in queue%10.3f\n\n", averageNum);
        System.out.printf("Server utilization%15.3f\n\n", serverUtilization);
        System.out.printf("Time simulation ended%12.3f minutes", SimData.GetSimTime());
    }
}
