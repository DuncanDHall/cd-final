/**
 * Created by duncan on 5/6/17.
 *
 * Represents an entry in the initial table of data transfers which
 * will be expanded into multiple AODVEvents
 */
public class RawEvent {
    private int time;
    private int nodeFrom;
    private int nodeTo;
    private String msg;

    public RawEvent(int time, int nodeFrom, int nodeTo, String msg) {
        this.time = time;
        this.nodeFrom = nodeFrom;
        this.nodeTo = nodeTo;
        this.msg = msg;
    }

    public int getTime() {
        return time;
    }

    public int getNodeFrom() {
        return nodeFrom;
    }

    public int getNodeTo() {
        return nodeTo;
    }

    public String getMsg() {
        return msg;
    }
}
