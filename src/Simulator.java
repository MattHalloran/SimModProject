import java.util.List;

/**
 * Holds all simulations that will be executed
 * @author Matt Halloran, Troy Pastirko, Ryan Ciocci
 *
 */
public class Simulator
{
	public static void main(String[] args)
	{
		Bank bank = new Bank();
		bank.SetLengthDoorsOpen(8.0);
		bank.SetMeanInterArrival(0.8);
		bank.SetMeanService(4.5);
		bank.RunTellerSimulations(4, 7, 1);
		
		MM1 single = new MM1();
		single.SetMeanInterArrival(3.0);
		single.SetMeanService(4.5);
		single.SetLengthDoorsOpen(8.0);
		single.SetMaxNumCustomers(1000);
		single.RunSimulation(true);
	}
	
	public static void RunThreaded(List<SimulationBase> simulations)
	{
		Thread[] threads = new Thread[simulations.size()];
		int i = 0;
		for(SimulationBase sim : simulations)
		{
			threads[i] = new Thread(() -> {
			    sim.RunSimulation(false);
			});
			threads[i].start();
			i++;
		}
		for(i = 0; i < threads.length; i++)
		{
			try
			{
				threads[i].join();
			}
			catch(Exception e) {}
		}
		for(SimulationBase sim : simulations)
		{
			sim.DisplayStartingData();
			sim.Report();
		}
	}
}
