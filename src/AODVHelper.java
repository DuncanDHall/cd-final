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

            // Step 1: RREQ flood network
            // note that this propagates RREQ through the destination
            // TODO: RREQ messages should be sent to nodes that already received them, just not forwarded by those nodes
            ArrayList<Node> nextNodes = new ArrayList<>();
            ArrayList<Node> currentNodes = new ArrayList<>();
            currentNodes.add(source);

            HashSet<Node> visited = new HashSet<>();

            int timeDestinationReached = 0;

            for (int hopCount = 1; !currentNodes.isEmpty(); hopCount++) {
                for (Node node : currentNodes) {
                    visited.add(node);
                    for (Node neighbor : network.getNeighbors(node)) {
                        if (!visited.contains(neighbor)){
                            if (neighbor.equals(destination)) timeDestinationReached = hopCount + e.getTime();
                            neighbor.addTableEntry(source, node, hopCount);
                            table.add(new AODVEvent(hopCount + e.getTime() - 1, 1, node, neighbor, source, destination, ""));
                            nextNodes.add(neighbor);
                        }
                    }
                }
                currentNodes = nextNodes;
                nextNodes = new ArrayList<>();
            }


            // Step 2: RREP sent destination -> source
            int time = timeDestinationReached - 1;
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

//            for (Node n: network.getVertices()) n.printTable();

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
