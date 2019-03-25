/**
 * Stores information about a Customer
 * @author mdhal
 *
 */
public class Customer
{
    private double timeArrived;
    private double timeLeft;
    private int    teller;

    /**
     * Constructor for objects of class Event
     */
    public Customer()
    {
        this.timeArrived = 0.0;
        this.teller       = 0;
    }

    /**
     * Constructor for objects of class Event
     */
    public Customer(double arrivalTime)
    {
        this.timeArrived = arrivalTime;
        this.teller       = 0;
    }
    
    /**
     * Constructor for objects of class Event
     */
    public Customer(double arrivalTime, int teller)
    {
        this.timeArrived = arrivalTime;
        this.teller       = teller;
    }

    public double GetTimeArrived()
    {
        return timeArrived;
    }
    
    public void SetTimeArrived(double time)
    {
    	this.timeArrived = time;
    }
    
    public int GetTeller()
    {
        return teller;
    }

	public double getTimeLeft() 
	{
		return timeLeft;
	}

	public void setTimeLeft(double timeLeft)
	{
		this.timeLeft = timeLeft;
	}
}
