import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;

/**
 * Created by duncan on 5/6/17.
 */
public class LinearNetworkTests {

    private Global g;
    private Graph<Node, Link> network;
    private Node[] nodes;


    private void makeLinearNetwork(int numNodes) {
        network = new SparseGraph<>();

        nodes = new Node[numNodes];

        // create a linear network of 5 nodes
        Node previous = null;
        for (int i = 0; i < numNodes; i++) {
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
    public void testLinearNetwork1() {
        /*
         *      O-1     message from 0 -> 1
         */
        makeLinearNetwork(2);

        RawEvent[] rawEvents = new RawEvent[1];
        rawEvents[0] = new RawEvent(1, 0, 1, "<node 0 to node 1>");

        AODVEvent[] expectedEvents = new AODVEvent[3];
        expectedEvents[0] = new AODVEvent(1, 1, nodes[0], nodes[1], nodes[0], nodes[1], "0-1", "", 1); // RREQ 0 -> 1
        expectedEvents[1] = new AODVEvent(2, 2, nodes[1], nodes[0], nodes[1], nodes[0], "0-1", "", 1); // RREP 1 -> 0
        expectedEvents[2] = new AODVEvent(3, 0, nodes[0], nodes[1], nodes[0], nodes[1], "0-1", "<node 0 to node 1>", 1); // data 0 -> 1

        AODVEvent[] res = AODVHelper.expandEvents(rawEvents, g.network, g.nodeLookup);
        System.out.println(Arrays.toString(res));

        assertArrayEquals(
                expectedEvents,
                res
        );
    }

    @Test
    public void testLinearNetwork2() {
        /*
         *      O-1-2       message 0 -> 2
         */
        makeLinearNetwork(3);

        RawEvent[] rawEvents = new RawEvent[1];
        rawEvents[0] = new RawEvent(1, 0, 2, "<node 0 to node 2>");

        AODVEvent[] expectedEvents = new AODVEvent[7];
        expectedEvents[0] = new AODVEvent(1, 1, nodes[0], nodes[1], nodes[0], nodes[2], "0-1", "", 1); // RREQ 0 -> 1
        expectedEvents[1] = new AODVEvent(2, 1, nodes[1], nodes[2], nodes[0], nodes[2], "0-1", "", 2);
        expectedEvents[2] = new AODVEvent(3, 2, nodes[2], nodes[1], nodes[2], nodes[0], "0-1", "", 1); // RREP 1 -> 0
        expectedEvents[3] = new AODVEvent(4, 2, nodes[1], nodes[0], nodes[2], nodes[0], "0-1", "", 2); // RREP 1 -> 0
        expectedEvents[4] = new AODVEvent(5, 0, nodes[0], nodes[1], nodes[0], nodes[2], "0-1", "<node 0 to node 2>", 1); // data 0 -> 1
        expectedEvents[5] = new AODVEvent(6, 0, nodes[1], nodes[2], nodes[0], nodes[2], "0-1", "<node 0 to node 2>", 2); // data 0 -> 1

        AODVEvent[] res = AODVHelper.expandEvents(rawEvents, g.network, g.nodeLookup);
        for (AODVEvent e : res) {
            System.out.println(e);
        }

        assertArrayEquals(
                expectedEvents,
                res
        );
    }

    @Test
    public void testLinearNetwork3() {
        /*
         *      O-1-2       message 0 -> 1
         */
        // NOTE this demonstrates that RREQ propagation continues through destination node

        makeLinearNetwork(3);

        RawEvent[] rawEvents = new RawEvent[1];
        rawEvents[0] = new RawEvent(1, 0, 1, "<node 0 to node 1>");

        AODVEvent[] expectedEvents = new AODVEvent[3];
        expectedEvents[0] = new AODVEvent(1, 1, nodes[0], nodes[1], nodes[0], nodes[1], "0-1", "", 1);
        expectedEvents[1] = new AODVEvent(2, 2, nodes[1], nodes[0], nodes[1], nodes[0], "0-1", "", 1);
        expectedEvents[2] = new AODVEvent(3, 0, nodes[0], nodes[1], nodes[0], nodes[1], "0-1", "<node 0 to node 1>", 1);

        AODVEvent[] res = AODVHelper.expandEvents(rawEvents, g.network, g.nodeLookup);
        for (AODVEvent e : res) {
            System.out.println(e);
        }

        assertArrayEquals(
                expectedEvents,
                res
        );
    }

//    @Test
//    public void testLinearNetwork4() {
//        /*
//         *      O-1       message 0 -> 1 (twice)
//         */
//        makeLinearNetwork(2);
//
//        RawEvent[] rawEvents = new RawEvent[2];
//        rawEvents[0] = new RawEvent(1, 0, 1, "<node 0 to node 1 a>");
//        rawEvents[1] = new RawEvent(2, 0, 1, "<node 0 to node 1 b>");
//
//        AODVEvent[] expectedEvents = new AODVEvent[6];
//        expectedEvents[0] = new AODVEvent(1, 1, nodes[0], nodes[1], nodes[0], nodes[1], floodID, "", hopCount);
//        expectedEvents[1] = new AODVEvent(2, 1, nodes[0], nodes[1], nodes[0], nodes[1], floodID, "", hopCount);
//        expectedEvents[2] = new AODVEvent(2, 2, nodes[1], nodes[0], nodes[1], nodes[0], floodID, "", hopCount);
//        expectedEvents[3] = new AODVEvent(3, 0, nodes[0], nodes[1], nodes[0], nodes[1], floodID, "<node 0 to node 1 a>", hopCount);
//        expectedEvents[4] = new AODVEvent(3, 2, nodes[1], nodes[0], nodes[1], nodes[0], floodID, "", hopCount);
//        expectedEvents[5] = new AODVEvent(4, 0, nodes[0], nodes[1], nodes[0], nodes[1], floodID, "<node 0 to node 1 b>", hopCount);
//
//        AODVEvent[] res = AODVHelper.expandEvents(rawEvents, g.network, g.nodeLookup);
//        for (AODVEvent e : res) {
//            System.out.println(e);
//        }
//
//        assertArrayEquals(
//                expectedEvents,
//                res
//        );
//    }

//    @Test
//    public void testLinearNetwork5() {
//        /*
//         *      O-1       message 0 -> 1 (twice)
//         */
//        makeLinearNetwork(2);
//
//        RawEvent[] rawEvents = new RawEvent[2];
//        rawEvents[0] = new RawEvent(1, 0, 1, "<node 0 to node 1 a>");
//        rawEvents[1] = new RawEvent(5, 0, 1, "<node 0 to node 1 b>");
//
//        AODVEvent[] expectedEvents = new AODVEvent[4];
//        expectedEvents[0] = new AODVEvent(1, 1, nodes[0], nodes[1], nodes[0], nodes[1], "0-1", "", hopCount);
//        expectedEvents[1] = new AODVEvent(2, 2, nodes[1], nodes[0], nodes[1], nodes[0], "0-1", "", hopCount);
//        expectedEvents[2] = new AODVEvent(3, 0, nodes[0], nodes[1], nodes[0], nodes[1], "0-1", "<node 0 to node 1 a>", hopCount);
//        expectedEvents[3] = new AODVEvent(5, 0, nodes[0], nodes[1], nodes[0], nodes[1], "0-1", "<node 0 to node 1 b>", hopCount);
//
//        AODVEvent[] res = AODVHelper.expandEvents(rawEvents, g.network, g.nodeLookup);
//        for (AODVEvent e : res) {
//            System.out.println(e);
//        }
//
//        assertArrayEquals(
//                expectedEvents,
//                res
//        );
//    }

}
