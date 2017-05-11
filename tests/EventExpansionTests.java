import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;

/**
 * Created by duncan on 5/6/17.
 */
public class EventExpansionTests {

    private Global g;
    private Node[] nodes;

    @Before
    public void setup() {
        nodes = new Node[5];

        Graph<Node, Link> network = new SparseGraph<>();

        // create a linear network of 5 nodes
        Node previous = null;
        for (int i = 0; i < 2; i++) {
            Node n = new Node(i);
            nodes[i] = n;
            network.addVertex(n);
            if (previous != null) {
                network.addEdge(new Link(1, i - 1), previous, n);
            }
            previous = n;
        }

        g = new Global(network);
    }

    @Test
    public void testExpand1() {
        RawEvent[] rawEvents = new RawEvent[1];
        rawEvents[0] = new RawEvent(1, 0, 1, "<node 0 to node 1>");

        AODVEvent[] expectedEvents = new AODVEvent[3];
        // TODO: implement these events
        expectedEvents[0] = new AODVEvent(1, 1, nodes[0], nodes[1], nodes[0], nodes[1], ""); // RREQ 0 -> 1
        expectedEvents[1] = new AODVEvent(2, 2, nodes[1], nodes[0], nodes[1], nodes[0], ""); // RREP 1 -> 0
        expectedEvents[2] = new AODVEvent(3, 0, nodes[0], nodes[1], nodes[0], nodes[1], "<node 0 to node 1>"); // data 0 -> 1

        AODVEvent[] res = AODVHelper.expandEvents(rawEvents, g.network, g.nodeLookup);
        System.out.println(Arrays.toString(res));

        assertArrayEquals(
                res,
                expectedEvents
        );
    }

//    @Test
    public void testExpand2() {
        // TODO: finish this test
        RawEvent[] rawEvents = new RawEvent[1];
        rawEvents[1] = new RawEvent(1, 0, 4, "<node 0 to node 4>");

        AODVEvent[] aodvEvents = AODVHelper.expandEvents(rawEvents, g.network, g.nodeLookup);
    }

}
