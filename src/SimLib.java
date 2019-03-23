import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 
 * @author Matt Halloran
 *
 */
public class SimLib {
	
	PrintWriter writer;
	BufferedReader reader;
	private static String outFileLocation = "", inFileLocation = "";
	
	private int maxatr = 0, maxlist = 0;
	
	//The simulation clock, updated by simlib function timing 
	private double sim_time, next_event_type;
	
	private float[] prob_distrib = new float[26];
	
	private ArrayList<LinkedList<Double>> eventLists;
	private int[] list_rank;
	private double[] transfer;
	
	private static int MODLUS = 2147483647, MULT1 = 24112, MULT2 = 26143;
	
	private static int FIRST = 1,//constant for the option of filing or removing a record at the beginning of a list
			LAST = 2,//constant for the option of filing or removing a record at the endvof a list
			INCREASING = 3,//constant for the option of keeping a list ranked in increasing order according to the attribute specified in the list_rank array
			DECREASING = 4,// constant for the option of keeping a list ranked in decreasing order according to the attribute specified in the list_rank array
			LIST_EVENT = 25,//constant for the number of the event list
			EVENT_TIME = 1,//constant for the attribute number of the event time in the event list
			EVENT_TYPE = 2,// constant for the attribute number of the event type in the event list
			MAX_LIST = 25,
			MAX_ATTR = 10;
	
	
	/**
	 * Invoked from the main function at the beginning of each 
	 * simulation run, initializes the simulation clock to 0.
	 */
	private void InitSimlib() {
		if(writer != null)
			writer.close();
		writer = new PrintWriter(outFileLocation);

		if(reader != null)
			reader.close();
		reader = new BufferedReader(new FileReader(inFileLocation));
	
	    int list, listsize;
	
	    if (maxlist < 1) 
	    	maxlist = MAX_LIST;
	    listsize = maxlist + 1;
	
	    // Initialize system attributes
	    sim_time = 0.0;
	    if (maxatr < 4) 
	    	maxatr = MAX_ATTR;
	
	    eventLists = new ArrayList<LinkedList<Double>>();
	    list_rank = new int[maxlist];
	    transfer = new double[maxatr+1];
	    
	    // Initialize list attributes
	    for(int i = 0; i < maxlist; i++) {
	        eventLists.add(new LinkedList<Double>());
	    }
	
	    // Set event list to be ordered by event time
	    list_rank[LIST_EVENT] = EVENT_TIME;
	
	    // Initialize statistical routines
	    sampst(0.0, 0);
	    timest(0.0, 0);
	}
	
	/**
	 * Inserts a record of type Object into the queue identified by queueNumber, where 1 <= queueNum <= 25, 
	 * and where order is and int indicating the following: 
	 * 1. (or FIRST) – record stored in front of Queue 
	 * 2. (or LAST) – record stored at the end of the Queue 
	 * 3. (or INCREASING) – record is placed at the appropriate location in queue such 
	 * that the queue is ranked in increasing order. If two records have the same order the rule is FIFO 
	 * 4. (or DECREASING) - record is placed at the appropriate location in queue such that the 
	 * queue is ranked in decreasing order. If two records have the same order the rule is FIFO
	 * @param obj
	 * @param option
	 * @param queueNum
	 */
	private void insertInQueue(Object obj, int option, int queueNum) 
	{
		    int item;
		    boolean postest;
		
		    // If the list value is improper, stop the simulation
		    if(!((queueNum >= 0) && (queueNum <= MAX_LIST))) {
		    	System.out.println("\nInvalid list " + queueNum + " for list_file at time " + sim_time);
		        System.exit(1);
		    }
		
		    // If the option value is improper, stop the simulation
		    if(!((option >= 1) && (option <= DECREASING))) {
		    	System.out.println("\n" + option + " is an invalid option for list_file on list " + queueNum + " at time " + sim_time);
		        System.exit(1);
		    }
		
		    // If this is the first record in this list, just make space for it
		    if(eventLists.get(queueNum).size() == 1) {
		
		        
		    }
		    // There are other records in the list
		    else 
		    {
		        // Check the value of option
		        if ((option == INCREASING) || (option == DECREASING)) 
		        {
		            item = list_rank[queueNum];
		            if(!((item >= 1) && (item <= maxatr))) 
		            {
		            	System.out.println(item + " is an improper value for rank of list " + queueNum + " at time " + sim_time);
		                System.exit(1);
		            }		
		            // Search for the correct location
		            if (option == INCREASING) {
		                postest = (transfer[item] >= list_rank[item]);
//		                while (postest) {
//		                    behind  = row;
//		                    row     = (*row).sr;
//		                    postest = (behind != tail[list]);
//		                    if (postest)
//		                        postest = (transfer[item] >= (*row).value[item]);
//		                }
		            }
		
		            else 
		            {
//		                postest = (transfer[item] <= (*row).value[item]);
//		                while (postest)
//		                {
//		                    behind  = row;
//		                    row     = (*row).sr;
//		                    postest = (behind != tail[list]);
//		                    if (postest)
//		                        postest = (transfer[item] <= (*row).value[item]);
//		                }
		            }
		            
		            // Check to see if position is first or last.  If so, take care of it below
//		            if (row == head[list])
//		                option = FIRST;
//		            else
//		            {
//		                if (behind == tail[list])
//		                    option = LAST;
//		                else { // Insert between preceding and succeeding records
//		
//		                    ahead        = (*behind).sr;
//		                    row          = (struct master *)
//		                                        malloc(sizeof(struct master));
//		                    (*row).pr    = behind;
//		                    (*behind).sr = row;
//		                    (*ahead).pr  = row;
//		                    (*row).sr    = ahead;
//		                }
//		            }
		        } // End of inserting in increasing or decreasing order
		        if (option == FIRST) 
		        {
//		            row         = (struct master *) malloc(sizeof(struct master));
//		            ihead       = head[list];
//		            (*ihead).pr = row;
//		            (*row).sr   = ihead;
//		            (*row).pr   = null;
//		            head[list]  = row;
		        }
		        if (option == LAST) 
		        {
//		            row         = (struct master *) malloc(sizeof(struct master));
//		            itail       = tail[list];
//		            (*row).pr   = itail;
//		            (*itail).sr = row;
//		            (*row).sr   = null;
//		            tail[list]  = row;
		        }
		    }
		
//		    // Copy the row values from the transfer array
//		    (*row).value = (float *) calloc(maxatr + 1, sizeof(float));
//		    for (item = 0; item <= maxatr; ++item)
//		        (*row).value[item] = transfer[item];
//		
//		
//		    // Update the area under the number-in-list curve
//		    timest((float)list_size[list], TIM_VAR + list);
	}
	
	/**
	 * Removes a record of type Object from the queue identified by queueNumber, 
	 * where 1 &lt;= queueNum &lt;= 25, and where order is and int indicating the following: 
	 * 1. (or FIRST) – remove the first record from the queue 
	 * 2. (or LAST) – remove the last record from the queue
	 * @param option
	 * @param queueNum
	 * @return
	 */
	private Object removeFromQueue(int option, int queueNum) 
	{
		    // If the list value is improper, stop the simulation
		    if(!((queueNum >= 0) && (queueNum <= MAX_LIST))) 
		    {
		    	System.out.println("\nInvalid list " + queueNum + " for list_remove at time " + sim_time);
		        System.exit(1);
		    }
		
		    // If the list is empty, stop the simulation
		    if(eventLists.get(queueNum).isEmpty())
		    {
		    	System.out.println("\nUnderflow of list " + queueNum + " at time " + sim_time); 
		        System.exit(1);
		    }
		
		    // If the option value is improper, stop the simulation
		    if(!(option == FIRST || option == LAST)) 
		    {
		    	System.out.println("\n" + option + " is an invalid option for list_remove on list " + queueNum + " at time " + sim_time);
		        System.exit(1);
		    }
		
		    if(eventLists.get(queueNum).isEmpty())
		    {
		        // There is only 1 record, so remove it
//		        row = head[list];
//		        head[list] = null;
//		        tail[list] = null;
		    }
		    else 
		    {
		        // There is more than 1 record, so remove according to the desired option
//		        switch(option) 
//		        {
//		            case FIRST:
//		                row         = head[list];
//		                ihead       = (*row).sr;
//		                (*ihead).pr = null;
//		                head[list]  = ihead;
//		                break;
//		            case LAST:
//		                row         = tail[list];
//		                itail       = (*row).pr;
//		                (*itail).sr = null;
//		                tail[list]  = itail;
//		                break;
//		        }
		    }
		
		    // Update the area under the number-in-list curve
		    //timest(eventLists.get(queueNum).size(), TIM_VAR + queueNum);
	}
	
	/**
	 * Removes the first record from queue number 25, the event list; since the 
	 * event list is kept in increasing order on the event time, we know this will 
	 * be the next event to occur.
	 */
	private void timing() 
	{
	    //Remove the first event from the event list and put it in transfer[]
	    removeFromQueue(FIRST, LIST_EVENT);
	
	    // Check for a time reversal
	    if(transfer[EVENT_TIME] < sim_time) 
	    {
	    	System.out.println("\nAttempt to schedule event type " + transfer[EVENT_TYPE] +
	    			" for time " + transfer[EVENT_TIME] + " at time " + sim_time); 
	        System.exit(1);
	    }
	
	    // Advance the simulation clock and set the next event type
	    sim_time        = transfer[EVENT_TIME];
	    next_event_type = transfer[EVENT_TYPE];
	}
	
	/**
	 * The user can invoke this function to schedule an event to occur at timeOfEvent 
	 * (a double) of type typeOfEvent (an int) into the event list. Normally, EventSchedule 
	 * will be invoked to schedule an event in the simulated future, so timeOfEvent would 
	 * be of the form simTime + timeInterval, where timeInterval is an interval of time 
	 * from now until the event is to happen.
	 * @param timeOfEvent
	 * @param typeOfEvent
	 */
	private void eventSchedule(double timeOfEvent, int typeOfEvent) 
	{
	    //transfer[EVENT_TIME] = timeOfEvent;
	    transfer[EVENT_TYPE] = typeOfEvent;
	    //list_file(INCREASING, LIST_EVENT);
	}
	
    private static float high, low, value;
	/**
	 * Cancels (removes) the first event in the event list with event type eventType (an int), 
	 * if there is such an event. If the event list does not have an event of type eventType, 
	 * no action is taken by EventCancel. If EventCancel finds an event of type eventType and 
	 * cancels it, the function returns an int value of 1; if no such event is found, the 
	 * function returns an int value of 0.
	 * @param eventType
	 * @return
	 */
	private int eventCancel(int eventType) 
	{
	    // If the event list is empty, do nothing and return 0
	    if(eventLists.get(LIST_EVENT).size() == 0) return 0;
	
	    // Search the event list
//	    row   = head[LIST_EVENT];
//	    low   = event_type - EPSILON;
//	    high  = event_type + EPSILON;
//	    value = (*row).value[EVENT_TYPE] ;
	
//	    while (((value <= low) || (value >= high)) && (row != tail[LIST_EVENT]))
//	    {
//	        row   = (*row).sr;
//	        value = (*row).value[EVENT_TYPE];
//	    }
	
	    // Check to see if this is the end of the event list
//	    if (row == tail[LIST_EVENT]) 
//	    {
//	
//	        // Double check to see that this is a match
//	        if ((value > low) && (value < high)) 
//	        {
//	            list_remove(LAST, LIST_EVENT);
//	            return 1;
//	        }
//	
//	        else // no match
//	            return 0;
//	    }
	
	    // Check to see if this is the head of the list.  If it is at the head, then
	    // it MUST be a match
//	    if (row == head[LIST_EVENT]) 
//	    {
//	        removeFromQueue(FIRST, LIST_EVENT);
//	        return 1;
//	    }
	
	    // Else remove this event somewhere in the middle of the event list
	
	
	    // Update the area under the number-in-event-list curve
	    //timest(eventLists.get(LIST_EVENT).size(), TIM_VAR + LIST_EVENT);
	    return 1;
	}

	/**
	 * Schedule an event at time event_time of type event_type.  If attributes
	 * beyond the first two (reserved for the event time and the event type) are
	 * being used in the event list, it is the user's responsibility to place their
	 * values into the transfer array before invoking event_schedule.
	 * @param time_of_event
	 * @param type_of_event
	 */
	private void event_schedule(double time_of_event, int type_of_event)
	{
	    transfer[EVENT_TIME] = time_of_event;
	    transfer[EVENT_TYPE] = type_of_event;
	    insertInQueue(INCREASING, LIST_EVENT);
	}
	
	private float sampst(double d, int variable)
	{
	//
	///* Initialize, update, or report statistics on discrete-time processes:
	//   sum/average, max (default -1E30), min (default 1E30), number of observations
	//   for sampst variable "variable", where "variable":
	//       = 0 initializes accumulators
	//       > 0 updates sum, count, min, and max accumulators with new observation
	//       < 0 reports stats on variable "variable" and returns them in transfer:
	//           [1] = average of observations
	//           [2] = number of observations
	//           [3] = maximum of observations
	//           [4] = minimum of observations */
	//
	//    static int   ivar, num_observations[SVAR_SIZE];
	//    static float max[SVAR_SIZE], min[SVAR_SIZE], sum[SVAR_SIZE];
	//
	//    /* If the variable value is improper, stop the simulation. */
	//
	//    if(!(variable >= -MAX_SVAR) && (variable <= MAX_SVAR)) {
	//        printf("\n%d is an improper value for a sampst variable at time %f\n",
	//            variable, sim_time);
	//        exit(1);
	//    }
	//
	//    /* Execute the desired option. */
	//
	//    if(variable > 0) { /* Update. */
	//        sum[variable] += value;
	//        if(value > max[variable]) max[variable] = value;
	//        if(value < min[variable]) min[variable] = value;
	//        num_observations[variable]++;
	//        return 0.0;
	//    }
	//
	//    if(variable < 0) { /* Report summary statistics in transfer. */
	//        ivar        = -variable;
	//        transfer[2] = (float) num_observations[ivar];
	//        transfer[3] = max[ivar];
	//        transfer[4] = min[ivar];
	//        if(num_observations[ivar] == 0)
	//            transfer[1] = 0.0;
	//        else
	//            transfer[1] = sum[ivar] / transfer[2];
	//        return transfer[1];
	//    }
	//
	//    /* Initialize the accumulators. */
	//
	//    for(ivar=1; ivar <= MAX_SVAR; ++ivar) {
	//        sum[ivar]              = 0.0;
	//        max[ivar]              = -INFINITY;
	//        min[ivar]              =  INFINITY;
	//        num_observations[ivar] = 0;
	//    }
	}
	
    static double area[TVAR_SIZE], max[TVAR_SIZE], min[TVAR_SIZE],
    preval[TVAR_SIZE], tlvc[TVAR_SIZE], treset;
	/**
	 * Initialize, update, or report statistics on continuous-time processes:
	 * integral/average, max (default -1e30), min (default 1e30) for timest 
	 * variable "variable", where:
	 * 	= 0 initializes counters
	 *  > 0 updates area, min, and max accumulators with new level of variable
	 *  < 0 reports stats on variable "variable", and returns them in transfer:
	 *  	[1] = time-average of variable updated to the time of this call
	 *  	[2] = maximum value variable has attained
	 *  	[3] = minimum value variable has attained
	 *  Note that variable TIM_VAR + 1 through TVAR_SIZE are used for automatic 
	 *  record-keeping on the length of lists 1 through MAX_LIST
	 * @param d
	 * @param variable
	 * @return
	 */
	private double timest(double d, int variable)
	{
	
	    int          ivar;
	
	    /* If the variable value is improper, stop the simulation. */
	
	    if(!(variable >= -MAX_TVAR) && (variable <= MAX_TVAR)) 
	    {
	    	System.out.println(variable + " is an improper value for a timest variable at time " + sim_time);
	    	System.exit(1);
	    }
	
	    /* Execute the desired option. */
	
	    if(variable > 0) { /* Update. */
	        area[variable] += (sim_time - tlvc[variable]) * preval[variable];
	        if(value > max[variable]) max[variable] = value;
	        if(value < min[variable]) min[variable] = value;
	        preval[variable] = value;
	        tlvc[variable]   = sim_time;
	        return 0;
	    }
	
	    if(variable < 0) { /* Report summary statistics in transfer. */
	        ivar         = -variable;
	        area[ivar]   += (sim_time - tlvc[ivar]) * preval[ivar];
	        tlvc[ivar]   = sim_time;
	        transfer[1]  = area[ivar] / (sim_time - treset);
	        transfer[2]  = max[ivar];
	        transfer[3]  = min[ivar];
	        return transfer[1];
	    }
	
	    /* Initialize the accumulators. */
	
	    for(ivar = 1; ivar <= MAX_TVAR; ++ivar) {
	        area[ivar]   = 0;
	        max[ivar]    = Double.MIN_VALUE;
	        min[ivar]    = Double.MAX_VALUE;
	        preval[ivar] = 0;
	        tlvc[ivar]   = sim_time;
	    }
	    treset = sim_time;
	}
	
	/**
	 * Report statistics on the length of list "list" in transfer:
	 * 	[1] = time-average of list length updated to the time of this call
	 *  [2] = maximum length list has attained
	 *  [3] = minimum length list has attained
	 * This uses timest variable TIM_VAR + list
	 * @param list
	 * @return
	 */
	private float filest(int list)
	{
	    return timest(0.0, -(TIM_VAR + list));
	}
	
	/**
	 * Write samst statistics for variable lowvar through highvar on 
	 * file unit
	 * @param writer
	 * @param lowvar
	 * @param highvar
	 */
	private void out_sampst(PrintWriter writer, int lowvar, int highvar)
	{
	
	    int ivar, iatrr;
	
	    if(lowvar>highvar || lowvar > MAX_SVAR || highvar > MAX_SVAR) return;
	
	    writer.println("sampst                         Number");
	    writer.println("variable                          of");
	    writer.println("number       Average           values          Maximum		minumum");
	    writer.println("----------------------------------------------------------");
	    for(ivar = lowvar; ivar <= highvar; ++ivar) {
	        writer.println("\n " + ivar);
	        sampst(0.00, -ivar);
	        for(iatrr = 1; iatrr <= 4; ++iatrr) pprint_out(writer, iatrr);
	    }
	    writer.println("----------------------------------------------------------\n\n\n");
	}
	
	/**
	 * Write timest statistics for variables lowvar through highvar on
	 * file unit
	 * @param writer
	 * @param lowvar
	 * @param highvar
	 */
	private void out_timest(PrintWriter writer, int lowvar, int highvar)
	{
	
	    int ivar, iatrr;
	
	    if(lowvar > highvar || lowvar > TIM_VAR || highvar > TIM_VAR ) return;
	
	
	    writer.println("timest");
	    writer.println("variable 	Time");
	    writer.println("number 		average		maximum		minumum");
	    writer.println("--------------------------------------------------------");
	    for(ivar = lowvar; ivar <= highvar; ++ivar) {
	    	writer.println("\n " + ivar);
	        timest(0.00, -ivar);
	        for(iatrr = 1; iatrr <= 3; ++iatrr) pprint_out(writer, iatrr);
	    }
	    writer.println("----------------------------------------------------------\n\n\n");
	}
	
	/**
	 * Write timest list-length statistics for lists lowlist through highlist on
	 * file "unit"
	 */
	private void out_filest(PrintWriter writer, int lowlist, int highlist)
	{
	    int list, iatrr;
	
	    if(lowlist > highlist || lowlist > MAX_LIST || highlist > MAX_LIST) return;
	
	    writer.println("\n  File 		Time");
	    writer.println("  number		average		maximum			minumum");
	    writer.println("-----------------------------------------------------");
	    for(int i = lowlist; i <= highlist; ++i) 
	    {
	    	writer.println("\n"+i);
	        filest(list);
	        for(iatrr = 1; iatrr <= 3; ++iatrr) pprint_out(writer, iatrr);
	    }
	    writer.println("-----------------------------------------------------");
	    writer.println("\n\n\n");
	}
	
	/**
	 * Write ith entry in transfer to file 
	 */
	private void pprint_out(PrintWriter writer, int i)
	{
	    if(transfer[i] == -1e30 || transfer[i] == 1e30)
	    	writer.println("0.00");
	    else
	    	writer.println(transfer[i]);
	}

}
