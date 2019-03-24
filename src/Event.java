
/**
 * Event Class
 * 
 * @author Professor Rabbitz, Matt Halloran
 * @version 3-22-19
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator; 

public class Event
{
    public enum Order {FIRST, LAST, INCREASING, DECREASING};

    static private final int             MAX_QUEUES = 26;          
    static private double                simTime;
    static private int                   nextEventType;
    static private int                   tellerNumber;
    static private LinkedList<Event>     eventList;
    static private ArrayList<LinkedList<Event>> queueLists;

    private double                      eventSimTime;
    private int                         eventType;
    private int                         teller;

    /**
     * Constructor for objects of class Event
     */
    public Event()
    {
        this.eventSimTime = 0.0;
        this.eventType    = 1;
        this.teller       = 0;
    }

    /**
     * Constructor for objects of class Event
     */
    public Event(double eventSimTime, int eventType)
    {
        this.eventSimTime = eventSimTime;
        this.eventType    = eventType;
        this.teller       = 0;
    }
    
    /**
     * Constructor for objects of class Event
     */
    public Event(double eventSimTime, int eventType, int teller)
    {
        this.eventSimTime = eventSimTime;
        this.eventType    = eventType;
        this.teller       = teller;
    }

    public double GetEventTime()
    {
        return eventSimTime;
    }
    
    public int GetEventType()
    {
        return eventType;
    }
    
    public int GetTeller()
    {
        return teller;
    }

    static public void EventSchedule(Event ev)//TODO
    {
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
    static public void InsertInQueue(Event ev, Order order, int qNum)//TODO
    {
    	switch(order)
    	{
		case FIRST:
			queueLists.get(qNum).addFirst(ev);
		case LAST:
			queueLists.get(qNum).addLast(ev);
		default:
			System.out.println("Invalid order number passed to Event.InsertInQueue()");
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
    static Event RemoveFromQueue(Order order, int qNum)//TODO
    {
		if(queueLists.get(qNum).isEmpty())
		{
			System.out.println("Tried to remove from empty queue in Event.RemoveFromQueue");
			return new Event();
		}

    	switch(order)
    	{
		case FIRST:
			return queueLists.get(qNum).removeFirst();
		case LAST:
			return queueLists.get(qNum).removeLast();
		default:
			System.out.println("Invalid order number passed to Event.RemoveFromQueue()");
			return new Event();
    	}
    }



    static public void Initialize()
    {        
    	eventList = new LinkedList<Event>();
    	queueLists = new ArrayList<LinkedList<Event>>();
    	for(int i = 0; i < MAX_QUEUES; i++)
    		queueLists.add(new LinkedList<Event>());
    }

    static public double GetSimTime()
    {
        return simTime;
    }
    
    static public int GetNextEventType()
    {
        return nextEventType;
    }
    
    static public int GetTellerNumber()
    {
        return tellerNumber;
    }

    static public int GetQueueSize(int qNum)
    {
    	if(qNum < 0 || qNum >= queueLists.size()) 
    	{ 
    		System.out.println("Tried to access non-existing queue in Event.GetQueueSize");
    		int maxSize = queueLists.size()-1;
    		System.out.println("Requested cue " + qNum + ", when the highest is " + maxSize);
    		return -1;
    	}
    	return queueLists.get(qNum).size();
    }

    static public void Timing()
    {
        Event ev      = (Event)RemoveFromQueue(Order.FIRST, 25);
        simTime       = ev.GetEventTime();
        nextEventType = ev.GetEventType();
        tellerNumber  = ev.GetTeller();
        
    }

    /**
     * Cancel the only arrival event in the event queue
     * @param eventArrival
     */
	public static void EventCancel(int eventArrival) {
		//TODO
	}
}
