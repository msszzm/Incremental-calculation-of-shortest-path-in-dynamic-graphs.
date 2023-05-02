package remoteBatch;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteBatch extends Remote{
    String executeBatch(String command) throws RemoteException;
}