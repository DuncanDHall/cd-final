import java.util.HashMap;
import java.util.HashSet;

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
    	
    	Node destination;
        Node nextHop;
    	int hopCount;

    	public TableEntry(Node destination, Node nextHop, int hopCount) {
    	    this.destination = destination;
    		this.nextHop = nextHop;
    		this.hopCount = hopCount;
    	}

        @Override
        public String toString() {
            return String.format("TableEntry: %s, %s, %d", destination, nextHop, hopCount);
        }
    }




	HashMap<Node, TableEntry> routingTable;
    int id;
    int floodID;
    HashSet<String> knownFloods;
    HashMap<String, String> unsentMessages;


    public Node(int id) {
        this.id = id;
        this.routingTable = new HashMap<>();
        floodID = 0;
        knownFloods = new HashSet<>();
        unsentMessages = new HashMap<>();

        addTableEntry(this, this, 0);
    }

    public String genFloodID(String message) {
        floodID++;
        String strID = id + "-" + floodID;
        unsentMessages.put(strID, message);
        return strID;
    }

    public boolean knowsFlood(String floodID) {
        return !knownFloods.add(floodID);
    }

    public void addTableEntry(Node destination, Node nextHop, int hopCount) {
        // boolean returns true if destination has been reached
    	this.routingTable.put(destination, new TableEntry(destination, nextHop, hopCount));
    }

    //look up in routing table
    public Node getNextHop(Node destination) {
        if (!routingTable.containsKey(destination)) return null;
    	return routingTable.get(destination).nextHop;
    }

    public int getHopCount(Node destination) {
        return routingTable.get(destination).hopCount;
    }

    public String getMessageForFloodID(String floodID) {
        return unsentMessages.get(floodID);
    }






    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return node.id == id;
    }

    @Override
    public String toString() {
        return String.format("Node %d", id);
    }

}
