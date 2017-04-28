import java.rmi.RemoteException;

/**
 * Created by duncan on 4/28/17.
 */
public class Node implements iNode {

    // id
    // other nodes
    // neighbors
    // path table

    @Override
    public void introduction(iNode newNode) throws RemoteException {

    }

    @Override
    public void pathDiscoveryOut(iNode start, iNode destination, int ttl) throws RemoteException {

    }

    @Override
    public void pathDiscoveryIn(iNode start, iNode destination) throws RemoteException {

    }

    @Override
    public void passMessage(iNode start, iNode destination, String message) throws RemoteException {

    }
}
