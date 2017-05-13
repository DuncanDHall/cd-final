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
    private String message;

    public AODVEvent(int time, int msgType, Node from, Node to, Node source, Node destination, String message) {
        if (msgType > 2) {
            throw new IndexOutOfBoundsException("Valid AODVEvents have a msgtype of 0 (data), 1 (rreq), or 2 (rrep)");
        }
        this.time = time;
        this.msgType = msgType;
        this.from = from;
        this.to = to;
        this.source = source;
        this.destination = destination;
        this.message = message;
    }

    public boolean isData() { return msgType == 0; }
    public boolean isRREQ() { return msgType == 1; }
    public boolean isRREP() { return msgType == 2; }

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
        if(this.time < event.time) {
            return -1;
        } else if(this.time > event.time) {
            return 1;
        } else {
            if(this.msgType < event.msgType) {
                return -1;
            } else if(this.msgType > event.msgType) {
                return 1;
            } else {
                return 0;
            }
        }
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
}
