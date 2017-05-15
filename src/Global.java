import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by duncan on 5/5/17.
 *
 * This project uses JUNG to model it's network. Here's a tutorial:
 * http://www.grotto-networking.com/JUNG/JUNG2-Tutorial.pdf
 *
 */
public class Global {

    /* network is represented by a JUNG graph:
     *      vertices are Nodes representing different machines in the network
     *      edges are integers representing distances between machines
     */
    Graph<Node, Link> network;
    HashMap<Integer, Node> nodeLookup;


    public Global(int numNodes, int numEdges, boolean dynamic) {
        this(generateSparseGraph(numNodes, numEdges));

        if (dynamic) {
            throw new NotImplementedException();
            // TODO-stretch: implement dynamic network
        }
    }

    public Global(Graph<Node, Link> network) {
        this.network = network;
        nodeLookup = new HashMap<>();
        for (Node n : network.getVertices()) {
            nodeLookup.put(n.id, n);
        }
    }

    public RawEvent[] generateDataTransferEvents(int n, int e, int interval) {
        // generate a deterministic thin table of 'send data' events
        RawEvent[] events = new RawEvent[e];
        for (int i = 0; i < e; i++) {
            int source = (int) (Math.random() * n);
            int destination = source;
            while (destination == source) {
                destination = (int) (Math.random() * n);
            }
            String msg = "node " + source + " to node " + destination;
            events[i] = new RawEvent(i * interval, source, destination, msg);
        }

        return events;
    }

    //generate sparse graph of n nodes, s edges
    public static Graph<Node, Link> generateSparseGraph(int n, int s) {
        //generate ring
        Graph<Node, Link> network = new SparseGraph<>();
        Node[] nodes = new Node[n];

        Node first = nodes[0] = new Node(0);
        network.addVertex(first);
        Node current;
        for(int i = 1; i < n; i++) {
            current = nodes[i] = new Node(i);
            int r = (int)(Math.random()*(i-1) + 0);
            Node node = nodes[r];
            network.addVertex(current);
            network.addEdge(new Link(1, i), current, node);
        }
        
        //then randomly create the remaining links
        if(s >= n) {
            int numLinks = s-n+1;
            for(int i = 0; i < numLinks; i++) {
                int i1 = (int)(Math.random()*n);
                int i2 = (int)(Math.random()*n);
                Node n1 = nodes[i1];
                Node n2 = nodes[i2];
                if((i1 == i2) || (network.getNeighbors(n1).contains(n2))) {
                    i--;
                } else {
                    network.addEdge(new Link(1, n+i), n1, n2);
                }
            }
        }

        return network;
    }


    public static void main(String[] args) {

        // User input of parameters
        Scanner reader = new Scanner(System.in);
        System.out.println("Please specify the number of nodes (int): ");
        int numNodes = reader.nextInt();
        System.out.println("Please specify the average degree of the nodes (double):\n(all nodes will be at least degree 1, and the network will always be connected)");
        int numEdges = (int) (Double.parseDouble(reader.next()) * numNodes / 2);
        System.out.println("Please specify the number of data transfers to be generated (int):");
        int dataTransfers = reader.nextInt();
        System.out.println("Please specify the number of time steps between messages (int):");
        int timeSteps = reader.nextInt();


        // generate a global state with static network
        Global g = new Global(numNodes, numEdges, false);

        // generate data transfers and associated AODV events
        RawEvent[] raws = g.generateDataTransferEvents(numNodes, dataTransfers, timeSteps);
        AODVEvent[] events = AODVHelper.expandEvents(raws, g.network, g.nodeLookup);

        // log the generated events for user inspection
        CSVHelper.writeCSV(events);

        // allows us to view the network layout
        Visualizer.visualizeNetwork(g.network);

        // TODO-stretch: Visualizer.visualize(fullEvents);  // this will be the animated representation
    }
}
