/**
 * Created by duncan on 5/5/17.
 *
 * A timed event in the AODV events list
 */
public class AODVEvent implements Comparable {

    private int time;
    // message types | TODO: is there a less stringly-typed way to do this?
    private int msgType;
    // 0 -> Data
    // 1 -> RREQ
    // 2 -> RREP
    private Node from;
    private Node to;
    private Node source;
    private Node destination;
    private String floodID;
    private String message;
    private int hopCount;

    public AODVEvent(int time, int msgType, Node from, Node to, Node source, Node destination, String floodID, String message, int hopCount) {
        if (msgType > 2) {
            throw new IndexOutOfBoundsException("Valid AODVEvents have a msgtype of 0 (data), 1 (rreq), or 2 (rrep)");
        }
        this.time = time;
        this.msgType = msgType;
        this.from = from;
        this.to = to;
        this.source = source;
        this.destination = destination;
        this.floodID = floodID;
        this.message = message;
        this.hopCount = hopCount;
    }

    public boolean isData() { return msgType == 0; }
    public boolean isRREQ() { return msgType == 1; }
    public boolean isRREP() { return msgType == 2; }


    public AODVEvent(AODVEvent e, Node from, Node to) {
        this.time = e.time + 1;
        this.msgType = e.msgType;
        this.from = from;
        this.to = to;
        this.source = e.source;
        this.destination = e.destination;
        this.floodID = e.floodID;
        this.message = e.message;
        this.hopCount = e.hopCount + 1;
    }

    @Override
    public String toString() {
        return String.format("<AODVEvent %d, %d, %s, %s, %s>", time, msgType, from, to, message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AODVEvent aodvEvent = (AODVEvent) o;

        if (time != aodvEvent.time) return false;
        if (msgType != aodvEvent.msgType) return false;
        if (from != null ? !from.equals(aodvEvent.from) : aodvEvent.from != null) return false;
        if (to != null ? !to.equals(aodvEvent.to) : aodvEvent.to != null) return false;
        if (source != null ? !source.equals(aodvEvent.source) : aodvEvent.source != null) return false;
        if (destination != null ? !destination.equals(aodvEvent.destination) : aodvEvent.destination != null)
            return false;
        return message != null ? message.equals(aodvEvent.message) : aodvEvent.message == null;
    }

    @Override
    public int compareTo(Object o) {

        // TODO: needs to be implemented better
        AODVEvent event = (AODVEvent) o;

        int[] v1 = new int[6];
        v1[0] = this.time;
        v1[1] = this.msgType;
        v1[2] = this.from.id;
        v1[3] = this.to.id;
        v1[4] = this.source.id;
        v1[5] = this.destination.id;

        int[] v2 = new int[6];
        v2[0] = event.time;
        v2[1] = event.msgType;
        v2[2] = event.from.id;
        v2[3] = event.to.id;
        v2[4] = event.source.id;
        v2[5] = event.destination.id;

        for (int i = 0; i < v1.length; i++) {
            if (v1[i] > v2[i]) return 1;
            if (v1[i] < v2[i]) return -1;
        }
        return 0;
    }

    public int getTime() {
        return time;
    }

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }

    public Node getSource() {
        return source;
    }

    public Node getDestination() {
        return destination;
    }

    public String getMessage() {
        return message;
    }

    public String getFloodID() {
        return floodID;
    }

    public int getHopCount() {
        return hopCount;
    }
}
