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
            network.addEdge(new Link(1, i), current, previous);
            previous = current;
        }
        if (n > 2) network.addEdge(new Link(1, n), first, current);

        return network;
    }

    @Test
    public void testRingNetworks1() {

        // test with 3 nodes
        network = makeRingNetwork(3);
        Global g = new Global(network);

        RawEvent[] rawEvents = new RawEvent[1];
        rawEvents[0] = new RawEvent(1, 0, 1, "<node 0 to node 1>");

        AODVEvent[] expectedEvents = new AODVEvent[6];
        expectedEvents[0] = new AODVEvent(1, 1, nodes[0], nodes[1], nodes[0], nodes[1], floodID, "", hopCount);
        expectedEvents[1] = new AODVEvent(1, 1, nodes[0], nodes[2], nodes[0], nodes[1], floodID, "", hopCount);
        expectedEvents[2] = new AODVEvent(2, 1, nodes[2], nodes[0], nodes[0], nodes[1], floodID, "", hopCount);
        expectedEvents[3] = new AODVEvent(2, 1, nodes[2], nodes[1], nodes[0], nodes[1], floodID, "", hopCount);
        expectedEvents[4] = new AODVEvent(2, 2, nodes[1], nodes[0], nodes[1], nodes[0], floodID, "", hopCount);
        expectedEvents[5] = new AODVEvent(3, 0, nodes[0], nodes[1], nodes[0], nodes[1], floodID, "<node 0 to node 1>", hopCount);

        AODVEvent[] res = AODVHelper.expandEvents(rawEvents, network, g.nodeLookup);
        for (AODVEvent e : res) System.out.println(e);

        assertArrayEquals(expectedEvents, res);
    }

    @Test
    public void testRingNetworks2() {

        // test with 3 nodes
        network = makeRingNetwork(4);
        Global g = new Global(network);

        RawEvent[] rawEvents = new RawEvent[1];
        rawEvents[0] = new RawEvent(1, 0, 2, "<node 0 to node 2>");

        AODVEvent[] expectedEvents = new AODVEvent[10];
        expectedEvents[0] = new AODVEvent(1, 1, nodes[0], nodes[1], nodes[0], nodes[2], floodID, "", hopCount);
        expectedEvents[1] = new AODVEvent(1, 1, nodes[0], nodes[3], nodes[0], nodes[2], floodID, "", hopCount);
        expectedEvents[2] = new AODVEvent(2, 1, nodes[1], nodes[0], nodes[0], nodes[2], floodID, "", hopCount);
        expectedEvents[3] = new AODVEvent(2, 1, nodes[1], nodes[2], nodes[0], nodes[2], floodID, "", hopCount);
        expectedEvents[4] = new AODVEvent(2, 1, nodes[3], nodes[0], nodes[0], nodes[2], floodID, "", hopCount);
        expectedEvents[5] = new AODVEvent(2, 1, nodes[3], nodes[2], nodes[0], nodes[2], floodID, "", hopCount);
        expectedEvents[6] = new AODVEvent(3, 2, nodes[2], nodes[1], nodes[2], nodes[0], floodID, "", hopCount);
        expectedEvents[7] = new AODVEvent(4, 2, nodes[1], nodes[0], nodes[2], nodes[0], floodID, "", hopCount);
        expectedEvents[8] = new AODVEvent(5, 0, nodes[0], nodes[1], nodes[0], nodes[2], floodID, "<node 0 to node 2>", hopCount);
        expectedEvents[9] = new AODVEvent(6, 0, nodes[1], nodes[2], nodes[0], nodes[2], floodID, "<node 0 to node 2>", hopCount);

        AODVEvent[] res = AODVHelper.expandEvents(rawEvents, network, g.nodeLookup);
        for (AODVEvent e : res) System.out.println(e);

        assertArrayEquals(expectedEvents, res);
    }

}
