/**
 * Base class for all simulation types
 * @author Matt Halloran, Troy Pastirko, Ryan Ciocci
 *
 */
public abstract class SimulationBase 
{
	protected SimData data;
	
    protected int numTellers, 
	 					 shortestLength, 
	 					 shortestQueue,
	 					 maxNumCustomers = Integer.MAX_VALUE;
    protected double lengthDoorsOpen; //in hours

    protected double meanInterArrival, meanService;

    protected double areaNumInQ;
    protected double areaServerStatus;
    protected double totalOfDelays;
    protected int eventCount;
    protected int totalCustomers;
    protected int nCustsDelayed;
    
    protected abstract void Initialize();
    protected abstract void DisplayStartingData();
    /**
     * Updates queues and statistics for a customer arriving
     */
    protected abstract void Arrive(double time);
    /**
     * Updates queues and statistics for a customer leaving
     */
    protected abstract void Depart(double time);
    /**
     * Displays simulation data
     */
    protected abstract void Report();
    
    public void SetMaxNumCustomers(int maxNumCustomers)
    {
    	this.maxNumCustomers = maxNumCustomers;
    }
    
    public void SetLengthDoorsOpen(double lengthDoorsOpen)
    {
    	this.lengthDoorsOpen = lengthDoorsOpen;
    }
    
    public void SetMeanInterArrival(double meanInterArrival)
    {
    	this.meanInterArrival = meanInterArrival;
    }
    
    public void SetMeanService(double meanService)
    {
    	this.meanService = meanService;
    }
    
    /**
     * Runs a single simulation
     */
    public void RunSimulation(boolean printToConsole)
    {
    	Initialize();
    	data.SetClosingTime(60 * lengthDoorsOpen);
    	if(printToConsole)
    	{
        	System.out.println("----------------------------------------------------");
    		DisplayStartingData();
    	}
    	
    	double nextArrivalTime = SimLib_Random.Expon(meanInterArrival),
        		nextDepartureTime = SimLib_Random.Expon(meanService);
        // Run the simulation while more delays are still needed 
        while(totalCustomers < maxNumCustomers && data.StoreOpen() || data.CustomersInStore() > 0)
        {
        	//System.out.println(SimData.GetSimTime());
        	//New customers only enter when the store is still open
        	if(data.CustomersInStore() > 0 && (!data.StoreOpen() || nextDepartureTime < nextArrivalTime))
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
        		
        	areaNumInQ += data.CustomersInStore();
        	areaServerStatus += data.CurrentServerUtilization();
        	eventCount++;
        }
        
        if(printToConsole)
        {
            System.out.println("----------------------------------------------------");
    		System.out.println("Finished the simulation");
            Report();
            System.out.println("----------------------------------------------------");
            System.out.println("\n\n\n");
        }
    }
    
    /**
     * Runs multiple simulations with different values 
     * for meanInterArrival
     * @param from
     * @param to
     * @param step
     */
    public void RunInterArrivalSimulations(double from, double to, double step)
    {
		while(from <= to)
		{
			meanInterArrival = from;
			RunSimulation(true);
			from += step;
		}
    }
    
    /**
     * Runs multiple simulations with different values 
     * for meanService
     * @param from
     * @param to
     * @param step
     */
    public void RunServiceSimulations(double from, double to, double step)
    {
		while(from <= to)
		{
			meanService = from;
			RunSimulation(true);
			from += step;
		}
    }
}
