import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.PriorityQueue;

/**
 * Created by duncan on 5/5/17.
 *
 * Assists in expanding a data transfer timetable (RawEvent[]) into a set of AODVEvents
 */
public class AODVHelper {
    public static AODVEvent[] expandEvents(RawEvent[] eventsTable, Graph<Node, Link> network) {
        // TODO: implement this
        PriorityQueue<AODVEvent> table = new PriorityQueue<>();
        for(RawEvent e : eventsTable) {
        	
        	//table.offer(e);
        }
        throw new NotImplementedException();
    }
}
