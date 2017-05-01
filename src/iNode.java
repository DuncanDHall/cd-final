/**
 * Created by duncan on 4/28/17.
 */
public interface iNode {
    public void introductionOut(iNode newNode);
    public void introductionIn(iNode newNode);
//    public void disconnect(iNode newNode);  // for later cause rapid connection/disconnection could cause problems
    public void pathDiscoveryOut(iNode start, iNode destination, int ttl);
    public void pathDiscoveryIn(iNode start, iNode destination);
    public void passMessage(iNode start, iNode destination, int messageID, String message);
    public void messageFailed(int messageID);
}
