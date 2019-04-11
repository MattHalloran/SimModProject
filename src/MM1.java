/**
 * A simulator for a single-server queuing system
 * @author Matt Halloran, Troy Pastirko, Ryan Ciocci
 */
public class MM1 extends SimulationBase
{
	public MM1()
	{
		numTellers = 1;
	}
	
	@Override
	protected void DisplayStartingData()
	{
		System.out.printf("Single-server queueing system\n");
        System.out.printf("Mean interarrival time%11.3f minutes\n", meanInterArrival);
        System.out.printf("Mean service time%16.3f minutes\n", meanService);
        System.out.printf("Number of customers%14d\n", maxNumCustomers);
	}

	@Override
	protected void Arrive(double time) 
	{
		Customer arrivingCustomer = new Customer(time, 0);
		if(data.GetQueueSize(0) > 0)
			totalCustomersDelayed++;
		data.InsertInQueue(arrivingCustomer, SimData.Order.LAST, 0);
	}

	@Override
	protected void Depart(double time) 
	{
		Customer leavingCustomer = data.RemoveFromQueue(SimData.Order.FIRST, 0, time);
		//total time the customer spent in the store
		totalTimeSpent += leavingCustomer.getTimeDeparted() - leavingCustomer.getTimeArrived();
		//time the customer spent waiting to be served
		totalDelayTime += leavingCustomer.getTimeServed() - leavingCustomer.getTimeArrived();
	}

	@Override
	protected void ReportAdditionalInfo() 
	{
	}
	
	@Override
    protected Object clone()
    {
		MM1 single =  new MM1();
		single.meanInterArrival = meanInterArrival;
		single.meanService = meanService;
		single.lengthDoorsOpen = lengthDoorsOpen;
		single.maxNumCustomers = maxNumCustomers;
		return single;
    }
}

