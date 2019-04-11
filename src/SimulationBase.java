import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all simulation types
 * @author Matt Halloran, Troy Pastirko, Ryan Ciocci
 *
 */
public abstract class SimulationBase implements Cloneable
{
	protected SimData data;
	
    protected int numTellers, 
	 					 shortestNumWaiting = Integer.MAX_VALUE,
	 					 longestNumWaiting,
	 					 maxNumCustomers = Integer.MAX_VALUE;
    protected double lengthDoorsOpen; //in hours

    protected double meanInterArrival, meanService;

    protected double areaNumInQ;
    protected double areaServerStatus;
    protected double totalDelayTime;
    protected double totalTimeSpent;
    protected int totalEventCount;
    protected int totalCustomersServed;
    protected int totalCustomersDelayed;
    
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
     * Displays simulation data that is not shared with the base
     */
    protected abstract void ReportAdditionalInfo();
    
    protected void Initialize()
    {
    	data = new SimData(numTellers);
    }
    
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
    
    public void SetNumTellers(int num)
    {
    	numTellers = num;
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
        		nextDepartureTime = SimLib_Random.Expon(meanService/numTellers),
        		lastEventTime = 0;
        // Run the simulation while more delays are still needed 
        while(totalCustomersServed < maxNumCustomers && (data.StoreOpen() || data.CustomersInStore() > 0))
        {
        	//New customers only enter when the store is still open
        	if(data.CustomersInStore() > 0 && (!data.StoreOpen() || nextDepartureTime <= nextArrivalTime))
        	{
        		Depart(nextDepartureTime);
        		UpdateAreaStatistics(nextDepartureTime - lastEventTime);
        		nextDepartureTime += SimLib_Random.Expon(meanService/numTellers);
        		if(data.CustomersInStore() < shortestNumWaiting)
        			shortestNumWaiting = data.CustomersInStore();
        	}
        	else
        	{
        		Arrive(nextArrivalTime);
        		UpdateAreaStatistics(nextArrivalTime - lastEventTime);
        		nextArrivalTime += SimLib_Random.Expon(meanInterArrival);
        		totalCustomersServed++;
        		if(data.CustomersInStore() > longestNumWaiting)
        			longestNumWaiting = data.CustomersInStore();
        	}
        		
        	lastEventTime = data.GetSimTime();
        	totalEventCount++;
        }
        
        if(printToConsole)
        {
            Report();
        }
    }
    
    protected void Report()
    {
    	System.out.println("----------------------------------------------------");
		System.out.println("Finished the simulation");
		ReportBaseInfo();
        ReportAdditionalInfo();
        System.out.println("----------------------------------------------------");
        System.out.println("\n\n\n");
    }
    
    private void ReportBaseInfo()
    {
    	double averageDelay = 0, averageNum = 0, serverUtilization = 0, averageTimeSpent = 0;
    	if(totalCustomersDelayed != 0)
    		averageDelay = totalDelayTime / totalCustomersDelayed;
    	if(totalEventCount > 0)
    	{
    		averageNum = areaNumInQ / data.GetSimTime();
    		serverUtilization = areaServerStatus / data.GetSimTime();
    		averageTimeSpent = totalTimeSpent / totalCustomersServed;
    	}
    	System.out.printf("\nAverage delay in queue%11.3f minutes\n", averageDelay);
        System.out.printf("Average number in queue%10.3f\n", averageNum);
        System.out.printf("Server utilization%15.3f\n", serverUtilization);
        System.out.println("Average time spent in store: " + averageTimeSpent);
        System.out.printf("Time simulation ended%12.3f minutes\n", data.GetSimTime());
    }
    
    private void UpdateAreaStatistics(double timeBetweenEvents)
    {
    	//System.out.println(timeBetweenEvents + " " + data.CustomersInStore() + " " + data.CurrentServerUtilization());
    	areaNumInQ += data.CustomersInStore() * timeBetweenEvents;
		areaServerStatus += data.CurrentServerUtilization() * timeBetweenEvents;
		totalEventCount++;
    }
    
    protected void reset()
    {
    	data = new SimData(numTellers);
    	shortestNumWaiting = 0;
    	longestNumWaiting = 0;
    	areaNumInQ = 0;
    	areaServerStatus = 0;
    	totalDelayTime = 0;
    	totalEventCount = 0;
    	totalCustomersServed = 0;
    	totalCustomersDelayed = 0;
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
		try
    	{
        	List<SimulationBase> simulations = new ArrayList<SimulationBase>();
    		while(from <= to)
    		{
    			SimulationBase sim = (SimulationBase)clone();
    			sim.SetMeanInterArrival(from);
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
    
    /**
     * Runs multiple simulations with different values 
     * for meanService
     * @param from
     * @param to
     * @param step
     */
    public void RunServiceSimulations(double from, double to, double step)
    {
    	try
    	{
        	List<SimulationBase> simulations = new ArrayList<SimulationBase>();
    		while(from <= to)
    		{
    			SimulationBase sim = (SimulationBase)clone();
    			sim.SetMeanService(from);
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
}
