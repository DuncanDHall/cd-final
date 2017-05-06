import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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

    public static void main(String[] args) {
        Global g = new Global(5, false);
        RawEvent[] dataTransferEvents = g.generateDataTransferEvents();

        AODVEvent[] fullEvents = AODVHelper.expandEvents(dataTransferEvents);

        // TODO: save fullEvents to csv

        // TODO-stretch: Visualizer.visualize(fullEvents);
    }
}
