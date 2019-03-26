/**
 * A simulator for a single-server queuing system
 * @author Matt Halloran
 */
public class MM1 extends SimulationBase
{
	@Override
	protected void Initialize() 
	{
		SimData.Initialize(1,1);
	}

	@Override
	protected void DisplayStartingData()
	{
		System.out.printf("Single-server queueing system\n\n");
        System.out.printf("Mean interarrival time%11.3f minutes\n\n", meanInterArrival);
        System.out.printf("Mean service time%16.3f minutes\n\n", meanService);
        System.out.printf("Number of customers%14d\n\n", maxNumCustomers);
	}

	@Override
	protected void Arrive(double time) 
	{
		Customer arrivingCustomer = new Customer(time, 0);
		SimData.InsertInQueue(arrivingCustomer, SimData.Order.LAST, 0);
		if(SimData.GetQueueSize(0) > 1)
		{
			nCustsDelayed++;
		}
	}

	@Override
	protected void Depart(double time) 
	{
		Customer leavingCustomer = SimData.RemoveFromQueue(SimData.Order.FIRST, 0, time);
		totalOfDelays += leavingCustomer.getTimeLeft() - leavingCustomer.GetTimeArrived();
	}

	@Override
	protected void Report() 
	{
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

