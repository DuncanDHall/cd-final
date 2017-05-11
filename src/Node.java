import java.util.HashMap;

/**
 * Created by duncan on 5/5/17.
 */
public class Node {
    // represents our network machines
    // TODO: layout class architecture
    // routing table
    // TODO: *then* implement

	//for routing table
    private class TableEntry {
    	
    	Node nextHop;
    	int hopCount;

    	public TableEntry(Node nextHop, int hopCount) {
    		this.nextHop = nextHop;
    		this.hopCount = hopCount;
    	}
    }

	HashMap<Node, TableEntry> routingTable;
    int id;

    public Node(int id) {
        this.id = id;
        this.routingTable = new HashMap<>();
    }

    public boolean addTableEntry(Node destination, Node nextHop, int hopCount) {
        // boolean returns true if destination has been reached
    	this.routingTable.put(destination, new TableEntry(nextHop, hopCount));
    	return this.equals(destination);
    }

    //look up in routing table
    public Node getNextHop(Node destination) {
    	return routingTable.get(destination).nextHop;
    }

    public int getHopCount(Node destination) {
        return routingTable.get(destination).hopCount;
    }

}
