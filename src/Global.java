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
    Graph<Node, Integer> network;


    public Global(int numNodes, boolean dynamic) {
        this.network = new SparseGraph<>();
        // TODO: populate network with nodes according to numNodes

        if (dynamic) {
            throw new NotImplementedException();
            // TODO-stretch: implement dynamic network
        }
    }

    public Object generateDataTransferEvents() {
        // TODO: figure out the correct data type here
        // TODO: generate a deterministic thin table of 'send data' events
        // TODO-stretch: generate a random thin table of 'send data' events instead
        return null;
    }

    public static void main(String[] args) {
        Global g = new Global(5, false);
        Object dataTransferEvents = g.generateDataTransferEvents();

        // TODO: figure out the correct data type here
        // table = AODVHelper.expandEvents(dataTransferEvents);

        // TODO: save fullEvents to csv

        // TODO-stretch: Visualizer.visualize(fullEvents);
    }
}
