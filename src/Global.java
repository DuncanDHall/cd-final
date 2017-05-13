import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import java.awt.*;
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

        Node first = nodes[0] = new Node(0);
        network.addVertex(first);
        Node current;
        for(int i = 1; i < n; i++) {
            current = nodes[i] = new Node(i);
            int r = (int)(Math.random()*(i-1) + 0);
            System.out.println(r);
            Node node = nodes[r];
            network.addVertex(current);
            network.addEdge(new Link(1, i), current, node);
        }
        
        //then randomly create the remaining links
        if(s > n) {
            int numLinks = s-n;
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

        Global g = new Global(5, false);

        Graph<Node, Link> network = g.generateSparseGraph(7, 7);

        // The Layout<V, E> is parameterized by the vertex and edge types
        Layout<Integer, String> layout = new CircleLayout(network);
        layout.setSize(new Dimension(300,300)); // sets the initial size of the space
        // The BasicVisualizationServer<V,E> is parameterized by the edge types
        BasicVisualizationServer<Integer,String> vv =
                new BasicVisualizationServer<Integer,String>(layout);
        vv.setPreferredSize(new Dimension(350,350)); //Sets the viewing area size
        JFrame frame = new JFrame("Simple Graph View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);



        // TODO: save fullEvents to csv

        // TODO-stretch: Visualizer.visualize(fullEvents);
    }
}
