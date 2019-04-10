/**
 * A simulator for a multi-queue banking system
 * @author Matt Halloran, Troy Pastirko, Ryan Ciocci
 */
public class Bank extends SimulationBase
{
	/**
	 * The jockeying method used in the simulation
	 * LARGEST - Last customer from largest line moves in, if that line is larger than 
	 *
	 */
	public static enum JockeyType {LARGEST, LEFT, RIGHT};
	
	/**
	 * Determines what type of jockeying method to use
	 */
	public JockeyType typeOfJockey;
	
	public JockeyType GetJockeyType()
	{
		return typeOfJockey;
	}
	
	public void SetJockeyType(JockeyType type)
	{
		typeOfJockey = type;
	}
	
	@Override
	protected void Initialize() 
	{
		data = new SimData(numTellers);
	}

	@Override
	protected void DisplayStartingData() 
	{
        System.out.printf("Multiteller bank with separate queues & jockeying\n\n");
        System.out.printf("Number of tellers%16d\n\n",numTellers);
        System.out.printf("Mean interarrival time%11.3f minutes\n\n", meanInterArrival);
        System.out.printf("Mean service time%16.3f minutes\n\n", meanService);
        System.out.printf("Bank closes after%16.3f hours\n\n\n\n", lengthDoorsOpen);
	}

	@Override
	protected void Arrive(double time) 
	{
		Customer arrivingCustomer = new Customer(time, data.GetShortestLine());
		data.InsertInQueue(arrivingCustomer, SimData.Order.LAST, arrivingCustomer.GetTeller());
		if(data.GetQueueSize(arrivingCustomer.GetTeller()) > 1)
		{
			nCustsDelayed++;
		}
	}

	@Override
	protected void Depart(double time) 
	{
		int finishingTeller = data.GetDepartingQueue();
		Customer leavingCustomer = data.RemoveFromQueue(SimData.Order.FIRST, finishingTeller, time);
		totalOfDelays += leavingCustomer.getTimeLeft() - leavingCustomer.GetTimeArrived();
		
        // Let a customer from the end of another queue jockey to the end of this queue, if possible. 
        Jockey(leavingCustomer.GetTeller());
	}
	
    /**
     * Jockey customers to the end of queue "teller" from the end of another queue, if possible.
     *  
     */
    private void Jockey(int teller)
    {
    	//Only jockeys if the difference in queue size is at least this
    	int minDifference = 2;
    	
    	boolean done = false;
    	while(!done)
    	{
    		int shorterQueueSize = data.GetQueueSize(teller);
    		int longestQueueIndex = data.GetLongestLine();
    		int longestQueueSize = data.GetQueueSize(longestQueueIndex);
    		if(longestQueueSize - shorterQueueSize >= minDifference)
    			data.JockeyCustomer(longestQueueIndex, teller);
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
        System.out.printf("Time simulation ended%12.3f minutes\n", data.GetSimTime());
	}
	
    /**
     * Runs multiple simulations with different numbers 
     * of tellers
     * @param from
     * @param to
     * @param step
     */
    public void RunTellerSimulations(int from, int to, int step)
    {
		while(from <= to)
		{
			numTellers = from;
			RunSimulation(true);
			from += step;
		}
    }
}
