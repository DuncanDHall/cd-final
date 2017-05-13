import edu.uci.ics.jung.graph.Graph;

import java.util.*;

/**
 * Created by duncan on 5/5/17.
 *
 * Assists in expanding a data transfer timetable (RawEvent[]) into a set of AODVEvents
 */
public class AODVHelper {
    public static AODVEvent[] expandEvents(RawEvent[] eventsTable, Graph<Node, Link> network, HashMap<Integer, Node> nodeLookup) {
        Queue<RawEvent> rawEvents = new LinkedList<>(Arrays.asList(eventsTable));
        PriorityQueue<AODVEvent> table = new PriorityQueue<>();

        ArrayList<AODVEvent> currentEvents = new ArrayList<>();
        ArrayList<AODVEvent> nextEvents;


        for (int t = 0; t > eventsTable[eventsTable.length - 1].getTime() && currentEvents.isEmpty(); t++) {
            nextEvents = new ArrayList<>();

            // calculate next events based on current
            for (AODVEvent event : currentEvents) {

                Node previousNode = event.getFrom();
                Node currentNode = event.getTo();

                // RREQ
                if (event.isRREQ()) {
                    if (!currentNode.knowsFlood(event.getFloodID())) {
                        // propagating RREQ
                        currentNode.addTableEntry(event.getSource(), previousNode, event.getHopCount());
                        if (!currentNode.equals(event.getDestination())) {
                            for (Node nextNode : network.getNeighbors(event.getTo())) {
                                if (!nextNode.equals(previousNode)) {
                                    nextEvents.add(new AODVEvent(event, currentNode, nextNode));
                                }
                            }
                        } else {
                            // destination reached
                            Node destination = event.getSource();
                            nextEvents.add(new AODVEvent(
                                    t, 2,
                                    currentNode, currentNode.getNextHop(destination),
                                    currentNode, destination,
                                    event.getFloodID(), "",
                                    1));
                        }
                    }
                }

                // RREP
                else if (event.isRREP()) {
                    currentNode.addTableEntry(event.getSource(), previousNode, event.getHopCount());
                    if (currentNode.equals(event.getDestination())) {
                        nextEvents.add(new AODVEvent(
                                t, 0,
                                currentNode, currentNode.getNextHop(event.getSource()),
                                currentNode, event.getDestination(), event.getFloodID(),
                                currentNode.getMessageForFloodID(event.getFloodID()), 1
                        ));
                    }
                    else {
                        nextEvents.add(new AODVEvent(event, currentNode, currentNode.getNextHop(event.getDestination())));
                    }
                }

                // data
                else if (event.isData()) {
                    currentNode.addTableEntry(event.getSource(), previousNode, event.getHopCount());
                    if (currentNode.equals(event.getDestination())) {
                        System.out.println(event.getSource() + "'s message reached " + currentNode);
                    }
                }
            }


            // check for new raw events to be included in simulation
            if (t == rawEvents.peek().getTime()) {
                RawEvent e = rawEvents.poll();
                Node source = nodeLookup.get(e.getNodeFrom());
                Node destination = nodeLookup.get(e.getNodeTo());

                String floodID = source.genFloodID();

                // spawn a new RREQ for each neighbor of the source
                for (Node neighbor : network.getNeighbors(source)) {
                    nextEvents.add(new AODVEvent(t, 1, source, neighbor, source, destination, floodID, "", 1));
                }
            }


            // TODO: add all nextEvents to table
            currentEvents = nextEvents;
        }

//        for(RawEvent e : eventsTable) {
//
//            Node source = nodeLookup.get(e.getNodeFrom());
//            Node destination = nodeLookup.get(e.getNodeTo());
//            Node current;
//            Node next;
//            int time = e.getTime();
//
//            if (source.getNextHop(destination) == null) {
//
//                // Step 1: RREQ flood network
//                // note that this propagates RREQ through the destination
//                ArrayList<Node> nextNodes = new ArrayList<>();
//                ArrayList<Node> currentNodes = new ArrayList<>();
//                currentNodes.add(source);
//
//                HashSet<Node> visited = new HashSet<>();
//                visited.add(source);
//
//                Integer timeDestinationReached = null;
//
//                for (int hopCount = 1; !currentNodes.isEmpty(); hopCount++) {
//                    for (Node node : currentNodes) {
//                        for (Node neighbor : network.getNeighbors(node)) {
//                            table.add(new AODVEvent(hopCount + time - 1, 1, node, neighbor, source, destination, floodID, "", hopCount));
//                            if (!visited.contains(neighbor)) {
//                                visited.add(neighbor);
//                                neighbor.addTableEntry(source, node, hopCount);
//                                if (neighbor.equals(destination)) {
//                                    timeDestinationReached = hopCount + time;
//                                } else {
//                                    nextNodes.add(neighbor);
//                                }
//                            }
//                        }
//
//                    }
//                    currentNodes = nextNodes;
//                    nextNodes = new ArrayList<>();
//                }
//
//
//                // Step 2: RREP sent destination -> source
//                time = timeDestinationReached;
//                current = destination;
//                int hopCount = 0;
//                while (!current.equals(source)) {
//                    hopCount++;
//                    next = current.getNextHop(source);
//                    next.addTableEntry(destination, current, hopCount);
//                    table.offer(new AODVEvent(time, 2, current, next, destination, source, floodID, "", hopCount));
//                    current = next;
//                    time++;
//                }
//            }
//
//
//            // Step 3: data sent source -> destination
//            current = source;
//            while(!current.equals(destination)) {
//            	next = current.getNextHop(destination);
//            	table.offer(new AODVEvent(time, 0, current, next, source, destination, floodID, e.getMsg(), hopCount));
//            	current = next;
//                time += 1;
//            }
//        }

        AODVEvent[] res = new AODVEvent[table.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = table.poll();
        }

        return res;
    }
}
