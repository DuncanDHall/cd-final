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

        for (int t = 0; t <= eventsTable[eventsTable.length - 1].getTime() || !currentEvents.isEmpty(); t++) {
            System.out.println(t);
            nextEvents = new ArrayList<>();

            // calculate next events based on current
            for (AODVEvent event : currentEvents) {

                System.out.println("event processing");

                table.add(event);

                Node previousNode = event.getFrom();
                Node currentNode = event.getTo();

                // RREQ
                if (event.isRREQ()) {
                    System.out.println("RREQ");
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
                                currentNode, event.getSource(),
                                event.getFloodID(),
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
                    } else {
                        System.out.println(event.getSource() + " || " + event.getDestination());
                        nextEvents.add(new AODVEvent(event, currentNode, currentNode.getNextHop(event.getDestination())));
                    }
                }
            }


            // check for new raw events to be included in simulation
            if (!rawEvents.isEmpty() && t == rawEvents.peek().getTime()) {
                System.out.println("new raw added");
                RawEvent e = rawEvents.poll();
                Node source = nodeLookup.get(e.getNodeFrom());
                Node destination = nodeLookup.get(e.getNodeTo());

                String floodID = source.genFloodID(e.getMsg());

                // spawn a new RREQ for each neighbor of the source
                for (Node neighbor : network.getNeighbors(source)) {
                    System.out.println("neighbor added");
                    nextEvents.add(new AODVEvent(t, 1, source, neighbor, source, destination, floodID, "", 1));
                }
            }


            // TODO: add all nextEvents to table
            currentEvents = nextEvents;
        }

        AODVEvent[] res = new AODVEvent[table.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = table.poll();
        }

        return res;
    }
}
