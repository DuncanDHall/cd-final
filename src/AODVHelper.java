import edu.uci.ics.jung.graph.Graph;

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
                            System.out.println(time);
                        }
                        n.addTableEntry(source, next, hopCount);
                        if (n.equals(destination)) {
                            timeDestinationReached = time;
                            System.out.println(timeDestinationReached);
                        }
                        table.offer(new AODVEvent(hopCount, 1, next, n, source, destination, ""));
                        nextNodes.offer(n);
                    }
                }
                visited.add(next);
            }

            // Step 2: RREP sent destination -> source
            time = timeDestinationReached - 1;
            Node current = destination;
            Node next;
            int hopCount = 0;
            while (!current.equals(source)) {
                time++;
                hopCount++;
                next = current.getNextHop(source);
                next.addTableEntry(destination, current, hopCount);
                table.offer(new AODVEvent(time, 2, current, next, destination, source, ""));
                current = next;
            }

            for (Node n: network.getVertices()) n.printTable();

            // Step 3: data sent source -> destination
            current = source;
            while(!current.equals(destination)) {
            	time += 1;
            	next = current.getNextHop(destination);
            	table.offer(new AODVEvent(time, 0, current, next, source, destination, e.getMsg()));
            	current = next;
            }
        }

        AODVEvent[] res = new AODVEvent[table.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = table.poll();
        }

        return res;
    }
}
