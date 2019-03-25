/**
 * @author Matt Halloran
 */
import java.util.ArrayList;

public class MM1
{
    static ArrayList<Double> timeArrival;

    static final int IDLE = 0;
    static final int BUSY = 1;

    static double meanInterArrival;
    static double meanService;
    static int nDelaysRequired;

    static int nEvents;
    static double simTime;
    static int serverStatus;
    static int numInQ;
    static double timeLastEvent;
    static int nCustsDelayed;
    static double totalOfDelays; 
    static double areaNumInQ;
    static double areaServerStatus;
    static double[] timeNextEvent = new double[3];
    static int nextEventType;
    static double total_of_delays;

    /**
     * Main
     * 
     * 1) The first arival event is created in Initialize
     * 2) The FIFO queue timeArrival holds "arrival times" that are waiting for server
     */
    public static void main(String[] args) 
    {
        nEvents = 2;

        meanInterArrival = 1.0;
        meanService = 0.5;
        nDelaysRequired = 1000;

        // Write report heading and input parameters. 
        System.out.printf("Single-server queueing system\n\n");
        System.out.printf("Mean interarrival time%11.3f minutes\n\n", meanInterArrival);
        System.out.printf("Mean service time%16.3f minutes\n\n", meanService);
        System.out.printf("Number of customers%14d\n\n", nDelaysRequired);

        Initialize();

        // Run the simulation while more delays are still needed 
        while (nCustsDelayed < nDelaysRequired)
        {
            Timing();                // Determine the next event.
            UpdateTimAvgStats();     // Update time-average statistical accumulators.

            switch (nextEventType)   // Invoke the appropriate event function.
            {
                case 1:
                   Arrive();
                break;
                case 2:
                   Depart();
                break;
            }
            //System.out.println("sim Time = " + simTime + " Qsize = " + time_arrival.size() );
        }
        Report();
    }

    /**
     * 
     */
    static void Initialize()
    {
        timeArrival = new ArrayList<Double>();

        simTime = 0.0; // Initialize the simulation clock.

        // Initialize the state variables. 
        serverStatus   = IDLE;
        numInQ = 0;
        timeLastEvent = 0.0;

        // Initialize the statistical counters. 
        nCustsDelayed = 0;
        totalOfDelays    = 0.0;
        areaNumInQ = 0.0;
        areaServerStatus = 0.0;

        // Initialize event list.  Since no customers are present, the departure(service completion) event is eliminated from consideration. 
        timeNextEvent[1] = simTime + SimLib_Random.Expon(meanInterArrival, 1);
        timeNextEvent[2] = 1.0e+30;
    }

    /**
     * Timing
     * 
     * This method basically updates the simTime to the next event (the min of timeNextEvent[1] or timeNextEvent[2]
     * This method also updates the nextEventType to be 1 - an arrival, or 2 - a departure
     * 
     * timeNextEvent[0] - empty
     * timeNextEvent[1] - the time of the next arrival event int simTime
     * timeNextEvent[2] - the time of the next departure event in simTime
     */
    static void Timing()
    {
        int   i;
        double min_time_next_event = 1.0e+29;

        nextEventType = 0;

        // Determine the event type of the next event to occur. 
        for (i = 1; i <= nEvents; ++i)
        {
            if (timeNextEvent[i] < min_time_next_event)
            {
                min_time_next_event = timeNextEvent[i];
                nextEventType = i;
            }
        }

        // Check to see whether the event list is empty
        if (nextEventType == 0)
        {
            // The event list is empty, so stop the simulation. 
            //            fprintf(outfile, "\nEvent list empty at time %f", simTime);
        }

        // The event list is not empty, so advance the simulation clock. 
        simTime = min_time_next_event;
    }

    /*****************************************************************************************************************************
     * 
     * Arrive
     * 
     *  1) The first thing the Arrival event does is schedule the next arrival event and store it in timeNextEvent[1]
     *  2) If the server is BUSY 
     *          the current simTime is added to the back of the queue ("this" event goes in the queue, and waits in line)
     *     else (server is IDLE)
     *          Schedule the next departure event ("this" event goes in server)
     *          set server status to BUSY
     *  
     */
    static void Arrive()
    {
        double delay;

        timeNextEvent[1] = simTime + SimLib_Random.Expon(meanInterArrival, 1);   // Compute the simTime of the next arrival event

        if (serverStatus == BUSY)                                  // Check to see whether server is busy.
        {
            numInQ++;;                                             // Server is busy, so increment number of customers in queue.
            if (numInQ > 100)                                 // Check to see whether an overflow condition exists.
            {
                /* The queue has overflowed, so stop the simulation. */
            }

            // The server is busy so add this arrival time to the back of the queue
            timeArrival.add(simTime);
        }

        else  // server is idle, so arriving customer has a delay of zero
        {
            // The following two statements are for program clarity and do not affect the results of the simulation
            delay            = 0.0;
            total_of_delays += delay;

            // Increment the number of customers delayed, and make server busy
            nCustsDelayed++;
            serverStatus = BUSY;

            // Schedule a departure since this customer is going right to the server
            timeNextEvent[2] = simTime + SimLib_Random.Expon(meanService, 1);
        }
    }

    /**
     * Depart
     * 
     * 1) If queue is empty (if no one is waiting)
     *          set the server back to IDEL because this customer (Depart event) is leaving the server
     *    else
     *          Remove next in line from queue
     *          and compute the time the event will take in server (compute a new Departure event)
     *
     */
    static void Depart()
    {
        int   i;
        double delay;

        /* Check to see whether the queue is empty. */

        if (timeArrival.size() == 0)
        {
            // The queue is empty so make the server idle and eliminate the departure (service completion) event from consideration
            serverStatus = IDLE;
            timeNextEvent[2] = 1.0e+30;
        }

        else
        {
            // Compute the delay of the customer who is beginning service and update the total delay accumulator
            // delay = simTime - time_arrival[1];
            delay = simTime - timeArrival.get(0);   // the amount of time spent in queue
            total_of_delays += delay;               // cumulative amout of time that was spent waiting in queue

            // Increment the number of customers delayed, and schedule departure
            nCustsDelayed++;
            timeNextEvent[2] = simTime + SimLib_Random.Expon(meanService, 1);
            
            // Remove the customer from the front of the queue
            timeArrival.remove(0);
        }
    }

    /**
     * UpdateTimAvgStats
     */
    static void UpdateTimAvgStats()
    {
        double timeSinceLastEvent;

        // Compute time since last event, and update last-event-time marker
        timeSinceLastEvent = simTime - timeLastEvent;
        timeLastEvent = simTime;

        // Update area under number-in-queue function
        areaNumInQ += timeArrival.size() * timeSinceLastEvent;  // timeArrival.size() is number in queue

        // Update area under server-busy indicator function
        areaServerStatus += serverStatus * timeSinceLastEvent;
    }

    /**
     * Computes and prints estimates of desired measures of performance 
     */
    static void Report()
    {
        System.out.printf("\n\nAverage delay in queue%11.3f minutes\n\n", total_of_delays / nCustsDelayed);
        System.out.printf("Average number in queue%10.3f\n\n", areaNumInQ / simTime);
        System.out.printf("Server utilization%15.3f\n\n", areaServerStatus / simTime);
        System.out.printf("Time simulation ended%12.3f minutes", simTime);
    }
}

