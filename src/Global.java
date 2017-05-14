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


    public Global(int numNodes, int numEdges, boolean dynamic) {
        this.network = generateSparseGraph(numNodes, numEdges);
        nodeLookup = new HashMap<>();
        for (Node n : network.getVertices()) {
            nodeLookup.put(n.id, n);
        }

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

    public RawEvent[] generateDataTransferEvents(int n, int e) {
        // generate a deterministic thin table of 'send data' events
        RawEvent[] events = new RawEvent[e];
        for (int i = 0; i < e; i++) {
            int source = (int) (Math.random() * n);
            int destination = source;
            while (destination == source) {
                destination = (int) (Math.random() * n);
            }
            String msg = "node " + source + " to node " + destination;
            events[i] = new RawEvent(i * 5, source, destination, msg);
        }

        return events;
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

        Global g = new Global(100, 300, false);

        RawEvent[] raws = g.generateDataTransferEvents(g.network.getVertexCount(), 3);
        System.out.println("ahaehrjkaejh");
        AODVEvent[] events = AODVHelper.expandEvents(raws, g.network, g.nodeLookup);

        System.out.println("herer");
        CSVHelper.writeCSV(events);

        // The Layout<V, E> is parameterized by the vertex and edge types
        Layout<Integer, String> layout = new CircleLayout(g.network);
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
