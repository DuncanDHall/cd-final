import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;

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


    public Global(int numNodes, boolean dynamic) {
        this.network = new SparseGraph<>();
        // TODO: populate network with nodes according to numNodes

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

    public RawEvent[] generateDataTransferEvents() {
        // generate a deterministic thin table of 'send data' events
        RawEvent[] events = new RawEvent[2];
        events[0] = new RawEvent(1, 0, 4, "<node 0 calling node 4>");
        events[1] = new RawEvent(11, 1, 3, "<node 1 calling node 3>");

        // TODO: test for events closer together, and concurrent events

        return events;

        // TODO-stretch: generate a random thin table of 'send data' events instead
    }

    //generate sparse graph of n nodes, s edges
    public Graph<Node, Link> generateSparseGraph(int n, int s) {
        //generate ring
        Graph<Node, Link> network = new SparseGraph<>();
        Node[] nodes = new Node[n];

        Node first = new Node(0);
        network.addVertex(first);
        Node current = null;
        for(int i = 1; i < n; i++) {
            current = nodes[i] = new Node(i);
            int r = (int)(Math.random()*(i-1) + 0);
            Node n = nodes[r];
            network.addVertex(current);
            network.addEdge(new Link(1, i), current, n);
        }
        
        //then randomly create the remaining links
        if(s > n) {
            int numLinks = s-n;
            for(int i = 0; i < numLinks; i++) {
                int i1 = (int)(Math.random()*n+0);
                int i2 = (int)(Math.random()*n+0);
                Node n1 = node[i1];
                Node n2 = node[i2];
                if((index1 == index2) || (network.getNeighbors(n1).contains(n2))) {
                    i--;
                } else {
                    network.addEdge(new Link(1, n+i), n1, n2);
                }
            }
        }
    }


    public static void main(String[] args) {
        Global g = new Global(5, false);
        RawEvent[] dataTransferEvents = g.generateDataTransferEvents();

        AODVEvent[] fullEvents = AODVHelper.expandEvents(dataTransferEvents, g.network, g.nodeLookup);


        // TODO: save fullEvents to csv

        // TODO-stretch: Visualizer.visualize(fullEvents);
    }
}
