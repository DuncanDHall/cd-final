import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by duncan on 4/28/17.
 */
public interface iNode extends Remote {
    public void introduction(iNode newNode) throws RemoteException;
//    public void disconnect(iNode newNode) throws RemoteException;  // for later cause rapid connection/disconnection could cause problems
    public void pathDiscoveryOut(iNode start, iNode destination, int ttl) throws RemoteException;
    public void pathDiscoveryIn(iNode start, iNode destination) throws RemoteException;
    public void passMessage(iNode start, iNode destination, String message) throws RemoteException;
}
