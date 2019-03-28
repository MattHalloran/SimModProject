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
		bank.SetMaxTellers(5);
		bank.SetMeanInterArrival(3.0);
		bank.SetMeanService(4.5);
		bank.SetMinTellers(5);
		bank.RunSimulation();
		
		MM1 single = new MM1();
		single.SetMeanInterArrival(3.0);
		single.SetMeanService(4.5);
		single.SetLengthDoorsOpen(8.0);
		single.SetMaxNumCustomers(1000);
		single.RunSimulation();
	}
}
