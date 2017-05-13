import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * Created by duncan on 5/12/17.
 */

public class NonlinearNetworkTests {

    private Global g;
    private Graph<Node, Link> network;
    private Node[] nodes;

    private Graph<Node, Link> makeRingNetwork(int n) {
        Graph<Node, Link> network = new SparseGraph<>();
        Node first, previous;
        nodes = new Node[n];
        first = previous = nodes[0] = new Node(0);
        Node current = null;
        for (int i = 1; i < n; i++) {
            current = nodes[i] = new Node(i);
            network.addVertex(current);
            current = new Node(i);
            network.addEdge(new Link(1, i), current, previous);
            previous = current;
        }
        if (n > 2) network.addEdge(new Link(1, n), first, current);

        return network;
    }

    @Test
    public void testRingNetworks() {

        // test with 3 nodes
        network = makeRingNetwork(3);
        Global g = new Global(network);

        RawEvent[] rawEvents = new RawEvent[1];
        rawEvents[0] = new RawEvent(1, 0, 1, "<node 0 to node 1>");

        AODVEvent[] expectedEvents = new AODVEvent[6];
        expectedEvents[0] = new AODVEvent(1, 1, nodes[0], nodes[1], nodes[0], nodes[1], "");
        expectedEvents[1] = new AODVEvent(1, 1, nodes[0], nodes[2], nodes[0], nodes[1], "");
        expectedEvents[2] = new AODVEvent(2, 1, nodes[2], nodes[1], nodes[0], nodes[1], "");
        expectedEvents[3] = new AODVEvent(2, 2, nodes[1], nodes[0], nodes[1], nodes[0], "");
        expectedEvents[4] = new AODVEvent(3, 0, nodes[0], nodes[1], nodes[0], nodes[1], "<node 0 to node 1>");

        AODVEvent[] res = AODVHelper.expandEvents(rawEvents, network, g.nodeLookup);
        for (AODVEvent e : res) System.out.println(e);

        assertArrayEquals(expectedEvents, res);
    }

//    @Test
//    public void testLinearNetwork1() {
//        /*
//         *      O-1     message from 0 -> 1
//         */
//        makeLinearNetwork(2);
//
//        RawEvent[] rawEvents = new RawEvent[1];
//        rawEvents[0] = new RawEvent(1, 0, 1, "<node 0 to node 1>");
//
//        AODVEvent[] expectedEvents = new AODVEvent[3];
//        expectedEvents[0] = new AODVEvent(1, 1, nodes[0], nodes[1], nodes[0], nodes[1], ""); // RREQ 0 -> 1
//        expectedEvents[1] = new AODVEvent(2, 2, nodes[1], nodes[0], nodes[1], nodes[0], ""); // RREP 1 -> 0
//        expectedEvents[2] = new AODVEvent(3, 0, nodes[0], nodes[1], nodes[0], nodes[1], "<node 0 to node 1>"); // data 0 -> 1
//
//        AODVEvent[] res = AODVHelper.expandEvents(rawEvents, g.network, g.nodeLookup);
//        System.out.println(Arrays.toString(res));
//
//        assertArrayEquals(
//                expectedEvents,
//                res
//        );
//    }

}
