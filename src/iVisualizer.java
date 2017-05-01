/**
 * Created by duncan on 4/28/17.
 */
public interface iVisualizer {
    public void pulseNode(String nodeID, String options);
    public void pulseConnection(String senderID, String receiverID, String options);
}
