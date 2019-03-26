/**
 * A simulator for a single-server queuing system
 * @author Matt Halloran
 */
import java.util.ArrayList;

public class MM1
{
    static int   shortestLength, shortestQueue, maxNumCustomers;
    static double lengthDoorsOpen;

    static double meanInterArrival, meanService;

    static int totalCustomers;
    static int nCustsDelayed;
    static double areaNumInQ;
    static double areaServerStatus;
    static double totalOfDelays;
    static int eventCount;

    /**
     * Main
     * 
     * 1) The first arival event is created in Initialize
     * 2) The FIFO queue timeArrival holds "arrival times" that are waiting for server
     */
    public static void main(String[] args) 
    {
        meanInterArrival = 3.0;
        meanService = 4.5;
        lengthDoorsOpen = 8.0;    // in hours
        maxNumCustomers = 1000;

        // Write report heading and input parameters. 
        System.out.printf("Single-server queueing system\n\n");
        System.out.printf("Mean interarrival time%11.3f minutes\n\n", meanInterArrival);
        System.out.printf("Mean service time%16.3f minutes\n\n", meanService);
        System.out.printf("Number of customers%14d\n\n", maxNumCustomers);

        Initialize();
        
        SimData.Initialize(1,1);
        SimData.SetClosingTime(60 * lengthDoorsOpen);
        
        double nextArrivalTime = SimLib_Random.Expon(meanInterArrival),
        		nextDepartureTime = SimLib_Random.Expon(meanService);
        // Run the simulation while more delays are still needed 
        while(totalCustomers < maxNumCustomers && SimData.StoreOpen() || SimData.CustomersInStore() > 0)
        {
        	System.out.println(SimData.GetSimTime());
        	//New customers only enter when the store is still open
        	if(SimData.CustomersInStore() > 0 && (!SimData.StoreOpen() || nextDepartureTime < nextArrivalTime))
        	{
        		nextDepartureTime += SimLib_Random.Expon(meanService);
        		Depart(nextDepartureTime);
        	}
        	else if(nextArrivalTime < nextDepartureTime)
        	{
        		nextArrivalTime += SimLib_Random.Expon(meanInterArrival);
        		Arrive(nextArrivalTime);
        		totalCustomers++;
        	}
        	else if(nextDepartureTime <= nextArrivalTime)
        		nextDepartureTime += nextDepartureTime += SimLib_Random.Expon(meanService);
        	else
        		nextArrivalTime += SimLib_Random.Expon(meanInterArrival);
        		
        	areaNumInQ += SimData.CustomersInStore();
        	areaServerStatus += SimData.CurrentServerUtilization();
        	eventCount++;
        }
        Report();
    }

    /**
     * 
     */
    static void Initialize()
    {
    	totalCustomers = 0;
    	nCustsDelayed = 0;
    	areaNumInQ = 0;
    	areaServerStatus = 0;
    	totalOfDelays = 0;
    	eventCount = 0;
    }
    
    /**
     * Updates queues and statistics for a customer arriving
     */
    private static void Arrive(double time)
    {
    	Customer arrivingCustomer = new Customer(time, 0);
		SimData.InsertInQueue(arrivingCustomer, SimData.Order.LAST, 0);
		if(SimData.GetQueueSize(0) > 1)
		{
			nCustsDelayed++;
		}
    }
    
    /**
     * Updates queues and statistics for a customer leaving
     */
    private static void Depart(double time)
    {
		Customer leavingCustomer = SimData.RemoveFromQueue(SimData.Order.FIRST, 0, time);
		totalOfDelays += leavingCustomer.getTimeLeft() - leavingCustomer.GetTimeArrived();
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
    	if(eventCount > 0)
    	{
    		averageNum = areaNumInQ / eventCount;
    		serverUtilization = areaServerStatus / eventCount;
    	}
    	System.out.printf("\n\nAverage delay in queue%11.3f minutes\n\n", averageDelay);
        System.out.printf("Average number in queue%10.3f\n\n", averageNum);
        System.out.printf("Server utilization%15.3f\n\n", serverUtilization);
        System.out.printf("Time simulation ended%12.3f minutes", SimData.GetSimTime());
    }
}

