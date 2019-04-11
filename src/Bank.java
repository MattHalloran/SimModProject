import java.util.ArrayList;
import java.util.List;

/**
 * A simulator for a multi-queue banking system
 * @author Matt Halloran, Troy Pastirko, Ryan Ciocci
 */
public class Bank extends SimulationBase
{
//	/**
//	 * The jockeying method used in the simulation
//	 * LARGEST - Last customer from largest line moves in, if that line is larger than 
//	 *
//	 */
//	public static enum JockeyType {LARGEST, LEFT, RIGHT};
//	
//	/**
//	 * Determines what type of jockeying method to use
//	 */
//	public JockeyType typeOfJockey;
//	
//	public JockeyType GetJockeyType()
//	{
//		return typeOfJockey;
//	}
//	
//	public void SetJockeyType(JockeyType type)
//	{
//		typeOfJockey = type;
//	}

	@Override
	protected void DisplayStartingData() 
	{
        System.out.printf("Multiteller bank with separate queues & jockeying\n");
        System.out.printf("Number of tellers%16d\n",numTellers);
        System.out.printf("Mean interarrival time%11.3f minutes\n", meanInterArrival);
        System.out.printf("Mean service time%16.3f minutes\n", meanService);
        System.out.printf("Bank closes after%16.3f hours\n\n", lengthDoorsOpen);
	}

	@Override
	protected void Arrive(double time) 
	{
		Customer arrivingCustomer = new Customer(time, data.GetShortestLine());
		if(data.GetQueueSize(arrivingCustomer.GetTeller()) > 0)
			totalCustomersDelayed++;
		data.InsertInQueue(arrivingCustomer, SimData.Order.LAST, arrivingCustomer.GetTeller());
	}

	@Override
	protected void Depart(double currentTime) 
	{
		int finishingTeller = data.GetDepartingQueue();
		Customer leavingCustomer = data.RemoveFromQueue(SimData.Order.FIRST, finishingTeller, currentTime);
		//total time the customer spent in the store
		totalTimeSpent += leavingCustomer.getTimeDeparted() - leavingCustomer.getTimeArrived();
		//time the customer spent waiting to be served
		totalDelayTime += leavingCustomer.getTimeServed() - leavingCustomer.getTimeArrived();
	    //System.out.println(leavingCustomer.getTimeArrived() + " " + leavingCustomer.getTimeServed() + " " + leavingCustomer.getTimeDeparted());
		
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
	protected void ReportAdditionalInfo() 
	{
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
		try
    	{
        	List<SimulationBase> simulations = new ArrayList<SimulationBase>();
    		while(from <= to)
    		{
    			Bank sim = (Bank)clone();
    			sim.SetNumTellers(from);
    			sim.Initialize();
    			simulations.add(sim);
    			from += step;
    		}
    		Simulator.RunThreaded(simulations);
    	}
    	catch(Exception e)
    	{
    		System.out.println("Failed to run meanService simulations");
    	}
    }
    
    @Override
    protected Object clone()
    {
        Bank b =  new Bank();
        b.meanInterArrival = meanInterArrival;
        b.meanService = meanService;
        b.lengthDoorsOpen = lengthDoorsOpen;
        b.maxNumCustomers = maxNumCustomers;
        b.numTellers = numTellers;
        return b;
    }
}
