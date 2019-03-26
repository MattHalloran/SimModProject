/**
 * A simulator for a multi-queue banking system
 * @author Matt Halloran
 */
public class Bank extends SimulationBase
{

	@Override
	protected void Initialize() 
	{
		numTellers = minTellers;
    	SimData.Initialize(maxTellers, numTellers);
	}

	@Override
	protected void DisplayStartingData() 
	{
        System.out.printf("Multiteller bank with separate queues & jockeying\n\n");
        System.out.printf("Number of tellers%16d to%3d\n\n",minTellers, maxTellers );
        System.out.printf("Mean interarrival time%11.3f minutes\n\n", meanInterArrival);
        System.out.printf("Mean service time%16.3f minutes\n\n", meanService);
        System.out.printf("Bank closes after%16.3f hours\n\n\n\n", lengthDoorsOpen);
	}

	@Override
	protected void Arrive(double time) 
	{
		Customer arrivingCustomer = new Customer(time, SimData.GetShortestLine());
		SimData.InsertInQueue(arrivingCustomer, SimData.Order.LAST, arrivingCustomer.GetTeller());
		if(SimData.GetQueueSize(arrivingCustomer.GetTeller()) > 1)
		{
			nCustsDelayed++;
		}
	}

	@Override
	protected void Depart(double time) 
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

	@Override
	protected void Report() 
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
        System.out.printf("Time simulation ended%12.3f minutes\n", SimData.GetSimTime());
	}
}
