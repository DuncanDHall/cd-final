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
    	public TableEntry(Node nextHop) {
    		this.nextHop = nextHop;
    	}
    }

	HashMap<Node, TableEntry> routingTable;
    int id;

    public Node(int id) {
        this.id = id;
        this.routingTable = new HashMap<>();
    }

    public void addTableEntry(Node destination, Node nextHop) {
    	this.routingTable.put(destination, new TableEntry(nextHop));
    }

    //look up in routing table
    public Node getNextHop(Node destination) {
    	return routingTable.get(destination).nextHop;
    }

}
