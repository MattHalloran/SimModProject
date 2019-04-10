import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Stores simulation information, such as the queue lines
 * and current time
 * @author Matt Halloran, Troy Pastirko, Ryan Ciocci
 *
 */
public class SimData 
{
	public static enum Order {FIRST, LAST, INCREASING, DECREASING};
    
    private int					 numTellers;
    private double                currentTime;
    private double				 closingTime;
    private int					 customersInStore;
    private LinkedList<Event>  eventList; //A list of all logged events
    private ArrayList<LinkedList<Customer>> queueLists; //Stores each teller's list of customers
    
    public SimData(int tellers)
    {        
    	numTellers = tellers;
    	customersInStore = 0;
    	currentTime = 0;
    	eventList = new LinkedList<Event>();
    	queueLists = new ArrayList<LinkedList<Customer>>();
    	for(int i = 0; i < tellers; i++)
    		queueLists.add(new LinkedList<Customer>());
    }
    
    public int CustomersInStore()
    {
    	return customersInStore;
    }
    
    public boolean StoreOpen() 
    {
    	return currentTime < closingTime;
    }
    
    public void SetClosingTime(double time)
    {
    	closingTime = time;
    }
    
    public double GetClosingTime()
    {
    	return closingTime;
    }
    
    public void EventSchedule(Event ev)
    {
    	eventList.add(ev);
    }
    
    /**
     * Adds an event to a teller's queue
     * 	[Order.FIRST] = Adds event to start of queue
     * 	[Order.LAST] = Adds event to end of queue
     * @param ev The event being added
     * @param order Specifies where the event is being added
     * @param qNum Specifies the queue that the event is being 
     * added to
     */
    public void InsertInQueue(Customer customer, Order order, int qNum)//TODO
    {
    	currentTime = customer.GetTimeArrived();
    	customersInStore++;
    	if(order == Order.FIRST)
    	{
    		queueLists.get(qNum).addFirst(customer);
    	}
    	else if(order == Order.LAST)
    	{
    		queueLists.get(qNum).addLast(customer);
    	}
    	else
    	{
			System.out.println("Invalid order type passed to Event.InsertInQueue()");
			System.exit(1);
    	}
    }
    
    /**
     * Removes an event from a teller's queue
     * 	[Order.FIRST] = Removes first event in queue
     * 	[Order.LAST] = Removes last event in queue
     * @param order
     * @param qNum
     * @return
     */
    public Customer RemoveFromQueue(Order order, int qNum, double time)
    {
		if(queueLists.get(qNum).isEmpty())
		{
			System.out.println("Tried to remove from empty queue in Event.RemoveFromQueue");
			System.exit(1);
		}

		customersInStore--;
		
		Customer leavingCustomer = new Customer();
		if(order == Order.FIRST)
		{
			leavingCustomer = queueLists.get(qNum).removeFirst();
		}
		else if(order == Order.LAST)
		{
			leavingCustomer = queueLists.get(qNum).removeLast();
		}
		else
		{
			System.out.println("Invalid order number passed to Event.RemoveFromQueue()");
		}
		
    	leavingCustomer.setTimeLeft(time);
    	currentTime = time;
    	return leavingCustomer;
    }
    
    public double GetSimTime()
    {
        return currentTime;
    }
    
    public Event.EventType GetNextEventType()
    {
        return eventList.get(0).getType();
    }
    
    public int GetQueueSize(int qNum)
    {
    	if(qNum < 0 || qNum >= queueLists.size()) 
    	{ 
    		System.out.println("Tried to access non-existing queue in Event.GetQueueSize");
    		int maxSize = queueLists.size()-1;
    		System.out.println("Requested cue " + qNum + ", when the highest is " + maxSize);
    		System.exit(1);
    	}
    	return queueLists.get(qNum).size();
    }
    
    /**
     * Cancels the first event that matches the event type
     * @param eventType
     */
	public void EventCancel(Event.EventType eventType) 
	{
		Iterator<Event> itty = eventList.iterator();
	    while (itty.hasNext())
		{
			Event ev = itty.next();
			if(ev.getType() == eventType)
			{
				itty.remove();
				return;
			}
		}
	}
	
	/**
	 * Finds the shortest queue that has a teller
	 * @return
	 */
	public int GetShortestLine()
	{
		int lineNumber = -1, shortestLine = Integer.MAX_VALUE;
		for(int i = 0; i < numTellers; i++)
		{
			int size = queueLists.get(i).size();
			if(size < shortestLine)
			{
				shortestLine = size;
				lineNumber = i;
			}
		}
		return lineNumber;
	}
	
	/**
	 * Finds the longest queue that has a teller
	 * @return
	 */
	public int GetLongestLine()
	{
		int lineNumber = -1, longestLine = Integer.MIN_VALUE;
		for(int i = 0; i < numTellers; i++)
		{
			int size = queueLists.get(i).size();
			if(size > longestLine)
			{
				longestLine = size;
				lineNumber = i;
			}
		}
		return lineNumber;
	}
	
	public int GetDepartingQueue()
	{
		List<Integer> queuesWithCustomers = new ArrayList<Integer>();
		for(int i = 0; i < numTellers; i++)
		{
			if(GetQueueSize(i) > 0)
				queuesWithCustomers.add(i);
		}
		
		return (int)SimLib_Random.RandomValue(queuesWithCustomers.toArray());
	}
	
	/**
	 * Moves a customer from the end of one line to another
	 * @param from
	 * @param to
	 */
	public void JockeyCustomer(int from, int to)
	{
		Customer c = queueLists.get(from).removeLast();
		queueLists.get(to).addFirst(c);
	}
	
	/**
	 * 
	 * @return The current percentage of tellers with customers
	 */
	public double CurrentServerUtilization()
	{
		int busyTellers = 0;
		for(int i = 0; i < numTellers; i++)
		{
			if(GetQueueSize(i) > 0)
				busyTellers++;
		}
		return busyTellers/numTellers;
	}
}
