import edu.uci.ics.jung.graph.Graph;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

/**
 * Created by duncan on 5/5/17.
 *
 * Assists in expanding a data transfer timetable (RawEvent[]) into a set of AODVEvents
 */
public class AODVHelper {
    public static AODVEvent[] expandEvents(RawEvent[] eventsTable, Graph<Node, Link> network, HashMap<Integer, Node> nodeLookup) {
        // TODO: implement this
        PriorityQueue<AODVEvent> table = new PriorityQueue<>();
        for(RawEvent e : eventsTable) {

            Node source = nodeLookup.get(e.getNodeFrom());
            Node destination = nodeLookup.get(e.getNodeTo());

            Queue<Node> nextNodes = new LinkedList<>();
            nextNodes.offer(source);

            HashSet<Node> visited = new HashSet<>();
            int time = 0;
            int timeDestinationReached = 0;

            // Step 1: RREQ flood network
            while (!nextNodes.isEmpty()) {
                Node next = nextNodes.poll();
                Collection<Node> ns = network.getNeighbors(next);
                for (Node n : ns) {
                    if (!visited.contains(n)) {
                        int hopCount = next.getHopCount(source) + 1;
                        if (time < hopCount + e.getTime()) {
                            time = hopCount + e.getTime();
                        }
                        if (n.addTableEntry(source, next, hopCount)) {
                            timeDestinationReached = time;
                        }
                        table.offer(new AODVEvent(hopCount, 1, next, n, source, destination, ""));
                        nextNodes.offer(n);
                    }
                }
                visited.add(next);
            }

            // Step 2: RREP sent destination -> source
            time = timeDestinationReached;
            Node current = destination;
            Node next;
            while (true) {
                time += 1;
                next = current.getNextHop(source);
                table.offer(new AODVEvent(time, 2, next, current, destination, source, ""));
                current = next;
            }

            // Step 3: data sent source -> destination
        }
        throw new NotImplementedException();
    }
}
