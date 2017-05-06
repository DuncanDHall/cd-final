import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * Created by duncan on 5/6/17.
 */
public class EventExpansionTests {

    private Global g;

    @Before
    public void setup() {
        Graph<Node, Link> network = new SparseGraph<>();

        // create a linear network of 5 nodes
        Node previous = null;
        for (int i = 0; i < 5; i++) {
            Node n = new Node(i);
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
        rawEvents[0] = new RawEvent(1, 0, 1, "<node 0 to node 4>");

        AODVEvent[] expectedEvents = new AODVEvent[3];
        expectedEvents[0] = new AODVEvent(); // RREQ 0 -> 1
        expectedEvents[1] = new AODVEvent(); // RREP 1 -> 0
        expectedEvents[2] = new AODVEvent(); // data 0 -> 1

        assertArrayEquals(
                AODVHelper.expandEvents(rawEvents),
                expectedEvents
        );
    }

//    @Test
    public void testExpand2() {
        // TODO: finish this test
        RawEvent[] rawEvents = new RawEvent[1];
        rawEvents[1] = new RawEvent(1, 0, 4, "<node 0 to node 4>");

        AODVEvent[] aodvEvents = AODVHelper.expandEvents(rawEvents);
    }

}
