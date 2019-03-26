/**
 * Base class for all simulation types
 * @author Matt Halloran
 *
 */
public abstract class SimulationBase 
{
    protected int minTellers, 
	 					 maxTellers, 
	 					 numTellers, 
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
    
    public void SetMinTellers(int minTellers)
    {
    	this.minTellers = minTellers;
    }
    
    public void SetMaxTellers(int maxTellers)
    {
    	this.maxTellers = maxTellers;
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
    
    public void RunSimulation()
    {
    	System.out.println("----------------------------------------------------");
    	Initialize();
    	SimData.SetClosingTime(60 * lengthDoorsOpen);
    	DisplayStartingData();
    	
    	double nextArrivalTime = SimLib_Random.Expon(meanInterArrival),
        		nextDepartureTime = SimLib_Random.Expon(meanService);
        // Run the simulation while more delays are still needed 
        while(totalCustomers < maxNumCustomers && SimData.StoreOpen() || SimData.CustomersInStore() > 0)
        {
        	//System.out.println(SimData.GetSimTime());
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
        
        System.out.println("----------------------------------------------------");
		System.out.println("Finished the simulation");
        Report();
        System.out.println("----------------------------------------------------");
        System.out.println("\n\n\n");
    }
}
