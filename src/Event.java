/**
 * Will be used to create a log of everything 
 * that happened in the simulation. not used yet
 * @author Matt Halloran, Troy Pastirko, Ryan Ciocci
 *
 */
public class Event 
{
	public static enum EventType 
	{
		ARRIVAL,
		DEPARTURE,
		JOCKEY
	}
	
	private double eventTime;
	private EventType type;
	private int tellerIndex;
	
	public Event(double time, EventType type)
	{
		this.eventTime = time;
		this.type = type;
		this.setTellerIndex(0);
	}
	
	public Event(double time, EventType type, int tellerIndex)
	{
		this.eventTime = time;
		this.type = type;
		this.setTellerIndex(tellerIndex);
	}

	public double getEventTime() 
	{
		return eventTime;
	}

	public void setEventTime(double eventTime) 
	{
		this.eventTime = eventTime;
	}

	public EventType getType() 
	{
		return type;
	}

	public void setType(EventType type) 
	{
		this.type = type;
	}

	public int getTellerIndex() 
	{
		return tellerIndex;
	}

	public void setTellerIndex(int tellerIndex)
	{
		this.tellerIndex = tellerIndex;
	}
}
