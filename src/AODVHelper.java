import edu.uci.ics.jung.graph.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * Created by duncan on 5/5/17.
 *
 * Assists in expanding a data transfer timetable (RawEvent[]) into a set of AODVEvents
 */
public class AODVHelper {
    public static AODVEvent[] expandEvents(RawEvent[] eventsTable, Graph<Node, Link> network, HashMap<Integer, Node> nodeLookup) {
        PriorityQueue<AODVEvent> table = new PriorityQueue<>();
        for(RawEvent e : eventsTable) {

            Node source = nodeLookup.get(e.getNodeFrom());
            Node destination = nodeLookup.get(e.getNodeTo());
            Node current;
            Node next;
            int time = e.getTime();

            if (source.getNextHop(destination) == null) {

                // Step 1: RREQ flood network
                // note that this propagates RREQ through the destination
                ArrayList<Node> nextNodes = new ArrayList<>();
                ArrayList<Node> currentNodes = new ArrayList<>();
                currentNodes.add(source);

                HashSet<Node> visited = new HashSet<>();
                visited.add(source);

                Integer timeDestinationReached = null;

                for (int hopCount = 1; !currentNodes.isEmpty(); hopCount++) {
                    for (Node node : currentNodes) {
                        for (Node neighbor : network.getNeighbors(node)) {
                            table.add(new AODVEvent(hopCount + time - 1, 1, node, neighbor, source, destination, ""));
                            if (!visited.contains(neighbor)) {
                                visited.add(neighbor);
                                neighbor.addTableEntry(source, node, hopCount);
                                if (neighbor.equals(destination)) {
                                    timeDestinationReached = hopCount + time;
                                } else {
                                    nextNodes.add(neighbor);
                                }
                            }
                        }

                    }
                    currentNodes = nextNodes;
                    nextNodes = new ArrayList<>();
                }


                // Step 2: RREP sent destination -> source
                time = timeDestinationReached;
                current = destination;
                int hopCount = 0;
                while (!current.equals(source)) {
                    hopCount++;
                    next = current.getNextHop(source);
                    next.addTableEntry(destination, current, hopCount);
                    table.offer(new AODVEvent(time, 2, current, next, destination, source, ""));
                    current = next;
                    time++;
                }
            }


            // Step 3: data sent source -> destination
            current = source;
            while(!current.equals(destination)) {
            	next = current.getNextHop(destination);
            	table.offer(new AODVEvent(time, 0, current, next, source, destination, e.getMsg()));
            	current = next;
                time += 1;
            }
        }

        AODVEvent[] res = new AODVEvent[table.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = table.poll();
        }

        return res;
    }
}
